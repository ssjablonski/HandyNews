package com.seba.handy_news.coach;

import com.seba.handy_news.club.Club;
import com.seba.handy_news.club.ClubRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoachService {
    private final CoachRepository coachRepository;
    private final ClubRepository clubRepository;

    public CoachService(CoachRepository coachRepository, ClubRepository clubRepository) {
        this.coachRepository = coachRepository;
        this.clubRepository = clubRepository;
    }

    public List<Coach> getAllCoaches() {
        return this.coachRepository.findAll();
    }

    public Coach getCoachById(Long id) {
        return this.coachRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Coach not found"));
    }

    public Coach createCoach(Coach coach) {
        return this.coachRepository.save(coach);
    }

    @Transactional
    public void deleteCoach(Long id) {
        this.coachRepository.deleteById(id);
    }

    @Transactional
    public Coach updateCoach(Long id, Coach coach) {
        Coach existingCoach = this.coachRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Coach not found"));

        existingCoach.setFirstName(coach.getFirstName());
        existingCoach.setLastName(coach.getLastName());
        existingCoach.setNationality(coach.getNationality());
        existingCoach.setDateOfBirth(coach.getDateOfBirth());

        return this.coachRepository.save(coach);
    }

    @Transactional
    public Coach addClubToCoach(Long coachId, Long clubId) {
        Coach coach = this.coachRepository.findById(coachId).orElseThrow(() -> new IllegalArgumentException("Coach not found"));
        Club club = this.clubRepository.findById(clubId).orElseThrow(() -> new IllegalArgumentException("Club not found"));

        coach.getClubs().add(club);
        club.setCoach(coach);
        return this.coachRepository.save(coach);
    }
}
