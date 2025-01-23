package com.seba.handy_news.season;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeasonRepository extends JpaRepository<Season, Long> {
    List<Season> findByYear(int year);

    List<Season> findByLeagueId(Long leagueId);
}
