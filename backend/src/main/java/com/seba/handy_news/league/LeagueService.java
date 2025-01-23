package com.seba.handy_news.league;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeagueService {
    private final LeagueRepository leagueRepository;

    public List<League> getAllLeagues() {
        return leagueRepository.findAll();
    }

    public League getLeagueById(Long id) {
        return leagueRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("League not found"));
    }

    public League createLeague(League league) {
        return leagueRepository.save(league);
    }

    public void deleteLeague(Long id) {
        leagueRepository.deleteById(id);
    }

    public League updateLeague(Long id, League league) {
        League existingLeague = getLeagueById(id);
        existingLeague.setCountry(league.getCountry());
        existingLeague.setName(league.getName());
        return leagueRepository.save(existingLeague);
    }
}
