package com.MajorProject.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class DoctorAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JsonBackReference
    private Doctor doctor;

    private LocalDate date;
    private LocalTime time;

    private boolean isBooked = false; // Marked true once a patient books this slot


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public Doctor getDoctor() {
		return doctor;
	}


	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}


	public LocalDate getDate() {
		return date;
	}


	public void setDate(LocalDate date) {
		this.date = date;
	}


	public LocalTime getTime() {
		return time;
	}


	public void setTime(LocalTime time) {
		this.time = time;
	}

	@JsonProperty("isBooked")
	public boolean isBooked() {
		return isBooked;
	}


	public void setBooked(boolean isBooked) {
		this.isBooked = isBooked;
	}


	@Override
	public String toString() {
	    return "DoctorAvailability [id=" + id + ", doctorId=" + (doctor != null ? doctor.getId() : null)
	            + ", date=" + date + ", time=" + time + ", isBooked=" + isBooked + "]";
	}
	
	@Transient
	public LocalDateTime getDatetime() {
	    return LocalDateTime.of(this.date, this.time);
	}
  
}
