package com.seba.handy_news.match;

import com.seba.handy_news.league.LeagueRepository;
import com.seba.handy_news.player.PlayerStats.PlayerStats;
import com.seba.handy_news.player.PlayerStats.PlayerStatsRepository;
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
    private final PlayerStatsRepository playerStatsRepository;
    private final LeagueRepository leagueRepository;
    private final SeasonRepository seasonRepository;

    public Match getMatchById(Long id) {
        return matchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Match not found with id: " + id));
    }

    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    public List<Match> getAllMatchesFromSeason(Long seasonId) {
        return matchRepository.findBySeasonId(seasonId);
    }

//    TODO - tu to samo, brac seasonid tutaj i przypisywac czy jak?
    public Match createMatch(Match match, Long seasonId) {
        return matchRepository.save(match);
    }

    public void deleteMatch(Long matchId) {
        matchRepository.deleteById(matchId);
    }

    @Transactional
    public Match updateMatch(Long id, Match updatedMatch) {
        Match existingMatch = getMatchById(id);

        existingMatch.setDate(updatedMatch.getDate());
        existingMatch.setHomeScore(updatedMatch.getHomeScore());
        existingMatch.setAwayScore(updatedMatch.getAwayScore());
//        TODO - nie wiem czy madrze tak robic update druzyn, zapytaj!
//        existingMatch.setAwayTeam(updatedMatch.getAwayTeam());
//        existingMatch.setHomeTeam(updatedMatch.getHomeTeam());
        existingMatch.setSeason(updatedMatch.getSeason());

        return matchRepository.save(existingMatch);
    }

//    TODO - tutaj czy w playerStats?
    public void addPlayerStatsToMatch(Long matchId, PlayerStats playerStats) {
        Match match = getMatchById(matchId);
        match.getPlayerStats().add(playerStats);
        playerStats.setMatch(match);
        matchRepository.save(match);
//        TODO tu tak samo, w takiej sytuacji musze tez zapisac playerStats jak zmieniamy w nim wartosc??? ZAPYTAJ!
        playerStatsRepository.save(playerStats);
    }


}
