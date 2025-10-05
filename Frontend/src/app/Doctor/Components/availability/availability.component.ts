import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DoctorService } from '../../Services/doctor.service';
import { PatientService } from 'src/app/Patient/Services/patient.service';

@Component({
  selector: 'app-availability',
  templateUrl: './availability.component.html',
  styleUrls: ['./availability.component.scss']
})
export class AvailabilityComponent implements OnInit {
  availabilityList: { id: any, date: string, time: string, isBooked: boolean }[] = [];
  availabilityForm!: FormGroup;
  setAvailability = false;
  doctorId: any = localStorage.getItem("DoctorUserId");
  appointments: any;
  showPatientDetailsModal = false;
  selectedPatient: any = null;
  isHovered:boolean= false;

  constructor(private fb: FormBuilder, private docService: DoctorService, private patientService: PatientService) { }

  ngOnInit(): void {
    // Initialize the form with a dateTime field
    this.availabilityForm = this.fb.group({
      dateTime: ['', Validators.required]
    });
    console.log(this.doctorId);

    this.findAllAvailability();

    this.docService.getAppointmentsWithPatients(this.doctorId).subscribe({
      next: (data) => {
        this.appointments = data;
        console.log('Appointments with patient details:', this.appointments);
      },
      error: (err) => {
        console.error('Error fetching appointments:', err);
      }
    });

  }

  openPatientDetails(availabilityId: any): void {
    console.log('Clicked availability ID:', availabilityId);

    const appointment = this.appointments?.find((appt: any) => +appt.availabilityId === +availabilityId);

    if (appointment) {
      this.selectedPatient = {
        name: appointment.patientName,
        age: appointment.patientAge,
        gender: appointment.patientGender,
        mobileno: appointment.patientMobile,
        history: appointment.patientHistory,
        reportFilePath: appointment.reportFilePath,
        reason: appointment.reason,
        status: appointment.status
      };
      this.showPatientDetailsModal = true;
    } else {
      console.warn('No appointment found for availability ID:', availabilityId);
    }
  }

  getDownloadUrl(fileName: string): string {
    return `http://localhost:9090/Patient/download-report/${fileName}`;
  }

  downloadFile(filename: string) {
    this.patientService.downloadReport(filename).subscribe({
      next: (blob) => {
        // Create a downloadable URL and simulate click
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = filename;  // suggested filename
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        window.URL.revokeObjectURL(url);
      },
      error: (err) => {
        console.error('Download failed', err);
      }
    });
  }

    getFileNameFromPath(fullPath: string): string {
      return fullPath.split(/[/\\]/).pop() || '';
    }

    closePatientDetails(): void {
      this.showPatientDetailsModal = false;
      this.selectedPatient = null;
    }

    popupAvailabilityInput(): void {
      this.setAvailability = true;
    }

    addAvailability(): void {
      const dateTime = this.availabilityForm.get('dateTime')?.value;
      if(dateTime) {
        const [date, time] = dateTime.split('T');

        const availability = {
          date,
          time,
          isBooked: false
        };

        console.log("availability =>", availability);
        this.docService.addAvailability(this.doctorId, availability).subscribe(
          response => {
            console.log(response);
          },
          error => {
            console.error('Error adding availability:', error);
          }
        );
      }

    this.setAvailability = false;
      setTimeout(() => {
      window.location.reload();
    }, 500);
    this.findAllAvailability();
  }

  findAllAvailability() {
    console.log("finding availability for doctor id : " , this.doctorId);
    this.docService.FindAllAvailability(this.doctorId).subscribe(
      response => {
        console.log('Fetched availability:', response);
        // Clear the existing availabilityList before adding new items
        this.availabilityList = [];
        this.availabilityList = response.map((item: any) => ({
          id: item.id,
          date: item.date,
          time: item.time,
          isBooked: item.isBooked
        }));
        console.log("availabilityList =>", this.availabilityList);

        // Sort the availabilityList by date and time
        this.availabilityList.sort((a, b) => {
          const dateA = new Date(`${a.date}T${a.time}`);
          const dateB = new Date(`${b.date}T${b.time}`);
          return dateA.getTime() - dateB.getTime();
        });
      }
    )
  }

  removeAvailability(index: number): void {
    const availabilityId = this.availabilityList[index].id;
    console.log("availabilityId =>", availabilityId);
    this.docService.DeleteAvailabilityById(availabilityId).subscribe();
    window.location.reload();
  }
}
