package com.group_project.MASS.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClinicInformationResponse {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String workingHours;
}
