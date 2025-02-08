import { League } from '../../leagues/models/league.model';
import { Match } from '../../matches/models/match.model';

export interface Season {
  readonly id: number;
  readonly name: string;
  readonly year: number;
  readonly league: League;
  readonly matches: Match[];
}
