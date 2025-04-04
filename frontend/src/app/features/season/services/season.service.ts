import { Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { Observable } from 'rxjs';
import { Season } from '../models/season.model';
import { HttpClient } from '@angular/common/http';
import { Team } from '../../clubs/models/team.model';
import { SeasonFormData } from '../models/seasonFormData.model';

@Injectable({
  providedIn: 'root',
})
export class SeasonService {
  private apiUrl = `${environment.apiUrl}/season`;

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

  public addSeason(
    leagueId: number,
    season: SeasonFormData
  ): Observable<Season> {
    return this.http.post<Season>(`${this.apiUrl}/${leagueId}`, season);
  }

  public updateSeason(
    seasonId: number,
    season: SeasonFormData
  ): Observable<Season> {
    return this.http.put<Season>(`${this.apiUrl}/${seasonId}`, season);
  }

  public deleteSeason(seasonId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${seasonId}`);
  }
}
