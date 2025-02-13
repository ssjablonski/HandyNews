import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export function noScoreWhenScheduled(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const homeScoreControl = control.get('homeScore');
    const awayScoreControl = control.get('awayScore');
    const statusControl = control.get('status');

    if (!homeScoreControl || !awayScoreControl || !statusControl) {
      return null;
    }

    const homeScore = homeScoreControl.value as number;
    const awayScore = awayScoreControl.value as number;
    const status = statusControl.value as string;
    console.log('noScoreWhenScheduled:', { homeScore, awayScore, status });

    if (status === 'SCHEDULED' && (homeScore !== 0 || awayScore !== 0)) {
      return { noScoreWhenScheduled: true };
    }

    return null;
  };
}
