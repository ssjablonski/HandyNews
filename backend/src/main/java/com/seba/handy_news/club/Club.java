package com.seba.handy_news.club;

import com.seba.handy_news.coach.Coach;
import com.seba.handy_news.league.League;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

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

    @ManyToOne
    private Coach coach;

    @ManyToOne
    private League league;


}
