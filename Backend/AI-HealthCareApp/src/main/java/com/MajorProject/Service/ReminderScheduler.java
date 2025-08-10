package com.MajorProject.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.MajorProject.model.Appointment;
import com.MajorProject.model.Doctor;
import com.MajorProject.model.Patient;
import com.MajorProject.model.User;

@Component
public class ReminderScheduler {

	@Autowired
	private PatientService ps;

	@Autowired
	private DoctorService ds;

	@Autowired
	private EmailService emailService;

	@Autowired
	private NotificationService notificationService;

	@Scheduled(fixedRate = 60000) // every 1 minute
	public void sendAppointmentReminders() {
		System.out.println("Reminder check running...");
		List<Appointment> upcomingAppointments = ps.getAllAppointments();
		LocalDateTime now = LocalDateTime.now();
		System.out.println("Found " + upcomingAppointments.size() + " total appointments.");

		for (Appointment appointment : upcomingAppointments) {
			LocalDateTime appointmentTime = appointment.getAvailability().getDatetime(); // assume availability has full
																							// datetime
			long minutesUntilAppointment = Duration.between(now, appointmentTime).toMinutes();

			System.out.println("Appointment at " + appointmentTime + " in " + minutesUntilAppointment + " mins");

			if (minutesUntilAppointment <= 30 && minutesUntilAppointment >= 25 && !appointment.isReminderSent()) {

				Patient patient = appointment.getPatient();
				Doctor doctor = appointment.getDoctor();

				String message = "Reminder: Your appointment is in 30 minutes at " + appointmentTime;

				User patientUser = patient.getUser();
				User doctorUser = doctor.getUser();

				if (patientUser != null && doctorUser != null) {
					System.out.println("Sending email to: " + patientUser.getEmailid() + " and " + doctorUser.getEmailid());
					
					emailService.sendEmail(patientUser.getEmailid(), "Appointment Reminder", message);
					emailService.sendEmail(doctorUser.getEmailid(), "Appointment Reminder", message);

					notificationService.sendNotification("patient", patient.getId(), message);
					notificationService.sendNotification("doctor", doctor.getId(), message);

					appointment.setReminderSent(true);
					ps.saveAppointment(appointment);
					System.out.println("Updated reminder flag for appointment ID: " + appointment.getAppointmentId());
				}

				// Notification
				notificationService.sendNotification("patient", patient.getId(), message);
				notificationService.sendNotification("doctor", doctor.getId(), message);

				// Mark as reminder sent (you’ll need to add this to the entity and DB)
				appointment.setReminderSent(true);
				ps.saveAppointment(appointment);
			}
		}
	}
}
