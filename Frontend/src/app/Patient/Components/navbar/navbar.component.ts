import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';


@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit{
PatientNav:any[] = [];

constructor(){ };

ngOnInit(): void {
  this.PatientNav = [
    {
      name:'Home',
      route:'patient/home',
      data:null
    },
    {
      name:'My Profile',
      route:'patient/profile',
      data:null
    },
    {
      name:'Find A Doctor',
      route:'patient/pre-search',
      data:null
    },
    {
      name:'Ask AI',
      route:'AI',
      data:null
    },
  ]
}


}
