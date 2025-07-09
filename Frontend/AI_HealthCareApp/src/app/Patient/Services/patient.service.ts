import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class PatientService {
  baseUrl = "http://localhost:9090/Patient";

  constructor(private http: HttpClient) { }

  AddPatientProfile(patient: any) {
    return this.http.post(`${this.baseUrl}/addProfile`, patient, { responseType: 'text' });
  }

  FindPatientById(id: any) {
    return this.http.get(`${this.baseUrl}/FindById/${id}`);
  }

  updateprofile(id: any, patientProfile: any) {
    return this.http.put(`${this.baseUrl}/updateprofile/${id}`, patientProfile);
  }

  BookAppointment(appointment: any) {
    return this.http.post(`${this.baseUrl}/BookAppointment`, appointment, { responseType: 'text' });
  }

  saveFeedback(feedback: any) {
    return this.http.post(`${this.baseUrl}/add-feedback`, feedback);
  }

  GetDoctorReviews(doctorId: number) {
    return this.http.get(`${this.baseUrl}/getAllFeedbacks/doctor/${doctorId}`);
  }
}
