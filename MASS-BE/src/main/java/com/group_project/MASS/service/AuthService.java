package com.group_project.MASS.service;

import com.group_project.MASS.dto.request.LoginRequest;
import com.group_project.MASS.dto.response.AuthResponse;
import com.group_project.MASS.dto.request.RegisterRequest;
import com.group_project.MASS.dto.request.RegisterRequest;

public interface AuthService {
    String login(LoginRequest loginRequest);
    void register(RegisterRequest registerRequest);
    AuthResponse getMe(String email);
    void generateAndSendOtp(String email);
    void verifyOtp(String email, String otp);
    void resetPassword(String email, String otp, String newPassword);
}
