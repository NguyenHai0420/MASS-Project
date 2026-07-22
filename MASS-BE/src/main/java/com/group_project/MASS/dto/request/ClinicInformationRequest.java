package com.group_project.MASS.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ClinicInformationRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String address;

    private String phone;

    private String email;

    private String workingHours;
}
