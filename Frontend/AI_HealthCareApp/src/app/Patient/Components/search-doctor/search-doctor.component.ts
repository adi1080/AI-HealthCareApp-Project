import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { DoctorService } from 'src/app/Doctor/Services/doctor.service';

@Component({
  selector: 'app-search-doctor',
  templateUrl: './search-doctor.component.html',
  styleUrls: ['./search-doctor.component.scss']
})
export class SearchDoctorComponent implements OnInit{
searchForm!:FormGroup;
doctors:any;
image:any;

  constructor(private router:Router , private docService:DoctorService , private fb:FormBuilder){}

  ngOnInit(): void {
      this.searchForm = this.fb.group({
        city:['',Validators.required],
        speciality:['',Validators.required],
        doctorname:[null],
      })
  }

  search(){
    console.log(this.searchForm.value);
     this.docService.FindAllByAddressAndName(this.searchForm.value).subscribe(
     (response) =>{
        console.log(response); 
        this.doctors = response;
     },
     (error)=>{
      console.log(error);
     }
    
    )
  }

  openInfo(id:any){
    localStorage.setItem("DocId" , id);
    this.router.navigate(["doc-info/"+id]);
  }
}
