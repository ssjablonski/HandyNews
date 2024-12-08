package com.seba.handy_news.domain;

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

    @OneToMany(mappedBy = "club")
    private Set<PlayerClub> playerClubs;

}
