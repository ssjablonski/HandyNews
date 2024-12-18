package com.seba.handy_news.club;

import com.seba.handy_news.enums.Hand;
import com.seba.handy_news.enums.Position;
import com.seba.handy_news.player.Player;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClubService {

    @PersistenceContext
    private EntityManager entityManager;

    private final ClubRepository clubRepository;

    public ClubService(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    public List<Club> getAllClubs() {
        return this.clubRepository.findAll();
    }

    public Club getClubById(Long id) {
        return this.clubRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Club not found"));
    }

    public List<Club> getClubsByName(String name) {
        return this.clubRepository.findClubByName(name);
    }

    public List<Club> getClubsByCity(String city) {
        return this.clubRepository.findClubByCity(city);
    }

    public Club createClub(Club club) {
        return this.clubRepository.save((club));
    }

    @Transactional
    public void deleteClub(Long clubId) {
        clubRepository.deleteById(clubId);
    }

    @Transactional
    public Club updateClub(Long id, Club updatedClub) {
        Club existingClub = clubRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Club not found"));

        existingClub.setCity(updatedClub.getCity());
        existingClub.setName(updatedClub.getName());
        existingClub.setFounded(updatedClub.getFounded());

        return clubRepository.save(existingClub);
    }

    @Transactional
    public List<Club> searchClubs(String name, String city, Date beforeFounded, Date afterFounded) {
        StringBuilder jpql = new StringBuilder("SELECT c FROM Club c WHERE 1=1");
        Map<String, Object> params = new HashMap<>();

        if (name != null && !name.isBlank()) {
            jpql.append(" AND LOWER(c.name) LIKE LOWER(:name)");
            params.put("name", "%" + name + "%");
        }

        if (city != null && !city.isBlank()) {
            jpql.append(" AND LOWER(c.city) LIKE LOWER(:city)");
            params.put("city", "%" + city + "%");
        }

        if (beforeFounded != null) {
            jpql.append(" AND c.founded <= :beforeFounded");
            params.put("beforeFounded", beforeFounded);
        }

        if (afterFounded != null) {
            jpql.append(" AND c.founded >= :afterFounded");
            params.put("afterFounded", afterFounded);
        }

        TypedQuery<Club> query = entityManager.createQuery(jpql.toString(), Club.class);
        params.forEach(query::setParameter);

        return query.getResultList();
    }
}
