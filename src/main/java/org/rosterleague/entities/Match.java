package org.rosterleague.entities;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "PERSISTENCE_ROSTER_MATCH")
public class Match implements Serializable {
    @Serial
    private static final long serialVersionUID = 1234567890123456789L;

    private String id;
    private Team homeTeam;
    private Team awayTeam;
    private int homeScore;
    private int awayScore;
    private LocalDate matchDate;

    public Match() {
    }

    public Match(String id, Team homeTeam, Team awayTeam, int homeScore, int awayScore, LocalDate matchDate) {
        this.id = id;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.matchDate = matchDate;
    }

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @ManyToOne
    public Team getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    @ManyToOne
    public Team getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }

    public LocalDate getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDate matchDate) {
        this.matchDate = matchDate;
    }

    public String getResult(Team team) {
        if (team.equals(homeTeam)) {
            if (homeScore > awayScore) return "W";
            if (homeScore < awayScore) return "L";
            return "D";
        } else if (team.equals(awayTeam)) {
            if (awayScore > homeScore) return "W";
            if (awayScore < homeScore) return "L";
            return "D";
        }
        return "";
    }

    public int getPoints(Team team) {
        String result = getResult(team);
        return switch (result) {
            case "W" -> 3;
            case "D" -> 1;
            default -> 0;
        };
    }
}