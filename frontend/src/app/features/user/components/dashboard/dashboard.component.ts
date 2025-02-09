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
import { DeleteAccountDialogComponent } from '../delete-account-dialog/delete-account-dialog.component';
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
    const dialogRef = this.dialog.open(DeleteAccountDialogComponent, {
      width: '600px',
      data: { email: this.userInfo?.id },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result === 'confirm' && this.userInfo) {
        this.deleteAccount(this.userInfo.id);
        this.snackBar.open('Account deleted successfully.', 'Close', {
          duration: 2000,
          horizontalPosition: 'right',
          verticalPosition: 'top',
        });
      }
    });
  }

  public deleteAccount(userId: number): void {
    this.userService.deleteAccount(userId).subscribe({
      next: () => {
        this.router.navigate(['user/login']);
      },
      error: (error) => {
        console.error(error);
      },
    });
  }
}
