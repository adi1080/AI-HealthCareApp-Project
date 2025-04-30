import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AiChatComponentComponent } from './ai-chat-component/ai-chat-component.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharedModule } from './shared/shared.module';
import { LoginComponent } from './Main/login/login.component';
import { PageNotFoundComponent } from './Main/page-not-found/page-not-found.component';
import { RegisterComponent } from './Main/register/register.component';
import { AdminModule } from './Admin/admin.module';
import { DoctorModule } from './Doctor/doctor.module';
import { PatientModule } from './Patient/patient.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';


@NgModule({
  declarations: [
    AppComponent,
    AiChatComponentComponent,
    LoginComponent,
    PageNotFoundComponent,
    RegisterComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    SharedModule,
    AdminModule,
    DoctorModule,
    PatientModule,
    BrowserAnimationsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
