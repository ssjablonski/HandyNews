package com.seba.handy_news.player;

import com.seba.handy_news.enums.Hand;
import com.seba.handy_news.enums.Position;
import com.seba.handy_news.player.PlayerClubHistory.PlayerClubHistory;
import com.seba.handy_news.player.PlayerStats.PlayerStats;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/player")
@RequiredArgsConstructor
public class PlayerController {
    private final PlayerService playerService;

    @GetMapping
    public ResponseEntity<List<Player>> getAllPlayers() {
        return ResponseEntity.ok(playerService.getAllPlayers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable Long id) {
        return ResponseEntity.ok(playerService.getPlayerById(id));
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<List<PlayerStats>> getAllPlayerStats(@PathVariable Long id) {
        return ResponseEntity.ok(playerService.getAllPlayerStats(id));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<PlayerClubHistory>> getAllPlayerClubsHistory(@PathVariable Long id) {
        return ResponseEntity.ok(playerService.getAllPlayerClubsHistory(id));
    }

//    TODO - nie wiem jak finalnie mam robic wiec w razie co druga opcja
//    @PostMapping
//    public ResponseEntity<Player> createPlayer(@RequestBody Player player, @RequestParam Long clubId) {
//        Player createdPlayer = playerService.createPlayer(player, clubId);
//        return ResponseEntity.ok(createdPlayer);
//    }

    @PostMapping
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {
        Player createdPlayer = playerService.createPlayer(player);
        return ResponseEntity.ok(createdPlayer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable Long id, @RequestBody Player updatedPlayer) {
        Player updated = playerService.updatePlayer(id, updatedPlayer);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
        return ResponseEntity.noContent().build();
    }



    @PostMapping("/{playerId}/club/{newClubId}")
    public ResponseEntity<Player> updatePlayerClub(@PathVariable Long playerId, @PathVariable Long newClubId, @RequestParam LocalDate startDate) {
        Player player = playerService.updatePlayerClub(playerId, newClubId, startDate);
        return ResponseEntity.ok(player);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Player>> searchPlayers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String nationality,
            @RequestParam(required = false) Hand hand,
            @RequestParam(required = false) Position position,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) Double minHeight,
            @RequestParam(required = false) Double maxHeight,
            @RequestParam(required = false) Double minWeight,
            @RequestParam(required = false) Double maxWeight,
            @PageableDefault(sort = "lastName", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<Player> players = playerService.searchPlayers(firstName, lastName, nationality, hand, position, minAge, maxAge, minHeight, maxHeight, minWeight, maxWeight, pageable);
        return ResponseEntity.ok(players);
    }

}
