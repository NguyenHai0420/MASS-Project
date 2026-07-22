package com.group_project.MASS.service;

import com.group_project.MASS.dto.request.UpdateProfileRequest;
import com.group_project.MASS.dto.UserProfileDto;

public interface UserService {
    UserProfileDto getUserProfile(String email);
    UserProfileDto updateProfile(String email, UpdateProfileRequest request);
}
