package org.rosterleague.common;

import java.io.Serial;
import java.io.Serializable;

public class StandingsEntry implements Serializable, Comparable<StandingsEntry> {
    @Serial
    private static final long serialVersionUID = 1122334455667788990L;

    private final String teamId;
    private final String teamName;
    private int matchesPlayed;
    private int points;
    private int wins;
    private int draws;
    private int losses;

    public StandingsEntry(String teamId, String teamName) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.matchesPlayed = 0;
        this.points = 0;
        this.wins = 0;
        this.draws = 0;
        this.losses = 0;
    }

    public void addWin() {
        wins++;
        points += 3;
        matchesPlayed++;
    }

    public void addDraw() {
        draws++;
        points += 1;
        matchesPlayed++;
    }

    public void addLoss() {
        losses++;
        matchesPlayed++;
    }

    public String getTeamId() {
        return teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public int getPoints() {
        return points;
    }

    public int getWins() {
        return wins;
    }

    public int getDraws() {
        return draws;
    }

    public int getLosses() {
        return losses;
    }

    @Override
    public int compareTo(StandingsEntry other) {
        // Sortare descrescător după puncte
        return Integer.compare(other.points, this.points);
    }

    @Override
    public String toString() {
        return teamName + " - MP: " + matchesPlayed + ", Pts: " + points +
                " (W:" + wins + " D:" + draws + " L:" + losses + ")";
    }
}