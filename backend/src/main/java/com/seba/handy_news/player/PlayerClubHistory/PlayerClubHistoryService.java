package com.seba.handy_news.player.PlayerClubHistory;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerClubHistoryService {
    private final PlayerClubHistoryRepository playerClubHistoryRepository;

    public PlayerClubHistory getPlayerClubHistoryById(Long id) {
        return playerClubHistoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PlayerClubHistory not found with id: " + id));
    }

//    TODO - czy  to wgl ma sens?
    public List<PlayerClubHistory> getAll() {
        return playerClubHistoryRepository.findAll();
    }

    public PlayerClubHistory createPlayerClubHistory(PlayerClubHistory playerClubHistory) {
        return playerClubHistoryRepository.save(playerClubHistory);
//        TODO - wiadomo tylko tyle czy przypisywac tutaj player id itp
    }

    public PlayerClubHistory updatePlayerClubHistory(Long id, PlayerClubHistory playerClubHistory) {
        PlayerClubHistory history = getPlayerClubHistoryById(id);
        history.setStartDate(playerClubHistory.getStartDate());
        history.setEndDate(playerClubHistory.getEndDate());
//        TODO to samo, tylko tyle czy np player i club tez zmieniac
        return playerClubHistoryRepository.save(history);
    }

    public void deletePlayerClubHistory(Long id) {
        playerClubHistoryRepository.deleteById(id);
    }
}
