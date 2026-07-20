package com.group_project.MASS.dto.request;

import com.group_project.MASS.model.AppointmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAppointmentStatusRequest {
    @NotNull(message = "Trang thái cuộc hẹn không được để trống")
    private AppointmentStatus appointmentStatus;
}
