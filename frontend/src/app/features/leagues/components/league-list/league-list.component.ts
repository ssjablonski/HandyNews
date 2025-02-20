import { LeagueService } from './../../services/league.service';
import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { League } from '../../models/league.model';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { MatSort, MatSortModule } from '@angular/material/sort';

import { ConfirmDialogComponent } from '../../../../shared/components/confirm-dialog/confirm-dialog.component';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';

@Component({
  selector: 'app-league-list',
  imports: [
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    MatCardModule,
    MatSortModule,
    MatPaginatorModule,
  ],
  templateUrl: './league-list.component.html',
  styleUrl: './league-list.component.scss',
})
export class LeagueListComponent implements OnInit, AfterViewInit {
  public leagues: League[] = [];
  public displayedColumns: string[] = ['logo', 'name', 'country', 'actions'];
  public dataSource = new MatTableDataSource<League>();
  public totalCount = 0;
  public isLoading = false;
  public pageSizeOptions = [5, 10, 15];

  @ViewChild(MatPaginator) public paginator!: MatPaginator;
  @ViewChild(MatSort) public sort!: MatSort;

  public constructor(
    private leagueService: LeagueService,
    private snackBar: MatSnackBar,
    private router: Router,
    private dialog: MatDialog
  ) {}

  public ngOnInit(): void {
    this.loadLeagues();
  }

  public ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;

    this.dataSource.sortingDataAccessor = (item: League, property: string) =>
      this.getSortingValue(item, property);
  }

  private sortingStrategies = {
    name: (item: League) => item.name.toLowerCase(),
    country: (item: League) => item.country.toLowerCase(),
    default: () => '',
  };

  private getSortingValue(item: League, property: string): string | number {
    const strategy =
      this.sortingStrategies[property as keyof typeof this.sortingStrategies];

    return strategy(item) || this.sortingStrategies.default();
  }

  public loadLeagues(): void {
    this.leagueService.getAllLeagues().subscribe({
      next: (leagues) => {
        this.leagues = leagues;
        this.dataSource.data = leagues;
        this.totalCount = leagues.length;
      },
      error: (err) => this.toastError('load', err),
    });
  }

  public createLeague(): void {
    this.router.navigate(['/user/leagues/form']);
  }

  public updateLeague(id: number): void {
    this.router.navigate([`/user/leagues/form/${id}`]);
  }

  public deleteLeague(id: number): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '600px',
      data: {
        title: 'Delete League',
        message: 'Are you sure you want to delete this league?',
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result === 'confirm') {
        this.leagueService.deleteLeague(id).subscribe({
          next: () => {
            this.loadLeagues();
            this.toastSuccess('League deleted successfully');
          },
          error: (err) => this.toastError('delete', err),
        });
      }
    });
  }

  public toastSuccess(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 5000,
      horizontalPosition: 'right',
      verticalPosition: 'top',
    });
  }

  public toastError(action: string, error: Error): void {
    this.snackBar.open(`Failed to ${action}: ${error.message}`, 'Close', {
      duration: 5000,
      horizontalPosition: 'right',
      verticalPosition: 'top',
    });
  }
}
