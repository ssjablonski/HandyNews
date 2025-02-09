package com.seba.handy_news.match;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchFilters {
    private String clubName;
    private String dateFrom;
    private String dateTo;
    private Integer seasonYear;
    private Long leagueId;
    private Boolean completed;
    private String sortBy = "date";
    private String sortDirection = "asc";
}