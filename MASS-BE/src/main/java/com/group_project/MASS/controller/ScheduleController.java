package com.group_project.MASS.controller;

import com.group_project.MASS.dto.ScheduleDto;
import com.group_project.MASS.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<List<ScheduleDto>> getSchedules(
            @RequestParam Long doctorId,
            @RequestParam String date) {
        return ResponseEntity.ok(scheduleService.getDoctorSchedules(doctorId, date));
    }
}
