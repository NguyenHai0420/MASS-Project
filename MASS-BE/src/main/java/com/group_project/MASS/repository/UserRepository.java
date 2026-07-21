package com.group_project.MASS.repository;

import com.group_project.MASS.model.Role;
import com.group_project.MASS.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    // Find all users by role, ordered by full name in ascending order
    List<User> findByRoleOrderByFullNameAsc(Role  role);

    // Find all users by role and full name containing a specific string, ignoring case
    List<User> findByRoleAndFullNameContainingIgnoreCase(Role  role, String fullName);

    // Find a user by phone number
    Optional<User> findByPhone(String phone);
}
