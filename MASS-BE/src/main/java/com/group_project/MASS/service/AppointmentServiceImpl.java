package com.group_project.MASS.service;

import com.group_project.MASS.dto.request.CancelAppointmentRequest;
import com.group_project.MASS.dto.request.CreateWalkInAppointmentRequest;
import com.group_project.MASS.dto.request.UpdateAppointmentRequest;
import com.group_project.MASS.dto.request.UpdateAppointmentStatusRequest;
import com.group_project.MASS.dto.response.ApiMessageResponse;
import com.group_project.MASS.dto.response.AppointmentDetailResponse;
import com.group_project.MASS.dto.response.AppointmentListResponse;
import com.group_project.MASS.dto.response.AvailableScheduleResponse;
import com.group_project.MASS.mapper.AppointmentMapper;
import com.group_project.MASS.model.*;
import com.group_project.MASS.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.time.Duration;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final ScheduleRepository scheduleRepository;
    private final PaymentRepository paymentRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final SpecialtyRepository specialtyRepository;
    private final UserRepository userRepository;


    // Appointment List
    @Override
    @Transactional(readOnly = true)
    public List<AppointmentListResponse> getAppointments(LocalDate date,
                                                         Long specialtyId,
                                                         AppointmentStatus status) {

        LocalDate selectedDate = date != null ? date : LocalDate.now();

        List<Appointment> appointments;

        if (specialtyId != null && status != null) {
            // filter appointments based on specialtyId and status
            appointments = appointmentRepository
                    .findByDoctorProfileSpecialtyIdAndScheduleDateAndStatusOrderByScheduleStartTimeAsc(specialtyId, selectedDate, status);

        } else if (specialtyId != null) {
            // filter appointments based on specialtyId only
            appointments = appointmentRepository
                    .findByDoctorProfileSpecialtyIdAndScheduleDateOrderByScheduleStartTimeAsc(specialtyId, selectedDate);
        } else if (status != null) {
            // filter appointments based on status only
            appointments = appointmentRepository
                    .findByScheduleDateAndStatusOrderByScheduleStartTimeAsc(selectedDate, status);
        } else {
            // no filters, get all appointments for the selected date
            appointments = appointmentRepository
                    .findByScheduleDateOrderByScheduleStartTimeAsc(selectedDate);
        }

        return appointments.stream()
                .map(this::mapToListResponse)
                .toList();
    }

    // Appointment Detail
    @Override
    @Transactional(readOnly = true)
    public AppointmentDetailResponse getAppointmentDetail(Long appointmentId) {

        if (appointmentId == null) {
            throw new RuntimeException("ID của lịch hẹn không được để trống");
        }

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cuộc hẹn với ID: " + appointmentId));

        Payment payment = paymentRepository
                .findByAppointmentId(appointmentId)
                .orElse(null);

        return AppointmentMapper.toDetailResponse(
                appointment,
                payment
        );
    }

    // Tạo lịch hẹn khám tại phòng khám
    @Override
    public AppointmentDetailResponse createWalkInAppointment(
            CreateWalkInAppointmentRequest request
    ) {
        if (request == null) {
            throw new IllegalArgumentException(
                    "Dữ liệu tạo lịch không được để trống"
            );
        }

        validateWalkInRequest(request);

        User patient = userRepository.findById(request.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Không tìm thấy bệnh nhân có ID: "
                                + request.getPatientId()
                ));

        if (patient.getRole() != Role.ROLE_PATIENT) {
            throw new IllegalArgumentException(
                    "Tài khoản được chọn không phải bệnh nhân"
            );
        }

        Specialty specialty = specialtyRepository
                .findById(request.getSpecialtyId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Không tìm thấy chuyên khoa có ID: "
                                + request.getSpecialtyId()
                ));

        LocalTime searchFromTime = determineSearchFromTime(
                request.getAppointmentDate(),
                request.getSearchFromTime()
        );

        Schedule selectedSchedule;

        // Trường hợp lễ tân đã chọn cụ thể bác sĩ.
        if (request.getDoctorProfileId() != null) {
            DoctorProfile doctorProfile = doctorProfileRepository
                            .findById(request.getDoctorProfileId())
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "Không tìm thấy bác sĩ có ID: " + request.getDoctorProfileId()
                            ));

            if (!doctorProfile.getSpecialty()
                    .getId()
                    .equals(specialty.getId())) {
                throw new IllegalArgumentException(
                        "Bác sĩ được chọn không thuộc chuyên khoa "
                                + specialty.getName()
                );
            }

            selectedSchedule = findNearestAvailableScheduleByDoctor(
                    doctorProfile.getId(),
                    request.getAppointmentDate(),
                    searchFromTime
            );

            /*
             * Không chọn bác sĩ: hệ thống tự tìm slot gần nhất
             * của các bác sĩ thuộc chuyên khoa.
             */
        } else {
            selectedSchedule = findNearestAvailableScheduleBySpecialty(
                    specialty.getId(),
                    request.getAppointmentDate(),
                    searchFromTime
            );
        }

        /*
         * Kiểm tra lại trước khi lưu để tránh sử dụng schedule
         * đã có appointment nhưng trạng thái available chưa đồng bộ.
         */
        boolean occupied = appointmentRepository
                .existsByScheduleIdAndStatusNot(
                        selectedSchedule.getId(),
                        AppointmentStatus.CANCELLED
                );

        if (occupied || !selectedSchedule.isAvailable()) {
            throw new IllegalStateException(
                    "Slot vừa được người khác sử dụng. "
                            + "Vui lòng chọn lại slot khác"
            );
        }

        int queueNumber = calculateQueueNumber(
                selectedSchedule.getStartTime()
        );

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctorProfile(selectedSchedule.getDoctorProfile())
                .schedule(selectedSchedule)
                // Walk-in vừa được tạo, mặc định chờ xử lý hoặc thanh toán.
                .status(AppointmentStatus.PENDING_PAYMENT)
                .reason(request.getReason().trim())
                .queueNumber(queueNumber)
                .type(AppointmentType.WALK_IN)
                .build();

        // Đánh dấu schedule không còn trống.
        selectedSchedule.setAvailable(false);
        scheduleRepository.save(selectedSchedule);

        Appointment savedAppointment =
                appointmentRepository.save(appointment);

        /*
         * Walk-in mới chưa có payment nên truyền null.
         * Payment sẽ được tạo trong module PayOS sau.
         */
        return AppointmentMapper.toDetailResponse(
                savedAppointment,
                null
        );
    }

    // Cập nhật lịch hẹn khám
    @Override
    public AppointmentDetailResponse updateAppointment(
            Long appointmentId,
            UpdateAppointmentRequest request
    ) {
        if (request == null) {
            throw new IllegalArgumentException(
                    "Dữ liệu cập nhật không được để trống"
            );
        }

        Appointment appointment = appointmentRepository
                .findById(appointmentId).orElseThrow(() ->  new IllegalArgumentException("Không tìm thấy lịch khám với ID: " + appointmentId));

        validateAppointmentCanBeModified(appointment);

        boolean hasScheduleChange = request.getScheduleId() != null
                && !request.getScheduleId()
                .equals(appointment.getSchedule().getId());

        boolean hasReasonChange = request.getReason() != null;

        if (!hasScheduleChange && !hasReasonChange) {
            throw new IllegalArgumentException(
                    "Không có thông tin nào được thay đổi"
            );
        }

        if (hasScheduleChange) {
            changeAppointmentSchedule(
                    appointment,
                    request.getScheduleId()
            );
        }

        if (hasReasonChange) {
            String newReason = request.getReason().trim();

            if (newReason.isBlank()) {
                throw new IllegalArgumentException(
                        "Lý do khám không được để trống"
                );
            }

            appointment.setReason(newReason);
        }

        Appointment savedAppointment =
                appointmentRepository.save(appointment);

        Payment payment = paymentRepository
                .findByAppointmentId(savedAppointment.getId())
                .orElse(null);

        return AppointmentMapper.toDetailResponse(
                savedAppointment,
                payment
        );
    }

    // Cập nhật trạng thái lịch hẹn khám
    @Override
    public ApiMessageResponse updateAppointmentStatus(
            Long appointmentId,
            UpdateAppointmentStatusRequest request
    ) {
        if (request == null || request.getAppointmentStatus() == null) {
            throw new IllegalArgumentException(
                    "Trạng thái appointment không được để trống"
            );
        }

        Appointment appointment = appointmentRepository
                .findById(appointmentId).orElseThrow(() ->  new IllegalArgumentException("Không tìm thấy lịch khám với ID: " + appointmentId));

        AppointmentStatus currentStatus =
                appointment.getStatus();

        AppointmentStatus newStatus =
                request.getAppointmentStatus();

        if (currentStatus == newStatus) {
            return new ApiMessageResponse(
                    "Appointment đã ở trạng thái " + newStatus
            );
        }

        if (newStatus == AppointmentStatus.CANCELLED) {
            throw new IllegalArgumentException(
                    "Vui lòng sử dụng chức năng hủy lịch "
                            + "để chuyển sang trạng thái CANCELLED"
            );
        }

        validateStatusTransition(
                currentStatus,
                newStatus
        );

        appointment.setStatus(newStatus);

        /*
         * COMPLETED và NO_SHOW kết thúc lượt khám.
         * Slot không nên được mở lại vì thời gian đó đã được sử dụng.
         */
        appointmentRepository.save(appointment);

        return new ApiMessageResponse(
                "Cập nhật trạng thái appointment thành công: "
                        + newStatus
        );
    }

    // Hủy lịch hẹn khám
    @Override
    public ApiMessageResponse cancelAppointment(
            Long appointmentId,
            CancelAppointmentRequest request
    ) {
        if (request == null
                || request.getCancelReason() == null
                || request.getCancelReason().isBlank()) {
            throw new IllegalArgumentException(
                    "Lý do hủy lịch không được để trống"
            );
        }

        Appointment appointment = appointmentRepository
                .findById(appointmentId).orElseThrow(() ->  new IllegalArgumentException("Không tìm thấy lịch khám với ID: " + appointmentId));

        if (appointment.getStatus()
                == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException(
                    "Appointment đã được hủy trước đó"
            );
        }

        if (appointment.getStatus()
                == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException(
                    "Không thể hủy lịch đã hoàn thành"
            );
        }

        if (appointment.getStatus()
                == AppointmentStatus.NO_SHOW) {
            throw new IllegalStateException(
                    "Không thể hủy lịch đã được đánh dấu không đến"
            );
        }

        Schedule schedule = appointment.getSchedule();

        /*
         * Chỉ mở lại slot nếu thời gian khám chưa xảy ra.
         */
        if (isScheduleInFuture(schedule)) {
            schedule.setAvailable(true);
            scheduleRepository.save(schedule);
        }

        appointment.setStatus(
                AppointmentStatus.CANCELLED
        );

        appointmentRepository.save(appointment);

        /*
         * Entity hiện tại chưa có cancellationReason,
         * nên lý do hủy chưa được lưu vào bảng appointments.
         * Phần Notification sau sẽ đưa lý do vào nội dung thông báo.
         */
        return new ApiMessageResponse(
                "Hủy lịch khám thành công. Lý do: "
                        + request.getCancelReason().trim()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvailableScheduleResponse> getAvailableSchedules(
            Long specialtyId,
            Long doctorProfileId,
            LocalDate date,
            LocalTime fromTime
    ) {
        if (date == null) {
            throw new IllegalStateException("Ngày khám không được để trống");
        }

        if (date.isBefore(LocalDate.now())) {
            throw new IllegalStateException("Ngày khám không được nhỏ hơn ngày hiện tại");
        }

        if (specialtyId == null && doctorProfileId == null) {
            throw new IllegalStateException("Phải chọn chuyên khoa hoặc bác sĩ");
        }

        LocalTime selectedFromTime = determineSearchFromTime(date, fromTime);

        List<Schedule> schedules;

        if (doctorProfileId != null) {
            DoctorProfile doctorProfile = doctorProfileRepository
                    .findById(doctorProfileId).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bác sĩ có ID: " + doctorProfileId));

            /*
             * Nếu đồng thời truyền specialtyId thì kiểm tra
             * bác sĩ có thuộc đúng chuyên khoa không.
             */
            if (specialtyId != null
                    && !doctorProfile.getSpecialty()
                    .getId()
                    .equals(specialtyId)) {
                throw new IllegalArgumentException(
                        "Bác sĩ không thuộc chuyên khoa đã chọn"
                );
            }

            schedules = scheduleRepository
                    .findByDoctorProfileIdAndDateAndStartTimeGreaterThanEqualAndIsAvailableTrueOrderByStartTimeAsc(
                            doctorProfileId,
                            date,
                            selectedFromTime
                    );
        } else {
            specialtyRepository.findById(specialtyId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Không tìm thấy chuyên khoa có ID: "
                                    + specialtyId
                    ));

            schedules = scheduleRepository
                    .findByDoctorProfileSpecialtyIdAndDateAndStartTimeGreaterThanEqualAndIsAvailableTrueOrderByStartTimeAsc(
                            specialtyId,
                            date,
                            selectedFromTime
                    );
        }

        return schedules.stream()
                /*
                 * Nếu nhiều bác sĩ có cùng giờ, sắp xếp thêm theo ID bác sĩ
                 * để kết quả ổn định.
                 */
                .sorted(
                        Comparator.comparing(Schedule::getStartTime)
                                .thenComparing(
                                        schedule ->
                                                schedule.getDoctorProfile().getId()
                                )
                )
                /*
                 * isAvailable có thể vẫn là true trong khi đã tồn tại
                 * appointment do dữ liệu chưa đồng bộ. Kiểm tra thêm để an toàn.
                 */
                .filter(schedule ->
                        !appointmentRepository
                                .existsByScheduleIdAndStatusNot(
                                        schedule.getId(),
                                        AppointmentStatus.CANCELLED
                                )
                )
                .map(schedule ->
                        AppointmentMapper.toScheduleResponse(
                                schedule,
                                calculateQueueNumber(
                                        schedule.getStartTime()
                                )
                        )
                )
                .toList();
    }

    /*
     * HELPER
     */

    private AppointmentListResponse mapToListResponse(Appointment appointment) {
        Payment payment = paymentRepository
                .findByAppointmentId(appointment.getId())
                .orElse(null);

        return AppointmentMapper.toListResponse(
                appointment,
                payment
        );
    }

    /*
     * Kiểm tra yêu cầu tạo mới
     */
    private void validateWalkInRequest(
            CreateWalkInAppointmentRequest request
    ) {
        if (request.getPatientId() == null) {
            throw new IllegalArgumentException(
                    "Patient ID không được để trống"
            );
        }

        if (request.getSpecialtyId() == null) {
            throw new IllegalArgumentException(
                    "Chuyên khoa không được để trống"
            );
        }

        if (request.getAppointmentDate() == null) {
            throw new IllegalArgumentException(
                    "Ngày khám không được để trống"
            );
        }

        if (request.getAppointmentDate()
                .isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Ngày khám không được nhỏ hơn ngày hiện tại"
            );
        }

        if (request.getReason() == null
                || request.getReason().isBlank()) {
            throw new IllegalArgumentException(
                    "Lý do khám không được để trống"
            );
        }
    }

    /*
     * Tìm thời gian gần nhất theo bác sĩ
     */
    private Schedule findNearestAvailableScheduleByDoctor(
            Long doctorProfileId,
            LocalDate date,
            LocalTime fromTime
    ) {
        List<Schedule> schedules = scheduleRepository
                .findByDoctorProfileIdAndDateAndStartTimeGreaterThanEqualAndIsAvailableTrueOrderByStartTimeAsc(
                        doctorProfileId,
                        date,
                        fromTime
                );

        return schedules.stream()
                .filter(schedule ->
                        !appointmentRepository
                                .existsByScheduleIdAndStatusNot(
                                        schedule.getId(),
                                        AppointmentStatus.CANCELLED
                                )
                )
                .min(Comparator.comparing(Schedule::getStartTime))
                .orElseThrow(() -> new IllegalStateException(
                        "Bác sĩ không còn slot trống phù hợp trong ngày "
                                + date
                ));
    }

    /*
     * Tìm slot gần nhất theo chuyên khoa
     */
    private Schedule findNearestAvailableScheduleBySpecialty(
            Long specialtyId,
            LocalDate date,
            LocalTime fromTime
    ) {
        List<Schedule> schedules = scheduleRepository
                .findByDoctorProfileSpecialtyIdAndDateAndStartTimeGreaterThanEqualAndIsAvailableTrueOrderByStartTimeAsc(
                        specialtyId,
                        date,
                        fromTime
                );

        return schedules.stream()
                .filter(schedule ->
                        !appointmentRepository
                                .existsByScheduleIdAndStatusNot(
                                        schedule.getId(),
                                        AppointmentStatus.CANCELLED
                                )
                )
                .min(
                        Comparator.comparing(Schedule::getStartTime)
                                .thenComparing(
                                        schedule ->
                                                schedule.getDoctorProfile().getId()
                                )
                )
                .orElseThrow(() -> new IllegalStateException(
                        "Không còn slot trống của chuyên khoa "
                                + "trong ngày " + date
                ));
    }

    /*
     * Xác định thời gian bắt đầu tìm slot
     */
    private LocalTime determineSearchFromTime(
            LocalDate appointmentDate,
            LocalTime requestedTime
    ) {
        LocalTime clinicOpeningTime = LocalTime.of(7, 0);

        /*
         * Ngày tương lai:
         * - Nếu không truyền giờ thì tìm từ 07:00.
         * - Nếu có truyền giờ thì tìm từ giờ được yêu cầu.
         */
        if (appointmentDate.isAfter(LocalDate.now())) {
            if (requestedTime == null) {
                return clinicOpeningTime;
            }

            return requestedTime.isBefore(clinicOpeningTime)
                    ? clinicOpeningTime
                    : requestedTime;
        }

        // Ngày hôm nay: không được tìm slot trong quá khứ.
        LocalTime currentTime = LocalTime.now();

        LocalTime result = requestedTime != null
                ? requestedTime
                : currentTime;

        if (result.isBefore(currentTime)) {
            result = currentTime;
        }

        if (result.isBefore(clinicOpeningTime)) {
            result = clinicOpeningTime;
        }

        return result;
    }

    /*
     * Tính số thứ tự cố định theo giờ
     */
    private int calculateQueueNumber(
            LocalTime slotStartTime
    ) {
        LocalTime clinicOpeningTime = LocalTime.of(7, 0);
        int slotDurationMinutes = 30;

        if (slotStartTime == null) {
            throw new IllegalArgumentException(
                    "Thời gian bắt đầu của slot không được để trống"
            );
        }

        if (slotStartTime.isBefore(clinicOpeningTime)) {
            throw new IllegalArgumentException(
                    "Slot khám không được bắt đầu trước 07:00"
            );
        }

        long minutesFromOpening = Duration.between(
                clinicOpeningTime,
                slotStartTime
        ).toMinutes();

        /*
         * Bảo đảm giờ bắt đầu đúng theo slot 30 phút.
         *
         * Ví dụ hợp lệ:
         * 07:00, 07:30, 08:00, 08:30...
         */
        if (minutesFromOpening % slotDurationMinutes != 0) {
            throw new IllegalArgumentException(
                    "Thời gian bắt đầu của schedule phải theo slot 30 phút"
            );
        }

        return (int) (minutesFromOpening / slotDurationMinutes) + 1;
    }

    /*
     * Helper thay đổi lịch hẹn khám
     */
    private void changeAppointmentSchedule(
            Appointment appointment,
            Long newScheduleId
    ) {
        Schedule oldSchedule = appointment.getSchedule();

        Schedule newSchedule = scheduleRepository
                .findById(newScheduleId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Không tìm thấy schedule có ID: "
                                + newScheduleId
                ));

        validateNewSchedule(
                appointment,
                newSchedule
        );

        // Giải phóng slot cũ.
        oldSchedule.setAvailable(true);

        // Khóa slot mới.
        newSchedule.setAvailable(false);

        scheduleRepository.save(oldSchedule);
        scheduleRepository.save(newSchedule);

        /*
         * Schedule chứa bác sĩ nên khi đổi schedule
         * cần cập nhật luôn doctorProfile.
         */
        appointment.setSchedule(newSchedule);
        appointment.setDoctorProfile(
                newSchedule.getDoctorProfile()
        );

        appointment.setQueueNumber(
                calculateQueueNumber(
                        newSchedule.getStartTime()
                )
        );
    }

    /*
     * Helper kiểm tra lịch hẹn khám mới
     */
    private void validateNewSchedule(
            Appointment appointment,
            Schedule newSchedule
    ) {
        if (!newSchedule.isAvailable()) {
            throw new IllegalStateException(
                    "Slot được chọn hiện không còn trống"
            );
        }

        boolean occupied = appointmentRepository
                .existsByScheduleIdAndStatusNot(
                        newSchedule.getId(),
                        AppointmentStatus.CANCELLED
                );

        if (occupied) {
            throw new IllegalStateException(
                    "Slot được chọn đã có lịch khám"
            );
        }

        if (newSchedule.getDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Không thể chuyển lịch sang ngày trong quá khứ"
            );
        }

        if (newSchedule.getDate().isEqual(LocalDate.now())
                && newSchedule.getStartTime()
                .isBefore(LocalTime.now())) {
            throw new IllegalArgumentException(
                    "Không thể chuyển lịch sang giờ đã qua"
            );
        }

        /*
         * Không cho đổi sang chuyên khoa khác.
         *
         * Nếu khách muốn khám chuyên khoa khác,
         * nên hủy lịch cũ và tạo lịch mới.
         */
        Long oldSpecialtyId = appointment
                .getDoctorProfile()
                .getSpecialty()
                .getId();

        Long newSpecialtyId = newSchedule
                .getDoctorProfile()
                .getSpecialty()
                .getId();

        if (!oldSpecialtyId.equals(newSpecialtyId)) {
            throw new IllegalArgumentException(
                    "Schedule mới phải thuộc cùng chuyên khoa "
                            + "với lịch khám hiện tại"
            );
        }
    }

    /*
     * Helper kiểm tra lịch hẹn khám có thể sửa
     */
    private void validateAppointmentCanBeModified(
            Appointment appointment
    ) {
        if (appointment.getStatus()
                == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException(
                    "Không thể cập nhật lịch đã bị hủy"
            );
        }

        if (appointment.getStatus()
                == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException(
                    "Không thể cập nhật lịch đã hoàn thành"
            );
        }

        if (appointment.getStatus()
                == AppointmentStatus.NO_SHOW) {
            throw new IllegalStateException(
                    "Không thể cập nhật lịch đã được đánh dấu không đến"
            );
        }
    }

    /*
     * Helper kiểm tra cập nhật trạng thái lịch khám
     */
    private void validateStatusTransition(
            AppointmentStatus currentStatus,
            AppointmentStatus newStatus
    ) {
        if (currentStatus == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException(
                    "Lịch đã hủy không thể thay đổi trạng thái"
            );
        }

        if (currentStatus == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException(
                    "Lịch đã hoàn thành không thể thay đổi trạng thái"
            );
        }

        if (currentStatus == AppointmentStatus.NO_SHOW) {
            throw new IllegalStateException(
                    "Lịch đã được đánh dấu không đến"
            );
        }

        if (currentStatus == AppointmentStatus.PENDING_PAYMENT
                && newStatus != AppointmentStatus.WAITING_CHECK_IN
                && newStatus != AppointmentStatus.NO_SHOW) {
            throw new IllegalArgumentException(
                    "Lịch PENDING chỉ có thể chuyển thành "
                            + "CONFIRMED hoặc NO_SHOW"
            );
        }

        if (currentStatus == AppointmentStatus.WAITING_CHECK_IN
                && newStatus != AppointmentStatus.COMPLETED
                && newStatus != AppointmentStatus.NO_SHOW) {
            throw new IllegalArgumentException(
                    "Lịch CONFIRMED chỉ có thể chuyển thành "
                            + "COMPLETED hoặc NO_SHOW"
            );
        }
    }

    /*
     * Helper kiểm tra lịch trong tương lai
     */
    private boolean isScheduleInFuture(
            Schedule schedule
    ) {
        if (schedule.getDate().isAfter(LocalDate.now())) {
            return true;
        }

        return schedule.getDate().isEqual(LocalDate.now())
                && schedule.getStartTime()
                .isAfter(LocalTime.now());
    }
}
