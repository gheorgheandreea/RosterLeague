/*
 * Copyright (c), Eclipse Foundation, Inc. and its licensors.
 *
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v1.0, which is available at
 * https://www.eclipse.org/org/documents/edl-v10.php
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package org.rosterleague.ejb;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.rosterleague.common.*;
import org.rosterleague.entities.*;

/**
 * This is the bean class for the RequestBean enterprise bean.
 *
 * @author ian
 */
@Stateless
public class RequestBean implements Request, Serializable {

    private static final Logger logger = Logger.getLogger(RequestBean.class.getName());

    @PersistenceContext
    private EntityManager em;

    private CriteriaBuilder cb;

    @PostConstruct
    private void init() {
        cb = em.getCriteriaBuilder();
    }

    @Override
    public void createPlayer(String id, String name, String position, double salary) {
        logger.info("createPlayer");
        try {

            Player player = new Player(id, name, position, salary);
            em.persist(player);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    @Override
    public void addPlayer(String playerId, String teamId) {
        logger.info("addPlayer");
        try {
            Player player = em.find(Player.class, playerId);
            Team team = em.find(Team.class, teamId);

            team.addPlayer(player);
            player.addTeam(team);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    @Override
    public void removePlayer(String playerId) {
        logger.info("removePlayer");
        try {
            Player player = em.find(Player.class, playerId);

            Collection<Team> teams = player.getTeams();
            for (Team team : teams) {
                team.dropPlayer(player);
            }

            em.remove(player);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    @Override
    public void dropPlayer(String playerId, String teamId) {
        logger.info("dropPlayer");
        try {
            Player player = em.find(Player.class, playerId);
            Team team = em.find(Team.class, teamId);

            team.dropPlayer(player);
            player.dropTeam(team);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    @Override
    public PlayerDetails getPlayer(String playerId) {
        logger.info("getPlayerDetails");
        try {
            Player player = em.find(Player.class, playerId);
            PlayerDetails playerDetails = new PlayerDetails(player.getId(), player.getName(), player.getPosition(), player.getSalary());
            return playerDetails;
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    @Override
    public List<PlayerDetails> getPlayersOfTeam(String teamId) {
        logger.info("getPlayersOfTeam");
        List<PlayerDetails> playerList;
        try {
            Team team = em.find(Team.class, teamId);
            playerList = this.copyPlayersToDetails((List<Player>) team.getPlayers());
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
        return playerList;
    }

    @Override
    public List<TeamDetails> getTeamsOfLeague(String leagueId) {
        logger.info("getTeamsOfLeague");
        List<TeamDetails> detailsList = new ArrayList<>();
        Collection<Team> teams;

        try {
            League league = em.find(League.class, leagueId);
            teams = league.getTeams();
        } catch (Exception ex) {
            throw new EJBException(ex);
        }

        for (Team team : teams) {
            TeamDetails teamDetails = new TeamDetails(team.getId(), team.getName(), team.getCity());
            detailsList.add(teamDetails);
        }
        return detailsList;
    }

    @Override
    public List<PlayerDetails> getPlayersByPosition(String position) {
        logger.info("getPlayersByPosition");
        List<Player> players = null;

        try {
            CriteriaQuery<Player> cq = cb.createQuery(Player.class);
            if (cq != null) {
                Root<Player> player = cq.from(Player.class);

                // set the where clause
                cq.where(cb.equal(player.get(Player_.position), position));
                cq.select(player);
                TypedQuery<Player> q = em.createQuery(cq);
                players = q.getResultList();
            }
            return copyPlayersToDetails(players);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    @Override
    public List<PlayerDetails> getPlayersByHigherSalary(String name) {
        logger.info("getPlayersByHigherSalary");
        List<Player> players = null;

        try {
            CriteriaQuery<Player> cq = cb.createQuery(Player.class);
            if (cq != null) {
                Root<Player> player1 = cq.from(Player.class);
                Root<Player> player2 = cq.from(Player.class);

                // create a Predicate object that finds players with a salary
                // greater than player1
                Predicate gtPredicate = cb.greaterThan(player1.get(Player_.salary), player2.get(Player_.salary));
                // create a Predicate object that finds the player based on
                // the name parameter
                Predicate equalPredicate = cb.equal(player2.get(Player_.name), name);
                // set the where clause with the predicates
                cq.where(gtPredicate, equalPredicate);
                // set the select clause, and return only unique entries
                cq.select(player1).distinct(true);
                TypedQuery<Player> q = em.createQuery(cq);
                players = q.getResultList();
            }
            return copyPlayersToDetails(players);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    @Override
    public List<PlayerDetails> getPlayersBySalaryRange(double low, double high) {
        logger.info("getPlayersBySalaryRange");
        List<Player> players = null;

        try {
            CriteriaQuery<Player> cq = cb.createQuery(Player.class);
            if (cq != null) {
                Root<Player> player = cq.from(Player.class);

                // set the where clause
                cq.where(cb.between(player.get(Player_.salary), low, high));
                // set the select clause
                cq.select(player).distinct(true);
                TypedQuery<Player> q = em.createQuery(cq);
                players = q.getResultList();
            }
            return copyPlayersToDetails(players);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    @Override
    public List<PlayerDetails> getPlayersByLeagueId(String leagueId) {
        logger.info("getPlayersByLeagueId");
        List<Player> players = null;

        try {
            CriteriaQuery<Player> cq = cb.createQuery(Player.class);
            if (cq != null) {
                Root<Player> player = cq.from(Player.class);
                Join<Player, Team> team = player.join(Player_.teams);
                Join<Team, League> league = team.join(Team_.league);

                // set the where clause
                cq.where(cb.equal(league.get(League_.id), leagueId));
                cq.select(player).distinct(true);
                TypedQuery<Player> q = em.createQuery(cq);
                players = q.getResultList();
            }
            return copyPlayersToDetails(players);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    @Override
    public List<PlayerDetails> getPlayersBySport(String sport) {
        logger.info("getPlayersByLeagueId");
        List<Player> players = null;

        try {
            CriteriaQuery<Player> cq = cb.createQuery(Player.class);
            if (cq != null) {
                Root<Player> player = cq.from(Player.class);
                Join<Player, Team> team = player.join(Player_.teams);
                Join<Team, League> league = team.join(Team_.league);

                // set the where clause
                cq.where(cb.equal(league.get(League_.sport), sport));
                cq.select(player).distinct(true);
                TypedQuery<Player> q = em.createQuery(cq);
                players = q.getResultList();
            }
            return copyPlayersToDetails(players);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    @Override
    public List<PlayerDetails> getPlayersByCity(String city) {
        logger.info("getPlayersByCity");
        List<Player> players = null;

        try {
            CriteriaQuery<Player> cq = cb.createQuery(Player.class);
            if (cq != null) {
                Root<Player> player = cq.from(Player.class);
                Join<Player, Team> team = player.join(Player_.teams);

                // set the where clause
                cq.where(cb.equal(team.get(Team_.city), city));
                cq.select(player).distinct(true);
                TypedQuery<Player> q = em.createQuery(cq);
                players = q.getResultList();
            }
            return copyPlayersToDetails(players);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    @Override
    public List<PlayerDetails> getAllPlayers() {
        logger.info("getAllPlayers");
        List<Player> players = null;

        try {
            CriteriaQuery<Player> cq = cb.createQuery(Player.class);
            if (cq != null) {
                Root<Player> player = cq.from(Player.class);

                cq.select(player);
                TypedQuery<Player> q = em.createQuery(cq);
                players = q.getResultList();
            }
            return copyPlayersToDetails(players);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    @Override
    public List<PlayerDetails> getPlayersNotOnTeam() {
        logger.info("getPlayersNotOnTeam");
        List<Player> players = null;

        try {
            CriteriaQuery<Player> cq = cb.createQuery(Player.class);
            if (cq != null) {
                Root<Player> player = cq.from(Player.class);

                // set the where clause
                cq.where(cb.isEmpty(player.get(Player_.teams)));
                cq.select(player).distinct(true);
                TypedQuery<Player> q = em.createQuery(cq);
                players = q.getResultList();
            }
            return copyPlayersToDetails(players);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    @Override
    public List<PlayerDetails> getPlayersByPositionAndName(String position, String name) {
        logger.info("getPlayersByPositionAndName");
        List<Player> players = null;

        try {
            CriteriaQuery<Player> cq = cb.createQuery(Player.class);
            if (cq != null) {
                Root<Player> player = cq.from(Player.class);

                // set the where clause
                cq.where(cb.equal(player.get(Player_.position), position), cb.equal(player.get(Player_.name), name));
                cq.select(player).distinct(true);
                TypedQuery<Player> q = em.createQuery(cq);
                players = q.getResultList();
            }
            return copyPlayersToDetails(players);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    @Override
    public List<LeagueDetails> getLeaguesOfPlayer(String playerId) {
        logger.info("getLeaguesOfPlayer");
        List<LeagueDetails> detailsList = new ArrayList<>();
        List<League> leagues = null;

        try {
            CriteriaQuery<League> cq = cb.createQuery(League.class);
            if (cq != null) {
                Root<League> league = cq.from(League.class);
                Join<League, Team> team = league.join(League_.teams);
                Join<Team, Player> player = team.join(Team_.players);

                cq.where(cb.equal(player.get(Player_.id), playerId));
                cq.select(league).distinct(true);
                TypedQuery<League> q = em.createQuery(cq);
                leagues = q.getResultList();
            }
        } catch (Exception ex) {
            throw new EJBException(ex);
        }

        if (leagues == null) {
            logger.log(Level.WARNING, "No leagues found for player with ID {0}.", playerId);
            return null;
        } else {
            for (League league : leagues) {
                LeagueDetails leagueDetails = new LeagueDetails(league.getId(), league.getName(), league.getSport());
                detailsList.add(leagueDetails);
            }

        }
        return detailsList;
    }

    @Override
    public List<String> getSportsOfPlayer(String playerId) {
        logger.info("getSportsOfPlayer");
        List<String> sports = new ArrayList<>();

        try {
            CriteriaQuery<String> cq = cb.createQuery(String.class);
            if (cq != null) {
                Root<Player> player = cq.from(Player.class);
                Join<Player, Team> team = player.join(Player_.teams);
                Join<Team, League> league = team.join(Team_.league);

                // set the where clause
                cq.where(cb.equal(player.get(Player_.id), playerId));
                cq.select(league.get(League_.sport)).distinct(true);
                TypedQuery<String> q = em.createQuery(cq);
                sports = q.getResultList();
            }
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
        return sports;
    }

    @Override
    public void createTeamInLeague(TeamDetails teamDetails, String leagueId) {
        logger.info("createTeamInLeague");
        try {
            League league = em.find(League.class, leagueId);
            Team team = new Team(teamDetails.getId(), teamDetails.getName(), teamDetails.getCity());
            em.persist(team);
            team.setLeague(league);
            league.addTeam(team);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    @Override
    public void removeTeam(String teamId) {
        logger.info("removeTeam");
        try {
            Team team = em.find(Team.class, teamId);

            Collection<Player> players = team.getPlayers();
            for (Player player : players) {
                player.dropTeam(team);
            }

            em.remove(team);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    @Override
    public TeamDetails getTeam(String teamId) {
        logger.info("getTeam");
        TeamDetails teamDetails;

        try {
            Team team = em.find(Team.class, teamId);
            teamDetails = new TeamDetails(team.getId(), team.getName(), team.getCity());
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
        return teamDetails;
    }

    @Override
    public void createLeague(LeagueDetails leagueDetails) {
        logger.info("createLeague");
        try {
            if (leagueDetails.getSport().equalsIgnoreCase("soccer") || leagueDetails.getSport().equalsIgnoreCase("swimming") || leagueDetails.getSport().equalsIgnoreCase("basketball") || leagueDetails.getSport().equalsIgnoreCase("baseball")) {
                SummerLeague league = new SummerLeague(leagueDetails.getId(), leagueDetails.getName(), leagueDetails.getSport());
                em.persist(league);
            } else if (leagueDetails.getSport().equalsIgnoreCase("hockey") || leagueDetails.getSport().equalsIgnoreCase("skiing") || leagueDetails.getSport().equalsIgnoreCase("snowboarding")) {
                WinterLeague league = new WinterLeague(leagueDetails.getId(), leagueDetails.getName(), leagueDetails.getSport());
                em.persist(league);
            } else {
                throw new IncorrectSportException("The specified sport is not valid.");
            }
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    @Override
    public void removeLeague(String leagueId) {
        logger.info("removeLeague");
        try {
            League league = em.find(League.class, leagueId);
            em.remove(league);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    @Override
    public LeagueDetails getLeague(String leagueId) {
        logger.info("getLeague");
        LeagueDetails leagueDetails;

        try {
            League league = em.find(League.class, leagueId);
            leagueDetails = new LeagueDetails(league.getId(), league.getName(), league.getSport());
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
        return leagueDetails;
    }

    @Override
    public void clearAllEntities() {
        em.createNativeQuery("DELETE FROM PERSISTENCE_ROSTER_TEAM_PLAYER").executeUpdate();
        em.createQuery("DELETE FROM Match").executeUpdate();
        em.createQuery("DELETE FROM Player").executeUpdate();
        em.createQuery("DELETE FROM Team").executeUpdate();
        em.createQuery("DELETE FROM League").executeUpdate();
    }

    private List<PlayerDetails> copyPlayersToDetails(List<Player> players) {
        List<PlayerDetails> detailsList = new ArrayList<>();
        for (Player player : players) {
            PlayerDetails playerDetails = new PlayerDetails(player.getId(), player.getName(), player.getPosition(), player.getSalary());
            detailsList.add(playerDetails);
        }
        return detailsList;
    }
    @Override
    public void createMatch(String id, String homeTeamId, String awayTeamId, int homeScore, int awayScore, LocalDate matchDate) {
        logger.info("createMatch");
        try {
            Team homeTeam = em.find(Team.class, homeTeamId);
            Team awayTeam = em.find(Team.class, awayTeamId);

            Match match = new Match(id, homeTeam, awayTeam, homeScore, awayScore, matchDate);
            em.persist(match);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    @Override
    public List<MatchDetails> getMatchesOfTeam(String teamId) {
        logger.info("getMatchesOfTeam");
        List<MatchDetails> detailsList = new ArrayList<>();

        try {
            Team team = em.find(Team.class, teamId);

            // Găsește toate meciurile unde echipa este acasă sau în deplasare
            CriteriaQuery<Match> cq = cb.createQuery(Match.class);
            if (cq != null) {
                Root<Match> match = cq.from(Match.class);

                Predicate homeTeamPredicate = cb.equal(match.get("homeTeam"), team);
                Predicate awayTeamPredicate = cb.equal(match.get("awayTeam"), team);

                cq.where(cb.or(homeTeamPredicate, awayTeamPredicate));
                cq.orderBy(cb.desc(match.get("matchDate")));

                TypedQuery<Match> q = em.createQuery(cq);
                List<Match> matches = q.getResultList();

                for (Match m : matches) {
                    MatchDetails md = new MatchDetails(
                            m.getId(),
                            m.getHomeTeam().getId(),
                            m.getHomeTeam().getName(),
                            m.getAwayTeam().getId(),
                            m.getAwayTeam().getName(),
                            m.getHomeScore(),
                            m.getAwayScore(),
                            m.getMatchDate()
                    );
                    detailsList.add(md);
                }
            }
        } catch (Exception ex) {
            throw new EJBException(ex);
        }

        return detailsList;
    }

    @Override
    public List<StandingsEntry> getLeagueStandings(String leagueId) {
        logger.info("getLeagueStandings");
        Map<String, StandingsEntry> standingsMap = new HashMap<>();

        try {
            League league = em.find(League.class, leagueId);

            // Inițializează clasamentul cu toate echipele din ligă
            for (Team team : league.getTeams()) {
                StandingsEntry entry = new StandingsEntry(team.getId(), team.getName());
                standingsMap.put(team.getId(), entry);
            }

            // Găsește toate meciurile din ligă
            CriteriaQuery<Match> cq = cb.createQuery(Match.class);
            if (cq != null) {
                Root<Match> match = cq.from(Match.class);
                Join<Match, Team> homeTeam = match.join("homeTeam");
                Join<Team, League> homeLeague = homeTeam.join("league");

                cq.where(cb.equal(homeLeague.get("id"), leagueId));

                TypedQuery<Match> q = em.createQuery(cq);
                List<Match> matches = q.getResultList();

                // Calculează punctele pentru fiecare echipă
                for (Match m : matches) {
                    String homeTeamId = m.getHomeTeam().getId();
                    String awayTeamId = m.getAwayTeam().getId();

                    StandingsEntry homeEntry = standingsMap.get(homeTeamId);
                    StandingsEntry awayEntry = standingsMap.get(awayTeamId);

                    if (homeEntry != null && awayEntry != null) {
                        int homeScore = m.getHomeScore();
                        int awayScore = m.getAwayScore();

                        if (homeScore > awayScore) {
                            homeEntry.addWin();
                            awayEntry.addLoss();
                        } else if (homeScore < awayScore) {
                            homeEntry.addLoss();
                            awayEntry.addWin();
                        } else {
                            homeEntry.addDraw();
                            awayEntry.addDraw();
                        }
                    }
                }
            }

            // Sortează descrescător după puncte
            List<StandingsEntry> standings = new ArrayList<>(standingsMap.values());
            Collections.sort(standings);

            return standings;

        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    @Override
    public List<LeagueDetails> getAllLeagues() {
        logger.info("getAllLeagues");
        List<LeagueDetails> detailsList = new ArrayList<>();

        try {
            CriteriaQuery<League> cq = cb.createQuery(League.class);
            if (cq != null) {
                Root<League> league = cq.from(League.class);
                cq.select(league);

                TypedQuery<League> q = em.createQuery(cq);
                List<League> leagues = q.getResultList();

                for (League l : leagues) {
                    LeagueDetails ld = new LeagueDetails(l.getId(), l.getName(), l.getSport());
                    detailsList.add(ld);
                }
            }
        } catch (Exception ex) {
            throw new EJBException(ex);
        }

        return detailsList;
    }
}
