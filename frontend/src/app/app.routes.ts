import { Routes } from '@angular/router';
import { HomePageComponent } from './core/layout/home-page/home-page.component';
import { PageNotFoundComponent } from './core/layout/page-not-found/page-not-found.component';
import { authMatchGuard } from './core/guards/auth-match.guard';
import { LoginComponent } from './features/user/components/login/login.component';
import { RegisterComponent } from './features/user/components/register/register.component';

export const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'register',
    component: RegisterComponent,
  },
  {
    path: '',
    component: HomePageComponent,
  },
  {
    path: 'user',
    loadChildren: () =>
      import('./features/user/user.routes').then((m) => m.userRoutes),
    canMatch: [authMatchGuard],
  },
  {
    path: 'page-not-found',
    component: PageNotFoundComponent,
  },
  {
    path: '**',
    redirectTo: 'page-not-found',
  },
];
