package com.group_project.MASS.repository;

import com.group_project.MASS.model.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpecialtyRepository extends JpaRepository<Specialty, Long> {
    // Find all specialties ordered by name in ascending order
    List<Specialty> findAllByOrderByNameAsc();

    // Find a specialty by name, ignoring case
    Optional<Specialty> findByNameIgnoreCase(String name);

    // Check if a specialty exists by name, ignoring case
    boolean existsByNameIgnoreCase(String name);
}
