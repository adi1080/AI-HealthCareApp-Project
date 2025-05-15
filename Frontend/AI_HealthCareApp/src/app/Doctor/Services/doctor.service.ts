import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DoctorService {
  private baseUrl = 'http://localhost:9090/Doctor';

  constructor(private http: HttpClient) { }

  addDoctorProfile(doctor: any) {
    return this.http.post(`${this.baseUrl}/addprofile`, doctor, { responseType: 'text' });
  }

  FindById(id: any): Observable<any> {
    return this.http.get(`${this.baseUrl}/profile/${id}`);
  }

  updateDoctor(id: string, data: FormData) {
    return this.http.put(`${this.baseUrl}/update/${id}`, data);
  }

}
