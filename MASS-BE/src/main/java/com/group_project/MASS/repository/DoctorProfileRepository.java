package com.group_project.MASS.repository;

import com.group_project.MASS.model.DoctorProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorProfileRepository extends JpaRepository<DoctorProfile, Long> {
    // Find all doctor profiles by specialty ID
    List<DoctorProfile> findBySpecialtyId(Long specialtyId);

    // Find a doctor profile by user ID
    Optional<DoctorProfile> findUserById(Long userId);
}
