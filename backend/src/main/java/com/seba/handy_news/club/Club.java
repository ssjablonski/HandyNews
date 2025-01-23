package com.seba.handy_news.club;

import com.seba.handy_news.season.SeasonClub.SeasonClub;
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
@Table(name = "club")
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String city;
    private String country;

    @OneToMany(mappedBy = "club")
    private List<SeasonClub> seasonClubs;

}
