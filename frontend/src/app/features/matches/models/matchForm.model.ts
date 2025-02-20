import { FormControl } from '@angular/forms';

export interface MatchForm {
  readonly date: FormControl<string | null>;
  readonly homeScore: FormControl<number | null>;
  readonly awayScore: FormControl<number | null>;
  readonly status: FormControl<string | null>;
  readonly homeId: FormControl<number | null>;
  readonly awayId: FormControl<number | null>;
  readonly leagueId: FormControl<number | null>;
  readonly seasonId: FormControl<number | null>;
}
