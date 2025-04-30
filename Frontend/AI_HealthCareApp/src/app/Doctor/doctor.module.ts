import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from './Components/navbar/navbar.component';
import { ProfileComponent } from './Components/profile/profile.component';
import { RouterModule, Routes } from '@angular/router';
import { SharedModule } from '../shared/shared.module';
import { HomeComponent } from './Components/home/home.component';
import { AddProfileComponent } from './Components/profile/add-profile/add-profile.component';
import { UpdateComponent } from './Components/profile/update/update.component';
import { ReactiveFormsModule } from '@angular/forms';


const routes: Routes = [
    {
    path: '',
    component: NavbarComponent,
    children: [
      {path:'' , component:HomeComponent , pathMatch:'full'},
      {path: 'home' , component: HomeComponent}, 
      {path: 'profile', component: ProfileComponent}, 
    ],
  },
];

@NgModule({
  declarations: [NavbarComponent, ProfileComponent, HomeComponent, AddProfileComponent, UpdateComponent,  ],
  imports: [CommonModule, RouterModule.forChild(routes), SharedModule , ReactiveFormsModule],
  exports: [NavbarComponent, ProfileComponent , HomeComponent,],
})
export class DoctorModule {}
