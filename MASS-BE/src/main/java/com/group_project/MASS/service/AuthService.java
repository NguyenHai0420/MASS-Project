package com.group_project.MASS.service;

import com.group_project.MASS.dto.AuthResponse;
import com.group_project.MASS.dto.LoginRequest;
import com.group_project.MASS.dto.RegisterRequest;
import com.group_project.MASS.model.PasswordResetToken;
import com.group_project.MASS.model.Role;
import com.group_project.MASS.model.User;
import com.group_project.MASS.repository.PasswordResetTokenRepository;
import com.group_project.MASS.repository.UserRepository;
import com.group_project.MASS.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private EmailService emailService;

    public String login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtUtils.generateJwtToken(authentication);
    }

    @Transactional
    public void register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        User user = User.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .fullName(registerRequest.getFullName())
                .role(Role.ROLE_PATIENT) // Default role for open registration
                .build();

        userRepository.save(user);
    }

    public AuthResponse getMe(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return AuthResponse.builder()
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .build();
    }

    @Transactional
    public void generateAndSendOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        tokenRepository.deleteByUser(user);

        String otp = String.format("%06d", new Random().nextInt(999999));
        
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(otp)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(15))
                .build();
                
        tokenRepository.save(resetToken);
        emailService.sendOtpEmail(email, otp);
    }

    public void verifyOtp(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PasswordResetToken resetToken = tokenRepository.findByToken(otp)
                .orElseThrow(() -> new RuntimeException("Invalid OTP"));

        if (!resetToken.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Invalid OTP for this user");
        }

        if (resetToken.isExpired()) {
            throw new RuntimeException("OTP is expired");
        }
    }

    @Transactional
    public void resetPassword(String email, String otp, String newPassword) {
        verifyOtp(email, otp); // Double check

        User user = userRepository.findByEmail(email).get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        tokenRepository.deleteByUser(user);
    }
}
