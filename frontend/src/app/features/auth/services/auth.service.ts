import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from '../../../../environments/environment';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { LoginData } from '../models/loginData.model';
import { LoginResponse } from '../models/loginResponse.model';
import { RegisterData } from '../models/registerData.model';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = environment.apiUrl;
  private userSubject = new BehaviorSubject<string | null>(null);
  public user$ = this.userSubject.asObservable();

  public constructor(private http: HttpClient, private router: Router) {}

  public login(loginData: LoginData): Observable<LoginResponse> {
    return this.http
      .post<LoginResponse>(`${this.apiUrl}/auth/authenticate`, loginData)
      .pipe(
        tap((response) => {
          this.saveToken(response.token);
          this.saveUserId(response.userId);
          this.userSubject.next(response.userId.toString());
        })
      );
  }

  public register(registerData: RegisterData): Observable<LoginResponse> {
    return this.http
      .post<LoginResponse>(`${this.apiUrl}/auth/register`, registerData)
      .pipe(
        tap((response) => {
          this.saveToken(response.token);
          this.saveUserId(response.userId);
          this.userSubject.next(response.userId.toString());
        })
        // TODO - ogarniecie jeszcze optional address!!
      );
  }

  public logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('userId');
    this.router.navigate(['/main-page']);
  }

  public isLoggedIn(): boolean {
    return !!this.getToken();
  }

  public saveToken(token: string): void {
    localStorage.setItem('token', token);
  }

  public getToken(): string | null {
    return localStorage.getItem('token');
  }

  public saveUserId(userId: number): void {
    localStorage.setItem('userId', userId.toString());
  }

  public getUserId(): string | null {
    return localStorage.getItem('userId');
  }
}
