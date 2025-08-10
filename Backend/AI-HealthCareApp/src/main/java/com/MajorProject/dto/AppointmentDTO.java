package com.MajorProject.dto;

import java.time.LocalDateTime;

public class AppointmentDTO {
    private long appointmentId;
    private Long doctorId;
    private Long availabilityId;
    private String reason;
    private String status;
    private String doctorName;
    private String doctorCity;
    private String clinicAddress;
    private LocalDateTime availabilityDatetime;

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
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public String getClinicAddress() {
		return clinicAddress;
	}
	public void setClinicAddress(String clinicAddress) {
		this.clinicAddress = clinicAddress;
	}
	public LocalDateTime getAvailabilityDatetime() {
		return availabilityDatetime;
	}
	public void setAvailabilityDatetime(LocalDateTime availabilityDatetime) {
		this.availabilityDatetime = availabilityDatetime;
	}
	public String getDoctorCity() {
		return doctorCity;
	}
	public void setDoctorCity(String doctorCity) {
		this.doctorCity = doctorCity;
	}
    
    
}
