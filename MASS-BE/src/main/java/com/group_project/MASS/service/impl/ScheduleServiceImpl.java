package com.group_project.MASS.service.impl;

import com.group_project.MASS.dto.ScheduleDto;
import com.group_project.MASS.dto.request.ScheduleRequest;
import com.group_project.MASS.dto.ScheduleResponse;
import com.group_project.MASS.model.DoctorProfile;
import com.group_project.MASS.model.Schedule;
import com.group_project.MASS.repository.DoctorProfileRepository;
import com.group_project.MASS.repository.ScheduleRepository;
import com.group_project.MASS.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private DoctorProfileRepository doctorProfileRepository;

    private ScheduleResponse toResponse(Schedule schedule) {
        return ScheduleResponse.builder()
                .id(schedule.getId())
                .date(schedule.getDate())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .available(schedule.isAvailable())
                .build();
    }

    @Override
    public List<ScheduleResponse> getMySchedules(String email) {
        DoctorProfile dp = doctorProfileRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy DoctorProfile cho email: " + email));
        return scheduleRepository.findByDoctorProfileOrderByDateAscStartTimeAsc(dp)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
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

    @Override
    public void deleteSchedule(Long id, String email) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy schedule với id: " + id));

        if (!schedule.getDoctorProfile().getUser().getEmail().equals(email)) {
            throw new RuntimeException("Bạn không có quyền xóa schedule này");
        }
        scheduleRepository.deleteById(id);
    }

    @Override
    public List<ScheduleDto> getDoctorSchedules(Long doctorId, String date) {
        LocalDate localDate = LocalDate.parse(date);
        return scheduleRepository.findByDoctorProfileIdAndDateAndIsAvailableTrueOrderByStartTimeAsc(doctorId, localDate)
                .stream().map(s -> ScheduleDto.builder()
                        .id(s.getId())
                        .date(s.getDate())
                        .startTime(s.getStartTime())
                        .endTime(s.getEndTime())
                        .isAvailable(s.isAvailable())
                        .build()
                ).collect(Collectors.toList());
    }
}
