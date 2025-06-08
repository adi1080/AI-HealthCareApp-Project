package com.MajorProject.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.MajorProject.model.DoctorAvailability;


public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability, Long>{

	List<DoctorAvailability> findByDoctor_Id(long doctorId);

}
