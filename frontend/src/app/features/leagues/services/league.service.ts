import { Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { BehaviorSubject, Observable } from 'rxjs';
import { League } from '../models/league.model';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class LeagueService {
  private apiUrl = `${environment.apiUrl}/league`;
  private leagueSubject = new BehaviorSubject<League[]>([]);
  public leagues = this.leagueSubject.asObservable();

  public constructor(private http: HttpClient) {}

  public getAllLeagues(): Observable<League[]> {
    return this.http.get<League[]>(`${this.apiUrl}`);
  }

  public getLeagueById(id: number): Observable<League> {
    return this.http.get<League>(`${this.apiUrl}/${id}`);
  }
}
