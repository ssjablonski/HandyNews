package com.seba.handy_news.season;

import com.seba.handy_news.league.League;
import com.seba.handy_news.match.Match;
import com.seba.handy_news.season.SeasonClub.SeasonClub;
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
@Table(name = "season")
public class Season {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "league_id", nullable = false)
    private League league;

    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL)
    private List<SeasonClub> seasonClubs;

    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL)
    private List<Match> matches;

}
