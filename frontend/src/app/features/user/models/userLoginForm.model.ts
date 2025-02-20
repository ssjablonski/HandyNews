import { FormControl } from '@angular/forms';

export interface UserLoginForm {
  readonly email: FormControl<string | null>;
  readonly password: FormControl<string | null>;
}
