import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
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
import { Season } from '../../models/season.model';
import { SeasonService } from '../../services/season.service';
import { LeagueService } from '../../../leagues/services/league.service';

@Component({
  selector: 'app-season-list',
  imports: [
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    MatCardModule,
    MatSortModule,
    MatPaginatorModule,
  ],
  templateUrl: './season-list.component.html',
  styleUrl: './season-list.component.scss',
})
export class SeasonListComponent implements OnInit, AfterViewInit {
  public seasons: Season[] = [];
  public displayedColumns: string[] = ['name', 'league', 'year', 'actions'];
  public dataSource = new MatTableDataSource<Season>();
  public totalCount = 0;
  public isLoading = false;
  public pageSizeOptions = [5, 10, 15];

  @ViewChild(MatPaginator) public paginator!: MatPaginator;
  @ViewChild(MatSort) public sort!: MatSort;

  public constructor(
    private seasonService: SeasonService,
    private leagueService: LeagueService,
    private snackBar: MatSnackBar,
    private router: Router,
    private dialog: MatDialog
  ) {}

  public ngOnInit(): void {
    this.loadSeasons();
  }

  public ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;

    this.dataSource.sortingDataAccessor = (item: Season, property: string) =>
      this.getSortingValue(item, property);
  }

  private sortingStrategies = {
    name: (item: Season) => item.name.toLowerCase(),
    year: (item: Season) => item.year || 0,
    league: (item: Season) => item.leagueDto.name.toLowerCase() || '',
    default: () => '',
  };

  private getSortingValue(item: Season, property: string): string | number {
    const strategy =
      this.sortingStrategies[property as keyof typeof this.sortingStrategies];

    return strategy(item) || this.sortingStrategies.default();
  }

  public loadSeasons(): void {
    this.seasonService.getAllSeasons().subscribe({
      next: (seasons) => {
        this.seasons = seasons;
        this.dataSource.data = this.seasons;
        this.totalCount = this.seasons.length;
      },
      error: (err) => {
        this.toastError('load', err);
      },
    });
  }

  public createSeason(): void {
    this.router.navigate(['/user/seasons/form']);
  }

  public updateSeason(id: number): void {
    this.router.navigate([`/user/seasons/form/${id}`]);
  }

  public deleteSeason(id: number): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: 'Delete Season',
        message:
          'Are you sure you want to delete this season? This action will also delete all matches associated with this season.',
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result === 'confirm') {
        this.seasonService.deleteSeason(id).subscribe({
          next: () => {
            this.loadSeasons();
            this.toastSuccess('Season deleted successfully');
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
