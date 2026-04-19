import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { API_ENDPOINTS } from '../core/constants/api.constants';

@Injectable({
    providedIn: 'root'
})
export class ClassesService {

    private readonly classApiUrl = environment.studentServiceUrl;

    constructor(private http: HttpClient) { }

    // Create / Register a new Class
    registerClass(payload: any): Observable<any> {
        console.log('Sending request to:', this.classApiUrl + API_ENDPOINTS.CLASS.CREATE_CLASS, 'with payload:', payload);
        return this.http.post(`${this.classApiUrl}` + API_ENDPOINTS.CLASS.CREATE_CLASS, payload, { responseType: 'text' });
    }

    // Get all Classes
    getClasses(): Observable<any> {
        console.log('Sending request to:', this.classApiUrl + API_ENDPOINTS.CLASS.GET_CLASSES);
        return this.http.get(`${this.classApiUrl}` + API_ENDPOINTS.CLASS.GET_CLASSES);
    }

    // Update Class
    updateClass(classObj: any): Observable<any> {
        console.log('Updating class at:', this.classApiUrl + API_ENDPOINTS.CLASS.UPDATE_CLASS, 'with data:', classObj);
        return this.http.put(`${this.classApiUrl}` + API_ENDPOINTS.CLASS.UPDATE_CLASS, classObj);
    }

    // Get Class by ID
    getClassById(id: number): Observable<any> {
        console.log('Fetching class with ID:', id, 'from:', this.classApiUrl + API_ENDPOINTS.CLASS.GET_CLASS_BY_ID(id));
        return this.http.get<any>(`${this.classApiUrl}` + API_ENDPOINTS.CLASS.GET_CLASS_BY_ID(id));
    }

    // Delete Class by ID
    deleteById(id: number): Observable<any> {
        console.log('Deleting class with ID:', id, 'from:', this.classApiUrl + API_ENDPOINTS.CLASS.DELETE_CLASS(id));
        return this.http.delete<any>(`${this.classApiUrl}` + API_ENDPOINTS.CLASS.DELETE_CLASS(id), { responseType: 'text' as 'json' });
    }
}
