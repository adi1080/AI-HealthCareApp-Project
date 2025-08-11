import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { PatientService } from 'src/app/Patient/Services/patient.service';

@Component({
  selector: 'app-update',
  templateUrl: './update.component.html',
  styleUrls: ['./update.component.scss']
})
export class UpdateComponent implements OnInit{
  loggedInUserId:any = localStorage.getItem('PatientUserId');
  Patient:any;
  updateForm!:FormGroup;
  msg!:any;

  constructor(private router:Router , private fb:FormBuilder , private patientService:PatientService){}

ngOnInit(): void {
    this.updateForm = this.fb.group({
      name:[],
      mobileno:[],
      gender:[],
      age:[],
      address:[],
      history:[]
    });

    this.patientService.FindPatientById(this.loggedInUserId).subscribe(
     (response) => {
      this.Patient = response;

     this.updateForm.patchValue({
       name:this.Patient.name,
       mobileno:this.Patient.mobileno,
       gender:this.Patient.gender,
       age:this.Patient.age,
       address:this.Patient.address,
       history:this.Patient.history
     });
});

}

  update(){
     this.patientService.updateprofile(this.loggedInUserId , this.updateForm.value).subscribe();
    // this.router.navigateByUrl("/patient/profile");
      this.router.navigate(['/patient/profile']);
  }
}
