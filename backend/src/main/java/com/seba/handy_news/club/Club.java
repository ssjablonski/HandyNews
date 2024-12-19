package com.seba.handy_news.club;

import com.seba.handy_news.coach.Coach;
import com.seba.handy_news.league.League;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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

    private LocalDate founded;

    @ManyToOne
    private Coach coach;

    @ManyToOne
    private League league;


}
