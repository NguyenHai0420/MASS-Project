package com.group_project.MASS.service.impl;

import com.group_project.MASS.dto.ScheduleDto;
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

    @Override
    public List<ScheduleDto> getDoctorSchedules(Long doctorId, String date) {
        LocalDate localDate = LocalDate.parse(date);
        
        return scheduleRepository.findByDoctorProfileIdAndDateAndIsAvailable(doctorId, localDate, true)
            .stream().map(s -> ScheduleDto.builder()
                .id(s.getId())
                .date(s.getDate())
                .startTime(s.getStartTime())
                .endTime(s.getEndTime())
                .isAvailable(s.isAvailable())
                .queueNumber((int) (java.time.Duration.between(java.time.LocalTime.of(7, 0), s.getStartTime()).toMinutes() / 30) + 1)
                .build()
            ).collect(Collectors.toList());
    }
}
