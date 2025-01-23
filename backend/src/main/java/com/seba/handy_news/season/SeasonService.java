package com.seba.handy_news.season;

import com.seba.handy_news.league.League;
import com.seba.handy_news.league.LeagueRepository;
import com.seba.handy_news.league.LeagueService;
import com.seba.handy_news.match.Match;
import com.seba.handy_news.match.MatchRepository;
import com.seba.handy_news.season.SeasonClub.SeasonClub;
import com.seba.handy_news.season.SeasonClub.SeasonClubRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeasonService {
    private final SeasonRepository seasonRepository;
    private final LeagueRepository leagueRepository;
    private final LeagueService leagueService;
    private final MatchRepository matchRepository;
    private final SeasonClubRepository seasonClubRepository;

    public Season getSeasonById(Long id) {
        return seasonRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Season not found with id: " + id));
    }

    public List<Season> getAllSeasonsFromLeague(Long leagueId) {
        return seasonRepository.findAllByLeagueId(leagueId);
    }

    public List<Season> getAll() {
        return seasonRepository.findAll();
    }

    public Season createSeason(Season season, Long leagueId) {
        League league = leagueService.getLeagueById(leagueId);
        season.setLeague(league);
        Season savedSeason = seasonRepository.save(season);
        league.getSeasons().add(savedSeason);
        leagueRepository.save(league);
        return savedSeason;
    }

    @Transactional
    public void deleteSeason(Long seasonId) {
        seasonRepository.deleteById(seasonId);
    }

    @Transactional
    public Season updateSeason(Long id, Season updatedSeason) {
        Season existingSeason = getSeasonById(id);
        existingSeason.setName(updatedSeason.getName());
        existingSeason.setStartDate(updatedSeason.getStartDate());
        existingSeason.setEndDate(updatedSeason.getEndDate());
        return seasonRepository.save(existingSeason);
    }

    @Transactional
    public void addMatchToSeason(Long seasonId, Match match) {
        Season season = getSeasonById(seasonId);
        season.getMatches().add(match);
        match.setSeason(season);
        matchRepository.save(match);
        seasonRepository.save(season);
    }

    @Transactional
    public void removeMatchFromSeason(Long seasonId, Long matchId) {
        Season season = getSeasonById(seasonId);
        Match match = season.getMatches().stream()
                .filter(m -> m.getId().equals(matchId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Match not found in season"));
        season.getMatches().remove(match);
        match.setSeason(null);
        seasonRepository.save(season);
        matchRepository.save(match);
    }

    public List<Match> getAllMatchesFromSeason(Long seasonId) {
        Season season = getSeasonById(seasonId);
        return new ArrayList<>(season.getMatches());
    }

    @Transactional
    public void addSeasonClubToSeason(Long seasonId, SeasonClub seasonClub) {
        Season season = getSeasonById(seasonId);
        season.getSeasonClubs().add(seasonClub);
        seasonClub.setSeason(season);
        seasonClubRepository.save(seasonClub);
        season.getSeasonClubs().add(seasonClub);
        seasonRepository.save(season);
    }

    @Transactional
    public void removeSeasonClubFromSeason(Long seasonId, Long seasonClubId) {
        Season season = getSeasonById(seasonId);
        SeasonClub seasonClubToRemove = season.getSeasonClubs().stream()
                .filter(sc -> sc.getId().equals(seasonClubId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("SeasonClub not found in the season with id: " + seasonId));

        season.getSeasonClubs().remove(seasonClubToRemove);
        seasonClubToRemove.setSeason(null);
        seasonRepository.save(season);
    }

    public List<SeasonClub> getAllSeasonClubsFromSeason(Long seasonId) {
        Season season = getSeasonById(seasonId);
        return new ArrayList<>(season.getSeasonClubs());
    }
}
