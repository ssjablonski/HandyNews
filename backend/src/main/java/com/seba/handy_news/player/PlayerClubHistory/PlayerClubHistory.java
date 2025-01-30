package com.seba.handy_news.player.PlayerClubHistory;

import com.seba.handy_news.club.Club;
import com.seba.handy_news.player.Player;
import com.seba.handy_news.season.SeasonClub.SeasonClub;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "player_club_history")
public class PlayerClubHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

    @ManyToOne
    @JoinColumn(name = "season_club_id")
    private SeasonClub seasonClub;

    private LocalDate startDate;
    private LocalDate endDate;
}

