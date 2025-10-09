package com.MajorProject.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.MajorProject.Entity.Doctor;
import com.MajorProject.Entity.Feedback;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long>{
	List<Feedback> findByDoctor(Optional<Doctor> doctor);

	List<Feedback> findByDoctor(Doctor doctor);

    List<Feedback> findByDoctor_Id(Long doctorId); //underscore makes it doctor.id

    @Query("SELECT f FROM Feedback f WHERE f.doctor.id = :doctorId AND f.Feedbackid > :lastAnalyzedFeedbackId")
    List<Feedback> findNewFeedbackAfterId(@Param("doctorId") Long doctorId, @Param("lastAnalyzedFeedbackId") Long lastAnalyzedFeedbackId);

    @Query("SELECT MAX(f.Feedbackid) FROM Feedback f WHERE f.doctor.id = :doctorId")
    Long findLatestFeedbackIdByDoctor_Id(@Param("doctorId") Long doctorId);

    Optional<Feedback> findByDoctor_IdAndPatient_Id(long id, long id1);
}
