import { Component, OnInit } from '@angular/core';
import { DoctorService } from 'src/app/Doctor/Services/doctor.service';

@Component({
  selector: 'app-doc-info',
  templateUrl: './doc-info.component.html',
  styleUrls: ['./doc-info.component.scss']
})
export class DocInfoComponent implements OnInit{
DoctorId:any;
doctorInfo:any;
doctorImage:any;

constructor(private doctorService:DoctorService){}

  ngOnInit(): void {
    this.DoctorId = localStorage.getItem("DocId");

    this.doctorService.FindById(this.DoctorId).subscribe(
      response =>{
        console.log(response);
         this.doctorInfo = response;
         this.doctorImage = 'data:image/jpeg;base64,' + this.doctorInfo.image;
      }
    );
  }
}
