import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class DraftStorageService {

  private key = 'userFormDraft';
  private userProfileKey = 'userCredentialDraft';

  saveDraft(data: any): void {
    localStorage.setItem(this.key, JSON.stringify(data));
  }

  loadDraft(): any | null {
    const raw = localStorage.getItem(this.key);
    return raw ? JSON.parse(raw) : null;
  }

  clearDraft(): void {
    localStorage.removeItem(this.key);
  }

  saveUserProfileDraft(userProfile: any): void {
    localStorage.setItem(this.userProfileKey, JSON.stringify(userProfile));
  }

  clearUserProfileDraft(): void {
    localStorage.removeItem(this.userProfileKey);
  }

  getUserProfileDraft(): any | null {
   const raw = localStorage.getItem(this.userProfileKey);
  return raw ? JSON.parse(raw) : null;
  }
}