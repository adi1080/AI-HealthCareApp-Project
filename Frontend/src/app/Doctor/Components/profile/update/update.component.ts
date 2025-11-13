import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { DoctorService } from 'src/app/Doctor/Services/doctor.service';
import { MessageService } from 'src/app/Services/message.service';

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
    private fb: FormBuilder,
    private messageService:MessageService
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
        this.messageService.showMessage('Details updated successfully',3000);
        this.router.navigate(['/doctor/profile']); // Navigate wherever you want
      },
      (error) => {
        console.error(error);
        this.messageService.showMessage('Error updating doctor details',3000);
      }
    );
  }

  onFileChange(event: any): void {
    this.imageChangedEvent = event;
  }

  onImageCropped(event: any): void {
    const base64 = event.base64;
    this.applyTransformations(base64).then((transformedBase64) => {
      this.croppedImage = transformedBase64;
      this.selectedFile = this.dataURLtoFile(
        transformedBase64,
        'transformed-image.png'
      );
    });
  }

  applyTransformations(base64: string): Promise<string> {
    return new Promise((resolve) => {
      const img = new Image();
      img.src = base64;

      img.onload = () => {
        const canvas = document.createElement('canvas');
        const ctx = canvas.getContext('2d')!;
        const angle = this.rotation;

        // Set canvas size based on rotation
        if (angle % 180 === 0) {
          canvas.width = img.width;
          canvas.height = img.height;
        } else {
          canvas.width = img.height;
          canvas.height = img.width;
        }

        // Move origin to center and rotate
        ctx.translate(canvas.width / 2, canvas.height / 2);
        ctx.rotate((angle * Math.PI) / 180);

        // Apply grayscale filter if needed
        if (this.isGrayscale) {
          ctx.filter = 'grayscale(100%)';
        }

        // Draw image rotated
        ctx.drawImage(img, -img.width / 2, -img.height / 2);

        // Return new base64
        resolve(canvas.toDataURL('image/png'));
      };
    });
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
    this.applyTransformations(this.croppedImage).then((transformedBase64) => {
      this.croppedImage = transformedBase64;
      this.selectedFile = this.dataURLtoFile(
        transformedBase64,
        'rotated-image.png'
      );
    });
  }

  toggleGrayscale(): void {
    this.isGrayscale = !this.isGrayscale;
    this.applyTransformations(this.croppedImage).then((transformedBase64) => {
      this.croppedImage = transformedBase64;
      this.selectedFile = this.dataURLtoFile(
        transformedBase64,
        'filtered-image.png'
      );
    });
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
