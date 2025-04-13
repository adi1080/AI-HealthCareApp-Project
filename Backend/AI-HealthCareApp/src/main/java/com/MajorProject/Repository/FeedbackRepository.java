package com.MajorProject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.MajorProject.model.Feedback;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long>{

}
