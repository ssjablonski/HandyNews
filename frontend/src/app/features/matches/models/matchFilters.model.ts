export interface MatchFilters {
  readonly clubName?: string;
  readonly dateFrom?: string;
  readonly dateTo?: string;
  readonly seasonId?: number;
  readonly leagueId?: number;
  readonly status?: 'SCHEDULED' | 'COMPLETED';
}
