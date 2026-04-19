import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';
import { providePrimeNG } from 'primeng/config';
import Aura from '@primeng/themes/aura';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { ConfirmationService, MessageService } from 'primeng/api';
import { AlertService } from './app/shared/common-service/alert-notify.service';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { ErrorHandler } from '@angular/core';
import { SharedModule } from './app/shared/shared-module.module';
import { FormSubmissionService } from './app/services/form-submission.service';
import { KeycloakInterceptor } from './app/shared/interceptors/keycloak.interceptor';
import { LoaderInterceptor } from './app/shared/interceptors/loader-interceptor';
import { GlobalErrorHandler } from './app/shared/interceptors/global-exception-handler';
import { HttpErrorInterceptor } from './app/shared/interceptors/http-error.interceptor';

// Now safely bootstrap your app
bootstrapApplication(AppComponent, {
  providers: [
    providePrimeNG({
      theme: {
        preset: Aura,
      },
    }),
    ...appConfig.providers,
    provideAnimations(),
    provideAnimationsAsync(),
    MessageService,
    ConfirmationService,
    AlertService,
    FormSubmissionService,
    SharedModule,
    { provide: ErrorHandler, useClass: GlobalErrorHandler },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpErrorInterceptor,
      multi: true,
    },
    // { provide: KeycloakService, useValue: keycloak },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: KeycloakInterceptor,
      multi: true,
    },
    { provide: HTTP_INTERCEPTORS, useClass: LoaderInterceptor, multi: true }
  ],
}).catch((err) => console.error('Error bootstrapping the application:', err));
