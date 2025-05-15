package com.MajorProject.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long appointmentId;

    @ManyToOne
    @JsonIgnore
    private Patient patient; // The patient who books the appointment

    @ManyToOne
    private Doctor doctor; // The doctor for the appointment

    private LocalDate date; // lowercase "date" for proper naming convention
                            // Date and time of the appointment
    
    private String reason; // Reason for the appointment
   
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status; // Status of the appointment (e.g., PENDING, CONFIRMED, CANCELLED)

    // Enum for appointment status with a constructor to set the string value
    public enum AppointmentStatus {
        PENDING("Pending"),
        CONFIRMED("Confirmed"),
        CANCELLED("Cancelled");

        private final String status;

        // Constructor for the enum
        AppointmentStatus(String status) {
            this.status = status;
        }

        // Getter for the status
        public String getStatus() {
            return status;
        }
    }

	public long getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(long appointmentId) {
		this.appointmentId = appointmentId;
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

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public AppointmentStatus getStatus() {
		return status;
	}

	public void setStatus(AppointmentStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Appointment [appointmentId=" + appointmentId + ", patient=" + patient + ", doctor=" + doctor + ", date="
				+ date + ", reason=" + reason + ", status=" + status + "]";
	}

 
}
