package com.group_project.MASS.controller;

import com.group_project.MASS.dto.AppointmentDto;
import com.group_project.MASS.dto.AppointmentRequestDto;
import com.group_project.MASS.dto.RescheduleRequestDto;
import com.group_project.MASS.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    private String getEmail(Principal principal) {
        if (principal == null) {
            // Fallback for testing if security is not fully enforced
            return "patient@test.com"; 
        }
        return principal.getName();
    }

    @PostMapping
    public ResponseEntity<AppointmentDto> bookAppointment(@RequestBody AppointmentRequestDto request, Principal principal) {
        return ResponseEntity.ok(appointmentService.bookAppointment(request, getEmail(principal)));
    }

    @GetMapping("/my-appointments")
    public ResponseEntity<List<AppointmentDto>> getMyAppointments(Principal principal) {
        return ResponseEntity.ok(appointmentService.getMyAppointments(getEmail(principal)));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<AppointmentDto> cancelAppointment(@PathVariable Long id, Principal principal) {
        return ResponseEntity.ok(appointmentService.cancelAppointment(id, getEmail(principal)));
    }

    @PutMapping("/{id}/reschedule")
    public ResponseEntity<AppointmentDto> rescheduleAppointment(
            @PathVariable Long id,
            @RequestBody RescheduleRequestDto request,
            Principal principal) {
        return ResponseEntity.ok(appointmentService.rescheduleAppointment(id, request, getEmail(principal)));
    }
}
