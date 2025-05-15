import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { DoctorService } from 'src/app/Doctor/Services/doctor.service';

@Component({
  selector: 'app-add-profile',
  templateUrl: './add-profile.component.html',
  styleUrls: ['./add-profile.component.scss'],
})
export class AddProfileComponent implements OnInit {
  uid!: any;
  AddProfileForm!: FormGroup;
  selectedFile: File | null = null;
  checkProfileExistance!:any;
  msg:string ='';

  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private docService: DoctorService
  ) {}

  ngOnInit(): void {
    this.uid = localStorage.getItem('userId');
    console.log(this.uid);

    this.AddProfileForm = this.formBuilder.group({
      id:[this.uid],
      name: [],
      mobileNo: [],
      gender: [],
      age: [],
      image: [],
      experience: [],
      speciality: [],
      clinicName: [],
      clinicAddress: [],
      consultationFees: [],
    });
  }

  onFileChange(event:any){
    this.selectedFile = event.target.files[0];
  }

  addDetails() {

    if (this.AddProfileForm.invalid) {
      this.msg = 'Please fill out all required fields.';
      this.AddProfileForm.markAllAsTouched(); // Highlights all invalid fields
      return;
    }

    //formdata is used to assign key-value pairs
    const formData = new FormData();
    formData.append('id', this.AddProfileForm.get('id')?.value);
    formData.append('name', this.AddProfileForm.get('name')?.value);
    formData.append('mobileNo', this.AddProfileForm.get('mobileNo')?.value);
    formData.append('age', this.AddProfileForm.get('age')?.value);
    formData.append('gender', this.AddProfileForm.get('gender')?.value);
    formData.append('experience', this.AddProfileForm.get('experience')?.value);
    formData.append('speciality', this.AddProfileForm.get('speciality')?.value);
    formData.append('image', this.selectedFile as File);
    formData.append('clinicName', this.AddProfileForm.get('clinicName')?.value);
    formData.append('clinicAddress', this.AddProfileForm.get('clinicAddress')?.value);
    formData.append('consultationFees', this.AddProfileForm.get('consultationFees')?.value);

    this.docService.addDoctorProfile(formData).subscribe(
      response => {
        console.log(response);
        if(response == "profile already exists"){
          this.msg = "profile already exists";
          this.router.navigateByUrl('doctor/profile/addInfo');
        }
        else{
          this.msg = "Profile Added Successfully";
          this.router.navigateByUrl('doctor/profile/addInfo');
        }
      },
      error => {
        console.error('Error adding profile:', error);
      } 
    );
  }

}
