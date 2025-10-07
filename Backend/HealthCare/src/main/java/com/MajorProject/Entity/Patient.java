package com.MajorProject.Entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

@Entity
@Table(name = "patient")
public class Patient {
	
	@Id
	private long id;
	
	@JsonProperty("Name")  //so that it doesn't give null values since backend takes data in small letter
	private String Name;
	@JsonProperty("Age")
	private int Age;
	@JsonProperty("Gender")
	private String Gender;
	@JsonProperty("Address")
	private String Address;
	@JsonProperty("Mobileno")
	private long Mobileno;
	@JsonProperty("History")
	private String History;
    @Column(length = 5000)
    private String healthSummary;
    @Column(length = 5000)
    private String healthSuggestions;
    private Double healthScore;
    private Boolean aiAnalysisDone = false;
	
   @OneToMany(mappedBy = "patient")
   private List<Appointment> appointments;
   
   @OneToOne
   @JoinColumn(name = "user_id")
   private User user;

    @Column(name = "report_file_path")
    private String reportFilePath;

    public String getReportFilePath() {
        return reportFilePath;
    }

    public void setReportFilePath(String reportFilePath) {
        this.reportFilePath = reportFilePath;
    }

   public User getUser() {
       return user;
   }

   public void setUser(User user) {
       this.user = user;
   }

    public String getHealthSummary() {
        return healthSummary;
    }

    public void setHealthSummary(String healthSummary) {
        this.healthSummary = healthSummary;
    }

    public String getHealthSuggestions() {
        return healthSuggestions;
    }

    public void setHealthSuggestions(String healthSuggestions) {
        this.healthSuggestions = healthSuggestions;
    }

    public Double getHealthScore() {
        return healthScore;
    }

    public void setHealthScore(Double healthScore) {
        this.healthScore = healthScore;
    }

    public Boolean getAiAnalysisDone() {
        return aiAnalysisDone;
    }

    public void setAiAnalysisDone(Boolean aiAnalysisDone) {
        this.aiAnalysisDone = aiAnalysisDone;
    }

    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public int getAge() {
		return Age;
	}

	public void setAge(int age) {
		Age = age;
	}

	public String getGender() {
		return Gender;
	}

	public void setGender(String gender) {
		Gender = gender;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public long getMobileno() {
		return Mobileno;
	}

	public void setMobileno(long mobileno) {
		Mobileno = mobileno;
	}

	public String getHistory() {
		return History;
	}

	public void setHistory(String history) {
		History = history;
	}

	public List<Appointment> getAppointments() {
		return appointments;
	}

	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
	}

	@Override
	public String toString() {
		return "Patient [id=" + id + ", Name=" + Name + ", Age=" + Age + ", Gender=" + Gender + ", Address=" + Address
				+ ", Mobileno=" + Mobileno + ", History=" + History + ", appointments=" + appointments + "]";
	}

}
