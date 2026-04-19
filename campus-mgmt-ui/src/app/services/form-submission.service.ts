// Create a new service: form-submission.service.ts
import { Injectable } from '@angular/core';
import { BehaviorSubject, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FormSubmissionService {
  private formSubmitSubject = new Subject<void>();
  formSubmit$ = this.formSubmitSubject.asObservable();

  private roleTypeSubject = new BehaviorSubject<string | null>(null);
  roleType$ = this.roleTypeSubject.asObservable();

  notifyFormSubmitted() {
    this.formSubmitSubject.next();
  }

  setRoleType(role: string) {
    this.roleTypeSubject.next(role);
  }

  clearRoleType() {
    this.roleTypeSubject.next(null);
  }
}