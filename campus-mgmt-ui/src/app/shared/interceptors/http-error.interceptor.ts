import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse,
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AlertService } from '../common-service/alert-notify.service';

@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor {
  constructor(private alertService: AlertService) { }

  intercept(
    request: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        // Add a property to mark this error as already handled by the interceptor
        (error as any).__interceptorHandled = true;

        if (error.error instanceof ErrorEvent) {
          // Client-side error
          this.alertService.error('Client Error', error.error.message);
        } else {
          // Server-side error
          let errorMessage = this.extractSpringBootErrorMessage(error);
          let title = this.extractSpringBootErrorTitle(error);

          this.alertService.error(title, errorMessage);

          // Log the full error for debugging
          console.error('Full server error:', error);
        }

        return throwError(() => error);
      })
    );
  }

  /**
   * Extract error message from Spring Boot's error response format
   */
  private extractSpringBootErrorMessage(error: HttpErrorResponse): string {
    // First, check if it's a string response
    if (typeof error.error === 'string') {
      return error.error;
    }

    // Check for Spring Boot's standard error format with 'message' field
    if (error.error && error.error.message) {
      return error.error.message;
    }

    // Handle the specific format from your GlobalExceptionHandler
    if (error.error && typeof error.error === 'object') {
      // This handles the LinkedHashMap response from GlobalExceptionHandler
      return error.error.message ||
        error.error.error ||
        (error.statusText ? `${error.status}: ${error.statusText}` : 'Unknown error');
    }

    // Check error.message as fallback
    if (error.message) {
      return error.message;
    }

    // Last resort - just show the status code and text
    return `Error ${error.status}: ${error.statusText || 'Unknown error'}`;
  }

  /**
   * Extract error title from Spring Boot's error response format
   */
  private extractSpringBootErrorTitle(error: HttpErrorResponse): string {
    // Check for Spring Boot's standard error format
    if (error.error && error.error.error) {
      return error.error.error;
    }

    // Handle specific HTTP status codes
    switch (error.status) {
      case 0:
        return 'Service Unavailable'
      case 401:
        return 'Authentication Error';
      case 403:
        return 'Authorization Error';
      case 404:
        return 'Not Found';
      case 409:
        return 'Conflict';
      case 500:
        return 'Server Error';
      default:
        return `Error ${error.status}`;
    }
  }
}