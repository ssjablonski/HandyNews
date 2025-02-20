import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export function completeAddressValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const city = control.get('city')?.value as string | undefined;
    const zipcode = control.get('zipcode')?.value as string | undefined;
    const houseNumber = control.get('houseNumber')?.value as string | undefined;
    const street = control.get('street')?.value as string | undefined;

    const fields = [city, zipcode, houseNumber, street];
    const allFieldsEmpty = fields.every((field) => !field);
    const allFieldsFilled = fields.every((field) => !!field);

    if (allFieldsEmpty || allFieldsFilled) {
      return null;
    }

    return { incompleteAddress: true };
  };
}
