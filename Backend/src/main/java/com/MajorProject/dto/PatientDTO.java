package com.MajorProject.dto;

import java.util.List;

public class PatientDTO {
    private long id;
    private String name;
    private int age;
    private String gender;
    private String address;
    private long mobileno;
    private String history;
    private List<AppointmentDTO> appointments;
    private String reportFilePath;

    public String getReportFilePath() {
        return reportFilePath;
    }
    public void setReportFilePath(String reportFilePath) {
        this.reportFilePath = reportFilePath;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public long getMobileno() {
        return mobileno;
    }
    public void setMobileno(long mobileno) {
        this.mobileno = mobileno;
    }
    public String getHistory() {
        return history;
    }
    public void setHistory(String history) {
        this.history = history;
    }
    public List<AppointmentDTO> getAppointments() {
        return appointments;
    }
    public void setAppointments(List<AppointmentDTO> appointments) {
        this.appointments = appointments;
    }
}
