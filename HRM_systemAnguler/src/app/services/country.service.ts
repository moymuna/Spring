import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Country } from '../models/country';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
@Injectable({
  providedIn: 'root',
})
export class CountryService {
  private apiUrl = `${environment.baseUrl}/api/country`;
  constructor(private http: HttpClient) {}
  saveCountry(country: Country): Observable<Country> {
    return this.http.post<Country>(`${this.apiUrl}/save`, country);
  }
  getAllCountries(): Observable<Country[]> {
    return this.http.get<Country[]>(`${this.apiUrl}/all`);
  }
  getCountryById(id: number): Observable<Country> {
    return this.http.get<Country>(`${this.apiUrl}/${id}`);
  }
  getCountryByName(name: string): Observable<Country> {
    return this.http.get<Country>(`${this.apiUrl}/name/${name}`);
  }
  updateCountry(id: number, country: Country): Observable<Country> {
    return this.http.put<Country>(`${this.apiUrl}/update/${id}`, country);
  }
  deleteCountry(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/delete/${id}`, {
      responseType: 'text',
    });
  }
}
