import { Component } from '@angular/core';
import { Router } from '@angular/router';


declare var bootstrap: any; 

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {
DoctorNav:any[]=[];

constructor(){}

  ngOnInit(): void {

    this.DoctorNav = [
      {
        name:"Home",
        route:'doctor/home',
        data:null
      },
      {
        name:"My Profile",
        route:'doctor/profile',
        data:null
      },
      {
        name:"Ask AI",
        route:'AI',
        data:null
      },
      {
        name:'Schedule',
        route:'doctor/schedule/',
        data:null
      }
    ];
  }

}
