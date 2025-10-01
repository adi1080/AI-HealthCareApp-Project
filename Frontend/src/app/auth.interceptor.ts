import { Injectable } from '@angular/core';
import {
  HttpInterceptor, HttpRequest, HttpHandler, HttpEvent
} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Skip adding auth token for login/register endpoints
    if (req.url.includes('/login') || req.url.includes('/register') || 
        req.url.includes('/reports/')) {
      return next.handle(req);
    }

    const token = localStorage.getItem('token');
    console.log('Token from localStorage:', token);

    if (token) {
      // For multipart requests, only add the Authorization header
      if (req.url.includes('/Patient/addProfile') && req.body instanceof FormData) {
        const cloned = req.clone({
          setHeaders: {
            'Authorization': `Bearer ${token}`
          }
        });
        return next.handle(cloned);
      } else {
        // For regular requests
        const cloned = req.clone({
          headers: req.headers.set('Authorization', `Bearer ${token}`)
        });
        return next.handle(cloned);
      }
    }
    return next.handle(req);
  }
}
