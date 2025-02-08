import { inject } from '@angular/core';
import { CanActivateFn } from '@angular/router';
import { AuthService } from '../../features/auth/services/auth.service';

export const authGuard: CanActivateFn = () => {
  const isLoggedIn = inject(AuthService).isLoggedIn();

  return isLoggedIn;
};
