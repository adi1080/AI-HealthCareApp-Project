package com.MajorProject.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;


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
	
	
	public long getFeedbackid() {
		return Feedbackid;
	}
	public void setFeedbackid(long feedbackid) {
		Feedbackid = feedbackid;
	}
	public Patient getPatient() {
		return patient;
	}
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	public Doctor getDoctor() {
		return doctor;
	}
	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public String getFeedbackComment() {
		return FeedbackComment;
	}
	public void setFeedbackComment(String feedbackComment) {
		FeedbackComment = feedbackComment;
	}
	public LocalDate getDate() {
		return Date;
	}
	public void setDate(LocalDate date) {
		Date = date;
	}
	@Override
	public String toString() {
		return "Feedback [Feedbackid=" + Feedbackid + ", patient=" + patient + ", doctor=" + doctor + ", rating="
				+ rating + ", FeedbackComment=" + FeedbackComment + ", Date=" + Date + "]";
	}
	
	
	
}
