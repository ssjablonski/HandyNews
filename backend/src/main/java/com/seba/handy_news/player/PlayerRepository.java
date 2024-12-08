package com.seba.handy_news.player;

import com.seba.handy_news.enums.Hand;
import com.seba.handy_news.enums.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findByClubId(Long clubId);

    List<Player> findByNationality(String nationality);

    List<Player> findByHand(Hand hand);

    List<Player> findByPosition(Position position);

    List<Player> findByHeightGreaterThan(double height);

    List<Player> findByWeightGreaterThan(double weight);
}
