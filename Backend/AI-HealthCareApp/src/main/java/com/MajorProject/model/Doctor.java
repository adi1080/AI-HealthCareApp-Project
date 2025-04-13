package com.MajorProject.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Doctor {

	@Id
	private long id;
	
	private String name;
	private long mobileNo;
	private String gender;
	private int age;
	
	@Lob
	private byte[] image;
	
	private String speciality;
	private String experience;
	private String clinicName;
	private String clinicAddress;
	private double consultationFees;
	
	@OneToMany(mappedBy = "doctor")
	private List<Patient> patients;
	
}
