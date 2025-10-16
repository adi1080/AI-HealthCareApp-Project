package com.MajorProject.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String username;
    private String emailid;
    private String password;
    private String role; // 'doctor' or 'patient/user'
    private boolean isPermitted;
    private LocalDateTime lastCancelReset;
    private int misconductScore = 0;
    private int appointmentsCanceled = 0;

    @Column(columnDefinition = "LONGTEXT")
    private String FeedbackAnalysis; // Doctors only

    @Column
    private Long lastAnalyzedFeedbackId;

    @Column(columnDefinition = "TEXT")
    private String misconductReason = "";

    public String getFeedbackAnalysis() {
        return FeedbackAnalysis;
    }

    public void setFeedbackAnalysis(String feedbackAnalysis) {
        FeedbackAnalysis = feedbackAnalysis;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isPermitted() {
        return isPermitted;
    }

    public void setPermitted(boolean permitted) {
        isPermitted = permitted;
    }

    public LocalDateTime getLastCancelReset() {
        return lastCancelReset;
    }

    public void setLastCancelReset(LocalDateTime lastCancelReset) {
        this.lastCancelReset = lastCancelReset;
    }

    public Integer getMisconductScore() {
        return misconductScore;
    }

    public void setMisconductScore(Integer misconductScore) {
        this.misconductScore = misconductScore;
    }

    public Integer getAppointmentsCanceled() {
        return appointmentsCanceled;
    }

    public void setAppointmentsCanceled(Integer appointmentsCanceled) {
        this.appointmentsCanceled = appointmentsCanceled;
    }

    public Long getLastAnalyzedFeedbackId() {
        return lastAnalyzedFeedbackId;
    }

    public void setLastAnalyzedFeedbackId(Long lastAnalyzedFeedbackId) {
        this.lastAnalyzedFeedbackId = lastAnalyzedFeedbackId;
    }

    public String getMisconductReason() {
        return misconductReason;
    }

    public void setMisconductReason(String misconductReason) {
        this.misconductReason = misconductReason;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", emailid='" + emailid + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", isPermitted=" + isPermitted +
                ", lastCancelReset=" + lastCancelReset +
                ", misconductScore=" + misconductScore +
                ", appointmentsCanceled=" + appointmentsCanceled +
                ", FeedbackAnalysis='" + FeedbackAnalysis + '\'' +
                ", lastAnalyzedFeedbackId=" + lastAnalyzedFeedbackId +
                ", misconductReason='" + misconductReason + '\'' +
                '}';
    }
}
