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

        String leagueId = request.getParameter("leagueId");

        List<LeagueDetails> allLeagues = ejbRequest.getAllLeagues();
        request.setAttribute("allLeagues", allLeagues);

        // Afișează clasamentul dacă a fost selectată o ligă
        if (leagueId != null && !leagueId.isEmpty()) {
            try {
                LeagueDetails league = ejbRequest.getLeague(leagueId);
                request.setAttribute("selectedLeague", league);
                List<StandingsEntry> standings = ejbRequest.getLeagueStandings(leagueId);
                request.setAttribute("standings", standings);

            } catch (Exception e) {
                request.setAttribute("error", "Eroare la încărcarea clasamentului: " + e.getMessage());
            }
        }
        request.getRequestDispatcher("/standings.jsp").forward(request, response);
    }
}