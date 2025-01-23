//package com.seba.handy_news.player.PlayerStats;
//
//import com.seba.handy_news.player.Player;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//
//
//public class PlayerStatsController {
//
//    @PostMapping("/{playerId}/match/{matchId}")
//    public ResponseEntity<Player> addMatchToPlayerStats(@PathVariable Long playerId, @PathVariable Long matchId) {
//        Player player = playerService.addMatchToPlayer(playerId, matchId);
//        return ResponseEntity.ok(player);
//    }
//}
