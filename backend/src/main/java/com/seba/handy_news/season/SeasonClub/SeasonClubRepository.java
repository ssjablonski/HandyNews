package com.seba.handy_news.season.SeasonClub;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeasonClubRepository extends JpaRepository<SeasonClub, Long> {
}
