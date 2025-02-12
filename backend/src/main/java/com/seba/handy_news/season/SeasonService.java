package com.seba.handy_news.season;

import com.seba.handy_news.club.Club;
import com.seba.handy_news.league.League;
import com.seba.handy_news.league.LeagueRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SeasonService {
    private final SeasonRepository seasonRepository;
    private final LeagueRepository leagueRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Season> getAllSeasons() {
        return seasonRepository.findAll();
    }

    public Season getSeasonById(Long id) {
        return seasonRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Season not found with id: " + id));
    }

    public List<Season> getAllSeasonsByLeague(Long leagueId) {
        if (!leagueRepository.existsById(leagueId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "League not found with id: " + leagueId);
        }
        return seasonRepository.findByLeagueId(leagueId);
    }

    public Set<Club> getClubsBySeason(Long seasonId) {
        Season season = seasonRepository.findById(seasonId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Season not found with id: " + seasonId));
        return season.getClubs();
    }

    public Season createSeason(Long leagueId, Season season) {
        League league = leagueRepository.findById(leagueId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "League not found with id: " + leagueId));
        season.setLeague(league);
        return seasonRepository.save(season);
    }

    public void deleteSeason(Long seasonId) {
        seasonRepository.deleteById(seasonId);
    }

    @Transactional
    public Season updateSeason(Long id, Season updatedSeason) {
        Season existingSeason = getSeasonById(id);
        existingSeason.setName(updatedSeason.getName());
        existingSeason.setYear(updatedSeason.getYear());
        return seasonRepository.save(existingSeason);
    }

    public List<Season> findSeasonByYear(int year) {
        return seasonRepository.findSeasonsByYearNative(year);
    }

    public Page<Season> searchSeasons(String name, Integer year, Long leagueId, Pageable pageable) {
        StringBuilder jpql = new StringBuilder("SELECT s FROM Season s WHERE 1=1");
        if (name != null && !name.isEmpty()) {
            jpql.append(" AND s.name LIKE :name");
        }
        if (year != null) {
            jpql.append(" AND s.year = :year");
        }
        if (leagueId != null) {
            jpql.append(" AND s.league.id = :leagueId");
        }
        Query query = entityManager.createQuery(jpql.toString(), Season.class);
        if (name != null && !name.isEmpty()) {
            query.setParameter("name", "%" + name + "%");
        }
        if (year != null) {
            query.setParameter("year", year);
        }
        if (leagueId != null) {
            query.setParameter("leagueId", leagueId);
        }
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<Season> seasons = query.getResultList();
        long total = getTotalCount(jpql.toString(), name, year, leagueId);
        return new PageImpl<>(seasons, pageable, total);
    }

    private long getTotalCount(String jpql, String name, Integer year, Long leagueId) {
        String countJpql = jpql.replace("SELECT s", "SELECT COUNT(s)");
        Query countQuery = entityManager.createQuery(countJpql);
        if (name != null && !name.isEmpty()) {
            countQuery.setParameter("name", "%" + name + "%");
        }
        if (year != null) {
            countQuery.setParameter("year", year);
        }
        if (leagueId != null) {
            countQuery.setParameter("leagueId", leagueId);
        }
        return (long) countQuery.getSingleResult();
    }
}
