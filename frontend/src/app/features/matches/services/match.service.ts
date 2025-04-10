import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from '../../../../environments/environment';
import { Observable } from 'rxjs';
import { Match } from '../models/match.model';
import { MatchFormData } from '../models/matchFormData.model';

@Injectable({
  providedIn: 'root',
})
export class MatchService {
  private apiUrl = `${environment.apiUrl}/match`;

  public constructor(private http: HttpClient, private router: Router) {}

  public getAllMatches(): Observable<Match[]> {
    return this.http.get<Match[]>(this.apiUrl);
  }

  public getMatchById(id: number): Observable<Match> {
    return this.http.get<Match>(`${this.apiUrl}/${id}`);
  }

  public addMatch(match: MatchFormData): Observable<Match> {
    return this.http.post<Match>(`${this.apiUrl}/${match.seasonId}`, match);
  }

  public updateMatch(id: number, match: MatchFormData): Observable<Match> {
    return this.http.put<Match>(`${this.apiUrl}/${id}`, match);
  }

  public deleteMatch(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
