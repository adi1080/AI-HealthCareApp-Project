import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent {

  constructor(private router:Router){}

 addDetails(){
     this.router.navigateByUrl('doctor/profile/addInfo');

 }

  EditDetails(){
    this.router.navigateByUrl('doctor/profile/update')
  }
}
