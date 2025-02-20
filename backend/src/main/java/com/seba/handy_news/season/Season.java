package com.seba.handy_news.season;

import com.fasterxml.jackson.annotation.*;
import com.seba.handy_news.club.Club;
import com.seba.handy_news.league.League;
import com.seba.handy_news.league.LeagueDto;
import com.seba.handy_news.match.Match;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "season")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Season {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "league_id", nullable = false)
    @JsonBackReference
    private League league;

    @Transient
    @JsonProperty
    public LeagueDto getLeagueDto() {
        return new LeagueDto(league.getId(), league.getName());
    }

    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Match> matches = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "season_club",
            joinColumns = @JoinColumn(name = "season_id"),
            inverseJoinColumns = @JoinColumn(name = "club_id")
    )
    private Set<Club> clubs = new HashSet<>();
}