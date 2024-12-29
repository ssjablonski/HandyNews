package com.seba.handy_news.league;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.seba.handy_news.club.Club;
import com.seba.handy_news.season.Season;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "league")
public class League {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String country;

    @OneToMany(mappedBy = "league", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Season> seasons;

    @OneToMany
    private List<Club> clubs;

}
