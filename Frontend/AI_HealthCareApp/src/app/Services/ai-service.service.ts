import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AiServiceService {

  private apiUrl = 'http://localhost:9090/ai/message_response';  // Spring Boot backend URL

  constructor(private http: HttpClient) {}

  getAiResponse(userQuery: string): Observable<string> {
    const encodedQuery = encodeURIComponent(userQuery);  // Ensure the query is URL-encoded
    return this.http.get<string>(`${this.apiUrl}?query=${encodedQuery}`, { responseType: 'text' as 'json' });
  }
  
  
  
}
