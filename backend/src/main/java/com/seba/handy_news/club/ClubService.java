package com.seba.handy_news.club;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubRepository clubRepository;
    
    public List<Club> getAllClubs() {
        return clubRepository.findAll();
    }

    public Club getClubById(Long id) {
        return this.clubRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Club not found with id: " + id));
    }

//    TODO - league id potrzben i przypisac do clubu
    public Club createClub(Club club) {
        return clubRepository.save((club));
    }

    @Transactional
    public void deleteClub(Long clubId) {
        clubRepository.deleteById(clubId);
    }

    @Transactional
    public Club updateClub(Long id, Club updatedClub) {
        Club existingClub = getClubById(id);
        existingClub.setCity(updatedClub.getCity());
        existingClub.setName(updatedClub.getName());
        return clubRepository.save(existingClub);
    }

    public Club findClubByName(String name) {
        return clubRepository.findByName(name).orElseThrow(() -> new EntityNotFoundException("Club not found with name: " + name));
    }
}
