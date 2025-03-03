import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';
import { environment } from '../../../../environments/environment';
import { BehaviorSubject, catchError, Observable, tap, throwError } from 'rxjs';
import { LoginData } from '../models/loginData.model';
import { LoginResponse } from '../models/loginResponse.model';
import { RegisterData } from '../models/registerData.model';
import { DecodedToken } from '../models/decodedToken.model';
import { UserInfo } from '../models/userInfo.model';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private apiUrl = environment.apiUrl;
  private userSubject = new BehaviorSubject<string | null>(null);
  public user$ = this.userSubject.asObservable();

  public constructor(private http: HttpClient, private router: Router) {
    this.checkIfLoggedIn();
  }

  public login(loginData: LoginData): Observable<LoginResponse> {
    return this.http
      .post<LoginResponse>(`${this.apiUrl}/auth/authenticate`, loginData)
      .pipe(
        catchError((err) => {
          return throwError(() => new Error(err.error));
        }),
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
        catchError((err) => {
          return throwError(() => new Error(err.error));
        }),
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
    this.userSubject.next(null);
    this.router.navigate(['/login']);
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

  private decodeToken(token: string): DecodedToken | null {
    try {
      return jwtDecode<DecodedToken>(token);
    } catch (err) {
      console.error('Error decoding token', err);

      return null;
    }
  }

  private isTokenExpired(token: string): boolean {
    if (!token) {
      return true;
    }

    const expirationDate = new Date(this.decodeToken(token)!.exp * 1000);

    return expirationDate < new Date();
  }

  public checkTokenExpiration(): void {
    const token = this.getToken();

    if (token && this.isTokenExpired(token)) {
      this.logout();
    }
  }

  public checkIfLoggedIn(): void {
    const token = this.getToken();
    if (token) {
      this.userSubject.next(this.getUserId());
    }
  }

  public getUserData(): Observable<UserInfo> {
    const userId = this.getUserId();
    if (!userId) {
      throw new Error('No user id found');
    }

    return this.http.get<UserInfo>(`${this.apiUrl}/auth/user/${userId}`);
  }

  public deleteAccount(userId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/auth/user/${userId}`).pipe(
      tap(() => {
        this.logout();
      })
    );
  }
}
