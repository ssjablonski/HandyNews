import { Routes } from '@angular/router';
import { HomePageComponent } from './core/layout/home-page/home-page.component';
import { PageNotFoundComponent } from './core/layout/page-not-found/page-not-found.component';
import { LoginComponent } from './features/auth/components/login/login.component';
import { RegisterComponent } from './features/auth/components/register/register.component';
import { authGuard } from './core/guards/auth.guard';

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
    path: 'main-page',
    loadChildren: () =>
      import('./features/main-page/main-page.routes').then(
        (m) => m.mainPageRoutes
      ),
    canMatch: [authGuard],
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
