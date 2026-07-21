package com.group_project.MASS.service;

import com.group_project.MASS.dto.UserResponse;
import com.group_project.MASS.dto.UserUpdateRequest;
import com.group_project.MASS.model.Role;
import com.group_project.MASS.model.User;
import com.group_project.MASS.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAdminService {

    @Autowired
    private UserRepository userRepository;

    // Chuyển User entity → UserResponse DTO
    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .gender(user.getGender())
                .avatarUrl(user.getAvatarUrl())
                .address(user.getAddress())
                .role(user.getRole().name())
                .active(user.getActive())
                .build();
    }

    // Lấy tất cả users
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Cập nhật thông tin user (admin có thể đổi role)
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với id: " + id));

        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getGender() != null) user.setGender(request.getGender());
        if (request.getAddress() != null) user.setAddress(request.getAddress());
        if (request.getRole() != null) {
            user.setRole(Role.valueOf(request.getRole()));
        }
        if (request.getActive() != null) {
            user.setActive(request.getActive());
        }

        return toResponse(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với id: " + id));
        user.setActive(false);
        userRepository.save(user);
    }
}
