import { Component, OnInit } from '@angular/core';
import { DoctorService } from 'src/app/Doctor/Services/doctor.service';

@Component({
  selector: 'app-update',
  templateUrl: './update.component.html',
  styleUrls: ['./update.component.scss']
})
export class UpdateComponent implements OnInit{
  loggedInUserId:any = localStorage.getItem('userId');
  Doctor:any;
  doctorImage:any;

  constructor(private docService:DoctorService){ }

  ngOnInit(): void {
    this.docService.FindById(this.loggedInUserId).subscribe(
      response => {
         this.Doctor = response;
         this.doctorImage = 'data:image/jpeg;base64,' + this.Doctor.image;
        });
  }

}
