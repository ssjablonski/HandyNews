import { Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { authGuard } from '../../core/guards/auth.guard';
import { MatchFormComponent } from '../matches/components/match-form/match-form.component';
import { MatchDetailsComponent } from '../matches/components/match-details/match-details.component';
import { MatchListComponent } from '../matches/components/match-list/match-list.component';
import { ClubFormComponent } from '../clubs/components/club-form/club-form.component';
import { SeasonFormComponent } from '../season/components/season-form/season-form.component';

export const userRoutes: Routes = [
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [authGuard],
  },
  {
    path: 'matches',
    component: MatchListComponent,
    canActivate: [authGuard],
  },
  {
    path: 'matches/form',
    component: MatchFormComponent,
    canActivate: [authGuard],
  },
  {
    path: 'matches/form/:id',
    component: MatchFormComponent,
    canActivate: [authGuard],
  },
  {
    path: 'matches/details/:id',
    component: MatchDetailsComponent,
    canActivate: [authGuard],
  },
  {
    path: 'clubs/form',
    component: ClubFormComponent,
    canActivate: [authGuard],
  },
  {
    path: 'clubs/form/:id',
    component: ClubFormComponent,
    canActivate: [authGuard],
  },
  {
    path: 'seasons/form',
    component: SeasonFormComponent,
    canActivate: [authGuard],
  },
  {
    path: 'seasons/form/:id',
    component: SeasonFormComponent,
    canActivate: [authGuard],
  },
];
