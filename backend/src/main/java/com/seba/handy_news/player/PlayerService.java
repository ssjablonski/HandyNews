package com.seba.handy_news.player;

import com.seba.handy_news.club.Club;
import com.seba.handy_news.club.ClubRepository;
import com.seba.handy_news.match.MatchRepository;
import com.seba.handy_news.player.PlayerClubHistory.PlayerClubHistory;
import com.seba.handy_news.player.PlayerStats.PlayerStats;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class PlayerService {

    @PersistenceContext
    private EntityManager entityManager;

    private final PlayerRepository playerRepository;
    private final ClubRepository clubRepository;
    private final MatchRepository matchRepository;

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public Player getPlayerById(Long id) {
        return playerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Player not found with id: " + id));
    }

    public List<PlayerStats> getAllPlayerStats(Long id) {
        Player player = getPlayerById(id);
        return new ArrayList<>(player.getPlayerStats());
    }

    public List<PlayerClubHistory> getAllPlayerClubsHistory(Long id) {
        Player player = getPlayerById(id);
        return new ArrayList<>(player.getClubHistory());
    }

//    public List<Player> getPlayersByHand(Hand hand) {
//        return playerRepository.findByHand(hand);
//    }
//
//    public List<Player> getPlayersByPosition(Position position) {
//        return playerRepository.findByPosition(position);
//    }
//
//    public List<Player> getPlayersByNationality(String nationality) {
//        return playerRepository.findByNationality(nationality);
//    }
//
//    public List<Player> getPlayersAboveHeight(double height) {
//        return playerRepository.findByHeightGreaterThan(height);
//    }
//
//    public List<Player> getPlayersAboveWeight(double weight) {
//        return playerRepository.findByWeightGreaterThan(weight);
//    }

    public Player createPlayer(Player player) {
        return playerRepository.save(player);
    }

    public void deletePlayer(Long playerId) {
        playerRepository.deleteById(playerId);
    }

    @Transactional
    public Player updatePlayer(Long id, Player updatedPlayer) {
        Player player = getPlayerById(id);

        player.setFirstName(updatedPlayer.getFirstName());
        player.setLastName(updatedPlayer.getLastName());
        player.setDateOfBirth(updatedPlayer.getDateOfBirth());
        player.setHeight(updatedPlayer.getHeight());
        player.setWeight(updatedPlayer.getWeight());
        player.setHand(updatedPlayer.getHand());
        player.setPosition(updatedPlayer.getPosition());
        player.setNationality(updatedPlayer.getNationality());

        return playerRepository.save(player);
    }


    @Transactional
    public void addPlayerClubToClubHistory(Long id, PlayerClubHistory playerClubHistory) {
        Player player = getPlayerById(id);
        player.getClubHistory().add(playerClubHistory);
        playerClubHistory.setPlayer(player);
        playerRepository.save(player);
    }

    @Transactional
    public void removePlayerClubFromCluHistory(Long playerId, Long playerClubHistoryId) {
        Player player = getPlayerById(playerId);
        PlayerClubHistory playerClubHistory = player.getClubHistory().stream()
                .filter(pch -> pch.getId().equals(playerClubHistoryId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("PlayerClubHistory with id: " + playerClubHistoryId + " not found in player"));
        player.getClubHistory().remove(playerClubHistory);
        playerClubHistory.setPlayer(null);
//        TODO to samo zapisac playerClubHisotry?
        playerRepository.save(player);
    }

    public void changeCurrentClub(Long playerId, Club newClub) {
        Player player = getPlayerById(playerId);
        player.setCurrentClub(newClub);
        playerRepository.save(player);
    }





//    @Transactional
//    public Page<Player> searchPlayers(String firstName, String lastName, String nationality, Hand hand, Position position,
//                                      Integer minAge, Integer maxAge, Double minHeight, Double maxHeight,
//                                      Double minWeight, Double maxWeight, Pageable pageable) {
//        StringBuilder jpql = new StringBuilder("SELECT p FROM Player p WHERE 1=1");
//        Map<String, Object> params = new HashMap<>();
//
//        if (firstName != null && !firstName.isBlank()) {
//            jpql.append(" AND LOWER(p.firstName) LIKE LOWER(:firstName)");
//            params.put("firstName", "%" + firstName + "%");
//        }
//
//        if (lastName != null && !lastName.isBlank()) {
//            jpql.append(" AND LOWER(p.lastName) LIKE LOWER(:lastName)");
//            params.put("lastName", "%" + lastName + "%");
//        }
//
//        if (minWeight != null) {
//            jpql.append(" AND p.weight >= :minWeight");
//            params.put("minWeight", minWeight);
//        }
//
//        if (maxWeight != null) {
//            jpql.append(" AND p.weight <= :maxWeight");
//            params.put("maxWeight", maxWeight);
//        }
//
//        if (minHeight != null) {
//            jpql.append(" AND p.height >= :minHeight");
//            params.put("minHeight", minHeight);
//        }
//
//        if (maxHeight != null) {
//            jpql.append(" AND p.height <= :maxHeight");
//            params.put("maxHeight", maxHeight);
//        }
//
//        if (nationality != null && !nationality.isBlank()) {
//            jpql.append(" AND LOWER(p.nationality) = LOWER(:nationality)");
//            params.put("nationality", nationality);
//        }
//
//        if (hand != null) {
//            jpql.append(" AND p.hand = :hand");
//            params.put("hand", hand);
//        }
//
//        if (position != null) {
//            jpql.append(" AND p.position = :position");
//            params.put("position", position);
//        }
//
//        if (minAge != null) {
//            jpql.append(" AND p.dateOfBirth <= :maxDateOfBirth");
//            params.put("maxDateOfBirth", LocalDate.now().minusYears(minAge));
//        }
//
//        if (maxAge != null) {
//            jpql.append(" AND p.dateOfBirth >= :minDateOfBirth");
//            params.put("minDateOfBirth", LocalDate.now().minusYears(maxAge));
//        }
//
//        TypedQuery<Player> query = entityManager.createQuery(jpql.toString(), Player.class);
//        params.forEach(query::setParameter);
//
////        TODO - pomyslec nad optymalizacja, jak bedzie duzo wynikow to moze byc problem
//        int totalRows = query.getResultList().size();
//        query.setFirstResult((int) pageable.getOffset());
//        query.setMaxResults(pageable.getPageSize());
//
//        List<Player> players = query.getResultList();
//        return new PageImpl<>(players, pageable, totalRows);
//    }
}
