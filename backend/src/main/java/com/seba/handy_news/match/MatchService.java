package com.seba.handy_news.match;

import com.seba.handy_news.league.LeagueRepository;
import com.seba.handy_news.season.Season;
import com.seba.handy_news.season.SeasonRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchRepository matchRepository;
    private final LeagueRepository leagueRepository;
    private final SeasonRepository seasonRepository;

    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    public List<Match> getAllMatchesFromSeason(Long seasonId) {
        return matchRepository.findBySeasonId(seasonId);
    }

    public Match getMatchById(Long id) {
        return matchRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Match not found"));
    }

    public Match createMatch(Match match, Long seasonId) {
        Season season = seasonRepository.findById(seasonId).orElseThrow(() -> new IllegalArgumentException("Season not found"));
        match.setSeason(season);
        return matchRepository.save(match);
    }

    @Transactional
    public void deleteMatch(Long matchId) {
        matchRepository.deleteById(matchId);
    }

    @Transactional
    public Match updateMatch(Long id, Match updatedMatch) {
        Match existingMatch = matchRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Match not found"));

        existingMatch.setDate(updatedMatch.getDate());
        existingMatch.setHomeScore(updatedMatch.getHomeScore());
        existingMatch.setAwayScore(updatedMatch.getAwayScore());
        existingMatch.setAwayTeam(updatedMatch.getAwayTeam());
        existingMatch.setHomeTeam(updatedMatch.getHomeTeam());
        existingMatch.setSeason(updatedMatch.getSeason());

        return matchRepository.save(existingMatch);
    }
}
