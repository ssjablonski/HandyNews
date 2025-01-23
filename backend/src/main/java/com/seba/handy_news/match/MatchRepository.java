package com.seba.handy_news.match;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
//    @Query("SELECT m FROM Match m JOIN m.")
}
