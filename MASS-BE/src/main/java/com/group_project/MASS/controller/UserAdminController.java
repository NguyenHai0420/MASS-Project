package com.group_project.MASS.controller;

import com.group_project.MASS.dto.response.UserResponse;
import com.group_project.MASS.dto.request.UserUpdateRequest;
import com.group_project.MASS.service.UserAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class UserAdminController {

    @Autowired
    private UserAdminService userAdminService;

    // GET /api/admin/users — Lấy tất cả users
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userAdminService.getAllUsers());
    }

    // PUT /api/admin/users/{id} — Cập nhật user
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok(userAdminService.updateUser(id, request));
    }

    // DELETE /api/admin/users/{id} — Xóa user
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userAdminService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
