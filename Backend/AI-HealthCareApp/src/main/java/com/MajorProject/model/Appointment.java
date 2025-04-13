package com.MajorProject.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

 
}
