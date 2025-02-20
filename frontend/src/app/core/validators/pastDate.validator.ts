import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export function pastDate(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const controlDate = new Date(control.value);

    const today = new Date();
    if (!(controlDate > today)) {
      return null;
    }

    return { invalidDate: true };
  };
}
