package com.seba.handy_news.season;

import com.seba.handy_news.club.Club;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    @GetMapping("/league/{leagueId}")
    public ResponseEntity<List<Season>> getAllSeasonsByLeague(@PathVariable Long leagueId) {
        return ResponseEntity.ok(seasonService.getAllSeasonsByLeague(leagueId));
    }

    @GetMapping("/{seasonId}/clubs")
    public ResponseEntity<Set<Club>> getClubsBySeason(@PathVariable Long seasonId) {
        return ResponseEntity.ok(seasonService.getClubsBySeason(seasonId));
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

    @GetMapping("/year")
    public ResponseEntity<List<Season>> searchSeasons(@RequestParam int year) {
        return ResponseEntity.ok(seasonService.findSeasonByYear(year));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Season>> searchSeasons(@RequestParam(required = false) String name,
                                                      @RequestParam(required = false) Integer year,
                                                      @RequestParam(required = false) Long leagueId,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size,
                                                      @RequestParam(defaultValue = "name") String sortBy) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Season> seasons = seasonService.searchSeasons(name, year, leagueId, pageRequest);
        return ResponseEntity.ok(seasons);
    }
}
