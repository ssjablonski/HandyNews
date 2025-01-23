package com.seba.handy_news.season;

import com.seba.handy_news.league.League;
import com.seba.handy_news.league.LeagueRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeasonService {
    private final SeasonRepository seasonRepository;
    private final LeagueRepository leagueRepository;

    public List<Season> getAllSeasons() {
        return seasonRepository.findAll();
    }

    public Season getSeasonById(Long id) {
        return seasonRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Season not found with id: " + id));
    }

    public Season createSeason(Long leagueId, Season season) {
        League league = leagueRepository.findById(leagueId).orElseThrow(() -> new EntityNotFoundException("League not found with id: " + leagueId));
        season.setLeague(league);
        return seasonRepository.save(season);
    }

    public void deleteSeason(Long seasonId) {
        seasonRepository.deleteById(seasonId);
    }

    @Transactional
    public Season updateSeason(Long id, Season updatedSeason) {
        Season existingSeason = getSeasonById(id);
        existingSeason.setName(updatedSeason.getName());
        existingSeason.setYear(updatedSeason.getYear());
        return seasonRepository.save(existingSeason);
    }

    public List<Season> findSeasonByYear(int year) {
        return seasonRepository.findByYear(year);
    }
}
