import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Club } from '../models/club.model';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Router } from '@angular/router';
import { ClubFormData } from '../models/clubFormData.model';

@Injectable({
  providedIn: 'root',
})
export class ClubService {
  private apiUrl = `${environment.apiUrl}/club`;

  public constructor(private http: HttpClient, private router: Router) {}

  public getAllClubs(): Observable<Club[]> {
    return this.http.get<Club[]>(this.apiUrl);
  }

  public getClubById(id: number): Observable<Club> {
    return this.http.get<Club>(`${this.apiUrl}/${id}`);
  }

  public addClub(seasonId: number, club: ClubFormData): Observable<Club> {
    return this.http.post<Club>(`${this.apiUrl}/season/${seasonId}`, club);
  }

  public updateClub(id: number, club: ClubFormData): Observable<Club> {
    return this.http.put<Club>(`${this.apiUrl}/${id}`, club);
  }

  public deleteClub(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
