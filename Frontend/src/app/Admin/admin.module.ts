import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from './Components/navbar/navbar.component';
import { RouterModule, Routes } from '@angular/router';
import { SharedModule } from "../shared/shared.module";
import { PatientsComponent } from './Components/patients/patients.component';
import { DoctorsComponent } from './Components/doctors/doctors.component';

const routes: Routes = [
  {
    path: '',
    component: NavbarComponent,
    children: [
      { path: 'DoctorsInfo', component: DoctorsComponent },
      { path: 'UsersInfo', component: PatientsComponent },
    ],
  },
];

@NgModule({
  declarations: [
    NavbarComponent,
    PatientsComponent,
    DoctorsComponent,
  ],
  imports: [
    CommonModule,
    SharedModule,
    RouterModule.forChild(routes),
  ],
  exports: [NavbarComponent, PatientsComponent,
    DoctorsComponent,],
})
export class AdminModule { }
