package com.seba.handy_news.player.PlayerClubHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerClubHistoryRepository extends JpaRepository<PlayerClubHistory, Long> {
}
