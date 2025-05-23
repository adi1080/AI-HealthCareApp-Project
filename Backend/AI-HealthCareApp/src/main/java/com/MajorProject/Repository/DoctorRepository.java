package com.MajorProject.Repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.MajorProject.model.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long>{

	public Set<Doctor> findAllByClinicNameAndClinicAddress(String name, String address);

}
