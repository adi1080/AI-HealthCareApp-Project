import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PatientService } from '../../Services/patient.service';
import { MessageService } from 'src/app/Services/message.service';

declare var bootstrap: any;

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss'],
})
export class ProfileComponent implements OnInit {
  Patient: any;
  loggedInUser: any = localStorage.getItem('PatientUserId');
  file_Name:string = '';
  showHealthScorePopup: boolean = false;

  
  constructor(private router: Router, private patientSevice: PatientService, private messageService:MessageService) {}
  
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
  
  openHealthScorePopup() {
    this.showHealthScorePopup = true;
  }

  closeHealthScorePopup() {
    this.showHealthScorePopup = false;
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
        this.messageService.showMessage('Appointment canceled successfully!', 3000);
        this.Patient.appointments = this.Patient.appointments.filter(
          (appt: any) => appt.appointmentId !== appointmentId
        );
      },
      (error) => {
        console.error('Cancel failed', error);
        this.messageService.showMessage('Failed to cancel appointment.', 3000);
      }
    );
  }

  downloadReport() {
    if (!this.Patient?.reportFilePath) {
      this.messageService.showMessage('No report available for download', 3000);
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
        this.messageService.showMessage('Failed to download the report', 3000);
      }
    });
  }

  getReportFileName(): string {
    if (!this.Patient?.reportFilePath) return '';
    const fullPath = this.Patient.reportFilePath;
    const filename = fullPath.split(/[/\\]/).pop() || '';
    this.file_Name = filename.split('_').slice(1).join('_');
    return filename;
  }

}
