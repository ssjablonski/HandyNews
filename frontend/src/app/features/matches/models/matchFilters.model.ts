export interface MatchFilters {
  readonly clubName?: string | null;
  readonly dateFrom?: string | null;
  readonly dateTo?: string | null;
  readonly seasonYear?: number | null;
  readonly leagueId?: string | null;
  readonly status?: 'scheduled' | 'completed';
  readonly sortDirection?: 'asc' | 'desc' | null;
}
