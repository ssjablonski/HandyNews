package com.seba.handy_news.player;

import com.seba.handy_news.club.Club;
import com.seba.handy_news.club.ClubRepository;
import com.seba.handy_news.enums.Hand;
import com.seba.handy_news.enums.Position;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
//    TODO trzeba sie zastanowic nad logika Club -> Team (chyba niepotrzbena i zostawic tylko club)
    public List<Player> getAllPlayersFromTeam(Long teamId) {
        return playerRepository.findByClubId(teamId);

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



    public List<Player> searchPlayers(String name, String nationality, Hand hand, Position position,
                                      Integer minAge, Integer maxAge, Double minHeight,
                                      Double maxHeight, Double minWeight, Double maxWeight) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Player> cq = cb.createQuery(Player.class);
        Root<Player> player = cq.from(Player.class);

        List<Predicate> predicates = new ArrayList<>();

        if (name != null && !name.isBlank()) {
            String namePattern = "%" + name.toLowerCase() + "%";
            predicates.add(cb.or(
                    cb.like(cb.lower(player.get("firstName")), namePattern),
                    cb.like(cb.lower(player.get("lastName")), namePattern)
            ));
        }
        if (nationality != null && !nationality.isBlank()) {
            predicates.add(cb.equal(cb.lower(player.get("nationality")), nationality.toLowerCase()));
        }
        if (hand != null) {
            predicates.add(cb.equal(player.get("hand"), hand));
        }
        if (position != null) {
            predicates.add(cb.equal(player.get("position"), position));
        }
        if (minAge != null) {
            LocalDate minBirthDate = LocalDate.now().minusYears(minAge);
            predicates.add(cb.lessThanOrEqualTo(player.get("dateOfBirth"), minBirthDate));
        }
        if (maxAge != null) {
            LocalDate maxBirthDate = LocalDate.now().minusYears(maxAge);
            predicates.add(cb.greaterThanOrEqualTo(player.get("dateOfBirth"), maxBirthDate));
        }
        if (minHeight != null) {
            predicates.add(cb.greaterThanOrEqualTo(player.get("height"), minHeight));
        }
        if (maxHeight != null) {
            predicates.add(cb.lessThanOrEqualTo(player.get("height"), maxHeight));
        }
        if (minWeight != null) {
            predicates.add(cb.greaterThanOrEqualTo(player.get("weight"), minWeight));
        }
        if (maxWeight != null) {
            predicates.add(cb.lessThanOrEqualTo(player.get("weight"), maxWeight));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(cq).getResultList();
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





}
