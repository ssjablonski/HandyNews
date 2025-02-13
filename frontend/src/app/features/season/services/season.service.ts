import { Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { BehaviorSubject, Observable } from 'rxjs';
import { Season } from '../models/season.model';
import { HttpClient } from '@angular/common/http';
import { Team } from '../../clubs/models/team.model';

@Injectable({
  providedIn: 'root',
})
export class SeasonService {
  private apiUrl = `${environment.apiUrl}/season`;
  private seasonSubject = new BehaviorSubject<Season[]>([]);
  public seasons = this.seasonSubject.asObservable();

  public constructor(private http: HttpClient) {}

  public getAllSeasons(): Observable<Season[]> {
    return this.http.get<Season[]>(`${this.apiUrl}`);
  }

  public getSeasonById(id: number): Observable<Season> {
    return this.http.get<Season>(`${this.apiUrl}/${id}`);
  }

  public getAllSeasonsFromLeague(leagueId: number): Observable<Season[]> {
    const test = this.http.get<Season[]>(`${this.apiUrl}/league/${leagueId}`);

    return test;
  }

  public getAllClubsFromSeason(seasonId: number): Observable<Team[]> {
    const test = this.http.get<Team[]>(`${this.apiUrl}/${seasonId}/clubs`);

    return test;
  }
}
