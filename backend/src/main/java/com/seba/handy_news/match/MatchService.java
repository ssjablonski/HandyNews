package com.seba.handy_news.match;

import com.seba.handy_news.club.Club;
import com.seba.handy_news.club.ClubRepository;
import com.seba.handy_news.season.Season;
import com.seba.handy_news.season.SeasonRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchRepository matchRepository;
    private final SeasonRepository seasonRepository;
    private final ClubRepository clubRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    public List<Match> getAllMatchesBySeason(Long seasonId) {
        return matchRepository.findBySeasonId(seasonId);
    }


    public Match getMatchById(Long id) {
        return matchRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Match not found with id: " + id));
    }

    public Match createMatch(Long seasonId, Match match, Long homeTeamId, Long awayTeamId) {
        Season season = seasonRepository.findById(seasonId).orElseThrow(() -> new EntityNotFoundException("Season not found with id: " + seasonId));
        Club awayTeam = clubRepository.findById(awayTeamId).orElseThrow(() -> new EntityNotFoundException("Club not found with id: " + awayTeamId));
        Club homeTeam = clubRepository.findById(homeTeamId).orElseThrow(() -> new EntityNotFoundException("Club not found with id: " + homeTeamId));
        match.setSeason(season);
        match.setAwayTeam(awayTeam);
        match.setHomeTeam(homeTeam);
        return matchRepository.save(match);
    }

    @Transactional
    public void deleteMatch(Long matchId) {
        matchRepository.deleteById(matchId);
    }

    @Transactional
    public Match updateMatch(Long id, Match updatedMatch) {
        Match existingMatch = matchRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Match not found with id: " + id));
        existingMatch.setDate(updatedMatch.getDate());
        existingMatch.setHomeScore(updatedMatch.getHomeScore());
        existingMatch.setAwayScore(updatedMatch.getAwayScore());
        return matchRepository.save(existingMatch);
    }

    public Page<Match> searchMatches(Long seasonId, Long homeTeamId, Long awayTeamId, Pageable pageable) {
        StringBuilder jpql = new StringBuilder("SELECT m FROM Match m WHERE 1=1");
        if (seasonId != null) {
            jpql.append(" AND m.season.id = :seasonId");
        }
        if (homeTeamId != null) {
            jpql.append(" AND m.homeTeam.id = :homeTeamId");
        }
        if (awayTeamId != null) {
            jpql.append(" AND m.awayTeam.id = :awayTeamId");
        }
        Query query = entityManager.createQuery(jpql.toString(), Match.class);
        if (seasonId != null) {
            query.setParameter("seasonId", seasonId);
        }
        if (homeTeamId != null) {
            query.setParameter("homeTeamId", homeTeamId);
        }
        if (awayTeamId != null) {
            query.setParameter("awayTeamId", awayTeamId);
        }
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<Match> matches = query.getResultList();
        long total = getTotalCount(jpql.toString(), seasonId, homeTeamId, awayTeamId);
        return new PageImpl<>(matches, pageable, total);
    }

    private long getTotalCount(String jpql, Long seasonId, Long homeTeamId, Long awayTeamId) {
        String countJpql = jpql.replace("SELECT m", "SELECT COUNT(m)");
        Query countQuery = entityManager.createQuery(countJpql);
        if (seasonId != null) {
            countQuery.setParameter("seasonId", seasonId);
        }
        if (homeTeamId != null) {
            countQuery.setParameter("homeTeamId", homeTeamId);
        }
        if (awayTeamId != null) {
            countQuery.setParameter("awayTeamId", awayTeamId);
        }
        return (long) countQuery.getSingleResult();
    }
}
