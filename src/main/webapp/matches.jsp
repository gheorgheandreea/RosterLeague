<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="activePage" value="matches" scope="request" />

<t:pageTemplate pageTitle="Matches">

    <div class="d-flex align-items-center justify-content-between mb-4">
        <h1 class="m-0">Matches</h1>
    </div>

    <div class="card mb-4">
        <div class="card-body">
            <form method="get" action="${pageContext.request.contextPath}/matches" class="row g-3 align-items-end">
                <div class="col-md-6">
                    <label for="leagueId" class="form-label">League</label>
                    <select name="leagueId" id="leagueId" class="form-select" onchange="this.form.submit()">
                        <option value="">-- Choose a league --</option>
                        <c:forEach items="${allLeagues}" var="league">
                            <option value="${league.id}" ${league.id == param.leagueId ? 'selected' : ''}>
                                    ${league.name} (${league.sport})
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="col-md-4">
                    <label for="teamId" class="form-label">Team</label>
                    <select name="teamId" id="teamId" class="form-select" ${empty param.leagueId ? 'disabled' : ''} onchange="this.form.submit()">
                        <option value="">-- Choose a team --</option>
                        <c:forEach items="${teams}" var="team">
                            <option value="${team.id}" ${team.id == param.teamId ? 'selected' : ''}>
                                    ${team.name} (${team.city})
                            </option>
                        </c:forEach>
                    </select>
                    <c:if test="${empty param.leagueId}">
                        <div class="form-text">Pick a league first.</div>
                    </c:if>
                </div>

                <div class="col-md-2">
                    <button type="submit" class="btn btn-primary w-100">Show</button>
                </div>
            </form>
        </div>
    </div>

    <c:if test="${not empty selectedTeam}">
        <div class="card">
            <div class="card-header">
                <strong>Matches: ${selectedTeam.name} (${selectedTeam.city})</strong>
            </div>

            <div class="card-body p-0">
                <c:choose>
                    <c:when test="${empty matches}">
                        <div class="p-4">
                            <div class="alert alert-info m-0">No  matches found for this team.</div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="table-responsive">
                            <table class="table table-striped table-hover align-middle mb-0">
                                <thead class="table-light">
                                <tr>
                                    <th style="width: 140px;">Date</th>
                                    <th>Home</th>
                                    <th style="width: 120px;" class="text-center">Score</th>
                                    <th>Away</th>
                                    <th style="width: 140px;" class="text-center">Result</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${matches}" var="match">
                                    <c:set var="isHome" value="${match.homeTeamId == param.teamId}" />
                                    <c:set var="teamScore" value="${isHome ? match.homeScore : match.awayScore}" />
                                    <c:set var="opponentScore" value="${isHome ? match.awayScore : match.homeScore}" />
                                    <c:set var="resultText" value="${teamScore > opponentScore ? 'Win' : (teamScore < opponentScore ? 'Loss' : 'Draw')}" />
                                    <c:set var="badgeClass" value="${teamScore > opponentScore ? 'text-bg-success' : (teamScore < opponentScore ? 'text-bg-danger' : 'text-bg-warning')}" />

                                    <tr>
                                        <td>${match.matchDate}</td>
                                        <td>
                                                ${match.homeTeamName}
                                            <c:if test="${isHome}"><span class="badge text-bg-primary ms-2">HOME</span></c:if>
                                        </td>
                                        <td class="text-center"><strong>${match.homeScore} - ${match.awayScore}</strong></td>
                                        <td>
                                                ${match.awayTeamName}
                                            <c:if test="${!isHome}"><span class="badge text-bg-secondary ms-2">AWAY</span></c:if>
                                        </td>
                                        <td class="text-center"><span class="badge ${badgeClass}">${resultText}</span></td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </c:if>

</t:pageTemplate>
