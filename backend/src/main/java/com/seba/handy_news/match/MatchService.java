package com.seba.handy_news.match;

import com.seba.handy_news.club.Club;
import com.seba.handy_news.club.ClubRepository;
import com.seba.handy_news.season.Season;
import com.seba.handy_news.season.SeasonRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchRepository matchRepository;
    private final SeasonRepository seasonRepository;
    private final ClubRepository clubRepository;

    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    public List<Match> getAllMatchesBySeason(Long seasonId) {
        return matchRepository.findBySeasonId(seasonId);
    }


    public Match getMatchById(Long id) {
        return matchRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Match not found with id: " + id));
    }

    public Match createMatch(Long seasonId, Match match, Long homeTeamId, Long awayTeamId) {
        Season season = seasonRepository.findById(seasonId).orElseThrow(() -> new EntityNotFoundException("Season not found with id: " + seasonId));
        Club awayTeam = clubRepository.findById(awayTeamId).orElseThrow(() -> new EntityNotFoundException("Club not found with id: " + awayTeamId));
        Club homeTeam = clubRepository.findById(homeTeamId).orElseThrow(() -> new EntityNotFoundException("Club not found with id: " + homeTeamId));
        match.setSeason(season);
        match.setAwayTeam(awayTeam);
        match.setHomeTeam(homeTeam);
        return matchRepository.save(match);
    }

    @Transactional
    public void deleteMatch(Long matchId) {
        matchRepository.deleteById(matchId);
    }

    @Transactional
    public Match updateMatch(Long id, Match updatedMatch) {
        Match existingMatch = matchRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Match not found with id: " + id));
        existingMatch.setDate(updatedMatch.getDate());
        existingMatch.setHomeScore(updatedMatch.getHomeScore());
        existingMatch.setAwayScore(updatedMatch.getAwayScore());
        return matchRepository.save(existingMatch);
    }

//    public List<Match> findMatchesByTeamName(String teamName) {
//        return matchRepository.findByTeamName(teamName);
//    }
}
