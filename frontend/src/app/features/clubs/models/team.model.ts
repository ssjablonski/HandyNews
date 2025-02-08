import { League } from '../../leagues/models/league.model';

export interface Team {
  readonly id: number;
  readonly name: string;
  readonly city: string;
  readonly logo: string;
  readonly league: League;
}
