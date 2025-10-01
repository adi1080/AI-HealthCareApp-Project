import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PatientService {
  baseUrl = "http://localhost:9090/Patient";

  constructor(private http: HttpClient) { }

  addPatientProfile(patientData: any, file?: File | null): Observable<any>{
  const formData = new FormData();
  formData.append('patient', JSON.stringify(patientData));
   if (file) {
    formData.append('report', file, file.name);
   }
   return this.http.post(`${this.baseUrl}/addProfile`, formData, {
    responseType: 'text',
   });
 }

  downloadReport(filename: string) {
    const url = `${this.baseUrl}/download-report/${filename}`;
    return this.http.get(url, {
      responseType: 'blob' 
    });
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
    return this.http.get(`${this.baseUrl}/getAllFeedbacks/${doctorId}`);
  }

  cancelAppointment(appointmentId: number) {
    return this.http.delete(`${this.baseUrl}/deleteAppointment/${appointmentId}`, { responseType: 'text' });
  }
}
