package com.group_project.MASS.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiMessageResponse {
    private String message;
    private LocalDateTime timestamp;

    public ApiMessageResponse(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
