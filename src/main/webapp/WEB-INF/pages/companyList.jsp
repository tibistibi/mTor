<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="companyList.title"/></title>
    <meta name="menu" content="CompanyMenu"/>
</head>

<div class="span10">
    <h2><fmt:message key="companyList.heading"/></h2>

    <form method="get" action="${ctx}/companies" id="searchForm" class="form-search">
    <div id="search" class="input-append">
        <input type="text" size="20" name="q" id="query" value="${param.q}"
               placeholder="<fmt:message key="search.enterTerms"/>" class="input-medium search-query"/>
        <button id="button.search" class="btn" type="submit">
            <i class="icon-search"></i> <fmt:message key="button.search"/>
        </button>
    </div>
    </form>

    <fmt:message key="companyList.message"/>

    <div id="actions" class="form-actions">
        <a class="btn btn-primary" href="<c:url value='/editCompany'/>" >
            <i class="icon-plus icon-white"></i> <fmt:message key="button.add"/>
        </a>
        <a class="btn" href="<c:url value="/mainMenu"/>" >
            <i class="icon-ok"></i> <fmt:message key="button.done"/>
        </a>
    </div>

    <display:table name="companies" class="table table-condensed table-striped table-hover" requestURI="" id="companyList" export="true" pagesize="25">
        <display:column property="id" sortable="true" href="editCompany" media="html"
            paramId="id" paramProperty="id" titleKey="company.id"/>
        <display:column property="id" media="csv excel xml pdf" titleKey="company.id"/>
        <display:column property="name" sortable="true" titleKey="company.name"/>

        <display:setProperty name="paging.banner.item_name"><fmt:message key="companyList.company"/></display:setProperty>
        <display:setProperty name="paging.banner.items_name"><fmt:message key="companyList.companies"/></display:setProperty>

        <display:setProperty name="export.excel.filename"><fmt:message key="companyList.title"/>.xls</display:setProperty>
        <display:setProperty name="export.csv.filename"><fmt:message key="companyList.title"/>.csv</display:setProperty>
        <display:setProperty name="export.pdf.filename"><fmt:message key="companyList.title"/>.pdf</display:setProperty>
    </display:table>
</div>
