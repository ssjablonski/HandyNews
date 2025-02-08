import { FormArray, FormControl, FormGroup } from '@angular/forms';
import { UserAddressForm } from './userAddressForm.model';

export interface UserRegisterForm {
  readonly email: FormControl<string | null>;
  readonly password: FormControl<string | null>;
  readonly firstName: FormControl<string | null>;
  readonly lastName: FormControl<string | null>;
  readonly dateOfBirth: FormControl<string | null>;
  readonly phoneNumber: FormControl<string | null>;
  //   TODO - musisz na backu dodac to!
  readonly address: FormArray<FormGroup<UserAddressForm>>;
}
