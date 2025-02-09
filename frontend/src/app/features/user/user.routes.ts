import { Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { authGuard } from '../../core/guards/auth.guard';
import { MatchFormComponent } from '../matches/components/match-form/match-form.component';
import { MatchDetailsComponent } from '../matches/components/match-details/match-details.component';
import { MatchListComponent } from '../matches/components/match-list/match-list.component';

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
];
