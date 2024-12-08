package com.seba.handy_news.domain;

import com.seba.handy_news.enums.Hand;
import com.seba.handy_news.enums.Position;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

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

    private Date dateOfBirth;

    private double height;

    private double weight;

    @Enumerated(EnumType.STRING)
    private Hand hand;

    @Enumerated(EnumType.STRING)
    private Position position;

    private String nationality;

    @ManyToOne
    @JoinColumn(name = "current_club_id")
    private Club club;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    private Set<PlayerMatch> playerMatches;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    private Set<PlayerClub> playerClubs;

}
