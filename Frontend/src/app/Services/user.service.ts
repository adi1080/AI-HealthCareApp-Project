import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  addUser(user: any) {
    return this.http.post("http://localhost:9090/register", user);
  }

  login(user: any): Observable<any> {
    return this.http.post("http://localhost:9090/login", user);
  }

  storeToken(token: string) {
    localStorage.setItem('token', token);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  removeToken() {
    localStorage.removeItem('token');
  }

  isLoggedIn(): boolean {
    const token = localStorage.getItem('token');
    if (!token) return false;

    return !this.isTokenExpired();
  }


  getUserInfoFromToken() {
    const token = this.getToken();
    if (token) {
      const decoded: any = jwtDecode(token);
      return {
        id: decoded.id,
        role: decoded.role,
        username: decoded.sub
      };
    }
    return null;
  }

  isTokenExpired(): boolean {
    const token = localStorage.getItem('token');
    if (!token) return true;

    const payload = JSON.parse(atob(token.split('.')[1]));
    const exp = payload.exp; // in seconds
    const now = Math.floor(Date.now() / 1000); // current time in seconds

    return exp < now;
  }

}
