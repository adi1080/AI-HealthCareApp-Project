import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from './Components/navbar/navbar.component';
import { RouterModule, Routes } from '@angular/router';
import { ProfileComponent } from './Components/profile/profile.component';
import { HomeComponent } from './Components/home/home.component';
import { SharedModule } from '../shared/shared.module';
import { AddInfoComponent } from './Components/profile/add-info/add-info.component';
import { UpdateComponent } from './Components/profile/update/update.component';
import { ReactiveFormsModule } from '@angular/forms';
import { SearchDoctorComponent } from './Components/search-doctor/search-doctor.component';

const routes: Routes = [
{
  path:'',
  component: NavbarComponent,
  children:[
    {path:'' , component:HomeComponent},
    {path:'home' , component:HomeComponent},
    {path:'profile' , component:ProfileComponent},
    {path:'Search' , component:SearchDoctorComponent},
    
  ]                             
}
];

@NgModule({
  declarations: [
    NavbarComponent,
    ProfileComponent,
    HomeComponent,
    AddInfoComponent,
    UpdateComponent,
    SearchDoctorComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    RouterModule.forChild(routes),
    ReactiveFormsModule,

  ],
  exports: [
    NavbarComponent,
    ProfileComponent

  ],
})
export class PatientModule {}
