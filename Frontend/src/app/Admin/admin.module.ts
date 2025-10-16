import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from './Components/navbar/navbar.component';
import { RouterModule, Routes } from '@angular/router';
import { SharedModule } from "../shared/shared.module";
import { PatientsComponent } from './Components/patients/patients.component';
import { DoctorsComponent } from './Components/doctors/doctors.component';
import { BlockedComponent } from './Components/blocked/blocked.component';

const routes: Routes = [
  {
    path: '',
    component: NavbarComponent,
    children: [
      { path: '', redirectTo: 'DoctorsInfo', pathMatch: 'full' },
      { path: 'DoctorsInfo', component: DoctorsComponent },
      { path: 'UsersInfo', component: PatientsComponent },
      { path: 'BlockedInfo', component:  BlockedComponent},
    ],
  },
];

@NgModule({
  declarations: [
    NavbarComponent,
    PatientsComponent,
    DoctorsComponent,
    BlockedComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    RouterModule.forChild(routes),
  ],
  exports: [NavbarComponent, PatientsComponent,
    DoctorsComponent,BlockedComponent],
})
export class AdminModule { }
