import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { RouterModule } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthService } from '../../../features/auth/services/auth.service';

@Component({
  selector: 'app-header',
  imports: [MatToolbarModule, MatButtonModule, RouterModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss',
})
export class HeaderComponent implements OnInit, OnDestroy {
  public user: string | null = null;
  private sub!: Subscription;

  public constructor(private authService: AuthService) {}

  public ngOnInit(): void {
    this.sub = this.authService.user$.subscribe({
      next: (user) => {
        this.user = user;
      },
    });
  }

  public logOut(): void {
    this.authService.logout();
  }

  public ngOnDestroy(): void {
    this.sub.unsubscribe();
  }
}
