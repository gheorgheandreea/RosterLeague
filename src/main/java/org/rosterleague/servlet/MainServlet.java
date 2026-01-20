package org.rosterleague.servlet;

import java.io.*;
import java.time.LocalDate;
import java.util.List;

import jakarta.inject.Inject;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.rosterleague.common.LeagueDetails;
import org.rosterleague.common.PlayerDetails;
import org.rosterleague.common.Request;
import org.rosterleague.common.TeamDetails;
import org.rosterleague.common.MatchDetails;
import org.rosterleague.common.StandingsEntry;

@WebServlet(name = "mainServlet",value = "/test")
public class MainServlet extends HttpServlet {
    @Inject
    Request ejbRequest;

    PrintWriter printer;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ejbRequest.clearAllEntities();
        insertInfo();
        insertMatches();

        printer = response.getWriter();
        response.setContentType("text/plain");

        getSomeInfo();
        getMoreInfo();
        showStandingsAndMatches();
        removeInfo();
    }

    private void insertInfo() {
        try {
            // Leagues
            ejbRequest.createLeague(new LeagueDetails("L1", "Mountain", "Soccer"));
            ejbRequest.createLeague(new LeagueDetails("L2", "Valley", "Basketball"));
            ejbRequest.createLeague(new LeagueDetails("L3", "Foothills", "Soccer"));
            ejbRequest.createLeague(new LeagueDetails("L4", "Alpine", "Snowboarding"));

            // Teams
            ejbRequest.createTeamInLeague(new TeamDetails("T1", "Honey Bees", "Visalia"), "L1");
            ejbRequest.createTeamInLeague(new TeamDetails("T2", "Gophers", "Manteca"), "L1");
            ejbRequest.createTeamInLeague(new TeamDetails("T5", "Crows", "Orland"), "L1");

            ejbRequest.createTeamInLeague(new TeamDetails("T3", "Deer", "Bodie"), "L2");
            ejbRequest.createTeamInLeague(new TeamDetails("T4", "Trout", "Truckee"), "L2");

            ejbRequest.createTeamInLeague(new TeamDetails("T6", "Marmots", "Auburn"), "L3");
            ejbRequest.createTeamInLeague(new TeamDetails("T7", "Bobcats", "Grass Valley"), "L3");
            ejbRequest.createTeamInLeague(new TeamDetails("T8", "Beavers", "Placerville"), "L3");

            ejbRequest.createTeamInLeague(new TeamDetails("T9", "Penguins", "Incline Village"), "L4");
            ejbRequest.createTeamInLeague(new TeamDetails("T10", "Land Otters", "Tahoe City"), "L4");

            // Players, Team T1
            ejbRequest.createPlayer("P1", "Phil Jones", "goalkeeper", 100.00);
            ejbRequest.addPlayer("P1", "T1");

            ejbRequest.createPlayer("P2", "Alice Smith", "defender", 505.00);
            ejbRequest.addPlayer("P2", "T1");

            ejbRequest.createPlayer("P3", "Bob Roberts", "midfielder", 65.00);
            ejbRequest.addPlayer("P3", "T1");

            ejbRequest.createPlayer("P4", "Grace Phillips", "forward", 100.00);
            ejbRequest.addPlayer("P4", "T1");

            ejbRequest.createPlayer("P5", "Barney Bold", "defender", 100.00);
            ejbRequest.addPlayer("P5", "T1");

            // Players, Team T2
            ejbRequest.createPlayer("P6", "Ian Carlyle", "goalkeeper", 555.00);
            ejbRequest.addPlayer("P6", "T2");

            ejbRequest.createPlayer("P7", "Rebecca Struthers", "midfielder", 777.00);
            ejbRequest.addPlayer("P7", "T2");

            ejbRequest.createPlayer("P8", "Anne Anderson", "forward", 65.00);
            ejbRequest.addPlayer("P8", "T2");

            ejbRequest.createPlayer("P9", "Jan Wesley", "defender", 100.00);
            ejbRequest.addPlayer("P9", "T2");

            ejbRequest.createPlayer("P10", "Terry Smithson", "midfielder", 100.00);
            ejbRequest.addPlayer("P10", "T2");

            // Players, Team T3
            ejbRequest.createPlayer("P11", "Ben Shore", "point guard", 188.00);
            ejbRequest.addPlayer("P11", "T3");

            ejbRequest.createPlayer("P12", "Chris Farley", "shooting guard", 577.00);
            ejbRequest.addPlayer("P12", "T3");

            ejbRequest.createPlayer("P13", "Audrey Brown", "small forward", 995.00);
            ejbRequest.addPlayer("P13", "T3");

            ejbRequest.createPlayer("P14", "Jack Patterson", "power forward", 100.00);
            ejbRequest.addPlayer("P14", "T3");

            ejbRequest.createPlayer("P15", "Candace Lewis", "point guard", 100.00);
            ejbRequest.addPlayer("P15", "T3");

            // Players, Team T4
            ejbRequest.createPlayer("P16", "Linda Berringer", "point guard", 844.00);
            ejbRequest.addPlayer("P16", "T4");

            ejbRequest.createPlayer("P17", "Bertrand Morris", "shooting guard", 452.00);
            ejbRequest.addPlayer("P17", "T4");

            ejbRequest.createPlayer("P18", "Nancy White", "small forward", 833.00);
            ejbRequest.addPlayer("P18", "T4");

            ejbRequest.createPlayer("P19", "Billy Black", "power forward", 444.00);
            ejbRequest.addPlayer("P19", "T4");

            ejbRequest.createPlayer("P20", "Jodie James", "point guard", 100.00);
            ejbRequest.addPlayer("P20", "T4");

            // Players, Team T5
            ejbRequest.createPlayer("P21", "Henry Shute", "goalkeeper", 205.00);
            ejbRequest.addPlayer("P21", "T5");

            ejbRequest.createPlayer("P22", "Janice Walker", "defender", 857.00);
            ejbRequest.addPlayer("P22", "T5");

            ejbRequest.createPlayer("P23", "Wally Hendricks", "midfielder", 748.00);
            ejbRequest.addPlayer("P23", "T5");

            ejbRequest.createPlayer("P24", "Gloria Garber", "forward", 777.00);
            ejbRequest.addPlayer("P24", "T5");

            ejbRequest.createPlayer("P25", "Frank Fletcher", "defender", 399.00);
            ejbRequest.addPlayer("P25", "T5");

            // Players, Team T9
            ejbRequest.createPlayer("P30", "Lakshme Singh", "downhill", 450.00);
            ejbRequest.addPlayer("P30", "T9");

            ejbRequest.createPlayer("P31", "Mariela Prieto", "freestyle", 420.00);
            ejbRequest.addPlayer("P31", "T9");

            // Players, Team T10
            ejbRequest.createPlayer("P32", "Soren Johannsen", "freestyle", 375.00);
            ejbRequest.addPlayer("P32", "T10");

            ejbRequest.createPlayer("P33", "Andre Gerson", "freestyle", 396.00);
            ejbRequest.addPlayer("P33", "T10");

            ejbRequest.createPlayer("P34", "Zoria Lepsius", "downhill", 431.00);
            ejbRequest.addPlayer("P34", "T10");

            // Players, no team
            ejbRequest.createPlayer("P26", "Hobie Jackson", "pitcher", 582.00);
            ejbRequest.createPlayer("P27", "Melinda Kendall", "catcher", 677.00);

            // Players, multiple teams
            ejbRequest.createPlayer("P28", "Constance Adams", "substitute", 966.00);
            ejbRequest.addPlayer("P28", "T1");
            ejbRequest.addPlayer("P28", "T3");

            // Adding existing players to second soccer league
            ejbRequest.addPlayer("P24", "T6");
            ejbRequest.addPlayer("P21", "T6");
            ejbRequest.addPlayer("P9", "T6");
            ejbRequest.addPlayer("P7", "T5");

        } catch (Exception ex) {
            printer.println("Caught an exception: " + ex.getClass() + " : " + ex.getMessage());
            ex.printStackTrace(printer);
        }
    }
    private void insertMatches() {
        try {
            // Meciuri pentru Liga L1 (Mountain Soccer)
            ejbRequest.createMatch("M1", "T1", "T2", 3, 1, LocalDate.of(2024, 11, 10));
            ejbRequest.createMatch("M2", "T1", "T5", 2, 2, LocalDate.of(2024, 11, 17));
            ejbRequest.createMatch("M3", "T2", "T5", 1, 0, LocalDate.of(2024, 11, 24));
            ejbRequest.createMatch("M4", "T5", "T1", 1, 3, LocalDate.of(2024, 12, 1));
            ejbRequest.createMatch("M5", "T2", "T1", 0, 2, LocalDate.of(2024, 12, 8));
            ejbRequest.createMatch("M6", "T5", "T2", 2, 1, LocalDate.of(2024, 12, 15));

            // Meciuri pentru Liga L2 (Valley Basketball)
            ejbRequest.createMatch("M7", "T3", "T4", 85, 78, LocalDate.of(2024, 11, 12));
            ejbRequest.createMatch("M8", "T4", "T3", 92, 88, LocalDate.of(2024, 11, 19));
            ejbRequest.createMatch("M9", "T3", "T4", 95, 95, LocalDate.of(2024, 11, 26));
            ejbRequest.createMatch("M10", "T4", "T3", 80, 85, LocalDate.of(2024, 12, 3));

        } catch (Exception ex) {
            if (printer != null) {
                printer.println("Caught an exception in insertMatches: " + ex.getClass() + " : " + ex.getMessage());
                ex.printStackTrace(printer);
            }
        }
    }
    private void getSomeInfo() {
        try {
            List<PlayerDetails> playerList;
            List<TeamDetails> teamList;
            List<LeagueDetails> leagueList;

            printer.println("List all players in team T2: ");
            playerList = ejbRequest.getPlayersOfTeam("T2");
            printDetailsList(playerList);
            printer.println();

            printer.println("List all teams in league L1: ");
            teamList = ejbRequest.getTeamsOfLeague("L1");
            printDetailsList(teamList);
            printer.println();

            printer.println("List all defenders: ");
            playerList = ejbRequest.getPlayersByPosition("defender");
            printDetailsList(playerList);
            printer.println();

            printer.println("List the leagues of player P28: ");
            leagueList = ejbRequest.getLeaguesOfPlayer("P28");
            printDetailsList(leagueList);
            printer.println();

        } catch (Exception ex) {
            printer.println("Caught an exception: " + ex.getClass() + " : " + ex.getMessage());
            ex.printStackTrace(printer);
        }
    }

    private void getMoreInfo() {
        try {
            LeagueDetails leagueDetails;
            TeamDetails teamDetails;
            PlayerDetails playerDetails;
            List<PlayerDetails> playerList;
            List<TeamDetails> teamList;
            List<LeagueDetails> leagueList;
            List<String> sportList;

            printer.println("Details of league L1: ");
            leagueDetails = ejbRequest.getLeague("L1");
            printer.println(leagueDetails.toString());
            printer.println();

            printer.println("Details of team T3: ");
            teamDetails = ejbRequest.getTeam("T3");
            printer.println(teamDetails.toString());
            printer.println();

            printer.println("Details of player P20: ");
            playerDetails = ejbRequest.getPlayer("P20");
            printer.println(playerDetails.toString());
            printer.println();

            printer.println("List all teams in league L3: ");
            teamList = ejbRequest.getTeamsOfLeague("L3");
            printDetailsList(teamList);
            printer.println();

            printer.println("List all players: ");
            playerList = ejbRequest.getAllPlayers();
            printDetailsList(playerList);
            printer.println();

            printer.println("List all players not on a team: ");
            playerList = ejbRequest.getPlayersNotOnTeam();
            printDetailsList(playerList);
            printer.println();

            printer.println("Details of Jack Patterson, a power forward: ");
            playerList = ejbRequest.getPlayersByPositionAndName("power forward", "Jack Patterson");
            printDetailsList(playerList);
            printer.println();

            printer.println("List all players in the city of Truckee: ");
            playerList = ejbRequest.getPlayersByCity("Truckee");
            printDetailsList(playerList);
            printer.println();

            printer.println("List all soccer players: ");
            playerList = ejbRequest.getPlayersBySport("Soccer");
            printDetailsList(playerList);
            printer.println();

            printer.println("List all players in league L1: ");
            playerList = ejbRequest.getPlayersByLeagueId("L1");
            printDetailsList(playerList);
            printer.println();

            printer.println("List all players making a higher salary than Ian Carlyle: ");
            playerList = ejbRequest.getPlayersByHigherSalary("Ian Carlyle");
            printDetailsList(playerList);
            printer.println();

            printer.println("List all players with a salary between 500 and 800: ");
            playerList = ejbRequest.getPlayersBySalaryRange(500.00, 800.00);
            printDetailsList(playerList);
            printer.println();

            printer.println("List all players of team T5: ");
            playerList = ejbRequest.getPlayersOfTeam("T5");
            printDetailsList(playerList);
            printer.println();

            printer.println("List all the leagues of player P28: ");
            leagueList = ejbRequest.getLeaguesOfPlayer("P28");
            printDetailsList(leagueList);
            printer.println();

            printer.println("List all the sports of player P28: ");
            sportList = ejbRequest.getSportsOfPlayer("P28");
            printDetailsList(sportList);
            printer.println();

        } catch (Exception ex) {
            printer.println("Caught an exception: " + ex.getClass() + " : " + ex.getMessage());
            ex.printStackTrace(printer);
        }
    }
    private void showStandingsAndMatches() {
        try {
            printer.println("========================================");
            printer.println("CLASAMENT LIGA L1 (Mountain Soccer):");
            printer.println("========================================");
            List<StandingsEntry> standingsL1 = ejbRequest.getLeagueStandings("L1");
            printDetailsList(standingsL1);
            printer.println();

            printer.println("========================================");
            printer.println("MECIURILE ECHIPEI T1 (Honey Bees):");
            printer.println("========================================");
            List<MatchDetails> matchesT1 = ejbRequest.getMatchesOfTeam("T1");
            printDetailsList(matchesT1);
            printer.println();

            printer.println("========================================");
            printer.println("CLASAMENT LIGA L2 (Valley Basketball):");
            printer.println("========================================");
            List<StandingsEntry> standingsL2 = ejbRequest.getLeagueStandings("L2");
            printDetailsList(standingsL2);
            printer.println();

            printer.println("========================================");
            printer.println("MECIURILE ECHIPEI T3 (Deer):");
            printer.println("========================================");
            List<MatchDetails> matchesT3 = ejbRequest.getMatchesOfTeam("T3");
            printDetailsList(matchesT3);
            printer.println();

        } catch (Exception ex) {
            printer.println("Caught an exception: " + ex.getClass() + " : " + ex.getMessage());
            ex.printStackTrace(printer);
        }
    }
    private void removeInfo() {
        try {
            printer.println("Removing team T6. ");
            ejbRequest.removeTeam("T6");
            printer.println();

            printer.println("Removing player P24 ");
            ejbRequest.removePlayer("P24");
            printer.println();

        } catch (Exception ex) {
            printer.println("Caught an exception: " + ex.getClass() + " : " + ex.getMessage());
            ex.printStackTrace(printer);
        }
    }

    private void printDetailsList(List list) {
        for (Object details : list) {
            printer.println(details.toString());
        }
        printer.println();
    }
}