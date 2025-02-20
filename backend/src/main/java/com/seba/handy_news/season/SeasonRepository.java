package com.seba.handy_news.season;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeasonRepository extends JpaRepository<Season, Long> {
    List<Season> findByYear(int year);

    @Query(value = "SELECT * FROM season WHERE year = :year", nativeQuery = true)
    List<Season> findSeasonsByYearNative(@Param("year") int year);

    List<Season> findByLeagueId(Long leagueId);
}
