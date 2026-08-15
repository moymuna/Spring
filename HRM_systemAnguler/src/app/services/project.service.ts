import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ProjectModel } from '../models/project.model';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
@Injectable({
  providedIn: 'root',
})
export class ProjectService {
  private apiUrl = `${environment.baseUrl}/api/project`;
  constructor(private http: HttpClient) {}
  createProject(project: ProjectModel): Observable<ProjectModel> {
    return this.http.post<ProjectModel>(this.apiUrl, project);
  }
  getAllProjects(): Observable<ProjectModel[]> {
    return this.http.get<ProjectModel[]>(this.apiUrl);
  }
  getByEmployeeId(employeeId: number): Observable<ProjectModel[]> {
    return this.http.get<ProjectModel[]>(`${this.apiUrl}/employee/${employeeId}/projects`);
  }
  getProjectById(id: number): Observable<ProjectModel> {
    return this.http.get<ProjectModel>(`${this.apiUrl}/${id}`);
  }
  updateProject(id: number, project: ProjectModel): Observable<ProjectModel> {
    return this.http.put<ProjectModel>(`${this.apiUrl}/${id}`, project);
  }
  deleteProject(id: number): Observable<string> {
    return this.http.delete<string>(`${this.apiUrl}/${id}`);
  }
}
