import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { RouterModule } from '@angular/router';
import { Subscription } from 'rxjs';
import { UserService } from '../../../features/user/services/user.service';

@Component({
  selector: 'app-header',
  imports: [MatToolbarModule, MatButtonModule, RouterModule, MatIconModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss',
})
export class HeaderComponent implements OnInit, OnDestroy {
  public user: string | null = null;
  private sub!: Subscription;

  public constructor(private userService: UserService) {}

  public ngOnInit(): void {
    this.sub = this.userService.user$.subscribe({
      next: (user) => {
        this.user = user;
      },
    });
  }

  public logOut(): void {
    this.userService.logout();
  }

  public ngOnDestroy(): void {
    this.sub.unsubscribe();
  }
}
