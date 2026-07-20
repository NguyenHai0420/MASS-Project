package com.group_project.MASS.repository;

import com.group_project.MASS.model.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

<<<<<<< HEAD
import java.util.List;
import java.util.Optional;

@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty, Long> {
    boolean existsByName(String name);

    // Find all specialties ordered by name in ascending order
    List<Specialty> findAllByOrderByNameAsc();

    // Find a specialty by name, ignoring case
    Optional<Specialty> findByNameIgnoreCase(String name);

    // Check if a specialty exists by name, ignoring case
    boolean existsByNameIgnoreCase(String name);
=======
@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty, Long> {
>>>>>>> origin/uyenht
}
