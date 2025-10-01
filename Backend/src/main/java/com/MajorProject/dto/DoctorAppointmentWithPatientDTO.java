package com.MajorProject.dto;

public class DoctorAppointmentWithPatientDTO {
    private long appointmentId;
    private String reason;
    private String status;
    private long availabilityId;
    private long patientId;
    private String patientName;
    private int patientAge;
    private String patientGender;
    private long patientMobile;
    private String patientHistory;
    private String reportFilePath;

    public long getAvailabilityId() {
        return availabilityId;
    }
    public void setAvailabilityId(long availabilityId) {
        this.availabilityId = availabilityId;
    }

    public long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(long appointmentId) { this.appointmentId = appointmentId; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public long getPatientId() { return patientId; }
    public void setPatientId(long patientId) { this.patientId = patientId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public int getPatientAge() { return patientAge; }
    public void setPatientAge(int patientAge) { this.patientAge = patientAge; }

    public String getPatientGender() { return patientGender; }
    public void setPatientGender(String patientGender) { this.patientGender = patientGender; }

    public long getPatientMobile() { return patientMobile; }
    public void setPatientMobile(long patientMobile) { this.patientMobile = patientMobile; }

    public String getPatientHistory() { return patientHistory; }
    public void setPatientHistory(String patientHistory) { this.patientHistory = patientHistory; }

    public String getReportFilePath() { return reportFilePath; }
    public void setReportFilePath(String reportFilePath) { this.reportFilePath = reportFilePath; }
}
