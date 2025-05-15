package com.MajorProject.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "patient")
public class Patient {
	
	@Id
	private long id;
	
	@JsonProperty("Name")
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
	
	@ManyToOne
	private Doctor doctor;

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

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	@Override
	public String toString() {
		return "Patient [id=" + id + ", Name=" + Name + ", Age=" + Age + ", Gender=" + Gender + ", Address=" + Address
				+ ", Mobileno=" + Mobileno + ", History=" + History + ", doctor=" + doctor + "]";
	}

	

}
