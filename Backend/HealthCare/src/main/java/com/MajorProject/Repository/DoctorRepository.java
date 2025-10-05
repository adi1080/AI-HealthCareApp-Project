package com.MajorProject.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.MajorProject.Entity.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long>{

    @Query("SELECT d FROM Doctor d WHERE " +
            "(:name IS NULL OR LOWER(TRIM(d.name)) LIKE LOWER(CONCAT('%', TRIM(:name), '%'))) AND " +
            "(:city IS NULL OR LOWER(TRIM(d.city)) LIKE LOWER(CONCAT('%', TRIM(:city), '%'))) AND " +
            "(:speciality IS NULL OR LOWER(TRIM(d.speciality)) LIKE LOWER(CONCAT('%', TRIM(:speciality), '%')))")
    List<Doctor> findDoctors(
            @Param("name") String name,
            @Param("city") String city,
            @Param("speciality") String speciality
    );
}
