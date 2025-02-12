import { FormControl } from '@angular/forms';
export interface FilterForm {
  readonly clubName: FormControl<string | null>;
  readonly dateFrom: FormControl<string | null>;
  readonly dateTo: FormControl<string | null>;
  readonly seasonId: FormControl<number | null>;
  readonly leagueId: FormControl<number | null>;
  readonly status: FormControl<string | null>;
  readonly sortDirection: FormControl<string | null>;
}
