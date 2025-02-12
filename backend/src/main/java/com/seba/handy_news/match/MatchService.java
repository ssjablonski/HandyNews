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

import java.time.LocalDate;
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

    public Match createMatch(Long seasonId, MatchRequest matchRequest) {
        Season season = seasonRepository.findById(seasonId)
                .orElseThrow(() -> new EntityNotFoundException("Season not found with id: " + seasonId));
        Club homeTeam = clubRepository.findById(matchRequest.getHomeId())
                .orElseThrow(() -> new EntityNotFoundException("Club not found with id: " + matchRequest.getHomeId()));
        Club awayTeam = clubRepository.findById(matchRequest.getAwayId())
                .orElseThrow(() -> new EntityNotFoundException("Club not found with id: " + matchRequest.getAwayId()));

        Match match = new Match();
        match.setDate(matchRequest.getDate());
        match.setHomeScore(matchRequest.getHomeScore());
        match.setAwayScore(matchRequest.getAwayScore());
        match.setStatus(matchRequest.getStatus());
        match.setSeason(season);
        match.setHomeTeam(homeTeam);
        match.setAwayTeam(awayTeam);
        match.setSeasonId(seasonId);
        match.setLeagueId(season.getLeague().getId());

        return matchRepository.save(match);
    }

    @Transactional
    public void deleteMatch(Long matchId) {
        if (!matchRepository.existsById(matchId)) {
            throw new EntityNotFoundException("Match not found with id: " + matchId);
        }
        matchRepository.deleteById(matchId);
    }

    @Transactional
    public Match updateMatch(Long id, Match updatedMatch) {
        Match existingMatch = matchRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Match not found with id: " + id));
        if (updatedMatch.getDate() != null) {
            existingMatch.setDate(updatedMatch.getDate());
        }
        if (updatedMatch.getHomeScore() != 0) {
            existingMatch.setHomeScore(updatedMatch.getHomeScore());
        }
        if (updatedMatch.getAwayScore() != 0) {
            existingMatch.setAwayScore(updatedMatch.getAwayScore());
        }
        if (updatedMatch.getStatus() != null) {
            existingMatch.setStatus(updatedMatch.getStatus());
        }
        if (updatedMatch.getSeasonId() != null) {
            existingMatch.setSeasonId(updatedMatch.getSeasonId());
        }
        if (updatedMatch.getLeagueId() != null) {
            existingMatch.setLeagueId(updatedMatch.getLeagueId());
        }
        return matchRepository.save(existingMatch);
    }

    public Page<Match> searchMatches(String clubName, String dateFrom, String dateTo, Integer seasonYear, Long leagueId, Boolean completed, Pageable pageable) {
        StringBuilder jpql = new StringBuilder("SELECT m FROM Match m WHERE 1=1");
        if (clubName != null && !clubName.isEmpty()) {
            jpql.append(" AND (m.homeTeam.name LIKE :clubName OR m.awayTeam.name LIKE :clubName)");
        }
        if (dateFrom != null) {
            jpql.append(" AND m.date >= :dateFrom");
        }
        if (dateTo != null) {
            jpql.append(" AND m.date <= :dateTo");
        }
        if (seasonYear != null) {
            jpql.append(" AND m.season.year = :seasonYear");
        }
        if (leagueId != null) {
            jpql.append(" AND m.leagueId = :leagueId");
        }
        if (completed != null) {
            jpql.append(" AND m.isFinished = :completed");
        }
        Query query = entityManager.createQuery(jpql.toString(), Match.class);
        if (clubName != null && !clubName.isEmpty()) {
            query.setParameter("clubName", "%" + clubName + "%");
        }
        if (dateFrom != null) {
            query.setParameter("dateFrom", LocalDate.parse(dateFrom));
        }
        if (dateTo != null) {
            query.setParameter("dateTo", LocalDate.parse(dateTo));
        }
        if (seasonYear != null) {
            query.setParameter("seasonYear", seasonYear);
        }
        if (leagueId != null) {
            query.setParameter("leagueId", leagueId);
        }
        if (completed != null) {
            query.setParameter("completed", completed);
        }
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<Match> matches = query.getResultList();
        long total = getTotalCount(jpql.toString(), clubName, dateFrom, dateTo, seasonYear, leagueId, completed);
        return new PageImpl<>(matches, pageable, total);
    }

    private long getTotalCount(String jpql, String clubName, String dateFrom, String dateTo, Integer seasonYear, Long leagueId, Boolean completed) {
        String countJpql = jpql.replace("SELECT m", "SELECT COUNT(m)");
        Query countQuery = entityManager.createQuery(countJpql);
        if (clubName != null && !clubName.isEmpty()) {
            countQuery.setParameter("clubName", "%" + clubName + "%");
        }
        if (dateFrom != null) {
            countQuery.setParameter("dateFrom", LocalDate.parse(dateFrom));
        }
        if (dateTo != null) {
            countQuery.setParameter("dateTo", LocalDate.parse(dateTo));
        }
        if (seasonYear != null) {
            countQuery.setParameter("seasonYear", seasonYear);
        }
        if (leagueId != null) {
            countQuery.setParameter("leagueId", leagueId);
        }
        if (completed != null) {
            countQuery.setParameter("completed", completed);
        }
        return (long) countQuery.getSingleResult();
    }
}
