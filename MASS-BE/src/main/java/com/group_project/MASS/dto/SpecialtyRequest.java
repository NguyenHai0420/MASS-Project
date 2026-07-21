package com.group_project.MASS.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SpecialtyRequest {
    @NotBlank(message = "Tên chuyên khoa không được để trống")
    private String name;

    private String description;
    private String imageUrl;
}
