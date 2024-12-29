package com.seba.handy_news.season;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("api/season")
@RequiredArgsConstructor
public class SeasonController {
    private final SeasonService seasonService;

    @GetMapping("/{seasonId}")
    public ResponseEntity<Season> getSeasonById(@PathVariable Long seasonId) {
        Season season = seasonService.getSeasonById(seasonId);
        return ResponseEntity.ok(season);
    }

    @GetMapping("/league/{leagueId}")
    public ResponseEntity<Set<Season>> getAllSeasonsFromLeague(@PathVariable Long leagueId) {
        Set<Season> seasons = seasonService.getAllSeasonsFromLeague(leagueId);
        return ResponseEntity.ok(seasons);
    }

    @PostMapping
    public ResponseEntity<Season> createSeason(@RequestBody Season season, @RequestParam Long leagueId) {
        Season createdSeason = seasonService.createSeason(season, leagueId);
        return ResponseEntity.ok(createdSeason);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Season> updateSeason(@PathVariable Long id, @RequestBody Season updatedSeason) {
        Season season = seasonService.updateSeason(id, updatedSeason);
        return ResponseEntity.ok(season);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeason(@PathVariable Long id) {
        seasonService.deleteSeason(id);
        return ResponseEntity.noContent().build();
    }
}
