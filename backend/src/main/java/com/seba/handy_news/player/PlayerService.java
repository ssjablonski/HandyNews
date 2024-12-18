package com.seba.handy_news.player;

import com.seba.handy_news.club.Club;
import com.seba.handy_news.club.ClubRepository;
import com.seba.handy_news.enums.Hand;
import com.seba.handy_news.enums.Position;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PlayerService {

    @PersistenceContext
    private EntityManager entityManager;

    private final PlayerRepository playerRepository;
    private final ClubRepository clubRepository;

    public PlayerService(
            PlayerRepository playerRepository,
            ClubRepository clubRepository
    ) {
        this.playerRepository = playerRepository;
        this.clubRepository = clubRepository;
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public Player getPlayerById(Long id) {
        return playerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Player not found"));
    }

    public List<Player> getPlayersByHand(Hand hand) {
        return playerRepository.findByHand(hand);
    }

    public List<Player> getPlayersByPosition(Position position) {
        return playerRepository.findByPosition(position);
    }

    public List<Player> getPlayersByNationality(String nationality) {
        return playerRepository.findByNationality(nationality);
    }

    public List<Player> getPlayersAboveHeight(double height) {
        return playerRepository.findByHeightGreaterThan(height);
    }

    public List<Player> getPlayersAboveWeight(double weight) {
        return playerRepository.findByWeightGreaterThan(weight);
    }

    public Player createPlayer(Player player, Long clubId) {
        Club club = clubRepository.findById(clubId).orElseThrow(() -> new IllegalArgumentException("Club not found."));
        player.setClub(club);
        return playerRepository.save(player);
    }

    @Transactional
    public void deletePlayer(Long playerId) {
        playerRepository.deleteById(playerId);
    }

    @Transactional
    public Player updatePlayer(Long id, Player updatedPlayer) {
        Player existingPlayer = playerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Player not found"));

        existingPlayer.setFirstName(updatedPlayer.getFirstName());
        existingPlayer.setLastName(updatedPlayer.getLastName());
        existingPlayer.setDateOfBirth(updatedPlayer.getDateOfBirth());
        existingPlayer.setHeight(updatedPlayer.getHeight());
        existingPlayer.setWeight(updatedPlayer.getWeight());
        existingPlayer.setHand(updatedPlayer.getHand());
        existingPlayer.setPosition(updatedPlayer.getPosition());
        existingPlayer.setNationality(updatedPlayer.getNationality());
        existingPlayer.setClub(updatedPlayer.getClub());

        return playerRepository.save(existingPlayer);
    }

//    TODO przemyslec to jak zrobic dodawanie klubu do histori gracza (nie mozemy dodac endDate bo mozliwe ze caly czas gra w klubie??)
//    TODO dodac endpointy po podjeciu decyzji jak to rozwiazac
    @Transactional
    public Player addClubToPlayer(Long playerId, Long clubId, LocalDate startDate, LocalDate endDate) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("Club not found"));

        PlayerClub newPlayerClub = new PlayerClub();
        newPlayerClub.setPlayer(player);
        newPlayerClub.setClub(club);
        newPlayerClub.setStartDate(startDate);
        newPlayerClub.setEndDate(endDate);

        player.getPlayerClubs().add(newPlayerClub);

        return playerRepository.save(player);
    }

    @Transactional
    public Player addMatchToPlayer(Long playerId, PlayerMatch newMatch) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        newMatch.setPlayer(player);
        player.getPlayerMatches().add(newMatch);

        return playerRepository.save(player);
    }

    @Transactional
    public List<Player> searchPlayers(String firstName, String lastName, String nationality, Hand hand, Position position,
                                      Integer minAge, Integer maxAge, Double minHeight,
                                      Double maxHeight, Double minWeight, Double maxWeight) {
        StringBuilder jpql = new StringBuilder("SELECT p FROM Player p WHERE 1=1");
        Map<String, Object> params = new HashMap<>();

        if (firstName != null && !firstName.isBlank()) {
            jpql.append(" AND LOWER(p.firstName) LIKE LOWER(:firstName)");
            params.put("firstName", "%" + firstName + "%");
        }

        if (lastName != null && !lastName.isBlank()) {
            jpql.append(" AND LOWER(p.lastName) LIKE LOWER(:lastName)");
            params.put("lastName", "%" + lastName + "%");
        }

        if (minWeight != null) {
            jpql.append(" AND p.weight >= :minWeight");
            params.put("minWeight", minWeight);
        }

        if (maxWeight != null) {
            jpql.append(" AND p.weight <= :maxWeight");
            params.put("maxWeight", maxWeight);
        }

        if (minHeight != null) {
            jpql.append(" AND p.height >= :minHeight");
            params.put("minHeight", minHeight);
        }

        if (maxHeight != null) {
            jpql.append(" AND p.height <= :maxHeight");
            params.put("maxHeight", maxHeight);
        }

        if (nationality != null && !nationality.isBlank()) {
            jpql.append(" AND LOWER(p.nationality) = LOWER(:nationality)");
            params.put("nationality", nationality);
        }

        if (hand != null) {
            jpql.append(" AND p.hand = :hand");
            params.put("hand", hand);
        }

        if (position != null) {
            jpql.append(" AND p.position = :position");
            params.put("position", position);
        }

        if (minAge != null) {
            jpql.append(" AND p.dateOfBirth <= :maxDateOfBirth");
            params.put("maxDateOfBirth", LocalDate.now().minusYears(minAge));
        }

        if (maxAge != null) {
            jpql.append(" AND p.dateOfBirth >= :minDateOfBirth");
            params.put("minDateOfBirth", LocalDate.now().minusYears(maxAge));
        }

        TypedQuery<Player> query = entityManager.createQuery(jpql.toString(), Player.class);
        params.forEach(query::setParameter);

        return query.getResultList();
    }
}
