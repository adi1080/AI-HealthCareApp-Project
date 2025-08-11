import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
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
  msg: string = '';

  constructor(
    private fb: FormBuilder,
    private patientService: PatientService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.AddProfileForm = this.fb.group({
      id: [this.loggedInUserId],
      Name: [],
      Age: [],
      Gender: [],
      Mobileno: [],
      History: [],
      Address: [],
    });
  }

  showMessage(message: string, duration: number) {
    // Create overlay background
    const overlayDiv = document.createElement('div');
    overlayDiv.className = `
    position-fixed top-0 start-0 w-100 h-100
    bg-dark bg-opacity-50 d-flex justify-content-center align-items-center
    fade show
  `;
    overlayDiv.style.zIndex = '1050'; // Like Bootstrap modals

    // Create message card
    const popup = document.createElement('div');
    popup.className = `
    card text-center shadow border-primary animate__animated animate__fadeIn
  `;
    popup.style.width = '22rem';
    popup.style.zIndex = '1060';
    popup.style.position = 'relative';

    // Header with close button
    const header = document.createElement('div');
    header.className =
      'card-header bg-primary text-white d-flex justify-content-between align-items-center';

    const title = document.createElement('span');
    title.textContent = 'Message';

    const closeButton = document.createElement('button');
    closeButton.innerHTML = '&times;';
    closeButton.className = 'btn-close btn-close-white';
    closeButton.style.fontSize = '1.4rem';
    closeButton.onclick = () => {
      overlayDiv.remove();
    };

    header.appendChild(title);
    header.appendChild(closeButton);

    // Body with message
    const body = document.createElement('div');
    body.className = 'card-body';

    const messageEl = document.createElement('p');
    messageEl.textContent = message;
    messageEl.className = 'card-text fs-5 fw-semibold text-dark';
    body.appendChild(messageEl);

    popup.appendChild(header);
    popup.appendChild(body);
    overlayDiv.appendChild(popup);
    document.body.appendChild(overlayDiv);

    // Auto-dismiss after duration
    setTimeout(() => {
      if (overlayDiv.parentNode) {
        overlayDiv.remove();
      }
    }, duration);
  }

  addDetails() {
    console.log(this.AddProfileForm.value);
    if (this.AddProfileForm.invalid) {
      this.showMessage('please fill out all the fields',3000);
      this.AddProfileForm.markAllAsTouched(); // Highlights all invalid fields
    }

    this.patientService.AddPatientProfile(this.AddProfileForm.value).subscribe(
      (response) => {
        if (response == 'profile already exists') {
          this.showMessage('profile already exists',3000);
          this.router.navigateByUrl('patient/profile/addInfo');
        } else {
          this.showMessage('Profile Added Successfully',3000);
          this.router.navigateByUrl('patient/profile/addInfo');
        }
      },
      (error) => {
        console.error('Error adding profile:', error);
      }
    );
  }
}
