import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { PatientService } from 'src/app/Patient/Services/patient.service';

@Component({
  selector: 'app-add-info',
  templateUrl: './add-info.component.html',
  styleUrls: ['./add-info.component.scss']
})
export class AddInfoComponent implements OnInit{
  AddProfileForm!:FormGroup; 
  loggedInUserId:any = localStorage.getItem('userId');
  msg:string = '';

  constructor(private fb:FormBuilder , private patientService:PatientService , private router:Router){}

ngOnInit(): void {
    
  this.AddProfileForm = this.fb.group({
    id:[this.loggedInUserId],
    Name:[],
    Age:[],
    Gender:[],
    Mobileno:[],
    History:[],
    Address:[]
  });
}

addDetails(){
   console.log(this.AddProfileForm.value);
  if(this.AddProfileForm.invalid){
    this.msg = "please fill out all the fields";
    this.AddProfileForm.markAllAsTouched(); // Highlights all invalid fields
  }

  this.patientService.AddPatientProfile(this.AddProfileForm.value).subscribe(
    (response) =>{
      if(response == "profile already exists"){
          this.msg = "profile already exists";
          this.router.navigateByUrl('patient/profile/addInfo');
        }
        else{
          this.msg = "Profile Added Successfully";
          this.router.navigateByUrl('patient/profile/addInfo');
          
        }
      },
      error => {
        console.error('Error adding profile:', error);
      } 
  );
}

}
