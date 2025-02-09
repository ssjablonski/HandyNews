import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from '../../../../environments/environment';
import { BehaviorSubject, Observable, combineLatest } from 'rxjs';
import { Match } from '../models/match.model';
import { MatchesPaginated } from '../models/matchesPaginated.model';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';
import { DatePipe } from '@angular/common';
import { MatchFilters } from '../models/matchFilters.model';

@Injectable({
  providedIn: 'root',
})
export class MatchService {
  private apiUrl = `${environment.apiUrl}/match`;
  private filters$ = new BehaviorSubject<MatchFilters>({});
  private pagination$ = new BehaviorSubject<{ page: number; size: number }>({
    page: 0,
    size: 5,
  });
  private sort$ = new BehaviorSubject<{ sort: string; direction: string }>({
    sort: 'date',
    direction: 'asc',
  });

  public matches$ = new BehaviorSubject<MatchesPaginated>({
    content: [],
    totalElements: 0,
    pageable: {
      pageNumber: 0,
      pageSize: 5,
      sort: {
        empty: true,
        sorted: false,
        unsorted: true,
      },
      offset: 0,
      paged: true,
      unpaged: false,
    },
  });
  public isLoading$ = new BehaviorSubject<boolean>(false);

  public constructor(
    private http: HttpClient,
    private router: Router,
    private datePipe: DatePipe
  ) {
    this.initDataStream();
  }

  private initDataStream(): void {
    combineLatest([
      this.filters$.pipe(debounceTime(500), distinctUntilChanged()),
      this.pagination$,
      this.sort$,
    ])
      .pipe(
        switchMap(([filters, pagination, sort]) => {
          this.isLoading$.next(true);

          return this.fetchMatches(filters, pagination, sort);
        })
      )
      .subscribe({
        next: (data) => {
          console.log(data, 'data');
          this.matches$.next(data);
          this.isLoading$.next(false);
        },
        error: (error) => {
          console.error('Error fetching matches:', error);
          this.isLoading$.next(false);
        },
      });
  }

  private fetchMatches(
    filters: MatchFilters,
    pagination: { page: number; size: number },
    sort: { sort: string; direction: string }
  ): Observable<MatchesPaginated> {
    const params = new HttpParams()
      .set('page', pagination.page.toString())
      .set('size', pagination.size.toString())
      .set('sort', `${sort.sort},${sort.direction}`);

    const processedFilters: MatchFilters = {
      ...filters,
      dateFrom: this.datePipe.transform(filters.dateFrom, 'yyyy-MM-dd'),
      dateTo: this.datePipe.transform(filters.dateTo, 'yyyy-MM-dd'),
    };

    return this.http.post<MatchesPaginated>(
      `${this.apiUrl}/search`,
      processedFilters,
      { params }
    );
  }

  public updateFilters(filters: MatchFilters): void {
    this.filters$.next(filters);
    this.pagination$.next({ ...this.pagination$.value, page: 0 });
  }

  public updatePagination(page: number, size: number): void {
    this.pagination$.next({ page, size });
  }

  public updateSort(sort: string, direction: string): void {
    this.sort$.next({ sort, direction: direction || 'asc' });
    this.pagination$.next({ ...this.pagination$.value, page: 0 });
  }

  public getAllMatches(): Observable<Match[]> {
    return this.http.get<Match[]>(this.apiUrl);
  }

  public getMatchById(id: number): Observable<Match> {
    return this.http.get<Match>(`${this.apiUrl}/${id}`);
  }

  public addMatch(match: Match): Observable<Match> {
    return this.http.post<Match>(this.apiUrl, match);
  }

  public updateMatch(id: number, match: Match): Observable<Match> {
    return this.http.put<Match>(`${this.apiUrl}/${id}`, match);
  }

  public deleteMatch(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
