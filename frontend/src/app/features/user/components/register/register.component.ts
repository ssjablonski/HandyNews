import { ChangeDetectionStrategy, Component } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatButtonModule } from '@angular/material/button';
import { provideNativeDateAdapter } from '@angular/material/core';
import { MatExpansionModule } from '@angular/material/expansion';
import {
  FormArray,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { UserRegisterForm } from '../../models/userRegisterForm.model';
import { UserAddressForm } from '../../models/userAddressForm.model';
import { pastDate } from '../../../../core/validators/pastDate.validator';
import { validPhoneNumber } from '../../../../core/validators/validPhoneNumber.validator';
import { MatSnackBar } from '@angular/material/snack-bar';
import { RegisterData } from '../../models/registerData.model';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-register',
  providers: [provideNativeDateAdapter()],
  imports: [
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatDatepickerModule,
    MatExpansionModule,
    ReactiveFormsModule,
    RouterModule,
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class RegisterComponent {
  public constructor(
    private userService: UserService,
    private snackBar: MatSnackBar,
    private router: Router
  ) {}

  protected registerForm: FormGroup<UserRegisterForm> =
    new FormGroup<UserRegisterForm>({
      email: new FormControl<string>('', {
        validators: [Validators.required, Validators.email],
      }),
      password: new FormControl<string>('', {
        validators: [Validators.required],
      }),
      firstName: new FormControl<string>('', {
        validators: [Validators.required],
      }),
      lastName: new FormControl<string>('', {
        validators: [Validators.required],
      }),
      dateOfBirth: new FormControl<string>('', {
        validators: [Validators.required, pastDate()],
      }),
      phoneNumber: new FormControl<string>('', {
        validators: [Validators.required, validPhoneNumber()],
      }),
      address: new FormArray<FormGroup<UserAddressForm>>([
        new FormGroup<UserAddressForm>(
          {
            city: new FormControl<string | null>(null, {}),
            zipcode: new FormControl<string | null>(null, {}),
            houseNumber: new FormControl<string | null>(null, {}),
            street: new FormControl<string | null>(null, {}),
          },
          {
            validators: [], // TODO - custom validator czy sa wszystkie czy nie
          }
        ),
      ]),
    });

  protected onSubmit(): void {
    const registerFormData = this.registerForm.value;
    this.userService.register(registerFormData as RegisterData).subscribe({
      next: () => {
        this.snackBar.open(
          'Your account has been successfully created.',
          'Close',
          {
            duration: 2000,
            horizontalPosition: 'right',
            verticalPosition: 'top',
          }
        );
        this.router.navigate(['/dashboard']);
      },
    });
  }

  protected get address(): FormArray<FormGroup<UserAddressForm>> {
    return this.registerForm.controls.address as FormArray<
      FormGroup<UserAddressForm>
    >;
  }
}
