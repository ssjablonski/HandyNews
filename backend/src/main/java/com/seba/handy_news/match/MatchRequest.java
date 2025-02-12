package com.seba.handy_news.match;

import com.seba.handy_news.enums.MatchStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MatchRequest {
    private LocalDate date;
    private int homeScore;
    private int awayScore;
    private Long homeId;
    private Long awayId;
    private MatchStatus status;
}