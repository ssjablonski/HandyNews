package com.seba.handy_news.season.SeasonPlayer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeasonPlayerRepository extends JpaRepository<SeasonPlayer, Long> {
    List<SeasonPlayer> findAllByPlayerId(Long playerId);
}