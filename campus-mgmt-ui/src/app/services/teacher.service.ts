import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import { API_ENDPOINTS } from '../core/constants/api.constants';

@Injectable({
  providedIn: 'root'
})
export class TeacherService {

  private readonly teacherApiUrl = environment.teacherServiceUrl;
  constructor(private http: HttpClient) { }


  registerTeacher(payload: any): Observable<any> {
    console.log('Sending request to:', this.teacherApiUrl + API_ENDPOINTS.TEACHER.CREATE_TEACHER, 'with payload:', payload);
    if (payload.id) {
      return this.http.put(`${this.teacherApiUrl}` + API_ENDPOINTS.TEACHER.UPDATE_TEACHER, payload);
    } else {
      // Create new teacher
      return this.http.post(`${this.teacherApiUrl}` + API_ENDPOINTS.TEACHER.CREATE_TEACHER, payload);
    }
  }

  getTeachers(): Observable<any> {
    return this.http.get(`${this.teacherApiUrl}` + API_ENDPOINTS.TEACHER.GET_TEACHERS);
  }

  // updateTeacher(teacher: any): Observable<any> {
  //   return this.http.put(`${this.teacherApiUrl}` + API_ENDPOINTS.TEACHER.UPDATE_TEACHER, teacher);
  // }

  getTeacherById(id: number): Observable<any> {
    return this.http.get<any>(`${this.teacherApiUrl}` + API_ENDPOINTS.TEACHER.GET_TEACHER_BY_ID(id));
  }

  deleteById(id: number): Observable<any> {
    return this.http.delete<any>(`${this.teacherApiUrl}` + API_ENDPOINTS.TEACHER.DELETE_TEACHER(id));
  }

  deleteMany(ids: number[]): Observable<any> {
    return this.http.delete(`${this.teacherApiUrl}` + API_ENDPOINTS.TEACHER.DELETE_MANY_TEACHERS(ids));
  }

}
