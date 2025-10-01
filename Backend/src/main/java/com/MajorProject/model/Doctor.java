package com.MajorProject.model;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Doctor {

	@Id
	private long id;
	
	private String name;
	private String about;
	private long mobileNo;
	private String gender;
	private int age;
	private String city;
	
	@Lob
	private byte[] image;
	
	private String speciality;
	private String experience;
	private String clinicName;
	private String clinicAddress;
	private double consultationFees;
	
	@OneToMany(mappedBy = "doctor")
	private List<DoctorAvailability> availability;
	
	@OneToMany(mappedBy = "doctor")
	private List<Appointment> appointments;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	public User getUser() {
	    return user;
	}

	public void setUser(User user) {
	    this.user = user;
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

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public long getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(long mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getSpeciality() {
		return speciality;
	}

	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public String getClinicName() {
		return clinicName;
	}

	public void setClinicName(String clinicName) {
		this.clinicName = clinicName;
	}

	public String getClinicAddress() {
		return clinicAddress;
	}

	public void setClinicAddress(String clinicAddress) {
		this.clinicAddress = clinicAddress;
	}

	public double getConsultationFees() {
		return consultationFees;
	}

	public void setConsultationFees(double consultationFees) {
		this.consultationFees = consultationFees;
	}

	public List<DoctorAvailability> getAvailability() {
		return availability;
	}

	public void setAvailability(List<DoctorAvailability> availability) {
		this.availability = availability;
	}

	public List<Appointment> getAppointments() {
		return appointments;
	}

	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
	}

	@Override
	public String toString() {
	    return "Doctor [id=" + id + ", name=" + name + ", about=" + about + ", mobileNo=" + mobileNo + ", gender=" 
	            + gender + ", age=" + age + ", city=" + city + ", image=" + Arrays.toString(image) 
	            + ", speciality=" + speciality + ", experience=" + experience + ", clinicName=" + clinicName 
	            + ", clinicAddress=" + clinicAddress + ", consultationFees=" + consultationFees + "]";
	}


	
}
