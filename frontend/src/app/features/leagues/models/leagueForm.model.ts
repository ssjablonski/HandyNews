import { FormControl } from '@angular/forms';

export interface LeagueForm {
  readonly name: FormControl<string | null>;
  readonly country: FormControl<string | null>;
  readonly logoUrl: FormControl<string | null>;
}
