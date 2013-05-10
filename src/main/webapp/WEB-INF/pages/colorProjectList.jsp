<%@ include file="/common/taglibs.jsp"%>
  
<head>
	<title><fmt:message key="projectList.title"/></title>
	<meta name="menu" content="ProjectMenu"/>
</head>

<security:authorize ifAllGranted="ROLE_ADMIN">
	<script type="text/javascript">
		function buildIdTd(id) {
			var anchorIdTd = $('<td/>');
			$('<a/>', {  
			    href: 'editProject?id=' + id,  
			    text: id  
			}).appendTo(anchorIdTd);
			return anchorIdTd;
		}
	</script>
</security:authorize>
<security:authorize ifAllGranted="ROLE_USER">
	<script type="text/javascript">
		function buildIdTd(id) {
			return $('<td/>', {
			    text: id 
			});
		}
	</script>
</security:authorize>
<script type="text/javascript">
	var TIME_5_SECONDS = 5000;
	var TIME_30_SECONDS = 30000;
	var data;

	function reloadProjectsTable() {
		$.ajax({
			tradional: true,
			type: 'POST',
			url: 'projectsList',
			data: data,
			timeout: TIME_5_SECONDS,
			success: function(data) {
				refreshProjectsTable(data);
				window.setTimeout(reloadProjectsTable, TIME_5_SECONDS);
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				window.setTimeout(reloadProjectsTable, TIME_30_SECONDS);
			}
		});
	}
	
	function refreshProjectsTable(rows) {
		$('#projectList tbody').html('');
		var odd = true;
		for (r in rows) {
			var trStatusClass = rows[r].status == 'ERROR' ? 'red' : rows[r].status == 'WARN' ? 'yellow' : 'green';
			var newRow = $('<tr/>', {
			    class: (odd ? 'odd ' : 'even ') + trStatusClass
			});
			odd = !odd;
			buildIdTd(rows[r].id).appendTo(newRow);
			$('<td/>', {
			    text: rows[r].name
			}).appendTo(newRow);
			$('<td/>', {
			    text: rows[r].status 
			}).appendTo(newRow);
			$('<td/>', {
			    text: rows[r].usernames 
			}).appendTo(newRow);
			$('<td/>', {
			    text: rows[r].monitoring 
			}).appendTo(newRow);
			newRow.appendTo($('#projectList tbody'));
		}
	}
  	
	$(document).ready(function() {
		$('#projectList thead tr th a').each(function() {
			$(this).attr('href', '#');
			$(this).click(function() {
				window.clearTimeout(reloadProjectsTable);
				$('#projectList thead tr th').not($(this).parent()).each(function() {
					$(this).removeClass('sorted');
					$(this).removeClass('order1');
					$(this).removeClass('order2');
				});
				if (!$(this).parent().hasClass('sorted')) {
					$(this).parent().addClass('sorted');
				}
				data = 'sortBy=' + $(this).text();
				if ($(this).parent().hasClass('order1')) {
					$(this).parent().removeClass('order1');
					$(this).parent().addClass('order2');
					data += '&sortOrder=asc';
				} else {
					$(this).parent().removeClass('order2');
					$(this).parent().addClass('order1');
					data += '&sortOrder=desc';
				}
				reloadProjectsTable();
			});
		});
		reloadProjectsTable();
	});

</script>

<div class="span10">
	<div id="projectData" >
		<h2>Project list with status</h2>
		
		<form method="get" action="${ctx}/projects" id="searchForm" class="form-search">
			<div id="search" class="input-append">
				<input 	type="text" size="20" name="q" id="query" value="${param.q}"
						placeholder="<fmt:message key="search.enterTerms"/>" class="input-medium search-query"/>
				<button id="button.search" class="btn" type="submit">
					<i class="icon-search"></i> <fmt:message key="button.search"/>
				</button>
			</div>
		</form>
	
		<fmt:message key="projectList.message"/>
	
		<div id="actions" class="form-actions">
			<security:authorize ifAllGranted="ROLE_ADMIN"> 
				<a class="btn btn-primary" href="<c:url value='/editProject'/>" >
					<i class="icon-plus icon-white"></i> <fmt:message key="button.add"/>
				</a>
			</security:authorize>
			<a class="btn" href="<c:url value="/mainMenu"/>" >
				<i class="icon-ok"></i> <fmt:message key="button.done"/>
			</a>
		</div>
		
		<s:url id="thisUrl"/>	
		<display:table 	id="projectList" name="projects" requestURI="${thisUrl}" 
						class="table table-condensed table-striped table-hover"
						export="true" pagesize="25" sort="list">
			<security:authorize ifAllGranted="ROLE_ADMIN"> 
				<display:column	property="id" sortable="true"  href="editProject" media="html"
				    			paramId="id" paramProperty="id" titleKey="project.id"/>
			</security:authorize>
			<security:authorize ifAllGranted="ROLE_USER"> 
				<display:column property="id" sortable="true" media="html"
				    			paramId="id" paramProperty="id" titleKey="project.id"/>
			</security:authorize>
			<display:column property="id" media="csv excel xml pdf" titleKey="project.id"/>
			<display:column property="name" sortable="true" titleKey="project.name"/>
			<display:column title="Status" sortable="true">
				${projectList.statusOfProject()}
			</display:column>  
			<display:column sortable="false" title="Users">
				${projectList.userNames()}
			</display:column> 
			<display:column property="monitoring" sortable="true" titleKey="project.monitoring"/>
			
			<display:setProperty name="paging.banner.item_name"><fmt:message key="projectList.project"/></display:setProperty>
			<display:setProperty name="paging.banner.items_name"><fmt:message key="projectList.projects"/></display:setProperty>
			
			<display:setProperty name="export.excel.filename"><fmt:message key="projectList.title"/>.xls</display:setProperty>
			<display:setProperty name="export.csv.filename"><fmt:message key="projectList.title"/>.csv</display:setProperty>
			<display:setProperty name="export.pdf.filename"><fmt:message key="projectList.title"/>.pdf</display:setProperty>
		</display:table>
	</div>
</div>
