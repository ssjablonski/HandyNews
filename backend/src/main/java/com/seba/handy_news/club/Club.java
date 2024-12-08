package com.seba.handy_news.club;

import com.seba.handy_news.player.PlayerClub;
import com.seba.handy_news.team.Team;
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
@Table(name = "club")
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String city;

    private Date founded;

    @OneToMany(mappedBy = "club")
    private Set<Team> teams;


}
