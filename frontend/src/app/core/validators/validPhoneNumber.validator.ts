import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export function validPhoneNumber(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const phonePattern = /^[0-9]{9}$/;
    if (!control.value || phonePattern.test(control.value)) {
      return null;
    }

    return {
      invalidPhoneNumber: true,
    };
  };
}
