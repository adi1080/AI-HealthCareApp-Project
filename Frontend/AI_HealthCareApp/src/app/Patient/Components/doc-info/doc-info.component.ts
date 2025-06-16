import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { DoctorService } from 'src/app/Doctor/Services/doctor.service';
import { PatientService } from '../../Services/patient.service';

@Component({
  selector: 'app-doc-info',
  templateUrl: './doc-info.component.html',
  styleUrls: ['./doc-info.component.scss']
})
export class DocInfoComponent implements OnInit{
DoctorId:any;
doctorInfo:any;
doctorImage:any;
appointmentForm!:FormGroup;
bookingAppointment:boolean = false;
LoggedInPatient:any = localStorage.getItem("PatientId");
availabilitySlots:{id:any , date: string, time: string, isBooked: boolean }[] = [];
bookingReason:string = "";
selectedtime:any;
message: {[slotId: string]: string } = {}

constructor(private doctorService:DoctorService , private _router:Router , private fb:FormBuilder , private patientService:PatientService){}

  ngOnInit(): void {
    this.DoctorId = localStorage.getItem("DocId");
    console.log("doctor id : "+this.DoctorId + "And patient id is : " + this.LoggedInPatient);

    this.doctorService.FindById(this.DoctorId).subscribe(
      response =>{
        console.log(response);
         this.doctorInfo = response;
         this.doctorImage = 'data:image/jpeg;base64,' + this.doctorInfo.image;
      }
    );

    this.doctorService.FindAllAvailability(this.DoctorId).subscribe(
            response => {
        console.log('Fetched availability:', response);
        // Clear the existing availabilityList before adding new items
        this.availabilitySlots = [];
        this.availabilitySlots = response.map((item: any) => ({
          id: item.id,
          date: item.date,
          time: item.time,
          isBooked: item.isBooked
        }));

        // Sort the availabilityList by date and time
        this.availabilitySlots.sort((a, b) => {
          const dateA = new Date(`${a.date}T${a.time}`);
          const dateB = new Date(`${b.date}T${b.time}`);
          return dateA.getTime() - dateB.getTime();
        });
      },
      error => {
          console.error("Error fetching availability slots:", error);
      }
    )

    this.appointmentForm = this.fb.group({
      reason: []
    });
  }

  popup(){
    this.bookingAppointment = true;
  }

  onSubmit(){
      if (this.appointmentForm.invalid || !this.selectedtime) {
      alert('Please fill in all fields and select a slot.');
      return;
    }

    const formValue = this.appointmentForm.value;

    const appointment = {
      reason: formValue.reason,
      doctor: { id: this.doctorInfo.id },
      patient: { id: Number(this.LoggedInPatient) }, // ✅ convert string to number
      availability: { id: this.selectedtime },
      status: 'PENDING'
    };
    console.log("Appointment details:", appointment);

    this.patientService.BookAppointment(appointment).subscribe({
      next: res => {
        alert('Appointment booked!');
        this.bookingAppointment = false;
        this.appointmentForm.reset();
        window.location.reload();
      },
      error: error => {
        console.error(error);
        alert('Booking failed.');
      }
    });
  }

  open(TimeSlotId:any){
  this.selectedtime = TimeSlotId;
  // Clear previous messages
  this.message = {};
  // Set message only for selected slot
  this.message[TimeSlotId] = 'Slot selected';
  }
}
