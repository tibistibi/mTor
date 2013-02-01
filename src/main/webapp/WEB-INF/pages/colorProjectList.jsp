<%@ include file="/common/taglibs.jsp"%>
<meta http-equiv="refresh" content="5" >
  
<head>
    <title><fmt:message key="projectList.title"/></title>
    <meta name="menu" content="ProjectMenu"/>
    <meta http-equiv="refresh" content="5" >
</head>

<div class="span10">
<div id="projectData" >
    <h2>Project list with status</h2>

    <form method="get" action="${ctx}/projects" id="searchForm" class="form-search">
    <div id="search" class="input-append">
        <input type="text" size="20" name="q" id="query" value="${param.q}"
               placeholder="<fmt:message key="search.enterTerms"/>" class="input-medium search-query"/>
        <button id="button.search" class="btn" type="submit">
            <i class="icon-search"></i> <fmt:message key="button.search"/>
        </button>
    </div>
    </form>

    <fmt:message key="projectList.message"/>

    <div id="actions" class="form-actions">
        <a class="btn btn-primary" href="<c:url value='/editProject'/>" >
            <i class="icon-plus icon-white"></i> <fmt:message key="button.add"/>
        </a>
        <a class="btn" href="<c:url value="/mainMenu"/>" >
            <i class="icon-ok"></i> <fmt:message key="button.done"/>
        </a>
    </div>

  
	<s:url id="thisUrl"/>	
    <display:table name="projects" class="table table-condensed table-striped table-hover" requestURI="${thisUrl}" id="projectList" export="true" pagesize="25" sort="list">
        <display:column property="id" sortable="true" href="editProject" media="html"
            paramId="id" paramProperty="id" titleKey="project.id"/>
        <display:column property="id" media="csv excel xml pdf" titleKey="project.id"/>
        <display:column property="name" sortable="true" titleKey="project.name"/>
        <display:column title="Status" sortable="true" class="thirdColumn">
        	${projectList.statusOfProject()}
        </display:column>  
       <display:column sortable="true" title="Users">${projectList.userNames()} </display:column> 

        <display:setProperty name="paging.banner.item_name"><fmt:message key="projectList.project"/></display:setProperty>
        <display:setProperty name="paging.banner.items_name"><fmt:message key="projectList.projects"/></display:setProperty>

        <display:setProperty name="export.excel.filename"><fmt:message key="projectList.title"/>.xls</display:setProperty>
        <display:setProperty name="export.csv.filename"><fmt:message key="projectList.title"/>.csv</display:setProperty>
        <display:setProperty name="export.pdf.filename"><fmt:message key="projectList.title"/>.pdf</display:setProperty>
    </display:table>
    
    </div>
    
</div>


<script type="text/javascript">

  if (!com) var com = {};
  com.mudrick = {

    onProjectTableLoad: function() {
      //color the table
  	  var rows = document.getElementsByClassName("thirdColumn");
      var tableLength = rows.length;
      
	  for  (var i=1; i<tableLength+1; i++){
		  	var status = $('#projectList tr:eq('+i+')').find("td:first").next().next().text();		  	
	   		if(status == "INFO") {	   			
	   			$('#projectList tr:eq('+i+')').addClass("green");
	   			$('#projectList tr:eq('+i+')').removeClass("red");
	   			$('#projectList tr:eq('+i+')').removeClass("yellow");
	   		} 
 	   		if(status == "WARN") {
	   			$('#projectList tr:eq('+i+')').addClass("yellow");
	   			$('#projectList tr:eq('+i+')').removeClass("red");
	   			$('#projectList tr:eq('+i+')').removeClass("green");
	   		}
	   		if(status == "ERROR") {
	   			$('#projectList tr:eq('+i+')').addClass("red");
	   			$('#projectList tr:eq('+i+')').removeClass("green");
	   			$('#projectList tr:eq('+i+')').removeClass("yellow");
	   		} 
	  	}

      // Gets called when the data loads
      $("table#projectList th.sortable").each(function() {
        // Iterate over each column header containing the sortable class, so
        // we can setup overriding click handlers to load via ajax, rather than
        // allowing the browser to follow a normal link
        $(this).click(function() {
          // "this" is scoped as the sortable th element
          var link = $(this).find("a").attr("href");
          $("div#projectData").load(link, {}, com.mudrick.onProjectTableLoad);
          // Stop event propagation, i.e. tell browser not to follow the clicked link
          return false;
        });
      });

      $("div#projectData .pagelinks a").each(function() {
        // Iterate over the pagination-generated links to override also
        $(this).click(function() {
          var link = $(this).attr("href");
          $("div#projectData").load(link, {}, com.mudrick.onProjectTableLoad);
          return false;
        });
      });
    }
  };

  $(document).ready(function() {
    // Load the initial rendering when the dom is ready.  Assuming you are injecting into a div
    // with id "projectData" that exists in the page.
    $("div#projectData").load("/WEB-INF/pages/colorProjectList.jsp", {}, com.mudrick.onProjectTableLoad);
  });

</script>
