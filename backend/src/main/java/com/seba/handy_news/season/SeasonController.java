package com.seba.handy_news.season;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/season")
@RequiredArgsConstructor
public class SeasonController {
    private final SeasonService seasonService;

    @GetMapping
    public ResponseEntity<List<Season>> getAllSeasons() {
        return ResponseEntity.ok(seasonService.getAllSeasons());
    }

    @GetMapping("/{seasonId}")
    public ResponseEntity<Season> getSeasonById(@PathVariable Long seasonId) {
        Season season = seasonService.getSeasonById(seasonId);
        return ResponseEntity.ok(season);
    }

    @PostMapping("/{leagueId}")
    public ResponseEntity<Season> createSeason(@PathVariable Long leagueId, @RequestBody Season season) {
        Season createdSeason = seasonService.createSeason(leagueId, season);
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

    @GetMapping("/search")
    public ResponseEntity<List<Season>> searchSeasons(@RequestParam int year) {
        return ResponseEntity.ok(seasonService.findSeasonByYear(year));
    }
}
