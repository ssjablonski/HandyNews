import { ConfirmDialogComponent } from './../../../../shared/components/confirm-dialog/confirm-dialog.component';
import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { UserInfo } from '../../models/userInfo.model';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-dashboard',
  imports: [
    MatCardModule,
    MatListModule,
    MatIconModule,
    MatDividerModule,
    MatButtonModule,
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent implements OnInit {
  public userInfo: UserInfo | null = null;

  public constructor(
    private userService: UserService,
    private dialog: MatDialog,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  public ngOnInit(): void {
    this.userService.getUserData().subscribe({
      next: (userData) => {
        this.userInfo = userData;
      },
      error: (error) => {
        console.error(error);
      },
    });
  }

  public openDeleteDialog(): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '600px',
      data: {
        title: 'Delete Account',
        message: 'Are you sure you want to delete your account?',
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result === 'confirm' && this.userInfo) {
        this.deleteAccount(this.userInfo.id);
      }
    });
  }

  public deleteAccount(userId: number): void {
    this.userService.deleteAccount(userId).subscribe({
      next: () => {
        this.snackBar.open('Account deleted successfully.', 'Close', {
          duration: 2000,
          horizontalPosition: 'right',
          verticalPosition: 'top',
        });
        this.router.navigate(['user/login']);
      },
      error: (error) => {
        console.error(error);
      },
    });
  }
}
