import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { DoctorService } from 'src/app/Doctor/Services/doctor.service';

@Component({
  selector: 'app-update',
  templateUrl: './update.component.html',
  styleUrls: ['./update.component.scss']
})
export class UpdateComponent implements OnInit {
  loggedInUserId: any = localStorage.getItem('userId');
  Doctor: any;
  doctorImage: any;
  selectedFile: File | null = null;
  updateForm!: FormGroup;
  msg: string = '';

  constructor(private docService: DoctorService, private router: Router, private fb: FormBuilder) { }

  ngOnInit(): void {
    this.updateForm = this.fb.group(
      {
        name: [],
        mobileNo: [],
        gender: [],
        age: [],
        image: [],
        speciality: [],
        experience: [],
        clinicName: [],
        clinicAddress: [],
        consultationFees: []
      });


    this.docService.FindById(this.loggedInUserId).subscribe(
      response => {
        this.Doctor = response;
        this.doctorImage = 'data:image/jpeg;base64,' + this.Doctor.image;

        this.updateForm.patchValue({
          name: this.Doctor.name,
          mobileNo: this.Doctor.mobileNo,
          gender: this.Doctor.gender,
          age: this.Doctor.age,
          speciality: this.Doctor.speciality,
          experience: this.Doctor.experience,
          clinicName: this.Doctor.clinicName,
          clinicAddress: this.Doctor.clinicAddress,
          consultationFees: this.Doctor.consultationFees
        });
      });


  }

  onFileChange(event: any) {
    this.selectedFile = event.target.files[0];
  }

  update() {
    const formData = new FormData();

    // Append each field
    formData.append('name', this.updateForm.get('name')?.value);
    formData.append('mobileNo', this.updateForm.get('mobileNo')?.value);
    formData.append('gender', this.updateForm.get('gender')?.value);
    formData.append('age', this.updateForm.get('age')?.value);
    formData.append('speciality', this.updateForm.get('speciality')?.value);
    formData.append('experience', this.updateForm.get('experience')?.value);
    formData.append('clinicName', this.updateForm.get('clinicName')?.value);
    formData.append('clinicAddress', this.updateForm.get('clinicAddress')?.value);
    formData.append('consultationFees', this.updateForm.get('consultationFees')?.value);

    // Only append image if new one selected
    if (this.selectedFile) {
      formData.append('image', this.selectedFile);
    }

    this.docService.updateDoctor(this.loggedInUserId, formData).subscribe(
      response => {
        this.msg = 'Details updated successfully';
        this.router.navigate(['/doctor/profile']); // Navigate wherever you want
      },
      error => {
        console.error(error);
        this.msg = 'Error updating doctor details';
      }
    );
  }

}
