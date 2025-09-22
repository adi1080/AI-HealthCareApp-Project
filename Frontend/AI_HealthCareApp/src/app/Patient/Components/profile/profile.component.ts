import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PatientService } from '../../Services/patient.service';

declare var bootstrap: any;

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss'],
})
export class ProfileComponent implements OnInit {
  Patient: any;
  loggedInUser: any = localStorage.getItem('PatientUserId');

  constructor(private router: Router, private patientSevice: PatientService) {}

  ngOnInit(): void {
    console.log(this.loggedInUser);
    this.patientSevice.FindPatientById(this.loggedInUser).subscribe(
      (response) => {
        console.log(response);
        this.Patient = response;
        localStorage.setItem('PatientId', this.Patient.id);
      },
      (error) => {
        console.log(error);
      }
    );
  }

  EditDetails() {
    this.router.navigateByUrl('patient/profile/update');
  }

  addDetails() {
    this.router.navigateByUrl('patient/profile/addInfo');
  }

  openAppointmentsModal() {
    const modalElement = document.getElementById('appointmentsModal');
    if (modalElement) {
      const modal = new bootstrap.Modal(modalElement);
      modal.show();
    }
  }

  cancelAppointment(appointmentId: number) {
    this.patientSevice.cancelAppointment(appointmentId).subscribe(
      (response) => {
        this.showMessage('Appointment canceled successfully!', 3000);
        this.Patient.appointments = this.Patient.appointments.filter(
          (appt: any) => appt.appointmentId !== appointmentId
        );
      },
      (error) => {
        console.error('Cancel failed', error);
        this.showMessage('Failed to cancel appointment.', 3000);
      }
    );
  }

  downloadReport() {
    if (!this.Patient?.reportFilePath) {
      this.showMessage('No report available for download', 3000);
      return;
    }

    const fileName = this.getReportFileName();

    this.patientSevice.downloadReport(fileName).subscribe({
      next: (blob: Blob) => {
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = fileName;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        window.URL.revokeObjectURL(url);
      },
      error: (err) => {
        console.error('Download failed', err);
        this.showMessage('Failed to download the report', 3000);
      }
    });
  }

  getReportFileName(): string {
    if (!this.Patient?.reportFilePath) return '';
    const fullPath = this.Patient.reportFilePath;
    const filename = fullPath.split(/[/\\]/).pop() || '';
    return filename.split('_').slice(1).join('_');
  }

  showMessage(message: string, duration: number) {
    const overlayDiv = document.createElement('div');
    overlayDiv.className = `
      position-fixed top-0 start-0 w-100 h-100
      bg-dark bg-opacity-50 d-flex justify-content-center align-items-center
      fade show
    `;
    overlayDiv.style.zIndex = '11000';

    const popup = document.createElement('div');
    popup.className = 'card text-center shadow border-primary animate__animated animate__fadeIn';
    popup.style.width = '22rem';
    popup.style.zIndex = '11001';
    popup.style.position = 'relative';

    const header = document.createElement('div');
    header.className = 'card-header bg-primary text-white d-flex justify-content-between align-items-center';
    const title = document.createElement('span');
    title.textContent = 'Message';
    const closeButton = document.createElement('button');
    closeButton.innerHTML = '&times;';
    closeButton.className = 'btn-close btn-close-white';
    closeButton.style.fontSize = '1.4rem';
    closeButton.onclick = () => overlayDiv.remove();

    header.appendChild(title);
    header.appendChild(closeButton);

    const body = document.createElement('div');
    body.className = 'card-body';
    const messageEl = document.createElement('p');
    messageEl.textContent = message;
    messageEl.className = 'card-text fs-5 fw-semibold text-dark';
    body.appendChild(messageEl);

    popup.appendChild(header);
    popup.appendChild(body);
    overlayDiv.appendChild(popup);
    document.body.appendChild(overlayDiv);

    setTimeout(() => {
      if (overlayDiv.parentNode) overlayDiv.remove();
    }, duration);
  }
}
