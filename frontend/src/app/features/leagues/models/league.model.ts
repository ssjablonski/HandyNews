import { Season } from '../../season/models/season.model';

export interface League {
  readonly id: number;
  readonly name: string;
  readonly country: string;
  readonly logo: string;
  readonly seasons: Season[];
}
