package com.group_project.MASS.service;

import com.group_project.MASS.model.Appointment;
import com.group_project.MASS.model.Payment;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void sendOtpEmail(String toEmail, String otp);
    void sendPaymentSuccessEmail(Appointment appointment, Payment payment);
}
