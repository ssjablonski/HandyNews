package com.seba.handy_news.league;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeagueService {
    private final LeagueRepository leagueRepository;
//    private final SeasonRepository seasonRepository;

    public List<League> getAllLeagues() {
        return leagueRepository.findAll();
    }

    public League getLeagueById(Long id) {
        return leagueRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("League not found"));
    }

    public League createLeague(League league) {
        return leagueRepository.save(league);
    }

    @Transactional
    public void deleteLeague(Long id) {
        leagueRepository.deleteById(id);
    }

    @Transactional
    public League updateLeague(Long id, League league) {
        League existingLeague = leagueRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("League not found"));

        existingLeague.setCountry(league.getCountry());
        existingLeague.setName(league.getName());
//        TODO - powinienem updatowac tez kolumny z relacjami??
        return leagueRepository.save(existingLeague);
    }

//    TODO - ew. search? Zastanow sie czy chcesz miec wyszukiwarke dla wszystkiego, może połączona ktora pokazuje wszystko mozliwe???
}
