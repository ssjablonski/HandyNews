import { inject } from '@angular/core';
import { CanMatchFn, Router } from '@angular/router';
import { UserService } from '../../features/user/services/user.service';

export const authMatchGuard: CanMatchFn = () => {
  const isLoggedIn = inject(UserService).isLoggedIn();
  inject(UserService).checkTokenExpiration();

  return isLoggedIn
    ? isLoggedIn
    : inject(Router).createUrlTree(['/users/login']);
};
