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

  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private docService: DoctorService
  ) {}

  ngOnInit(): void {
    var userId = localStorage.getItem('userId'); // Get userId from localStorage
    console.log('User ID from localStorage:', userId); // Log userId to verify it is being retrieved
    this.uid = userId;

    this.AddProfileForm = this.formBuilder.group({
      id:[this.uid],
      name: [null],
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

  addDetailsById() {
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

    console.log(formData);
    this.docService.addDoctorProfile(formData).subscribe(
      response => {
        console.log('Profile added successfully:', response);
      },
      error => {
        console.error('Error adding profile:', error);
      }
    );
   
    this.router.navigateByUrl('profile');
  }

}
