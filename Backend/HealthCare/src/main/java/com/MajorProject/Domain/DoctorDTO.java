package com.MajorProject.Domain;

public class DoctorDTO {
    private long id;
    private String name;
    private String about;
    private long mobileNo;
    private String gender;
    private int age;
    private String city;
    private String image; // base64 string
    private String speciality;
    private String experience;
    private String clinicName;
    private String clinicAddress;
    private double consultationFees;
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
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
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
	@Override
	public String toString() {
		return "DoctorDTO [id=" + id + ", name=" + name + ", about=" + about + ", mobileNo=" + mobileNo + ", gender="
				+ gender + ", age=" + age + ", city=" + city + ", image=" + image + ", speciality=" + speciality
				+ ", experience=" + experience + ", clinicName=" + clinicName + ", clinicAddress=" + clinicAddress
				+ ", consultationFees=" + consultationFees + "]";
	}
    
}
