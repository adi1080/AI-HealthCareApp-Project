import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface User {
  id: number;
  username: string;
  emailid: string;
  password: string;
  role: string;
}

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  baseurl = "http://localhost:9090";

  constructor(private Http:HttpClient) { }

  getAllUsers(): Observable<User[]>{
    return this.Http.get<User[]>(`${this.baseurl}/getAllUsers`);
  }
}
