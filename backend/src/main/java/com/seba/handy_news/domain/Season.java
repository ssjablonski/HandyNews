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
@Table(name = "season")
public class Season {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Date startDate;
    private Date endDate;

    @ManyToOne
    @JoinColumn(name = "league_id", nullable = false)
    private League league;

    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL)
    private Set<Match> matches;
}
