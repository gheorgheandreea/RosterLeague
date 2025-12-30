package org.rosterleague.servlet;
import java.io.*;
import java.time.LocalDate;
import java.util.List;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.rosterleague.common.*;

@WebServlet(name = "standingsServlet", value = "/standings")
public class StandingsServlet extends HttpServlet {
    @Inject
    Request ejbRequest;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String leagueId = request.getParameter("leagueId");

        out.println("<!DOCTYPE html>");
        out.println("<html lang='ro'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Roster League - Clasamente</title>");
        out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'>");
        out.println("<style>");
        out.println("body { background-color: #f8f9fa; }");
        out.println(".navbar { background-color: #2c3e50; }");
        out.println(".navbar-brand, .nav-link { color: #ecf0f1 !important; }");
        out.println(".nav-link:hover { color: #3498db !important; }");
        out.println(".standings-table { background-color: white; border-radius: 8px; }");
        out.println(".top-3 { font-weight: bold; }");
        out.println(".gold { background-color: #ffd700; }");
        out.println(".silver { background-color: #c0c0c0; }");
        out.println(".bronze { background-color: #cd7f32; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");

        // Navbar
        out.println("<nav class='navbar navbar-expand-lg'>");
        out.println("<div class='container-fluid'>");
        out.println("<a class='navbar-brand' href='#'>Roster League</a>");
        out.println("<div class='navbar-nav'>");
        out.println("<a class='nav-link active' href='standings'>Clasamente</a>");
        out.println("<a class='nav-link' href='matches'>Meciuri</a>");
        out.println("<a class='nav-link' href='/'>Acasă</a>");
        out.println("</div>");
        out.println("</div>");
        out.println("</nav>");

        out.println("<div class='container mt-4'>");
        out.println("<h1 class='mb-4'>Clasamente Liga</h1>");

        // Selector de ligă
        out.println("<div class='card mb-4'>");
        out.println("<div class='card-body'>");
        out.println("<form method='get' action='standings' class='row g-3'>");
        out.println("<div class='col-md-8'>");
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
        out.println("</form>");
        out.println("</div>");
        out.println("</div>");

        // Afișează clasamentul dacă a fost selectată o ligă
        if (leagueId != null && !leagueId.isEmpty()) {
            try {
                LeagueDetails league = ejbRequest.getLeague(leagueId);
                List<StandingsEntry> standings = ejbRequest.getLeagueStandings(leagueId);

                out.println("<div class='card standings-table'>");
                out.println("<div class='card-header'>");
                out.println("<h3>Clasament: " + league.getName() + " - " + league.getSport() + "</h3>");
                out.println("</div>");
                out.println("<div class='card-body'>");

                if (standings.isEmpty()) {
                    out.println("<p class='text-muted'>Nu există meciuri jucate în această ligă.</p>");
                } else {
                    out.println("<table class='table table-hover'>");
                    out.println("<thead class='table-dark'>");
                    out.println("<tr>");
                    out.println("<th>Poziție</th>");
                    out.println("<th>Echipă</th>");
                    out.println("<th>Meciuri Jucate</th>");
                    out.println("<th>Victorii</th>");
                    out.println("<th>Egaluri</th>");
                    out.println("<th>Înfrângeri</th>");
                    out.println("<th>Puncte</th>");
                    out.println("</tr>");
                    out.println("</thead>");
                    out.println("<tbody>");

                    int position = 1;
                    for (StandingsEntry entry : standings) {
                        String rowClass = "";
                        if (position == 1) rowClass = "gold";
                        else if (position == 2) rowClass = "silver";
                        else if (position == 3) rowClass = "bronze";

                        out.println("<tr class='" + rowClass + "'>");
                        out.println("<td><strong>" + position + "</strong></td>");
                        out.println("<td>" + entry.getTeamName() + "</td>");
                        out.println("<td>" + entry.getMatchesPlayed() + "</td>");
                        out.println("<td>" + entry.getWins() + "</td>");
                        out.println("<td>" + entry.getDraws() + "</td>");
                        out.println("<td>" + entry.getLosses() + "</td>");
                        out.println("<td><strong>" + entry.getPoints() + "</strong></td>");
                        out.println("</tr>");
                        position++;
                    }

                    out.println("</tbody>");
                    out.println("</table>");
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