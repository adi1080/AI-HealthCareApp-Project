package com.MajorProject.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class DoctorAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    private Doctor doctor;

    private LocalDateTime startTime;


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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    @Override
    public String toString() {
        return "DoctorAvailability [id=" + id + ", doctor=" + doctor + ", startTime=" + startTime +
               ", isBooked=" + isBooked + "]";
    }
}
