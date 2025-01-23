package com.seba.handy_news.player.PlayerStats;

import com.seba.handy_news.player.Player;
import com.seba.handy_news.player.PlayerRepository;
import com.seba.handy_news.player.PlayerService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerStatsService {
    private final PlayerStatsRepository playerStatsRepository;
    private final PlayerRepository playerRepository;
    private final PlayerService playerService;

    public PlayerStats getPlayerStatsById(Long id) {
        return playerStatsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PlayerStats not found with id: " + id));
    }

    public PlayerStats createPlayerStats(PlayerStats playerStats) {
        return playerStatsRepository.save(playerStats);
    }

    public PlayerStats updatePlayerStats(Long id, PlayerStats playerStats) {
        PlayerStats stats = getPlayerStatsById(id);
        stats.setGoals(playerStats.getGoals());
        stats.setAssists(playerStats.getAssists());
        stats.setYellowCards(playerStats.getYellowCards());
        stats.setRedCards(playerStats.getRedCards());
        stats.setSuspensions(playerStats.getSuspensions());
        return playerStatsRepository.save(stats);
    }

    public void deletePlayerStats(Long id) {
        playerStatsRepository.deleteById(id);
    }

//    TODO - mialem to w player ale chyba tutaj lepiej???
    @Transactional
    public void addMatchToPlayerStats(Long playerId, PlayerStats playerStats) {
        Player player = playerService.getPlayerById(playerId);
        player.getPlayerStats().add(playerStats);
        playerStats.setPlayer(player);
        playerStatsRepository.save(playerStats);
//        TODO - zapisac playerStats czy nie??
        playerRepository.save(player);
    }

    @Transactional
    public void removeMatchFromPlayerStats(Long playerId, Long playerStatsId) {
        Player player = playerService.getPlayerById(playerId);
        PlayerStats playerStats = player.getPlayerStats().stream()
                .filter(ps -> ps.getId().equals(playerStatsId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("PlayerStats not found in player"));
        player.getPlayerStats().remove(playerStats);
        playerStats.setPlayer(null);
        playerRepository.save(player);
    }
}
