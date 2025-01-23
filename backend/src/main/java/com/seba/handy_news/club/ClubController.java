package com.seba.handy_news.club;

import com.seba.handy_news.season.SeasonClub.SeasonClub;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/club")
public class ClubController {
    private final ClubService clubService;

    @GetMapping("/{id}")
    public ResponseEntity<Club> getById(@PathVariable Long id) {
        return ResponseEntity.ok(clubService.getClubById(id));
    }

    @GetMapping
    public ResponseEntity<List<Club>> getAll() {
        return ResponseEntity.ok(clubService.getAllClubs());
    }

    @PostMapping
    public ResponseEntity<Club> create(@RequestBody Club club) {
        return new ResponseEntity<>(clubService.createClub(club), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Club> update(@PathVariable Long id, @RequestBody Club club) {
        return ResponseEntity.ok(clubService.updateClub(id, club));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clubService.deleteClub(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{clubId}/season-clubs")
    public ResponseEntity<Void> addSeasonClubToClub(@PathVariable Long clubId, @RequestBody SeasonClub seasonClub) {
        clubService.addSeasonClubToClub(clubId, seasonClub);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{clubId}/season-clubs/{seasonClubId}")
    public ResponseEntity<Void> removeSeasonClubFromClub(@PathVariable Long clubId, @PathVariable Long seasonClubId) {
        clubService.removeSeasonClubFromClub(clubId, seasonClubId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{clubId}/season-clubs")
    public ResponseEntity<List<SeasonClub>> getAllSeasonClubsFromClub(@PathVariable Long clubId) {
        return ResponseEntity.ok(clubService.getAllSeasonClubsFromClub(clubId));
    }


}
