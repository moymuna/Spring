import { HttpClient, HttpContext, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {
  Department,
  Designation,
  District,
  Division,
  EmployeeRequest,
  EmployeeResponse,
  Office,
  PageResponse,
  PoliceStation,
} from '../models/employee.model';
import { Observable } from 'rxjs';
import { Country } from '../models/country';
import { environment } from '../../environments/environment';
@Injectable({
  providedIn: 'root',
})
export class EmployeeService {
  private apiUrl = `${environment.baseUrl}/api`;
  constructor(private http: HttpClient) {}
  createEmployee(employee: EmployeeRequest, image: File | null): Observable<EmployeeResponse> {
    const formData = new FormData();
    formData.append('employee', JSON.stringify(employee));
    if (image) {
      formData.append('image', image);
    }
    return this.http.post<EmployeeResponse>(`${this.apiUrl}/employees`, formData);
  }
  updateEmployee(
    id: number,
    employee: EmployeeRequest,
    image: File | null,
  ): Observable<EmployeeResponse> {
    const formData = new FormData();
    const employeeBlob = new Blob([JSON.stringify(employee)], { type: 'application/json' });
    formData.append('employee', employeeBlob);
    if (image) {
      formData.append('image', image);
    }
    return this.http.put<EmployeeResponse>(`${this.apiUrl}/employees/${id}`, formData);
  }
  deleteEmployee(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/employees/${id}`, {
      responseType: 'text',
    });
  }
  getEmployeeById(id: number): Observable<EmployeeResponse> {
    return this.http.get<EmployeeResponse>(`${this.apiUrl}/employees/${id}`);
  }
  /** Promotes a hired applicant into an employee, reusing their existing account. */
  hireApplicant(applicationId: number, terms: any): Observable<EmployeeResponse> {
    return this.http.post<EmployeeResponse>(`${this.apiUrl}/employees/hire/${applicationId}`, terms);
  }
  getAllEmployees(): Observable<EmployeeResponse[]> {
    return this.http.get<EmployeeResponse[]>(`${this.apiUrl}/employees`);
  }
  getByEmployeeCode(code: string): Observable<EmployeeResponse> {
    return this.http.get<EmployeeResponse>(`${this.apiUrl}/employees/code/${code}`);
  }
  getByEmail(email: string): Observable<EmployeeResponse> {
    return this.http.get<EmployeeResponse>(`${this.apiUrl}/employees/email/${email}`);
  }
  getByUserId(userId: number, context?: HttpContext): Observable<EmployeeResponse> {
    return this.http.get<EmployeeResponse>(
      `${this.apiUrl}/employees/user/${userId}`,
      context ? { context } : {},
    );
  }
  searchEmployee(keyword: string): Observable<EmployeeResponse[]> {
    const params = new HttpParams().set('keyword', keyword);
    return this.http.get<EmployeeResponse[]>(`${this.apiUrl}/employees/search`, {
      params,
    });
  }
  getEmployeesByPage(page: number, size: number): Observable<PageResponse<EmployeeResponse>> {
    let params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<PageResponse<EmployeeResponse>>(`${this.apiUrl}/employees/page`, {
      params,
    });
  }
  getEmployeeCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/employees/count`);
  }
  getActiveEmployeeCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/employees/count/active`);
  }
  getInactiveEmployeeCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/employees/count/inactive`);
  }
  getDepartments(): Observable<Department[]> {
    return this.http.get<Department[]>(`${this.apiUrl}/department`);
  }
  getDesignations(): Observable<Designation[]> {
    return this.http.get<Designation[]>(`${this.apiUrl}/designation`);
  }
  getDesignationsByDepartment(departmentId: number): Observable<Designation[]> {
    return this.http.get<Designation[]>(`${this.apiUrl}/designation/department/${departmentId}`);
  }
  getOffices(): Observable<Office[]> {
    return this.http.get<Office[]>(`${this.apiUrl}/office`);
  }
  getCountries(): Observable<Country[]> {
    return this.http.get<Country[]>(`${this.apiUrl}/country/all`);
  }
  getDivisionsByCountry(countryId: number): Observable<Division[]> {
    return this.http.get<Division[]>(`${this.apiUrl}/division/country/${countryId}`);
  }
  getDistrictsByDivision(divisionId: number): Observable<District[]> {
    return this.http.get<District[]>(`${this.apiUrl}/district/division/${divisionId}`);
  }
  getPoliceStationsByDistrict(districtId: number): Observable<PoliceStation[]> {
    return this.http.get<PoliceStation[]>(`${this.apiUrl}/policeStation/district/${districtId}`);
  }
  getManagers(): Observable<EmployeeResponse[]> {
    return this.http.get<EmployeeResponse[]>(`${this.apiUrl}/employees`);
  }
  getByStatus(status: string): Observable<EmployeeResponse[]> {
    return this.http.get<EmployeeResponse[]>(`${this.apiUrl}/employees/status/${status}`);
  }
  getEmployeesByDepartment(id: number) {
    return this.http.get<EmployeeResponse[]>(`${this.apiUrl}/employees/department/${id}`);
  }
}
