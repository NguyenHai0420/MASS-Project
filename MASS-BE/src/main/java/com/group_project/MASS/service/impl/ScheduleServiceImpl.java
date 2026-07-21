package com.group_project.MASS.service.impl;

import com.group_project.MASS.dto.ScheduleDto;
import com.group_project.MASS.repository.ScheduleRepository;
import com.group_project.MASS.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import com.group_project.MASS.repository.AppointmentRepository;
import com.group_project.MASS.model.AppointmentStatus;

import java.time.LocalTime;
import java.util.ArrayList;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    public List<ScheduleDto> getDoctorSchedules(Long doctorId, String date) {
        LocalDate localDate = LocalDate.parse(date);
        
        List<LocalTime> slots = new ArrayList<>();
        LocalTime time = LocalTime.of(7, 0);
        while (time.isBefore(LocalTime.of(11, 30))) {
            slots.add(time);
            time = time.plusMinutes(30);
        }
        time = LocalTime.of(13, 0);
        while (time.isBefore(LocalTime.of(17, 0))) {
            slots.add(time);
            time = time.plusMinutes(30);
        }

        List<ScheduleDto> dtos = new ArrayList<>();
        long fakeId = -1L;
        for (LocalTime slotTime : slots) {
            boolean isBooked = appointmentRepository.existsByDoctorProfileIdAndScheduleDateAndScheduleStartTimeAndStatusNot(
                    doctorId, localDate, slotTime, AppointmentStatus.CANCELLED);
            
            int queueNumber = (int) (java.time.Duration.between(LocalTime.of(7, 0), slotTime).toMinutes() / 30) + 1;
            
            dtos.add(ScheduleDto.builder()
                .id(fakeId--)
                .date(localDate)
                .startTime(slotTime)
                .endTime(slotTime.plusMinutes(30))
                .isAvailable(!isBooked)
                .queueNumber(queueNumber)
                .build());
        }
        
        return dtos;
    }
}
