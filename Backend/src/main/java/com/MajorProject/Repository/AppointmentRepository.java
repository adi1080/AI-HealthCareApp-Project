package com.MajorProject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.MajorProject.model.Appointment;
import com.MajorProject.model.DoctorAvailability;

import jakarta.transaction.Transactional;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    boolean existsByAvailability(DoctorAvailability availability);

    @Modifying
    @Transactional
    @Query("DELETE FROM Appointment a WHERE a.availability.id = :availabilityId")
    void deleteByAvailabilityId(@Param("availabilityId") Long availabilityId);

    @Query("SELECT a FROM Appointment a JOIN FETCH a.patient WHERE a.doctor.id = :doctorId")
    List<Appointment> findByDoctorId(@Param("doctorId") Long doctorId);
}

