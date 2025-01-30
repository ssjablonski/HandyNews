package com.seba.handy_news.season.SeasonPlayer;


import com.seba.handy_news.player.Player;
import com.seba.handy_news.season.SeasonClub.SeasonClub;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "season_player")
public class SeasonPlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "season_club_id", nullable = false)
    private SeasonClub seasonClub;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

}
