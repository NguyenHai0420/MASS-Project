package com.group_project.MASS.repository;

import com.group_project.MASS.model.ClinicInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClinicInformationRepository extends JpaRepository<ClinicInformation, Long> {
}
