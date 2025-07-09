package com.MajorProject.dto;

import java.util.List;

import com.MajorProject.model.Appointment;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.OneToMany;

public class patientProfileDTO {
	private String Name;
	private int Age;
	private String Gender;
	private String Address;
	private long Mobileno;
	private String History;
	
	private List<Appointment> appointments;

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
		return "patientProfileDTO [Name=" + Name + ", Age=" + Age + ", Gender=" + Gender + ", Address=" + Address
				+ ", Mobileno=" + Mobileno + ", History=" + History + ", appointments=" + appointments + "]";
	}
	
	
}
