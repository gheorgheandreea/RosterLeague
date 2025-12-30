package org.rosterleague.common;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public class MatchDetails implements Serializable {
    @Serial
    private static final long serialVersionUID = 987654321098765432L;

    private final String id;
    private final String homeTeamId;
    private final String homeTeamName;
    private final String awayTeamId;
    private final String awayTeamName;
    private final int homeScore;
    private final int awayScore;
    private final LocalDate matchDate;

    public MatchDetails(String id, String homeTeamId, String homeTeamName,
                        String awayTeamId, String awayTeamName,
                        int homeScore, int awayScore, LocalDate matchDate) {
        this.id = id;
        this.homeTeamId = homeTeamId;
        this.homeTeamName = homeTeamName;
        this.awayTeamId = awayTeamId;
        this.awayTeamName = awayTeamName;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.matchDate = matchDate;
    }

    public String getId() {
        return id;
    }

    public String getHomeTeamId() {
        return homeTeamId;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public String getAwayTeamId() {
        return awayTeamId;
    }

    public String getAwayTeamName() {
        return awayTeamName;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public LocalDate getMatchDate() {
        return matchDate;
    }

    @Override
    public String toString() {
        return homeTeamName + " " + homeScore + " - " + awayScore + " " + awayTeamName + " (" + matchDate + ")";
    }
}