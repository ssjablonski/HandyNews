import { FormControl } from '@angular/forms';

export interface SeasonForm {
  readonly leagueId: FormControl<number | null>;
  readonly name: FormControl<string | null>;
  readonly year: FormControl<number | null>;
}
