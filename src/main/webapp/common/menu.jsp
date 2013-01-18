<%@ include file="/common/taglibs.jsp"%>

<menu:useMenuDisplayer name="Velocity" config="navbarMenu.vm" permissions="rolesAdapter">
<div class="nav-collapse collapse">
<ul class="nav">
    <c:if test="${empty pageContext.request.remoteUser}">
        <li class="active">
            <a href="<c:url value="/login"/>"><fmt:message key="login.title"/></a>
        </li>
    </c:if>
    <menu:displayMenu name="MainMenu"/>
    <menu:displayMenu name="UserMenu"/>
    <menu:displayMenu name="AdminMenu"/>
    <menu:displayMenu name="Logout"/>
    
    <!--Company-START-->
    <menu:displayMenu name="CompanyMenu"/>
    <!--Company-END-->
    <!--Project-START-->
    <menu:displayMenu name="ProjectMenu"/>
    <!--Project-END-->
</ul>
</div>
</menu:useMenuDisplayer>
