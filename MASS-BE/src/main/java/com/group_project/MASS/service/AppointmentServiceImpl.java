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
    // /api/receptionist/appointments
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

    private AppointmentListResponse mapToListResponse(Appointment appointment) {
        Payment payment = paymentRepository
                .findByAppointmentId(appointment.getId())
                .orElse(null);

        return AppointmentMapper.toListResponse(
                appointment,
                payment
        );
    }

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

        /*
         * Trường hợp lễ tân đã chọn cụ thể bác sĩ.
         */
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
                /*
                 * Walk-in vừa được tạo, mặc định chờ xử lý hoặc thanh toán.
                 */
                .status(AppointmentStatus.PENDING_PAYMENT)
                .reason(request.getReason().trim())
                .queueNumber(queueNumber)
                .type(AppointmentType.WALK_IN)
                .build();

        /*
         * Đánh dấu schedule không còn trống.
         */
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

    @Override
    public AppointmentDetailResponse updateAppointment(
            Long appointmentId,
            UpdateAppointmentRequest request
    ) {
        throw new UnsupportedOperationException(
                "Chức năng cập nhật lịch chưa được triển khai"
        );
    }

    @Override
    public ApiMessageResponse updateAppointmentStatus(
            Long appointmentId,
            UpdateAppointmentStatusRequest request
    ) {
        throw new UnsupportedOperationException(
                "Chức năng cập nhật trạng thái chưa được triển khai"
        );
    }

    @Override
    public ApiMessageResponse cancelAppointment(
            Long appointmentId,
            CancelAppointmentRequest request
    ) {
        throw new UnsupportedOperationException(
                "Chức năng hủy lịch chưa được triển khai"
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

        /*
         * Ngày hôm nay: không được tìm slot trong quá khứ.
         */
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

}
