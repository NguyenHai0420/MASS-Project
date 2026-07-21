package com.group_project.MASS.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CancelAppointmentRequest {
    @NotBlank(message = "Lý do hủy không được để trống")
    @Size(max = 500, message = "Lý do hủy không được vượt quá 500 ký tự")
    private String cancelReason;

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }
}

