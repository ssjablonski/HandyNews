package com.seba.handy_news.match;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.seba.handy_news.player.PlayerStats.PlayerStats;
import com.seba.handy_news.season.Season;
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
@Table(name = "match")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private int homeScore;
    private int awayScore;

    @ManyToOne
    @JoinColumn(name = "season_id", nullable = false)
    @JsonBackReference
    private Season season;

    @ManyToOne
    @JoinColumn(name = "home_team_id", nullable = false)
    private SeasonClub homeTeam;

    @ManyToOne
    @JoinColumn(name = "away_team_id", nullable = false)
    private SeasonClub awayTeam;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<PlayerStats> playerStats;
}
