import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { DoctorService } from 'src/app/Doctor/Services/doctor.service';

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
LoggedInPatient = localStorage.getItem("PatientId");
availabilitySlots:{id:any , date: string, time: string, isBooked: boolean }[] = [];
bookingReason:string = "";


constructor(private doctorService:DoctorService , private _router:Router , private fb:FormBuilder){}

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
  }

  popup(){
    this.bookingAppointment = true;
  }

  confirmBooking(){

  }

  open(timeslotid:any){

  }
}
