package com.seba.handy_news.match;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seba.handy_news.club.Club;
import com.seba.handy_news.enums.MatchStatus;
import com.seba.handy_news.season.Season;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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

    private MatchStatus status;

    private Long leagueId;

    @Column(name = "season_id", insertable = false, updatable = false)
    private Long seasonId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id")
    @JsonBackReference("season-matches")
    private Season season;

    @ManyToOne
    @JoinColumn(name = "home_team_id", nullable = false)
    private Club homeTeam;

    @ManyToOne
    @JoinColumn(name = "away_team_id", nullable = false)
    private Club awayTeam;
}
