import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { PatientService } from 'src/app/Patient/Services/patient.service';

@Component({
  selector: 'app-add-info',
  templateUrl: './add-info.component.html',
  styleUrls: ['./add-info.component.scss'],
})
export class AddInfoComponent implements OnInit {
  AddProfileForm!: FormGroup;
  loggedInUserId: any = localStorage.getItem('PatientUserId');
  file: File | null = null;
  fileError: string = '';
  message: string = '';
  errorMessage: boolean = false;
  isSubmitting: boolean = false;

  constructor(
    private fb: FormBuilder,
    private patientService: PatientService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.AddProfileForm = this.fb.group({
      id: [this.loggedInUserId],
      Name: ['', [Validators.required, Validators.minLength(3)]],
      Age: [null, [Validators.required, Validators.min(1), Validators.max(120)]],
      Gender: ['', Validators.required],
      Mobileno: [
        '',
        [Validators.required, Validators.pattern(/^[0-9]{10}$/)],
      ],
      Address: ['', Validators.required],
      History: ['', Validators.required],
    });
  }

  onFileSelected(event: any): void {
    const selectedFile: File = event.target.files[0];
    const allowedTypes = ['application/pdf', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document', 'text/csv'];

    if (selectedFile && selectedFile.size > 5 * 1024 * 1024) {
      this.fileError = 'File size should not exceed 5MB';
      this.file = null;
    } else if (selectedFile && !allowedTypes.includes(selectedFile.type)) {
      this.fileError = 'Invalid file type. Only PDF, DOCX, CSV are allowed.';
      this.file = null;
    } else {
      this.file = selectedFile;
      this.fileError = '';
    }
  }

  showMessage(message: string, duration: number): void {
    this.message = message;
    this.errorMessage = message.toLowerCase().includes('already');
    setTimeout(() => {
      this.message = '';
    }, duration);
  }

  addDetails(): void {
    if (this.AddProfileForm.invalid || this.fileError) {
      this.AddProfileForm.markAllAsTouched();
      this.showMessage('Please correct the form errors', 3000);
      return;
    }

    this.isSubmitting = true;

    console.log("sending value to save profile in backend : ",this.AddProfileForm.value , this.file);
    this.patientService.addPatientProfile(this.AddProfileForm.value, this.file).subscribe(
        (response) => {
          this.isSubmitting = false;
          if (response === 'Profile already exists') {
            this.showMessage('Profile already exists', 3000);
          } else {
            this.showMessage('Profile Added Successfully', 3000);
            this.router.navigateByUrl('patient/profile/addInfo');
          }
        },
        (error) => {
          console.error('Error adding profile:', error);
          this.isSubmitting = false;
          this.showMessage('Something went wrong', 3000);
        }
      );
  }
}
