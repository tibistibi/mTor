<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="messageList.title"/></title>
    <meta name="menu" content="MessageMenu"/>
</head>

<div class="span10">
    <h2><fmt:message key="messageList.heading"/></h2>

    <form method="get" action="${ctx}/messages" id="searchForm" class="form-search">
    <div id="search" class="input-append">
        <input type="text" size="20" name="q" id="query" value="${param.q}"
               placeholder="<fmt:message key="search.enterTerms"/>" class="input-medium search-query"/>
        <button id="button.search" class="btn" type="submit">
            <i class="icon-search"></i> <fmt:message key="button.search"/>
        </button>
    </div>
    </form>

    <fmt:message key="messageList.message"/>

    <div id="actions" class="form-actions">
        <a class="btn btn-primary" href="<c:url value='/editMessage'/>" >
            <i class="icon-plus icon-white"></i> <fmt:message key="button.add"/>
        </a>
        <a class="btn" href="<c:url value="/mainMenu"/>" >
            <i class="icon-ok"></i> <fmt:message key="button.done"/>
        </a>
    </div>

    <display:table name="mTorMessages" class="table table-condensed table-striped table-hover" requestURI="" id="messageList" export="true" pagesize="25">
        <display:column property="id" sortable="true" href="editMessage" media="html"
            paramId="id" paramProperty="id" titleKey="message.id"/>
        <display:column property="id" media="csv excel xml pdf" titleKey="message.id"/>
        <display:column property="content" sortable="true" titleKey="message.content"/>
        <display:column property="status" sortable="true" titleKey="message.status"/>
        <display:column sortProperty="timestamp" sortable="true" titleKey="message.timestamp">
             <fmt:formatDate value="${messageList.timestamp}"  type="both" timeStyle="long" dateStyle="long" />
        </display:column>

        <display:setProperty name="paging.banner.item_name"><fmt:message key="messageList.message"/></display:setProperty>
        <display:setProperty name="paging.banner.items_name"><fmt:message key="messageList.messages"/></display:setProperty>

        <display:setProperty name="export.excel.filename"><fmt:message key="messageList.title"/>.xls</display:setProperty>
        <display:setProperty name="export.csv.filename"><fmt:message key="messageList.title"/>.csv</display:setProperty>
        <display:setProperty name="export.pdf.filename"><fmt:message key="messageList.title"/>.pdf</display:setProperty>
    </display:table>
</div>
