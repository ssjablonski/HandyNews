import { FormControl } from '@angular/forms';

export interface UserAddressForm {
  readonly city: FormControl<string | null>;
  readonly zipcode: FormControl<string | null>;
  readonly houseNumber: FormControl<string | null>;
  readonly street: FormControl<string | null>;
}
