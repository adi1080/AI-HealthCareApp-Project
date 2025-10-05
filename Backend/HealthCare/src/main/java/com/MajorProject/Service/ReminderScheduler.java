package com.MajorProject.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.MajorProject.Entity.Appointment;
import com.MajorProject.Entity.Doctor;
import com.MajorProject.Entity.Patient;
import com.MajorProject.Entity.User;

@Component
public class ReminderScheduler {

	@Autowired
	private PatientService ps;

	@Autowired
	private DoctorService ds;

	@Autowired
	private EmailService emailService;

	@Scheduled(fixedRate = 60000) // every 1 minute
	public void sendAppointmentReminders() {
		List<Appointment> upcomingAppointments = ps.getAllAppointments();
		LocalDateTime now = LocalDateTime.now();
		//System.out.println("Found " + upcomingAppointments.size() + " total appointments.");

		for (Appointment appointment : upcomingAppointments) {
			LocalDateTime appointmentTime = appointment.getAvailability().getDatetime(); // assume availability has full
																							// datetime
			long minutesUntilAppointment = Duration.between(now, appointmentTime).toMinutes();

			//System.out.println("Appointment at " + appointmentTime + " in " + minutesUntilAppointment + " mins");

			if (minutesUntilAppointment <= 30 && minutesUntilAppointment >= 5 && !appointment.isReminderSent()) {

				Patient patient = appointment.getPatient();
				Doctor doctor = appointment.getDoctor();
				
		        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMM dd 'at' hh:mm a");
		        String formattedTime = appointmentTime.format(formatter);

		        String patientMessage = "Dear " + patient.getName() + ",\n\n"
		                + "This is a friendly reminder that you have an upcoming appointment in approximately "
		                + minutesUntilAppointment + " minutes.\n\n"
		                + "📅 Date & Time: " + formattedTime + "\n"
		                + "👨‍⚕️ Doctor: Dr. " + doctor.getName() + "\n"
		                + "📍 Location: " + doctor.getClinicAddress() + ", " + doctor.getCity() + "\n\n"
		                + "Please ensure you arrive a few minutes early. If you have any questions, feel free to contact us.\n\n"
		                + "Thank you,\n"
		                + "Your Healthcare Team";

		        // Optional: message for doctor
		        String doctorMessage = "Reminder: You have an upcoming appointment with patient " + patient.getName() 
		                + " on " + formattedTime + " on location " + doctor.getClinicAddress() + ", " + doctor.getCity() + ".";

				User patientUser = patient.getUser();
				User doctorUser = doctor.getUser();

				if (patientUser != null && doctorUser != null) {
					//System.out.println("Sending email to: " + patientUser.getEmailid() + " and " + doctorUser.getEmailid());
					
					emailService.sendEmail(patientUser.getEmailid(), "Appointment Reminder", patientMessage);
					emailService.sendEmail(doctorUser.getEmailid(), "Appointment Reminder", doctorMessage);

					appointment.setReminderSent(true);
					ps.saveAppointment(appointment);
					//System.out.println("Updated reminder flag for appointment ID: " + appointment.getAppointmentId());
				}

				// Mark as reminder sent (you’ll need to add this to the entity and DB)
				appointment.setReminderSent(true);
				ps.saveAppointment(appointment);
			}
		}
	}
}
