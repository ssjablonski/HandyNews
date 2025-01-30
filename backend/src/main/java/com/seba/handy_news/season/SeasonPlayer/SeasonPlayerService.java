package com.seba.handy_news.season.SeasonPlayer;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeasonPlayerService {
    private final SeasonPlayerRepository seasonPlayerRepository;

    public SeasonPlayer getSeasonPlayerById(Long id) {
        return seasonPlayerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("SeasonPlayer not found with id: " + id));
    }

    public List<SeasonPlayer> getAllFromPlayer(Long playerId) {
        return seasonPlayerRepository.findAllByPlayerId(playerId);
    }

//    TODO - no wlasnie nie wiem brac tu w argumencie seasonclubid i playerid??
    public SeasonPlayer createSeasonPlayer(SeasonPlayer seasonPlayer) {
        return seasonPlayerRepository.save(seasonPlayer);
    }

//    TODO chyba nie ma co zmieniac???
//    public SeasonPlayer updateSeasonPlayer(Long id, SeasonPlayer seasonPlayer) {
//        SeasonPlayer season = getSeasonPlayerById(id);
//        season.
//    }

    public void deleteSeasonPlayer(Long id) {
        seasonPlayerRepository.deleteById(id);
    }
}
