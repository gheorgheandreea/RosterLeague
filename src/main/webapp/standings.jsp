<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="activePage" value="standings" scope="request" />

<t:pageTemplate pageTitle="Standings">
    <div class="d-flex align-items-center justify-content-between mb-4">
        <h1 class="m-0">Standings</h1>
    </div>

    <div class="card mb-4">
        <div class="card-body">
            <form method="get" action="${pageContext.request.contextPath}/standings" class="row g-3 align-items-end">
                <div class="col-md-10">
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

                <div class="col-md-2">
                    <button type="submit" class="btn btn-primary w-100">Show</button>
                </div>
            </form>
        </div>
    </div>

    <c:if test="${not empty selectedLeague}">
        <div class="card">
            <div class="card-header">
                <strong>Standings: ${selectedLeague.name} (${selectedLeague.sport})</strong>
            </div>

            <div class="card-body p-0">
                <c:choose>
                    <c:when test="${empty standings}">
                        <div class="p-4">
                            <div class="alert alert-info m-0">
                                No matches played in this league yet.
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="table-responsive">
                            <table class="table table-striped table-hover align-middle mb-0">
                                <thead class="table-light">
                                <tr>
                                    <th style="width: 80px;">#</th>
                                    <th>Team</th>
                                    <th class="text-center"> MP</th>
                                    <th class="text-center">W</th>
                                    <th class="text-center">D</th>
                                    <th class="text-center">L</th>
                                    <th class="text-center">Pts</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${standings}" var="entry" varStatus="status">
                                    <c:set var="rowClass" value="${status.index == 0 ? 'table-warning' : (status.index == 1 ? 'table-secondary' : (status.index == 2 ? 'table-info' : ''))}" />
                                    <tr class="${rowClass}">
                                        <td><strong>${status.index + 1}</strong></td>
                                        <td>${entry.teamName}</td>
                                        <td class="text-center">${entry.matchesPlayed}</td>
                                        <td class="text-center">${entry.wins}</td>
                                        <td class="text-center">${entry.draws}</td>
                                        <td class="text-center">${entry.losses}</td>
                                        <td class="text-center"><span class="badge text-bg-primary">${entry.points}</span></td>
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
