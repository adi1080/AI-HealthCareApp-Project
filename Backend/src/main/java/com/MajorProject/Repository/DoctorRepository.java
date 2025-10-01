package com.MajorProject.Repository;

import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.MajorProject.model.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long>{

	   @Query("SELECT d FROM Doctor d WHERE " +
	           "(:name IS NULL OR LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
	           "(:city IS NULL OR LOWER(d.city) LIKE LOWER(CONCAT('%', :city, '%'))) AND " +
	           "(:speciality IS NULL OR LOWER(d.speciality) LIKE LOWER(CONCAT('%', :speciality, '%')))")
	    List<Doctor> findDoctors(
	        @Param("name") String name,
	        @Param("city") String city,
	        @Param("speciality") String speciality
	    );
}
