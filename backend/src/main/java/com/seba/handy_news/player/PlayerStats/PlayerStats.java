package com.seba.handy_news.player.PlayerStats;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.seba.handy_news.match.Match;
import com.seba.handy_news.player.Player;
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
@Table(name = "player_stats")
public class PlayerStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    @JsonBackReference
    private Player player;

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    @JsonBackReference
    private Match match;

    private int goals=0;
    private int assists=0;
    private int yellowCards=0;
    private int redCards=0;
    private int suspensions=0;
}
