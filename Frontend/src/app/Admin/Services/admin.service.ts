import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface User {
  id: number;
  username: string;
  emailid: string;
  password: string;
  role: string;
  feedbackAnalysis: string | null;
  lastCancelReset: string | null;
  misconductScore: number;
  appointmentsCanceled: number;
  misconductReason: string | null;
  permitted: boolean;
}


@Injectable({
  providedIn: 'root'
})
export class AdminService {
  baseurl = "http://localhost:9090";

  constructor(private Http: HttpClient) { }

  getAllUsers(): Observable<User[]> {
    return this.Http.get<User[]>(`${this.baseurl}/getAllUsers`);
  }

  analyzeAllFeedbacks() {
    return this.Http.post(`${this.baseurl}/Doctor/analyze/all-feedbacks`, {});
  }

  analyzeDoctorFeedback(doctorId: number) {
    return this.Http.post(`${this.baseurl}/Doctor/analyze/feedback/${doctorId}`, {});
  }

  triggerSmartAnalysis() {
    return this.Http.post(`${this.baseurl}/Doctor/analyze/smart`, {}, { responseType: 'text' });
  }

  // ✅ Permit doctor
  permit(id: number): Observable<string> {
    return this.Http.post(`${this.baseurl}/permit/${id}`, {}, { responseType: 'text' });
  }

  // ✅ Block doctor
  block(id: number): Observable<string> {
    return this.Http.post(`${this.baseurl}/block/${id}`, {}, { responseType: 'text' });
  }
}
