import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { DoctorService } from 'src/app/Doctor/Services/doctor.service';

@Component({
  selector: 'app-update',
  templateUrl: './update.component.html',
  styleUrls: ['./update.component.scss'],
})
export class UpdateComponent implements OnInit {
  loggedInUserId: any = localStorage.getItem('DoctorUserId');
  Doctor: any;
  doctorImage: any;
  selectedFile: File | null = null;
  updateForm!: FormGroup;
  msg: string = '';
  imageChangedEvent: any = '';
  croppedImage: any = '';
  rotation = 0;
  isGrayscale = false;

  constructor(
    private docService: DoctorService,
    private router: Router,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.updateForm = this.fb.group({
      name: [],
      about: [],
      mobileNo: [],
      gender: [],
      age: [],
      city: [],
      image: [],
      speciality: [],
      experience: [],
      clinicName: [],
      clinicAddress: [],
      consultationFees: [],
    });

    this.docService.FindById(this.loggedInUserId).subscribe((response) => {
      this.Doctor = response;
      this.doctorImage = 'data:image/jpeg;base64,' + this.Doctor.image;

      this.updateForm.patchValue({
        name: this.Doctor.name,
        about: this.Doctor.about,
        mobileNo: this.Doctor.mobileNo,
        gender: this.Doctor.gender,
        age: this.Doctor.age,
        city: this.Doctor.city,
        speciality: this.Doctor.speciality,
        experience: this.Doctor.experience,
        clinicName: this.Doctor.clinicName,
        clinicAddress: this.Doctor.clinicAddress,
        consultationFees: this.Doctor.consultationFees,
      });
    });
  }

  // onFileChange(event: any) {
  //   this.selectedFile = event.target.files[0];
  // }

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

  update() {
    const formData = new FormData();

    // Append each field
    formData.append('name', this.updateForm.get('name')?.value);
    formData.append('about', this.updateForm.get('about')?.value);
    formData.append('mobileNo', this.updateForm.get('mobileNo')?.value);
    formData.append('gender', this.updateForm.get('gender')?.value);
    formData.append('age', this.updateForm.get('age')?.value);
    formData.append('city', this.updateForm.get('city')?.value);
    formData.append('speciality', this.updateForm.get('speciality')?.value);
    formData.append('experience', this.updateForm.get('experience')?.value);
    formData.append('clinicName', this.updateForm.get('clinicName')?.value);
    formData.append(
      'clinicAddress',
      this.updateForm.get('clinicAddress')?.value
    );
    formData.append(
      'consultationFees',
      this.updateForm.get('consultationFees')?.value
    );

    // Only append image if new one selected
    if (this.selectedFile) {
      formData.append('image', this.selectedFile);
    }

    this.docService.updateDoctor(this.loggedInUserId, formData).subscribe(
      (response) => {
        this.showMessage('Details updated successfully',3000);
        this.router.navigate(['/doctor/profile']); // Navigate wherever you want
      },
      (error) => {
        console.error(error);
        this.showMessage('Error updating doctor details',3000);
      }
    );
  }

  onFileChange(event: any): void {
    this.imageChangedEvent = event;
  }

  onImageCropped(event: any): void {
    this.croppedImage = event.base64;

    // Convert base64 to file for upload
    const file = this.dataURLtoFile(event.base64, 'cropped-image.png');
    this.selectedFile = file;
  }

  onImageLoaded(): void {
    console.log('Image loaded');
  }

  onCropperReady(): void {
    console.log('Cropper ready');
  }

  onLoadImageFailed(): void {
    console.error('Load failed');
  }

  rotateImage(): void {
    this.rotation = (this.rotation + 90) % 360;
    document
      .querySelector('image-cropper')
      ?.setAttribute('style', `transform: rotate(${this.rotation}deg);`);
  }

  toggleGrayscale(): void {
    this.isGrayscale = !this.isGrayscale;
  }

  get imageFilter(): string {
    return this.isGrayscale ? 'grayscale(100%)' : 'none';
  }

  // Helper to convert base64 to File
  dataURLtoFile(dataurl: string, filename: string): File {
    const arr = dataurl.split(',');
    const mime = arr[0].match(/:(.*?);/)![1];
    const bstr = atob(arr[1]);
    let n = bstr.length;
    const u8arr = new Uint8Array(n);
    while (n--) {
      u8arr[n] = bstr.charCodeAt(n);
    }
    return new File([u8arr], filename, { type: mime });
  }
}
