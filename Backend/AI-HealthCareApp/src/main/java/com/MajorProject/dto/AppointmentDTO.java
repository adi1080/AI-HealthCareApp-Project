package com.MajorProject.dto;

public class AppointmentDTO {
    private long appointmentId;
    private Long doctorId;
    private Long availabilityId;
    private String reason;
    private String status;

    // Getters and Setters

    public long getAppointmentId() {
        return appointmentId;
    }
    public void setAppointmentId(long appointmentId) {
        this.appointmentId = appointmentId;
    }
    public Long getDoctorId() {
        return doctorId;
    }
    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }
    public Long getAvailabilityId() {
        return availabilityId;
    }
    public void setAvailabilityId(Long availabilityId) {
        this.availabilityId = availabilityId;
    }
    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
