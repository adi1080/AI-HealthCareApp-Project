package com.MajorProject.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Feedback {

	@Id
	@GeneratedValue( strategy = GenerationType.AUTO)
	private long Feedbackid;
	
	@ManyToOne
    private Patient patient;
	
	@ManyToOne
	private Doctor doctor;
	
	private int rating;//1-5
	private String FeedbackComment;
	private LocalDate Date;
	
}
