import { provideAnimations } from '@angular/platform-browser/animations';
import { importProvidersFrom } from '@angular/core';
import { SharedPrimeNgModule } from './shared.primeng-module';

export const primeNgConfig = [
  provideAnimations(),
  importProvidersFrom(SharedPrimeNgModule)
];