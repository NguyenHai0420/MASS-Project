package com.group_project.MASS.service.impl;

import com.group_project.MASS.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your OTP Code for Password Reset - MASS");
        message.setText("Your OTP code is: " + otp + "\nThis code will expire in 15 minutes.\nIf you did not request a password reset, please ignore this email.");
        mailSender.send(message);
    }
}
