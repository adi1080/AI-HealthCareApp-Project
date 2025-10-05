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
        name:[null],
      })
  }

  search(){
  const formValue = this.searchForm.value;

  // Sanitize: convert empty string or "null" strings to undefined (so params are omitted)
  const params: any = {};
  if (formValue.name && formValue.name.trim() !== '' && formValue.name.toLowerCase() !== 'null') {
    params.name = formValue.name.trim();
  }
  if (formValue.city && formValue.city.trim() !== '' && formValue.city.toLowerCase() !== 'null') {
    params.city = formValue.city.trim();
  }
  if (formValue.speciality && formValue.speciality.trim() !== '' && formValue.speciality.toLowerCase() !== 'null') {
    params.speciality = formValue.speciality.trim();
  }

  this.docService.FindAllByAddressAndName(params).subscribe(
    (response) => {
      this.doctors = response;
    },
    (error) => {
      console.log(error);
    }
  );
  }

  openInfo(id:any){
    localStorage.setItem("DocId" , id);
    this.router.navigate(["doc-info/"+id]);
  }
}
