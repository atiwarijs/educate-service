import { Injectable, Injector } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
} from '@angular/common/http';
import { Observable, from, throwError } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { AuthService } from '../../services/account-service/auth.service';
import { Router } from '@angular/router';

@Injectable()
export class KeycloakInterceptor implements HttpInterceptor {
  constructor(private injector: Injector, private router: Router) { }

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {

    const excludedUrls = ['/api/v1/security/auth/login', '/api/security/auth/login', '/api/security/auth/forgot-password'];

    const shouldSkip = excludedUrls.some(url => req.url.includes(url));

    if (!shouldSkip) {
      const authService = this.injector.get(AuthService);

      const token = authService.getToken();

      if (token) {
        req = req.clone({
          setHeaders: { Authorization: `Bearer ${token}` },
        });
      }
    }
    return next.handle(req).pipe(
      catchError((error) => {
        if (error.status === 401) {
          // Token expired or invalid
          // localStorage.clear();
          sessionStorage.clear();
          const authService = this.injector.get(AuthService);
          authService.logout();
          this.router.navigate(['/login/auth']);
        }
        return throwError(() => error);
      })
    );
  }
}
