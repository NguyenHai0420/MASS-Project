package com.group_project.MASS.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequestDto {
    private Long doctorId;
    private Long specialtyId; // Optional in backend if not strictly required, but matching frontend signature
    private Long scheduleId;
    private String reason;
}
