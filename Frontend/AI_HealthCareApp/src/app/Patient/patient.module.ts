import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from './Components/navbar/navbar.component';
import { RouterModule, Routes } from '@angular/router';
import { ProfileComponent } from './Components/profile/profile.component';
import { HomeComponent } from './Components/home/home.component';
import { SharedModule } from '../shared/shared.module';

const routes: Routes = [
{
  path: '',
  component: NavbarComponent,
  children:[
    {path:'profile' , component:ProfileComponent},
    {path:'home' , component:HomeComponent},

  ]                             
}
];

@NgModule({
  declarations: [
    NavbarComponent,
    ProfileComponent,
    HomeComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    RouterModule.forChild(routes),


  ],
  exports: [
    NavbarComponent,
    ProfileComponent

  ],
})
export class PatientModule {}
