import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { DoctorService } from 'src/app/Doctor/Services/doctor.service';
import { MessageService } from 'src/app/Services/message.service';

@Component({
  selector: 'app-add-profile',
  templateUrl: './add-profile.component.html',
  styleUrls: ['./add-profile.component.scss'],
})
export class AddProfileComponent implements OnInit {
  uid!: any;
  AddProfileForm!: FormGroup;
  selectedFile: File | null = null;
  checkProfileExistance!: any;
  msg: string = '';
  imageChangedEvent: any = '';
  croppedImage: any = '';
  rotation = 0;
  isGrayscale = false;

  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private docService: DoctorService,
    private messageService:MessageService
  ) {}

  ngOnInit(): void {
    this.uid = localStorage.getItem('DoctorUserId');
    console.log(this.uid);

    this.AddProfileForm = this.formBuilder.group({
      id: [this.uid],
      name: [],
      about: [],
      mobileNo: [],
      gender: [],
      age: [],
      city: [],
      image: [],
      experience: [],
      speciality: [],
      clinicName: [],
      clinicAddress: [],
      consultationFees: [],
    });
  }

  // onFileChange(event:any){
  //   this.selectedFile = event.target.files[0];
  // }

  addDetails() {
    if (this.AddProfileForm.invalid) {
      this.messageService.showMessage('Please fill out all required fields.', 3000);
      this.AddProfileForm.markAllAsTouched(); // Highlights all invalid fields
      return;
    }

    //formdata is used to assign key-value pairs
    const formData = new FormData();
    formData.append('id', this.AddProfileForm.get('id')?.value);
    formData.append('name', this.AddProfileForm.get('name')?.value);
    formData.append('about', this.AddProfileForm.get('about')?.value);
    formData.append('mobileNo', this.AddProfileForm.get('mobileNo')?.value);
    formData.append('age', this.AddProfileForm.get('age')?.value);
    formData.append('gender', this.AddProfileForm.get('gender')?.value);
    formData.append('city', this.AddProfileForm.get('city')?.value);
    formData.append('experience', this.AddProfileForm.get('experience')?.value);
    formData.append('speciality', this.AddProfileForm.get('speciality')?.value);
    formData.append('image', this.selectedFile as File);
    formData.append('clinicName', this.AddProfileForm.get('clinicName')?.value);
    formData.append(
      'clinicAddress',
      this.AddProfileForm.get('clinicAddress')?.value
    );
    formData.append(
      'consultationFees',
      this.AddProfileForm.get('consultationFees')?.value
    );

    this.docService.addDoctorProfile(formData).subscribe(
      (response) => {
        console.log(response);
        if (response == 'profile already exists') {
          this.messageService.showMessage('profile already exists', 3000);
          this.router.navigateByUrl('doctor/profile/addInfo');
        } else {
          this.messageService.showMessage('Profile Added Successfully', 3000);
          this.router.navigateByUrl('doctor/profile/addInfo');
        }
      },
      (error) => {
        console.error('Error adding profile:', error);
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
