// profile-data.service.ts
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ApplicationDataService {
  private data: any;

  setProfileData(data: any) {
    this.data = data;
  }

  getProfileData() {
    return this.data;
  }

  clear() {
    this.data = null;
  }
}
