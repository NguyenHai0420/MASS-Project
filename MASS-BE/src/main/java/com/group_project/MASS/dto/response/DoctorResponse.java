package com.group_project.MASS.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorResponse {
    private Long doctorProfileId;
    private Long userId;
    private String fullName;
    private String avatarUrl;
    private String specialtyName;
}
