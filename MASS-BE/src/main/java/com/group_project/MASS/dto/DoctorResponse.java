package com.group_project.MASS.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponse {
    private Long id;               // DoctorProfile id
    private Long userId;
    private String fullName;
    private String email;
    private String phone;
    private String gender;
    private String avatarUrl;

    // DoctorProfile fields
    private Long specialtyId;
    private String specialtyName;
    private String degree;
    private String experience;
    private String description;
    private Boolean active;
}
