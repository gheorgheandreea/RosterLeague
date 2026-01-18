package org.rosterleague.servlet;

import java.io.*;
import java.util.List;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.rosterleague.common.*;

@WebServlet(name = "matchesServlet", value = "/matches")
public class MatchesServlet extends HttpServlet {
    @Inject
    Request ejbRequest;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String teamId = request.getParameter("teamId");
        String leagueId = request.getParameter("leagueId");

        List<LeagueDetails> allLeagues = ejbRequest.getAllLeagues();
        request.setAttribute("allLeagues", allLeagues);

        // Selector de echipă (doar dacă s-a selectat o ligă)
        if (leagueId != null && !leagueId.isEmpty()) {
         try{
            List<TeamDetails> teams = ejbRequest.getTeamsOfLeague(leagueId);
            request.setAttribute("teams", teams);

             if (teamId != null && !teamId.isEmpty()) {
                 TeamDetails team = ejbRequest.getTeam(teamId);
                 request.setAttribute("selectedTeam", team);

                 List<MatchDetails> matches = ejbRequest.getMatchesOfTeam(teamId);
                 request.setAttribute("matches", matches);
             }

            } catch (Exception e) {
             request.setAttribute("error", "Eroare la încărcarea datelor: " + e.getMessage());
            }
        }
        request.getRequestDispatcher("/matches.jsp").forward(request, response);
    }
}