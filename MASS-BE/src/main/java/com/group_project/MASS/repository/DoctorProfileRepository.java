package com.group_project.MASS.repository;

import com.group_project.MASS.model.DoctorProfile;
import com.group_project.MASS.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorProfileRepository extends JpaRepository<DoctorProfile, Long> {
    Optional<DoctorProfile> findByUser(User user);
    Optional<DoctorProfile> findByUserEmail(String email);

    // Find all doctor profiles by specialty ID
    List<DoctorProfile> findBySpecialtyId(Long specialtyId);

    // Find a doctor profile by user ID
    Optional<DoctorProfile> findUserById(Long userId);
}
