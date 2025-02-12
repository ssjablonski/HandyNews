import { FormControl } from '@angular/forms';

export interface ClubForm {
  readonly leagueId: FormControl<number | null>;
  readonly seasonId: FormControl<number | null>;
  readonly name: FormControl<string | null>;
  readonly city: FormControl<string | null>;
  readonly logoUrl: FormControl<string | null>;
}
