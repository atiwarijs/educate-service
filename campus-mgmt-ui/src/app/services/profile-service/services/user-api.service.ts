import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { API_ENDPOINTS } from '../../../core/constants/api.constants';

@Injectable({
  providedIn: 'root'
})
export class UserApiService {

  // private baseUrl = '/educampus';
  private readonly baseUrl = environment.userServiceUrl;
  private readonly securityUrl = environment.securityServiceUrl;
  constructor(private http: HttpClient) { }

  // createUserProfile(payload: any): Observable<any> {
  //   console.log('Sending request to:', this.apiUrl + '/user/register', 'with payload:', payload);
  //   return this.http.post(`${this.apiUrl}/user/register`, payload, { responseType: 'text' });
  // }

  getAllUserProfile(): Observable<any> {
    return this.http.get(`${this.baseUrl}` + API_ENDPOINTS.USERS.PROFILE_LIST);
  }

  getUserProfileById(userId: number): Observable<any> {
    return this.http.get(`${this.baseUrl}` + API_ENDPOINTS.USERS.PROFILE_USERID(userId));
  }

  updateUserProfile(payload: any): Observable<any> {
    return this.http.put(`${this.baseUrl}` + API_ENDPOINTS.USERS.PROFILE, payload);
  }

  checkEmailExists(email: string): Observable<any> {
    // console.log("email check... ",`${this.securityUrl}` + API_ENDPOINTS.AUTH.PROFILE_EMAILID(email));
    return this.http.get<any[]>(`${this.securityUrl}` + API_ENDPOINTS.AUTH.PROFILE_EMAILID(email));
  }

  updateBasicSection(payload: any, userId: string): Observable<any> {
    console.log("updating basic details for:", payload);
    return this.http.put<any[]>(`${this.baseUrl}` + API_ENDPOINTS.USERS.UPDATE_BASIC, payload);
  }

  updateFamilySection(payload: any, userId: string): Observable<any> {
    console.log("updating family details for:", payload);
    return this.http.put<any[]>(`${this.baseUrl}` + API_ENDPOINTS.USERS.UPDATE_FAMILY, payload);
  }

  updateAddressSection(payload: any, userId: string): Observable<any> {
    console.log("updating address details for:", payload);
    return this.http.put<any[]>(`${this.baseUrl}` + API_ENDPOINTS.USERS.UPDATE_ADDRESS, payload);
  }

  updateEducationSection(payload: any, userId: string): Observable<any> {
    console.log("updating education details for:", payload);
    return this.http.put<any[]>(`${this.baseUrl}` + API_ENDPOINTS.USERS.UPDATE_EDUCATION, payload);
  }

  updateExperienceSection(payload: any, userId: string): Observable<any> {
    console.log("updating experience details for:", payload);
    return this.http.put<any[]>(`${this.baseUrl}` + API_ENDPOINTS.USERS.UPDATE_EXPERIENCE, payload);
  }

  updateInsuranceSection(payload: any, userId: string): Observable<any> {
    console.log("updating insurance details for:", payload);
    return this.http.put<any[]>(`${this.baseUrl}` + API_ENDPOINTS.USERS.UPDATE_INSURANCE, payload);
  }

  updateMedicalSection(payload: any, userId: string): Observable<any> {
    console.log("updating medical details for:", payload);
    return this.http.put<any[]>(`${this.baseUrl}` + API_ENDPOINTS.USERS.UPDATE_MEDICAL, payload);
  }

  updateVehicleSection(payload: any, userId: string): Observable<any> {
    console.log("updating vehicle details for:", payload);
    return this.http.put<any[]>(`${this.baseUrl}` + API_ENDPOINTS.USERS.UPDATE_VEHICLE, payload);
  }
}
