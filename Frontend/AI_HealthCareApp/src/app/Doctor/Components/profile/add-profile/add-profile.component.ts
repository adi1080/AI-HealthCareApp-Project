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
      about:[],
      mobileNo: [],
      gender: [],
      age: [],
      city:[],
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
      this.msg = 'Please fill out all required fields.';
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

imageChangedEvent: any = '';
croppedImage: any = '';
rotation = 0;
isGrayscale = false;

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
  document.querySelector('image-cropper')?.setAttribute('style', `transform: rotate(${this.rotation}deg);`);
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
