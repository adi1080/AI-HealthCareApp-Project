package com.MajorProject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.MajorProject.model.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

}
