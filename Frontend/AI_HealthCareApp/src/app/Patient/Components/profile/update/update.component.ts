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
  loggedInUserId:any = localStorage.getItem('userId');
  Patient:any;
  updateForm!:FormGroup;
  msg!:any;

  constructor(private router:Router , private fb:FormBuilder , private patientService:PatientService){}

ngOnInit(): void {
    this.updateForm = this.fb.group({
      Name:[],
      Mobileno:[],
      Gender:[],
      Age:[],
      Address:[],
      History:[]
    });

    this.patientService.FindPatientById(this.loggedInUserId).subscribe(
     (response) => {
      this.Patient = response;

     this.updateForm.patchValue({
       Name:this.Patient.Name,
       Mobileno:this.Patient.Mobileno,
       Gender:this.Patient.Gender,
       Age:this.Patient.Age,
       Address:this.Patient.Address,
       History:this.Patient.History
     });
});

}

  update(){
     this.patientService.updateprofile(this.loggedInUserId , this.updateForm.value).subscribe();
    // this.router.navigateByUrl("/patient/profile");
      this.router.navigate(['/patient/profile']);
  }
}
