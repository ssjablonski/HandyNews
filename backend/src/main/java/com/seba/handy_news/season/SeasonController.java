package com.seba.handy_news.season;

import com.seba.handy_news.match.Match;
import com.seba.handy_news.season.SeasonClub.SeasonClub;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/season")
@RequiredArgsConstructor
public class SeasonController {
    private final SeasonService seasonService;

    @GetMapping("/{id}")
    public ResponseEntity<Season> getSeasonById(@PathVariable Long id) {
        return ResponseEntity.ok(seasonService.getSeasonById(id));
    }

    @GetMapping
    public ResponseEntity<List<Season>> getAllSeasons() {
        return ResponseEntity.ok(seasonService.getAll());
    }

    @GetMapping("/league/{leagueId}")
    public ResponseEntity<List<Season>> getAllFromLeague(@PathVariable Long leagueId) {
        return ResponseEntity.ok(seasonService.getAllSeasonsFromLeague(leagueId));
    }

    @GetMapping("/{seasonId}/matches")
    public ResponseEntity<List<Match>> getAllMatchesFromSeason(@PathVariable Long seasonId) {
        return ResponseEntity.ok(seasonService.getAllMatchesFromSeason(seasonId));
    }

    @GetMapping("/{seasonId}/seasonClubs")
    public ResponseEntity<List<SeasonClub>> getAllSeasonClubsFromSeason(@PathVariable Long seasonId) {
        return ResponseEntity.ok(seasonService.getAllSeasonClubsFromSeason(seasonId));
    }

    @PostMapping("/league/{leagueId}")
    public ResponseEntity<Season> createSeason(@PathVariable Long leagueId, @RequestBody Season season) {
        return new ResponseEntity<>(seasonService.createSeason(season, leagueId), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Season> updateSeason(@PathVariable Long id, @RequestBody Season updatedSeason) {
        return ResponseEntity.ok(seasonService.updateSeason(id, updatedSeason));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeason(@PathVariable Long id) {
        seasonService.deleteSeason(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{seasonId}/match")
    public ResponseEntity<Void> addMatchToSeason(@PathVariable Long seasonId, @RequestBody Match match) {
        seasonService.addMatchToSeason(seasonId, match);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{seasonId}/match/{matchId}")
    public ResponseEntity<Void> removeMatchFromSeason(@PathVariable Long seasonId, @PathVariable Long matchId) {
        seasonService.removeMatchFromSeason(seasonId, matchId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{seasonId}/seasonClub")
    public ResponseEntity<Void> addSeasonClubToSeason(@PathVariable Long seasonId, @RequestBody SeasonClub seasonClub) {
        seasonService.addSeasonClubToSeason(seasonId, seasonClub);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{seasonId}/seasonClub/{seasonClubId}")
    public ResponseEntity<Void> removeSeasonClubFromSeason(@PathVariable Long seasonId, @PathVariable Long seasonClubId) {
        seasonService.removeSeasonClubFromSeason(seasonId, seasonClubId);
        return ResponseEntity.noContent().build();
    }

}
