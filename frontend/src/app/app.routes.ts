import { Routes } from '@angular/router';
import { HomePageComponent } from './core/layout/home-page/home-page.component';
import { PageNotFoundComponent } from './core/layout/page-not-found/page-not-found.component';

export const routes: Routes = [
  {
    path: '',
    component: HomePageComponent,
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
