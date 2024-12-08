package com.seba.handy_news.club;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
    List<Club> findClubByName(String name);
    List<Club> findClubByCity(String city);
}
