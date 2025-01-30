package com.seba.handy_news.club;

import com.seba.handy_news.league.League;
import com.seba.handy_news.league.LeagueService;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubRepository clubRepository;
    private final LeagueService leagueService;

    @PersistenceContext
    private EntityManager entityManager;
    
    public List<Club> getAllClubs() {
        return clubRepository.findAll();
    }

    public Club getClubById(Long id) {
        return this.clubRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Club not found with id: " + id));
    }

    @Transactional
    public Club createClub(Club club, Long leagueId) {
        League league = leagueService.getLeagueById(leagueId);
        club.setLeague(league);
        return clubRepository.save((club));
    }

    @Transactional
    public void deleteClub(Long clubId) {
        clubRepository.deleteById(clubId);
    }

    @Transactional
    public Club updateClub(Long id, Club updatedClub) {
        Club existingClub = getClubById(id);
        existingClub.setCity(updatedClub.getCity());
        existingClub.setName(updatedClub.getName());
        return clubRepository.save(existingClub);
    }

//    najlepiej dodac search do repo, query builder
    public Page<Club> searchClubs(String name, String city, Long leagueId, Pageable pageable) {
        StringBuilder jpql = new StringBuilder("SELECT c FROM Club c WHERE 1=1");
        if (name != null && !name.isEmpty()) {
            jpql.append(" AND c.name LIKE :name");
        }
        if (city != null && !city.isEmpty()) {
            jpql.append(" AND c.city LIKE :city");
        }
        if (leagueId != null) {
            jpql.append(" AND c.league.id = :leagueId");
        }
        Query query = entityManager.createQuery(jpql.toString(), Club.class);
        if (name != null && !name.isEmpty()) {
            query.setParameter("name", "%" + name + "%");
        }
        if (city != null && !city.isEmpty()) {
            query.setParameter("city", "%" + city + "%");
        }
        if (leagueId != null) {
            query.setParameter("leagueId", leagueId);
        }
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<Club> clubs = query.getResultList();
        long total = getTotalCount(jpql.toString(), name, city, leagueId);
        return new PageImpl<>(clubs, pageable, total);
    }

    private long getTotalCount(String jpql, String name, String city, Long leagueId) {
        String countJpql = jpql.replace("SELECT c", "SELECT COUNT(c)");
        Query countQuery = entityManager.createQuery(countJpql);
        if (name != null && !name.isEmpty()) {
            countQuery.setParameter("name", "%" + name + "%");
        }
        if (city != null && !city.isEmpty()) {
            countQuery.setParameter("city", "%" + city + "%");
        }
        if (leagueId != null) {
            countQuery.setParameter("leagueId", leagueId);
        }
        return (long) countQuery.getSingleResult();
    }

}
