package com.seba.handy_news.player;

import com.seba.handy_news.club.Club;
import com.seba.handy_news.enums.Hand;
import com.seba.handy_news.enums.Position;
import com.seba.handy_news.player.PlayerClubHistory.PlayerClubHistory;
import com.seba.handy_news.player.PlayerStats.PlayerStats;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "player")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private double height;
    private double weight;

    @Enumerated(EnumType.STRING)
    private Hand hand;

    @Enumerated(EnumType.STRING)
    private Position position;
    private String nationality;

    @ManyToOne
    @JoinColumn(name = "current_club_id")
    private Club currentClub;

//    TODO tutaj czy po drugiej stronie??
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    private List<PlayerStats> playerStats;

    @OneToMany(mappedBy = "player")
    private List<PlayerClubHistory> clubHistory;

}
