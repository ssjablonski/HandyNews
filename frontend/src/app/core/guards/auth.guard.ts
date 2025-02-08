import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { UserService } from '../../features/user/services/auth.service';

export const authGuard: CanActivateFn = () => {
  const isLoggedIn = inject(UserService).isLoggedIn();
  inject(UserService).checkTokenExpiration();

  return isLoggedIn ? isLoggedIn : inject(Router).createUrlTree(['/login']);
};
