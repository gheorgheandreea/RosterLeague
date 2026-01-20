<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<header data-bs-theme="dark">
    <nav class="navbar navbar-expand-md navbar-dark fixed-top bg-dark">
        <div class="container-fluid"> <a class="navbar-brand" href="${pageContext.request.contextPath}">Roster League</a>
            <button class="navbar-toggler" type="button"  data-bs-toggle="collapse" data-bs-target="#navbarCollapse" aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarCollapse">
                <ul class="navbar-nav me-auto mb-2 mb-md-0">
                    <li class="nav-item">
                            <a class="nav-link ${activePage eq 'matches' ? 'active' : ''}" href="${pageContext.request.contextPath}/matches">Matches</a>
                    </li>
                    <li class="nav-item">
                            <a class="nav-link ${activePage eq 'standings' ? 'active' : ''}" href="${pageContext.request.contextPath}/standings">Standings</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
</header>