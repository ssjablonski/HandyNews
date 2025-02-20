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
import { Club } from '../../models/club.model';
import { ClubService } from '../../services/club.service';

@Component({
  selector: 'app-club-list',
  imports: [
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    MatCardModule,
    MatSortModule,
    MatPaginatorModule,
  ],
  templateUrl: './club-list.component.html',
  styleUrl: './club-list.component.scss',
})
export class ClubListComponent implements OnInit, AfterViewInit {
  public clubs: Club[] = [];
  public displayedColumns: string[] = ['logo', 'name', 'city', 'actions'];
  public dataSource = new MatTableDataSource<Club>();
  public totalCount = 0;
  public isLoading = false;
  public pageSizeOptions = [5, 10, 15];

  @ViewChild(MatPaginator) public paginator!: MatPaginator;
  @ViewChild(MatSort) public sort!: MatSort;

  public constructor(
    private clubService: ClubService,
    private snackBar: MatSnackBar,
    private router: Router,
    private dialog: MatDialog
  ) {}

  public ngOnInit(): void {
    this.loadClubs();
  }

  public ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;

    this.dataSource.sortingDataAccessor = (item: Club, property: string) =>
      this.getSortingValue(item, property);
  }

  private sortingStrategies = {
    name: (item: Club) => item.name.toLowerCase(),
    city: (item: Club) => item.city.toLowerCase(),
    default: () => '',
  };

  private getSortingValue(item: Club, property: string): string {
    const strategy =
      this.sortingStrategies[property as keyof typeof this.sortingStrategies];

    return strategy(item) || this.sortingStrategies.default();
  }

  public loadClubs(): void {
    this.clubService.getAllClubs().subscribe({
      next: (clubs) => {
        this.clubs = clubs;
        this.dataSource.data = clubs;
        this.totalCount = clubs.length;
      },
      error: (err) => {
        this.toastError('load', err);
      },
    });
  }

  public createClub(): void {
    this.router.navigate(['/user/clubs/form']);
  }

  public updateClub(id: number): void {
    this.router.navigate([`/user/clubs/form/${id}`]);
  }

  public deleteClub(id: number): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: 'Delete Club',
        message: 'Are you sure you want to delete this club?',
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result === 'confirm') {
        this.clubService.deleteClub(id).subscribe({
          next: () => {
            this.loadClubs();
            this.toastSuccess('Club deleted successfully');
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
