import { Club } from './../../clubs/models/club.model';
import { LeagueDto } from './../../leagues/models/leagueDto.model';
import { Match } from '../../matches/models/match.model';

export interface Season {
  readonly id: number;
  readonly name: string;
  readonly year: number;
  readonly leagueDto: LeagueDto;
  readonly matches: Match[];
  readonly clubs: Club[];
}
