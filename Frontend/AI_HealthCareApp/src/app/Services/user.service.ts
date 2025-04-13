import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http:HttpClient) { }

  addUser(user:any){
     return this.http.post("http://localhost:9090/register",user);
  }

  login(user: any): Observable<any> {
    return this.http.post("http://localhost:9090/login", user , {responseType : 'text'}) ;
  }
}
