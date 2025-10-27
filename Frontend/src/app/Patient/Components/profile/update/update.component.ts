import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { PatientService } from 'src/app/Patient/Services/patient.service';

@Component({
  selector: 'app-update',
  templateUrl: './update.component.html',
  styleUrls: ['./update.component.scss']
})
export class UpdateComponent implements OnInit {
  loggedInUserId: any = localStorage.getItem('PatientUserId');
  Patient: any;
  updateForm!: FormGroup;
  msg!: any;
  selectedFile!: File | null;

  constructor(private router: Router, private fb: FormBuilder, private patientService: PatientService) { }

  ngOnInit(): void {
    this.updateForm = this.fb.group({
      name: [],
      mobileno: [],
      gender: [],
      age: [],
      address: [],
      history: []
    });

    this.patientService.FindPatientById(this.loggedInUserId).subscribe(
      (response) => {
        this.Patient = response;

        this.updateForm.patchValue({
          name: this.Patient.name,
          mobileno: this.Patient.mobileno,
          gender: this.Patient.gender,
          age: this.Patient.age,
          address: this.Patient.address,
          history: this.Patient.history
        });
      });

  }

  onFileSelected(event: any) {
    const file: File = event.target.files[0];
    if (file) {
      const allowedTypes = ['application/pdf',
        'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
        'text/plain'];

      if (!allowedTypes.includes(file.type)) {
        this.msg = '❌ Invalid file type. Please upload only PDF, DOCX, or TXT.';
        this.selectedFile = null;
        (event.target as HTMLInputElement).value = ''; // Reset file input
        return;
      }

      this.msg = ''; // Clear message if valid
      this.selectedFile = file;
    }
  }


  update() {
    const patientData = this.updateForm.value;
    const formData = new FormData();

    formData.append('patient', JSON.stringify(patientData));
    if (this.selectedFile) {
      formData.append('report', this.selectedFile);
    }

    this.patientService.updateprofile(this.loggedInUserId, formData).subscribe(
      (response) => {
        console.log('Profile updated successfully:', response);
        this.router.navigate(['/patient/profile']);
      },
      (error) => {
        console.error('Error updating profile:', error);
      }
    );
  }
}
