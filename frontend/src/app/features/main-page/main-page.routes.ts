import { Routes } from '@angular/router';
import { MainPageComponent } from './components/main-page/main-page.component';
import { authGuard } from '../../core/guards/auth.guard';

export const mainPageRoutes: Routes = [
  {
    path: '',
    component: MainPageComponent,
    canActivate: [authGuard],
  },
];
