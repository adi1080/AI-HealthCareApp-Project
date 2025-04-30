import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DoctorService } from '../../Services/doctor.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit{
  loggedInUserId:any = localStorage.getItem('userId');
  Doctor:any;
  doctorImage!:string;

  constructor(private router:Router , private docService:DoctorService){}

ngOnInit(): void {
     this.docService.FindById(this.loggedInUserId).subscribe(
        (doctor) =>{
            this.Doctor = doctor;
            this.doctorImage = 'data:image/jpeg;base64,' + doctor.image;
        console.log(this.Doctor);
      },
       (error)=>{
        console.log(error);
       }
    );
}

 addDetails(){
     this.router.navigateByUrl('doctor/profile/addInfo');
 }

  EditDetails(){
    this.router.navigateByUrl('doctor/profile/update')
  }

  

}
