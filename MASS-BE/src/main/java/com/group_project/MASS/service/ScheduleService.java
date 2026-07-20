package com.group_project.MASS.service;

import com.group_project.MASS.dto.ScheduleDto;
import com.group_project.MASS.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    public List<ScheduleDto> getDoctorSchedules(Long doctorId, String date) {
        LocalDate localDate = LocalDate.parse(date);
        // Only return available schedules
        return scheduleRepository.findByDoctorProfileIdAndDateAndIsAvailable(doctorId, localDate, true)
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
