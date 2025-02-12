import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatchService } from '../../services/match.service';
import { Match } from '../../models/match.model';
import { Router, RouterModule } from '@angular/router';
import { League } from '../../../leagues/models/league.model';
import { LeagueService } from '../../../leagues/services/league.service';
import { FilterForm } from '../../models/filterForm.model';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatSelectModule } from '@angular/material/select';
import { MatRadioModule } from '@angular/material/radio';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatchFilters } from '../../models/matchFilters.model';

@Component({
  selector: 'app-match-list',
  imports: [
    RouterModule,
    ReactiveFormsModule,
    CommonModule,
    MatFormFieldModule,
    MatDatepickerModule,
    MatSelectModule,
    MatRadioModule,
    MatCardModule,
    MatIconModule,
    MatPaginator,
    MatSortModule,
    MatTableModule,
    MatButtonModule,
    MatInputModule,
  ],
  templateUrl: './match-list.component.html',
  styleUrl: './match-list.component.scss',
})
export class MatchListComponent implements OnInit, AfterViewInit {
  public matches: Match[] = [];
  public leagues: League[] = [];
  public displayedColumns: string[] = [
    'date',
    'homeTeam',
    'homeScore',
    'awayScore',
    'awayTeam',
    'actions',
  ];
  public dataSource = new MatTableDataSource<Match>();
  public totalCount = 0;
  public isLoading = false;
  public pageSizeOptions = [5, 10, 15];

  protected filterForm: FormGroup<FilterForm> = new FormGroup<FilterForm>({
    clubName: new FormControl<string | null>(null),
    dateFrom: new FormControl<string | null>(null),
    dateTo: new FormControl<string | null>(null),
    seasonYear: new FormControl<number | null>(null),
    leagueId: new FormControl<number | null>(null),
    status: new FormControl<'SCHEDULED' | 'COMPLETED' | null>(null),
    sortDirection: new FormControl<string | null>('asc'),
  });

  @ViewChild(MatPaginator) public paginator!: MatPaginator;
  @ViewChild(MatSort) public sort!: MatSort;

  public constructor(
    private matchService: MatchService,
    private leagueService: LeagueService,
    private router: Router
  ) {}

  public ngOnInit(): void {
    this.loadLeagues();
    this.updateDataSource();

    this.filterForm.valueChanges.subscribe(() => {
      this.applyFilters();
    });
  }

  public ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;

    this.dataSource.sortingDataAccessor = (
      item: Match,
      property: string
    ): string | number => {
      switch (property) {
        case 'date':
          return new Date(item.date).getTime();
        case 'homeTeam':
          return item.homeTeam.name.toLowerCase();

        case 'awayTeam':
          return item.awayTeam.name.toLowerCase();

        case 'homeScore':
          return item.homeScore || 0;

        case 'awayScore':
          return item.awayScore || 0;

        case 'status':
          return item.status;

        default:
          return '';
      }
    };
  }

  public updateDataSource(): void {
    this.matchService.getAllMatches().subscribe({
      next: (data) => {
        this.dataSource.data = data;
        this.totalCount = data.length;
      },
      error: (err) => console.log(err),
    });
  }

  public applyFilters(): void {
    const filterValues = this.filterForm.getRawValue();

    this.dataSource.filterPredicate = (
      data: Match,
      filter: string
    ): boolean => {
      const filterObj: MatchFilters = JSON.parse(filter);
      const matchDate = new Date(data.date);

      return this.checkFilters(data, filterObj, matchDate);
    };

    this.dataSource.filter = JSON.stringify(filterValues);
    this.totalCount = this.dataSource.filteredData.length;
    this.paginator.firstPage();
  }

  private checkFilters(
    data: Match,
    filterObj: MatchFilters,
    matchDate: Date
  ): boolean {
    return Object.entries(filterObj).every(([key, value]) => {
      if (!value) return true;

      switch (key) {
        case 'clubName':
          return this.checkClubName(data, value as string);
        case 'dateFrom':
          return matchDate >= new Date(value as string);
        case 'dateTo':
          return matchDate <= new Date(value as string);
        case 'seasonYear':
          return data.seasonId === value;
        case 'leagueId':
          return data.leagueId === parseInt(value, 10);
        case 'status':
          return data.status === value;
        default:
          return true;
      }
    });
  }

  private checkClubName(data: Match, clubName?: string): boolean {
    if (!clubName) return true;

    return [data.homeTeam.name, data.awayTeam.name].some((name) =>
      name.toLowerCase().includes(clubName.toLowerCase())
    );
  }

  private checkDateFrom(matchDate: Date, dateFrom?: string): boolean {
    if (!dateFrom) return true;

    return matchDate >= new Date(dateFrom);
  }

  private checkDateTo(matchDate: Date, dateTo?: string): boolean {
    if (!dateTo) return true;

    return matchDate <= new Date(dateTo);
  }

  private checkSeasonYear(data: Match, seasonYear?: number): boolean {
    if (!seasonYear) return true;

    return data.seasonId === seasonYear;
  }

  private checkLeagueId(data: Match, leagueId?: number): boolean {
    if (!leagueId) return true;

    return data.leagueId === leagueId;
  }

  private checkStatus(
    data: Match,
    status?: 'SCHEDULED' | 'COMPLETED'
  ): boolean {
    if (!status) return true;

    return data.status === status;
  }

  public resetFilters(): void {
    this.filterForm.reset();
    this.applyFilters();
    this.updateDataSource();
  }

  public loadLeagues(): void {
    this.leagueService.getAllLeagues().subscribe({
      next: (data) => {
        this.leagues = data;
      },
      error: (err) => console.log(err),
    });
  }

  public redirectToMatchDetails(id: number): void {
    this.router.navigate(['/matches', id]);
  }

  public redirectToForm(): void {
    console.log('lala');
    this.router.navigate(['/matches/form']);
  }
}
