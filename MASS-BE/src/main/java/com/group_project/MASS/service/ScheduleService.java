package com.group_project.MASS.service;

import com.group_project.MASS.dto.ScheduleRequest;
import com.group_project.MASS.dto.ScheduleResponse;
import com.group_project.MASS.model.DoctorProfile;
import com.group_project.MASS.model.Schedule;
import com.group_project.MASS.repository.DoctorProfileRepository;
import com.group_project.MASS.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private DoctorProfileRepository doctorProfileRepository;

    // Chuyển Schedule entity → ScheduleResponse DTO
    private ScheduleResponse toResponse(Schedule schedule) {
        return ScheduleResponse.builder()
                .id(schedule.getId())
                .date(schedule.getDate())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .available(schedule.isAvailable())
                .build();
    }

    // Lấy tất cả schedule của doctor đang đăng nhập (theo email)
    public List<ScheduleResponse> getMySchedules(String email) {
        DoctorProfile dp = doctorProfileRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy DoctorProfile cho email: " + email));
        return scheduleRepository.findByDoctorProfileOrderByDateAscStartTimeAsc(dp)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Tạo schedule mới cho doctor đang đăng nhập
    public ScheduleResponse createSchedule(String email, ScheduleRequest request) {
        DoctorProfile dp = doctorProfileRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy DoctorProfile cho email: " + email));

        Schedule schedule = Schedule.builder()
                .doctorProfile(dp)
                .date(request.getDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .isAvailable(true)
                .build();
        return toResponse(scheduleRepository.save(schedule));
    }

    // Xóa schedule (chỉ xóa được schedule của chính mình)
    public void deleteSchedule(Long id, String email) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy schedule với id: " + id));

        // Kiểm tra schedule có thuộc về doctor này không
        if (!schedule.getDoctorProfile().getUser().getEmail().equals(email)) {
            throw new RuntimeException("Bạn không có quyền xóa schedule này");
        }
        scheduleRepository.deleteById(id);
    }
}
