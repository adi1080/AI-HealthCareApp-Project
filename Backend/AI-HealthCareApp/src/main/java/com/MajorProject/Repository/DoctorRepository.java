package com.MajorProject.Repository;

import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.MajorProject.model.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long>{

	public List<Doctor> findAllByCityIgnoreCaseAndSpecialityIgnoreCase(String city , String speciality);

	List<Doctor> findByNameContainingIgnoreCase(String name);


}
