package com.seba.handy_news.league;

import com.seba.handy_news.season.Season;
import com.seba.handy_news.season.SeasonRepository;
import com.seba.handy_news.season.SeasonService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeagueService {
    private final LeagueRepository leagueRepository;
    private final SeasonRepository seasonRepository;
    private final SeasonService seasonService;

    public List<League> getAllLeagues() {
        return this.leagueRepository.findAll();
    }

    public League getLeagueById(Long id) {
        return this.leagueRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("League not found with id: " + id));
    }

    public League createLeague(League league) {
        return leagueRepository.save(league);
    }

    @Transactional
    public League updateLeague(Long id, League updatedLeague) {
        League existingLeague = getLeagueById(id);
        existingLeague.setName(updatedLeague.getName());
        existingLeague.setCountry(updatedLeague.getCountry());
        return leagueRepository.save(existingLeague);
    }

    @Transactional
    public void deleteLeague(Long leagueId) {
        leagueRepository.deleteById(leagueId);
    }

//    TODO chyba nie potrzebne? mozna poprostu albo usunac i stworzyc nowy sezon, metody z seasonService
//    @Transactional
//    public void addSeasonToLeague(Long id, Season season) {
//        League league = getLeagueById(id);
//        season.setLeague(league);
//        Season savedSeason = seasonRepository.save(season);
//        league.getSeasons().add(savedSeason);
//        leagueRepository.save(league);
//    }
//
//    @Transactional
//    public void removeSeasonFromLeague(Long id, Long seasonId) {
//        Season season = seasonService.getSeasonById(seasonId);
//        League league = getLeagueById(id);
//        season.setLeague(null);
//        seasonRepository.save(season);
//        league.getSeasons().remove(season);
//        leagueRepository.save(league);
//    }

    public List<Season> getAllSeasonsFromLeague(Long id) {
        League league = getLeagueById(id);
        return new ArrayList<>(league.getSeasons());
    }
}