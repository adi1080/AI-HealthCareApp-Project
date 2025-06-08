import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PatientService } from '../../Services/patient.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit{
  Patient:any;
  loggedInUser:any = localStorage.getItem('userId');

constructor(private router:Router , private patientSevice:PatientService){}

  ngOnInit(): void {
       console.log(this.loggedInUser);
    this.patientSevice.FindPatientById(this.loggedInUser).subscribe(
      (response)=>{
        this.Patient = response;
        localStorage.setItem('PatientId', this.Patient.id);
      },
      (error)=>{
        console.log(error);
      }  
    );

  }

  EditDetails(){
     this.router.navigateByUrl('patient/profile/update');
  }

  addDetails(){
       this.router.navigateByUrl('patient/profile/addInfo');
  }

}
