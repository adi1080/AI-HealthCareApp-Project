import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AiChatComponentComponent } from './ai-chat-component/ai-chat-component.component';
import { PageNotFoundComponent } from './Main/page-not-found/page-not-found.component';
import { LoginComponent } from './Main/login/login.component';
import { RegisterComponent } from './Main/register/register.component';

import { AddProfileComponent as AddDoctorProfileComponent } from './Doctor/Components/profile/add-profile/add-profile.component';
import { UpdateComponent as DoctorProfileUpdateComponent } from './Doctor/Components/profile/update/update.component';

import { AddInfoComponent as AddPatientProfileComponent } from './Patient/Components/profile/add-info/add-info.component';
import { UpdateComponent as PatientProfileUpdateComponent } from './Patient/Components/profile/update/update.component';

import { DocInfoComponent } from './Patient/Components/doc-info/doc-info.component';
import { SearchDoctorComponent } from './Patient/Components/search-doctor/search-doctor.component';

import { AuthGuard } from './auth.guard';
import { RoleGuard } from './role.guard';

const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'Register', component: RegisterComponent },

  // Doctor routes (secured)
  {
    path: 'doctor/profile/addInfo',
    component: AddDoctorProfileComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'Doctor' }
  },
  {
    path: 'doctor/profile/update',
    component: DoctorProfileUpdateComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'Doctor' }
  },

  {
    path: 'doctor',
    loadChildren: () => import('./Doctor/doctor.module').then(m => m.DoctorModule),
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'Doctor' }
  },

  // Patient routes (secured)
  {
    path: 'patient/search',
    component: SearchDoctorComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'User' }
  },
  {
    path: 'patient/profile/addInfo',
    component: AddPatientProfileComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'User' }
  },
  {
    path: 'patient/profile/update',
    component: PatientProfileUpdateComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'User' }
  },
  {
    path: 'patient',
    loadChildren: () => import('./Patient/patient.module').then(m => m.PatientModule),
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'User' }
  },

  // Publicly available
  { path: 'doc-info/:id', component: DocInfoComponent },
  { path: 'AI', component: AiChatComponentComponent, canActivate: [AuthGuard] },

  // Admin (optional role check)
  {
    path: 'admin',
    loadChildren: () => import('./Admin/admin.module').then(m => m.AdminModule),
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'Admin' }
  },

  // Wildcard for 404
  { path: '**', component: PageNotFoundComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
