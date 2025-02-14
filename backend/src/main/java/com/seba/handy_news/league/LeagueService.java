package com.seba.handy_news.league;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeagueService {
    private final LeagueRepository leagueRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<League> getAllLeagues() {
        return leagueRepository.findAll();
    }

    public League getLeagueById(Long id) {
        return leagueRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("League not found"));
    }

    public League createLeague(League league) {
        return leagueRepository.save(league);
    }

    public void deleteLeague(Long id) {
        leagueRepository.deleteById(id);
    }

    public League updateLeague(Long id, League league) {
        League existingLeague = getLeagueById(id);
        existingLeague.setCountry(league.getCountry());
        existingLeague.setName(league.getName());
        existingLeague.setLogoUrl(league.getLogoUrl());
        return leagueRepository.save(existingLeague);
    }

    public Page<League> searchLeagues(String name, String country, Pageable pageable) {
        StringBuilder jpql = new StringBuilder("SELECT l FROM League l WHERE 1=1");
        if (name != null && !name.isEmpty()) {
            jpql.append(" AND l.name LIKE :name");
        }
        if (country != null && !country.isEmpty()) {
            jpql.append(" AND l.country LIKE :country");
        }
        Query query = entityManager.createQuery(jpql.toString(), League.class);
        if (name != null && !name.isEmpty()) {
            query.setParameter("name", "%" + name + "%");
        }
        if (country != null && !country.isEmpty()) {
            query.setParameter("country", "%" + country + "%");
        }
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<League> leagues = query.getResultList();
        long total = getTotalCount(jpql.toString(), name, country);
        return new PageImpl<>(leagues, pageable, total);
    }

    private long getTotalCount(String jpql, String name, String country) {
        String countJpql = jpql.replace("SELECT l", "SELECT COUNT(l)");
        Query countQuery = entityManager.createQuery(countJpql);
        if (name != null && !name.isEmpty()) {
            countQuery.setParameter("name", "%" + name + "%");
        }
        if (country != null && !country.isEmpty()) {
            countQuery.setParameter("country", "%" + country + "%");
        }
        return (long) countQuery.getSingleResult();
    }
}
