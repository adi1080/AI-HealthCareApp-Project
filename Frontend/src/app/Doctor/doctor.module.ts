import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from './Components/navbar/navbar.component';
import { ProfileComponent } from './Components/profile/profile.component';
import { RouterModule, Routes } from '@angular/router';
import { SharedModule } from '../shared/shared.module';
import { HomeComponent } from './Components/home/home.component';
import { AddProfileComponent } from './Components/profile/add-profile/add-profile.component';
import { UpdateComponent } from './Components/profile/update/update.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ImageCropperModule } from 'ngx-image-cropper';
import { AvailabilityComponent } from './Components/availability/availability.component';


const routes: Routes = [
    {
    path: '',
    component: NavbarComponent,
    children: [
      {path:'' , component:HomeComponent , pathMatch:'full'},
      {path: 'home' , component: HomeComponent}, 
      {path: 'profile', component: ProfileComponent}, 
      {path:'schedule' , component:AvailabilityComponent},
    ],
  },
];

@NgModule({
  declarations: [NavbarComponent, ProfileComponent, HomeComponent, AddProfileComponent, UpdateComponent, AvailabilityComponent,  ],
  imports: [CommonModule, RouterModule.forChild(routes), SharedModule , ReactiveFormsModule , ImageCropperModule, FormsModule],
  exports: [NavbarComponent, ProfileComponent , HomeComponent,  AddProfileComponent, UpdateComponent, AvailabilityComponent,],
})
export class DoctorModule {}
