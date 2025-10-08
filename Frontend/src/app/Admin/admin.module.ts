import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from './Components/navbar/navbar.component';
import { RouterModule, Routes } from '@angular/router';
import { SharedModule } from "../shared/shared.module";
import { HomeComponent } from './Components/home/home.component';

const routes: Routes = [
  {
    path: '',
    component: NavbarComponent,
    children: [
        {path:'Home', component:HomeComponent}
    ],
  },
];

@NgModule({
  declarations: [
    NavbarComponent,
    HomeComponent,
  ],
  imports: [
    CommonModule, 
    SharedModule,
    RouterModule.forChild(routes),
  ],
  exports: [NavbarComponent,HomeComponent],
})
export class AdminModule {}
