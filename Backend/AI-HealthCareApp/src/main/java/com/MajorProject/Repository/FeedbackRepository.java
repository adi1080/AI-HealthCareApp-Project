package com.MajorProject.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.MajorProject.model.Doctor;
import com.MajorProject.model.Feedback;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long>{
	List<Feedback> findByDoctor(Optional<Doctor> doctor);

	List<Feedback> findByDoctor(Doctor doctor);

}
