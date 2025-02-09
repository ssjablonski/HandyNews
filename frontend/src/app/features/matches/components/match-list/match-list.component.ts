import {
  AfterViewInit,
  ChangeDetectorRef,
  Component,
  OnInit,
  ViewChild,
} from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatchService } from '../../services/match.service';
import { Match } from '../../models/match.model';
import { Router, RouterModule } from '@angular/router';
import { League } from '../../../leagues/models/league.model';
import { LeagueService } from '../../../leagues/services/league.service';
import { FilterForm } from '../../models/filterForm.model';
import { CommonModule } from '@angular/common';
import { MatchFilters } from '../../models/matchFilters.model';

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
    leagueId: new FormControl<string | null>(null),
    status: new FormControl<string | null>(null),
    sortDirection: new FormControl<string | null>('asc'),
  });

  @ViewChild(MatPaginator) public paginator!: MatPaginator;
  @ViewChild(MatSort) public sort!: MatSort;

  public constructor(
    private matchService: MatchService,
    private leagueService: LeagueService,
    private cd: ChangeDetectorRef,
    private router: Router
  ) {}

  public ngOnInit(): void {
    this.loadLeagues();
    this.matchService.matches$.subscribe((data) => {
      this.matches = data.content;
      this.totalCount = data.totalElements;
      this.dataSource.data = this.matches;
      this.cd.markForCheck();
    });

    this.matchService.isLoading$.subscribe((isLoading) => {
      this.isLoading = isLoading;
      this.cd.markForCheck();
    });
  }

  public ngAfterViewInit(): void {
    this.paginator.page.subscribe((event) => {
      this.matchService.updatePagination(event.pageIndex, event.pageSize);
    });

    this.sort.sortChange.subscribe((sort) => {
      this.matchService.updateSort(sort.active, sort.direction);
    });

    this.filterForm.valueChanges.subscribe(() => {
      this.matchService.updateFilters(this.getFilters());
    });
  }

  private getFilters(): MatchFilters {
    return {
      clubName: this.filterForm.value.clubName || '',
      dateFrom: this.filterForm.value.dateFrom,
      dateTo: this.filterForm.value.dateTo,
      seasonYear: this.filterForm.value.seasonYear,
      leagueId: this.filterForm.value.leagueId,
      status: this.filterForm.value.status as 'scheduled' | 'completed',
    };
  }

  public redirectToMatchDetails(id: number): void {
    this.router.navigate([`user/matches/details/${id}`]);
  }

  private loadLeagues(): void {
    this.leagueService.getAllLeagues().subscribe((leagues) => {
      this.leagues = leagues;
      this.cd.markForCheck();
    });
  }

  public resetFilters(): void {
    this.filterForm.reset();
    this.matchService.updateFilters({});
  }
}
