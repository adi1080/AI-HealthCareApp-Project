import { HttpClient } from '@angular/common/http';
import { Injectable, NgZone } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AiServiceService {
 private apiUrl = 'http://localhost:8081/ai/message_stream';  // Ensure correct port & URL

  constructor(private ngZone: NgZone) { }

  getAiResponseStream(userQuery: string): Observable<string> {
    return new Observable<string>(observer => {
      const url = `${this.apiUrl}?query=${encodeURIComponent(userQuery)}`;

      const eventSource = new EventSource(url);

      eventSource.onmessage = (event) => {
        this.ngZone.run(() => {
          observer.next(event.data);
        });
      };

      eventSource.onerror = (error) => {
        this.ngZone.run(() => {
          console.error("Error receiving AI response: Maximum pool size: undefined/unknown" , error);
          observer.error('EventSource error');
        });
        eventSource.close();
      };

      return () => {
        eventSource.close();
      };
    });
  }
}


