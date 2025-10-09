package com.MajorProject.Entity;

import jakarta.persistence.*;


@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
     
    private String username;
    private String emailid;
    private String password;
    private String role; // 'doctor' or 'patient/user'
    @Column(columnDefinition = "LONGTEXT")
    private String FeedbackAnalysis; // Doctors only
    @Column
    private Long lastAnalyzedFeedbackId;
    private boolean isPermitted;

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
    public Long getLastAnalyzedFeedbackId() {
        return lastAnalyzedFeedbackId;
    }
    public void setLastAnalyzedFeedbackId(Long lastAnalyzedFeedbackId) {
        this.lastAnalyzedFeedbackId = lastAnalyzedFeedbackId;
    }
    public boolean isPermitted() {
        return isPermitted;
    }
    public void setPermitted(boolean permitted) {
        isPermitted = permitted;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", emailid='" + emailid + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", FeedbackAnalysis='" + FeedbackAnalysis + '\'' +
                ", lastAnalyzedFeedbackId=" + lastAnalyzedFeedbackId +
                ", isPermitted=" + isPermitted +
                '}';
    }
}
