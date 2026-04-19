import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, shareReplay } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { API_ENDPOINTS } from '../../../core/constants/api.constants';

@Injectable({
  providedIn: 'root',
})
export class LocationService {

  private baseUrl = environment.userServiceUrl;

  private states$: Observable<any[]> | null = null;

  constructor(private http: HttpClient) { }

  getAllStateList(): Observable<any[]> {
    if (!this.states$) {
      this.states$ = this.http.get<any[]>(`${this.baseUrl}` + API_ENDPOINTS.LOCATION.STATE).pipe(
        shareReplay(1) // cache the response
      );
    }
    return this.states$;
  }

  clearCache() {
    this.states$ = null;
  }
}
