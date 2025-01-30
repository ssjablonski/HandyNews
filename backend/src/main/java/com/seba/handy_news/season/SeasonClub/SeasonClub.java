package com.seba.handy_news.season.SeasonClub;

import com.seba.handy_news.club.Club;
import com.seba.handy_news.season.Season;
import com.seba.handy_news.season.SeasonPlayer.SeasonPlayer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "season_club")
public class SeasonClub {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "season_id", nullable = false)
    private Season season;

    @ManyToOne
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @OneToMany(mappedBy = "seasonTeam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SeasonPlayer> seasonPlayers;
}
