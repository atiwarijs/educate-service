import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { API_ENDPOINTS } from '../core/constants/api.constants';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class StudentService {

  private readonly studentApiUrl = environment.studentServiceUrl;
  constructor(private http: HttpClient) { }

  registerStudent(payload: any): Observable<any> {
    console.log('Sending request to:', this.studentApiUrl + API_ENDPOINTS.STUDENT.CREATE_STUDENT, 'with payload:', payload);
    return this.http.post(`${this.studentApiUrl}` + API_ENDPOINTS.STUDENT.CREATE_STUDENT, payload, { responseType: 'text' });
  }

  getStudents(): Observable<any> {
    console.log('Sending request to:', this.studentApiUrl + API_ENDPOINTS.STUDENT.GET_STUDENTS);
    return this.http.get(`${this.studentApiUrl}` + API_ENDPOINTS.STUDENT.GET_STUDENTS);
  }

  updateStudent(student: any): Observable<any> {
    return this.http.put(`${this.studentApiUrl}` + API_ENDPOINTS.STUDENT.UPDATE_STUDENT, student);
  }

  getStudentById(id: number): Observable<any> {
    return this.http.get<any>(`${this.studentApiUrl}` + API_ENDPOINTS.STUDENT.GET_STUDENT_BY_ID(id));
  }

  deleteById(id: number): Observable<any> {
    return this.http.delete<any>(`${this.studentApiUrl}` + API_ENDPOINTS.STUDENT.DELETE_STUDENT(id));
  }
}
