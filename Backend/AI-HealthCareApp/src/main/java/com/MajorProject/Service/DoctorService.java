package com.MajorProject.Service;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.MajorProject.Repository.AppointmentRepository;
import com.MajorProject.Repository.DoctorAvailabilityRepository;
import com.MajorProject.Repository.DoctorRepository;
import com.MajorProject.dto.DoctorDTO;
import com.MajorProject.model.Doctor;
import com.MajorProject.model.DoctorAvailability;
import com.MajorProject.model.Patient;
import jakarta.transaction.Transactional;

@Service
public class DoctorService {

    private final AppointmentRepository appointmentRepository;

	@Autowired
	private DoctorRepository doctorRepository;
	
	@Autowired 
	DoctorAvailabilityRepository availabilityRepo;

	@Autowired
    DoctorService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

	  // Find doctor by ID
    public Optional<Doctor> FindDoctorById(long id) {
        return doctorRepository.findById(id);
    }

    // Save or update doctor profile
    public Doctor saveProfile(Doctor doctor) {
        return doctorRepository.save(doctor); // This will either update or insert the doctor based on whether the ID exists
    }
	
    public boolean checkprofileExistsOrNot(long id) {
    	if(doctorRepository.existsById(id)) {
    		return true;
    	}
    	else {
    		return false;
    	}
    	
    }

    public List<DoctorDTO> searchDoctors(String name, String city, String speciality) {
        name = (name != null && !name.trim().isEmpty()) ? name.trim() : null;
        city = (city != null && !city.trim().isEmpty()) ? city.trim() : null;
        speciality = (speciality != null && !speciality.trim().isEmpty()) ? speciality.trim() : null;
        List<Doctor> doctors = doctorRepository.findDoctors(name, city, speciality);
        return doctors.stream().map(this::convertToDTO).toList();
    }

    private DoctorDTO convertToDTO(Doctor doctor) {
        DoctorDTO dto = new DoctorDTO();
        dto.setId(doctor.getId());
        dto.setName(doctor.getName());
        dto.setAbout(doctor.getAbout());
        dto.setMobileNo(doctor.getMobileNo());
        dto.setGender(doctor.getGender());
        dto.setAge(doctor.getAge());
        dto.setCity(doctor.getCity());
        dto.setSpeciality(doctor.getSpeciality());
        dto.setExperience(doctor.getExperience());
        dto.setClinicName(doctor.getClinicName());
        dto.setClinicAddress(doctor.getClinicAddress());
        dto.setConsultationFees(doctor.getConsultationFees());
        // Convert image to base64 only if it exists
        if (doctor.getImage() != null) {
            String base64Image = Base64.getEncoder().encodeToString(doctor.getImage());
            dto.setImage(base64Image);
        }
        return dto;
    }

	
	public DoctorAvailability saveAvailability(DoctorAvailability availability) {
		return availabilityRepo.save(availability);
	}

	public List<DoctorAvailability> FindAllAvailabilities(long doctorId) {
		
		return availabilityRepo.findByDoctor_Id(doctorId);
	}

	 @Transactional
	 public void deleteAvailability(Long availabilityId) {
	        // 1. Delete all appointments with this availability
	        appointmentRepository.deleteByAvailabilityId(availabilityId);

	        // 2. Delete the availability itself
	        availabilityRepo.deleteById(availabilityId);
	    }

	public Optional<DoctorAvailability> findAvailability(long id) {
		return availabilityRepo.findById(id);
	}

}
