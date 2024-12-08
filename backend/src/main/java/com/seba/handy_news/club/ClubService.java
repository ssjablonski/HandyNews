package com.seba.handy_news.club;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubService {

    private final ClubRepository clubRepository;

    public ClubService(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    public List<Club> getAllClubs() {
        return this.clubRepository.findAll();
    }

    public Club getClubById(Long id) {
        return this.clubRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Club not found"));
    }

    public List<Club> getClubsByName(String name) {
        return this.clubRepository.findClubByName(name);
    }

    public List<Club> getClubsByCity(String city) {
        return this.clubRepository.findClubByCity(city);
    }

    public Club createClub(Club club) {
        return this.clubRepository.save((club));
    }

    @Transactional
    public void deleteClub(Long clubId) {
        clubRepository.deleteById(clubId);
    }

    @Transactional
    public Club updateClub(Long id, Club updatedClub) {
        Club existingClub = clubRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Club not found"));

        existingClub.setCity(updatedClub.getCity());
        existingClub.setName(updatedClub.getName());
        existingClub.setFounded(updatedClub.getFounded());

        return clubRepository.save(existingClub);
    }

//    TODO search z roznymi parametrami + endpoint
}
