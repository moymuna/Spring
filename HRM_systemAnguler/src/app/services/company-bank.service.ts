import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface CompanyBankAccount {
  id?: number;
  companyName: string;
  bankName: string;
  bankBranch?: string;
  accountName: string;
  accountNumber: string;
  updatedAt?: string;
}

@Injectable({
  providedIn: 'root',
})
export class CompanyBankService {
  private apiUrl = `${environment.baseUrl}/api/company-bank`;
  constructor(private http: HttpClient) {}
  get(): Observable<CompanyBankAccount | null> {
    return this.http.get<CompanyBankAccount | null>(this.apiUrl);
  }
  save(account: CompanyBankAccount): Observable<CompanyBankAccount> {
    return this.http.post<CompanyBankAccount>(this.apiUrl, account);
  }
}
