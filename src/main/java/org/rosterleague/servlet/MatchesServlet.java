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

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String teamId = request.getParameter("teamId");
        String leagueId = request.getParameter("leagueId");

        out.println("<!DOCTYPE html>");
        out.println("<html lang='ro'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Roster League - Meciuri</title>");
        out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'>");
        out.println("<style>");
        out.println("body { background-color: #f8f9fa; }");
        out.println(".navbar { background-color: #2c3e50; }");
        out.println(".navbar-brand, .nav-link { color: #ecf0f1 !important; }");
        out.println(".nav-link:hover { color: #3498db !important; }");
        out.println(".match-card { background-color: white; border-radius: 8px; margin-bottom: 15px; }");
        out.println(".win { border-left: 4px solid #28a745; }");
        out.println(".loss { border-left: 4px solid #dc3545; }");
        out.println(".draw { border-left: 4px solid #ffc107; }");
        out.println(".score { font-size: 1.5rem; font-weight: bold; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");

        // Navbar
        out.println("<nav class='navbar navbar-expand-lg'>");
        out.println("<div class='container-fluid'>");
        out.println("<a class='navbar-brand' href='#'>Roster League</a>");
        out.println("<div class='navbar-nav'>");
        out.println("<a class='nav-link' href='standings'>Clasamente</a>");
        out.println("<a class='nav-link active' href='matches'>Meciuri</a>");
        out.println("<a class='nav-link' href='/'>Acasă</a>");
        out.println("</div>");
        out.println("</div>");
        out.println("</nav>");

        out.println("<div class='container mt-4'>");
        out.println("<h1 class='mb-4'>Meciuri Echipă</h1>");

        // Selector de ligă și echipă
        out.println("<div class='card mb-4'>");
        out.println("<div class='card-body'>");
        out.println("<form method='get' action='matches' class='row g-3'>");

        // Selector de ligă
        out.println("<div class='col-md-5'>");
        out.println("<label for='leagueId' class='form-label'>Selectează Liga:</label>");
        out.println("<select name='leagueId' id='leagueId' class='form-select' onchange='this.form.submit()'>");
        out.println("<option value=''>-- Alege o ligă --</option>");

        List<LeagueDetails> allLeagues = ejbRequest.getAllLeagues();
        for (LeagueDetails league : allLeagues) {
            String selected = league.getId().equals(leagueId) ? "selected" : "";
            out.println("<option value='" + league.getId() + "' " + selected + ">" +
                    league.getName() + " (" + league.getSport() + ")</option>");
        }

        out.println("</select>");
        out.println("</div>");

        // Selector de echipă (doar dacă s-a selectat o ligă)
        if (leagueId != null && !leagueId.isEmpty()) {
            out.println("<div class='col-md-5'>");
            out.println("<label for='teamId' class='form-label'>Selectează Echipa:</label>");
            out.println("<select name='teamId' id='teamId' class='form-select' onchange='this.form.submit()'>");
            out.println("<option value=''>-- Alege o echipă --</option>");

            // Adaugă leagueId hidden pentru a-l păstra
            out.println("<input type='hidden' name='leagueId' value='" + leagueId + "'>");

            List<TeamDetails> teams = ejbRequest.getTeamsOfLeague(leagueId);
            for (TeamDetails team : teams) {
                String selected = team.getId().equals(teamId) ? "selected" : "";
                out.println("<option value='" + team.getId() + "' " + selected + ">" +
                        team.getName() + " (" + team.getCity() + ")</option>");
            }

            out.println("</select>");
            out.println("</div>");
        }

        out.println("</form>");
        out.println("</div>");
        out.println("</div>");

        // Afișează meciurile dacă a fost selectată o echipă
        if (teamId != null && !teamId.isEmpty()) {
            try {
                TeamDetails team = ejbRequest.getTeam(teamId);
                List<MatchDetails> matches = ejbRequest.getMatchesOfTeam(teamId);

                out.println("<div class='card'>");
                out.println("<div class='card-header'>");
                out.println("<h3>Meciurile echipei: " + team.getName() + " (" + team.getCity() + ")</h3>");
                out.println("</div>");
                out.println("<div class='card-body'>");

                if (matches.isEmpty()) {
                    out.println("<p class='text-muted'>Nu există meciuri pentru această echipă.</p>");
                } else {
                    for (MatchDetails match : matches) {
                        // Determină rezultatul pentru echipa curentă
                        String resultClass = "";
                        String resultText = "";

                        boolean isHome = match.getHomeTeamId().equals(teamId);
                        int teamScore = isHome ? match.getHomeScore() : match.getAwayScore();
                        int opponentScore = isHome ? match.getAwayScore() : match.getHomeScore();

                        if (teamScore > opponentScore) {
                            resultClass = "win";
                            resultText = "Victorie";
                        } else if (teamScore < opponentScore) {
                            resultClass = "loss";
                            resultText = "Înfrângere";
                        } else {
                            resultClass = "draw";
                            resultText = "Egal";
                        }

                        out.println("<div class='card match-card " + resultClass + "'>");
                        out.println("<div class='card-body'>");
                        out.println("<div class='row align-items-center'>");

                        // Echipa gazdă
                        out.println("<div class='col-md-4 text-end'>");
                        out.println("<h5>" + match.getHomeTeamName() + "</h5>");
                        out.println("</div>");

                        // Scor
                        out.println("<div class='col-md-2 text-center'>");
                        out.println("<span class='score'>" + match.getHomeScore() + " - " + match.getAwayScore() + "</span>");
                        out.println("</div>");

                        // Echipa oaspete
                        out.println("<div class='col-md-4'>");
                        out.println("<h5>" + match.getAwayTeamName() + "</h5>");
                        out.println("</div>");

                        // Rezultat
                        out.println("<div class='col-md-2'>");
                        out.println("<span class='badge bg-secondary'>" + resultText + "</span><br>");
                        out.println("<small class='text-muted'>" + match.getMatchDate() + "</small>");
                        out.println("</div>");

                        out.println("</div>");
                        out.println("</div>");
                        out.println("</div>");
                    }
                }


                out.println("</div>");
                out.println("</div>");

            } catch (Exception e) {
                out.println("<div class='alert alert-danger'>Eroare: " + e.getMessage() + "</div>");
            }
        }

        out.println("</div>");

        out.println("<script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js'></script>");
        out.println("</body>");
        out.println("</html>");
    }
}