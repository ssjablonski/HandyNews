package com.seba.handy_news.season;

import com.seba.handy_news.league.League;
import com.seba.handy_news.league.LeagueRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SeasonService {
    private final SeasonRepository seasonRepository;
    private final LeagueRepository leagueRepository;

    public Set<Season> getAllSeasonsFromLeague(Long leagueId) {
        League league = leagueRepository.findById(leagueId).orElseThrow(() -> new IllegalArgumentException("League not found"));
        return league.getSeasons();
    }

    public Season getSeasonById(Long id) {
        return seasonRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Season not found"));
    }

    public Season createSeason(Season season, Long leagueId) {
        League league = leagueRepository.findById(leagueId).orElseThrow(() -> new IllegalArgumentException("League not found"));
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
        Season existingSeason = seasonRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Season not found"));

        existingSeason.setName(updatedSeason.getName());
        existingSeason.setStartDate(updatedSeason.getStartDate());
        existingSeason.setEndDate(updatedSeason.getEndDate());
        existingSeason.setLeague(updatedSeason.getLeague());
        existingSeason.setMatches(updatedSeason.getMatches());

        return seasonRepository.save(existingSeason);
    }
}
