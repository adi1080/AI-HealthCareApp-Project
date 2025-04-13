import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AiChatComponentComponent } from './ai-chat-component/ai-chat-component.component';
import { PageNotFoundComponent } from './Main/page-not-found/page-not-found.component';
import { LoginComponent } from './Main/login/login.component';
import { RegisterComponent } from './Main/register/register.component';
import { AddProfileComponent as AddDoctorProfileComponent} from './Doctor/Components/profile/add-profile/add-profile.component';
import { UpdateComponent as DoctorProfileUpdateComponent} from './Doctor/Components/profile/update/update.component';


const routes: Routes = [
  {path: '' , redirectTo : 'login' , pathMatch : 'full'},
  {path : 'login' , component : LoginComponent},
  {path : 'Register' , component : RegisterComponent},

  {path: 'doctor/profile/addInfo' , component:AddDoctorProfileComponent},
  {path: 'doctor/profile/update' , component:DoctorProfileUpdateComponent},

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
