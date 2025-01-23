package com.seba.handy_news.season.SeasonClub;

import com.seba.handy_news.club.Club;
import com.seba.handy_news.club.ClubService;
import com.seba.handy_news.season.Season;
import com.seba.handy_news.season.SeasonPlayer.SeasonPlayer;
import com.seba.handy_news.season.SeasonPlayer.SeasonPlayerRepository;
import com.seba.handy_news.season.SeasonService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeasonClubService {
    private final SeasonClubRepository seasonClubRepository;
    private final ClubService clubService;
    private final SeasonService seasonService;
    private final SeasonPlayerRepository seasonPlayerRepository;

    public SeasonClub getSeasonClubById(Long id) {
        return seasonClubRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("SeasonClub not found with id: " + id));
    }

    public SeasonClub createSeasonClub(SeasonClub seasonClub, Long clubId, Long seasonId) {
        Club club = clubService.getClubById(clubId);
        Season season = seasonService.getSeasonById(seasonId);
        seasonClub.setClub(club);
        seasonClub.setSeason(season);
        return seasonClubRepository.save(seasonClub);
    }

    public SeasonClub updateSeasonClub(Long id, SeasonClub newSeasonClub) {
        SeasonClub seasonClub = getSeasonClubById(id);
        seasonClub.setClub(newSeasonClub.getClub());
        seasonClub.setSeason(newSeasonClub.getSeason());
        return seasonClubRepository.save(seasonClub);
    }

    public void deleteSeasonClub(Long id) {
        seasonClubRepository.deleteById(id);
    }

    public List<SeasonPlayer> getPlayersFromSeasonClub(Long id) {
        SeasonClub seasonClub = getSeasonClubById(id);
        return new ArrayList<>(seasonClub.getSeasonPlayers());
    }

    public void addPlayerToSeasonClub(Long id, SeasonPlayer seasonPlayer) {
        SeasonClub seasonClub = getSeasonClubById(id);
        seasonClub.getSeasonPlayers().add(seasonPlayer);
        seasonPlayer.setSeasonClub(seasonClub);
        seasonClubRepository.save(seasonClub);
        seasonPlayerRepository.save(seasonPlayer);
    }

    @Transactional
    public void removePlayerFromSeasonClub(Long id, Long playerId) {
        SeasonClub seasonClub = getSeasonClubById(id);
        SeasonPlayer player = seasonClub.getSeasonPlayers().stream()
                .filter(p -> p.getId().equals(playerId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("SeasonPlayer not found with id: " + playerId));
        seasonClub.getSeasonPlayers().remove(player);
        player.setSeasonClub(null);
        seasonClubRepository.save(seasonClub);
        seasonPlayerRepository.save(player);
    }
}
