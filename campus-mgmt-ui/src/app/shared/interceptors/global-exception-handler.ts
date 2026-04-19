import { ErrorHandler, Injectable, NgZone } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { AlertService } from '../common-service/alert-notify.service';

@Injectable()
export class GlobalErrorHandler implements ErrorHandler {
  constructor(
    private alertService: AlertService,
    private zone: NgZone
  ) { }

  handleError(error: any): void {
    // Use NgZone to ensure UI updates correctly
    this.zone.run(() => {
      console.error('Global error handler caught:', error);

      // Skip handling HTTP errors that were already handled by the interceptor
      if (error instanceof HttpErrorResponse && (error as any).__interceptorHandled) {
        // This HTTP error was already handled by the interceptor, so we skip it
        return;
      }

      // Extract message from different error types
      let errorMessage: string;
      let title: string = 'Error';

      if (error instanceof HttpErrorResponse) {
        // Handle HTTP errors that somehow bypassed the interceptor
        title = `HTTP Error ${error.status}`;
        errorMessage = error.message;
      } else if (error instanceof TypeError) {
        title = 'Type Error';
        errorMessage = error.message;
      } else if (error instanceof ReferenceError) {
        title = 'Reference Error';
        errorMessage = error.message;
      } else if (error?.rejection instanceof Error) {
        title = 'Promise Error';
        errorMessage = error.rejection.message;
      } else if (error instanceof Error) {
        errorMessage = error.message;
      } else if (typeof error === 'string') {
        errorMessage = error;
      } else {
        try {
          errorMessage = JSON.stringify(error);
        } catch {
          errorMessage = 'An unknown error occurred';
        }
      }

      // Send to alert service
      this.alertService.error(title, errorMessage);

      // Here you could also log to a monitoring service
      // this.loggingService.logError(error);
    });
  }
}