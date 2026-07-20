package com.group_project.MASS.service;

import com.group_project.MASS.dto.DoctorRequest;
import com.group_project.MASS.dto.DoctorResponse;
import com.group_project.MASS.model.DoctorProfile;
import com.group_project.MASS.model.Role;
import com.group_project.MASS.model.Specialty;
import com.group_project.MASS.model.User;
import com.group_project.MASS.repository.DoctorProfileRepository;
import com.group_project.MASS.repository.SpecialtyRepository;
import com.group_project.MASS.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorProfileService {

    @Autowired
    private DoctorProfileRepository doctorProfileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SpecialtyRepository specialtyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Chuyển DoctorProfile entity → DoctorResponse DTO
    private DoctorResponse toResponse(DoctorProfile dp) {
        return DoctorResponse.builder()
                .id(dp.getId())
                .userId(dp.getUser().getId())
                .fullName(dp.getUser().getFullName())
                .email(dp.getUser().getEmail())
                .phone(dp.getUser().getPhone())
                .gender(dp.getUser().getGender())
                .avatarUrl(dp.getUser().getAvatarUrl())
                .specialtyId(dp.getSpecialty().getId())
                .specialtyName(dp.getSpecialty().getName())
                .degree(dp.getDegree())
                .experience(dp.getExperience())
                .description(dp.getDescription())
                .build();
    }

    // Lấy tất cả bác sĩ
    public List<DoctorResponse> getAllDoctors() {
        return doctorProfileRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Tạo bác sĩ mới: tạo User (ROLE_DOCTOR) + DoctorProfile
    public DoctorResponse createDoctor(DoctorRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email '" + request.getEmail() + "' đã được sử dụng");
        }

        Specialty specialty = specialtyRepository.findById(request.getSpecialtyId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyên khoa với id: " + request.getSpecialtyId()));

        // Tạo User với role DOCTOR, password mặc định
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode("doctor@123"))
                .phone(request.getPhone())
                .gender(request.getGender())
                .role(Role.ROLE_DOCTOR)
                .build();
        user = userRepository.save(user);

        // Tạo DoctorProfile
        DoctorProfile dp = DoctorProfile.builder()
                .user(user)
                .specialty(specialty)
                .degree(request.getDegree())
                .experience(request.getExperience())
                .description(request.getDescription())
                .build();
        return toResponse(doctorProfileRepository.save(dp));
    }

    // Cập nhật DoctorProfile (và một số thông tin User)
    public DoctorResponse updateDoctor(Long id, DoctorRequest request) {
        DoctorProfile dp = doctorProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ với id: " + id));

        Specialty specialty = specialtyRepository.findById(request.getSpecialtyId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyên khoa với id: " + request.getSpecialtyId()));

        // Cập nhật User
        User user = dp.getUser();
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setGender(request.getGender());
        userRepository.save(user);

        // Cập nhật DoctorProfile
        dp.setSpecialty(specialty);
        dp.setDegree(request.getDegree());
        dp.setExperience(request.getExperience());
        dp.setDescription(request.getDescription());
        return toResponse(doctorProfileRepository.save(dp));
    }

    // Xóa bác sĩ (xóa cả DoctorProfile và User)
    public void deleteDoctor(Long id) {
        DoctorProfile dp = doctorProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ với id: " + id));
        Long userId = dp.getUser().getId();
        doctorProfileRepository.deleteById(id);
        userRepository.deleteById(userId);
    }
}
