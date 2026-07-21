package com.group_project.MASS.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvailableScheduleResponse {
    private Long scheduleId;
    private Long doctorProfileId;
    private String doctorName;
    private Long specialtyId;
    private String specialtyName;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer queueNumber;
}
