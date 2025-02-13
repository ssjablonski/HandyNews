import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export function sameTeamValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const homeControl = control.get('homeId');
    const awayControl = control.get('awayId');

    if (!homeControl || !awayControl) {
      return null;
    }

    const homeId = homeControl.value as number;
    const awayId = awayControl.value as number;
    console.log('sameTeamValidator:', { homeId, awayId });

    return homeId === awayId ? { sameTeam: true } : null;
  };
}
