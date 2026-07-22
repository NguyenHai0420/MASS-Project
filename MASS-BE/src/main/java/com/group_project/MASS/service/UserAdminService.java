package com.group_project.MASS.service;

import com.group_project.MASS.dto.response.UserResponse;
import com.group_project.MASS.dto.request.UserUpdateRequest;

import java.util.List;

public interface UserAdminService {
    List<UserResponse> getAllUsers();
    UserResponse updateUser(Long id, UserUpdateRequest request);
    void deleteUser(Long id);
}
