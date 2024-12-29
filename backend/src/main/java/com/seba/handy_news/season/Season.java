package com.seba.handy_news.season;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.seba.handy_news.league.League;
import com.seba.handy_news.match.Match;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

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
    @JsonBackReference
    private League league;

    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL)
    private Set<Match> matches;
}
