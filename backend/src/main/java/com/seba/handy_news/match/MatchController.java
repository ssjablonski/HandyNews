package com.seba.handy_news.match;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/match")
@RequiredArgsConstructor
public class MatchController {
    private final MatchService matchService;

    @GetMapping
    public ResponseEntity<List<Match>> getAllMatches() {
        List<Match> matches = matchService.getAllMatches();
        return ResponseEntity.ok(matches);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Match> getMatchById(@PathVariable Long id) {
        Match match = matchService.getMatchById(id);
        return ResponseEntity.ok(match);
    }

    @PostMapping("/{seasonId}")
    public ResponseEntity<Match> createMatch(@PathVariable Long seasonId, @RequestBody MatchRequest matchRequest) {
        Match createdMatch = matchService.createMatch(seasonId, matchRequest);
        return ResponseEntity.ok(createdMatch);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Match> updateMatch(@PathVariable Long id, @RequestBody Match updatedMatch) {
        Match updated = matchService.updateMatch(id, updatedMatch);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable Long id) {
        matchService.deleteMatch(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/search")
    public ResponseEntity<Page<Match>> searchMatches(@RequestBody MatchFilters filters,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        Sort sort = Sort.by(Sort.Direction.fromString(filters.getSortDirection()), filters.getSortBy());
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        Page<Match> matches = matchService.searchMatches(filters.getClubName(), filters.getDateFrom(), filters.getDateTo(), filters.getSeasonYear(), filters.getLeagueId(), filters.getCompleted(), pageRequest);
        return ResponseEntity.ok(matches);
    }
}
