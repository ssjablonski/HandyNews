package com.seba.handy_news.league;

import com.seba.handy_news.season.Season;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/league")
@RequiredArgsConstructor
public class LeagueController {
    private final LeagueService leagueService;

    @GetMapping
    public ResponseEntity<List<League>> getAllLeagues() {
        return ResponseEntity.ok(leagueService.getAllLeagues());
    }

    @GetMapping("/{id}")
    public ResponseEntity<League> getLeagueById(@PathVariable Long id) {
        return ResponseEntity.ok(leagueService.getLeagueById(id));
    }

    @PostMapping
    public ResponseEntity<League> createLeague(@RequestBody League league) {
        League createdLeague = leagueService.createLeague(league);
        return ResponseEntity.ok(createdLeague);
    }

    @PutMapping("/{id}")
    public ResponseEntity<League> updateLeague(@PathVariable Long id, @RequestBody League league) {
        League updated = leagueService.updateLeague(id, league);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeague(@PathVariable Long id) {
        leagueService.deleteLeague(id);
        return ResponseEntity.noContent().build();
    }

//    TODO - nie wiem wlasnie co z tym
//    @PostMapping("/{leagueId}/season")
//    public ResponseEntity<Void> addSeasonToLeague(@PathVariable Long leagueId, @RequestBody Season season) {
//        leagueService.addSeasonToLeague(leagueId, season);
//        return ResponseEntity.noContent().build();
//    }
//
//    @DeleteMapping("/{leagueId}/seasons/{seasonId}")
//    public ResponseEntity<Void> removeSeasonFromLeague(@PathVariable Long leagueId, @PathVariable Long seasonId) {
//        leagueService.removeSeasonFromLeague(leagueId, seasonId);
//        return ResponseEntity.noContent().build();
//    }

    @GetMapping("/{leagueId}/seasons")
    public ResponseEntity<List<Season>> getAllSeasonsFromLeague(@PathVariable Long leagueId) {
        return ResponseEntity.ok(leagueService.getAllSeasonsFromLeague(leagueId));
    }
}