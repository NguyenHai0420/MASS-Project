package com.group_project.MASS.dto;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String fullName;
    private String phone;
    private String gender;
    private String address;
    private String role;   // admin có thể đổi role
}
