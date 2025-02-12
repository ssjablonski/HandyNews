import { Team } from '../../clubs/models/team.model';

export interface Match {
  readonly id: number;
  readonly date: Date;
  readonly homeScore: number;
  readonly awayScore: number;
  readonly homeTeam: Team;
  readonly awayTeam: Team;
  readonly status: 'SCHEDULED' | 'COMPLETED';
  readonly leagueId: number;
  readonly seasonId: number;
}
