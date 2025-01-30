package com.seba.handy_news.club;


import com.seba.handy_news.season.SeasonClub.SeasonClub;
import com.seba.handy_news.season.SeasonRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;
    private final SeasonRepository seasonRepository;

    public Club getClubById(Long id) {
        return clubRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Club not found with id: " + id));
    }

    public List<Club> getAllClubs() {
        return clubRepository.findAll();
    }

    public Club createClub(Club club) {
        return clubRepository.save(club);
    }

    public Club updateClub(Long id, Club updatedClub) {
        Club existingClub = getClubById(id);
        existingClub.setName(updatedClub.getName());
        existingClub.setCity(updatedClub.getCity());
        existingClub.setCountry(updatedClub.getCountry());
        return clubRepository.save(existingClub);
    }

    public void deleteClub(Long id) {
        clubRepository.deleteById(id);
    }

    public void addSeasonClubToClub(Long clubId, SeasonClub seasonClub) {
        Club club = getClubById(clubId);
        club.getSeasonClubs().add(seasonClub);
        clubRepository.save(club);
    }

    @Transactional
    public void removeSeasonClubFromClub(Long clubId, Long seasonClubId) {
        Club club = getClubById(clubId);
        SeasonClub seasonClubToRemove = club.getSeasonClubs().stream()
                .filter(sc -> sc.getId().equals(seasonClubId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("SeasonClub not found in the club with id: " + clubId));

        club.getSeasonClubs().remove(seasonClubToRemove);
        seasonClubToRemove.setClub(null);
        clubRepository.save(club);
    }

    public List<SeasonClub> getAllSeasonClubsFromClub(Long clubId) {
        Club club = getClubById(clubId);
        return new ArrayList<>(club.getSeasonClubs());
    }
}
