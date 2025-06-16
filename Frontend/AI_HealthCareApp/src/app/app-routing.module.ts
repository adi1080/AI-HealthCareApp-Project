import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AiChatComponentComponent } from './ai-chat-component/ai-chat-component.component';
import { PageNotFoundComponent } from './Main/page-not-found/page-not-found.component';
import { LoginComponent } from './Main/login/login.component';
import { RegisterComponent } from './Main/register/register.component';
import { AddProfileComponent as AddDoctorProfileComponent} from './Doctor/Components/profile/add-profile/add-profile.component';
import { UpdateComponent as DoctorProfileUpdateComponent} from './Doctor/Components/profile/update/update.component';
import { AddInfoComponent as AddPatientProfileComponent} from './Patient/Components/profile/add-info/add-info.component';
import { UpdateComponent as PatientProfileUpdateComponent} from './Patient/Components/profile/update/update.component';
import { DocInfoComponent } from './Patient/Components/doc-info/doc-info.component';
import { BookAppointmentComponent } from './Patient/Components/book-appointment/book-appointment.component';
import { SearchDoctorComponent } from './Patient/Components/search-doctor/search-doctor.component';


const routes: Routes = [
  {path: '' , redirectTo : 'login' , pathMatch : 'full'},
  {path : 'login' , component : LoginComponent},
  {path : 'Register' , component : RegisterComponent},

  {path: 'doctor/profile/addInfo' , component:AddDoctorProfileComponent},
  {path: 'doctor/profile/update' , component:DoctorProfileUpdateComponent},

  {path:'patient/search', component:SearchDoctorComponent},

  
  {path: 'patient/profile/addInfo' , component:AddPatientProfileComponent},
  {path: 'patient/profile/update' , component:PatientProfileUpdateComponent},
  {path:'doc-info/:id' , component:DocInfoComponent},
  {path:'book-appointment' , component:BookAppointmentComponent},

  {path:'admin' , loadChildren:() =>import('./Admin/admin.module').then((m) => m.AdminModule)},
  {path:'doctor' , loadChildren:()=>import('./Doctor/doctor.module').then((m)=>m.DoctorModule)},
  {path:'patient', loadChildren:()=>import('./Patient/patient.module').then((m)=>m.PatientModule)},

  {path: 'AI' , component:AiChatComponentComponent},

  {path: '**', component: PageNotFoundComponent}

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
