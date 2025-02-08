import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { UserService } from '../../features/user/services/user.service';

export const headerAuthInterceptor: HttpInterceptorFn = (req, next) => {
  const token = inject(UserService).getToken();

  if (token) {
    const newReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`,
      },
    });

    return next(newReq);
  }

  return next(req);
};
