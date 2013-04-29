<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="projectsOverview.title"/></title>
    <meta name="menu" content="ProjectMenu"/>
</head>

<script type="text/javascript" src="<c:url value='/scripts/lib/plugins/jquery-ui-1.10.2.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/lib/plugins/jquery.jqplot.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/lib/plugins/jqplot.pieRenderer.min.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value='/styles/jquery.jqplot.css'/>" />

<script type="text/javascript">
	function update() {
		var TIME_3_SECONDS = 3000;
		var TIME_30_SECONDS = 30000;
		var TIME_120_SECONDS = 120000;
		
		$.ajax({
			tradional: true,
			type: 'POST',
			url: 'overviewAjaxProjectsInfo',
			timeout: TIME_3_SECONDS,
			success: function(data) {
				for (i in data) {
					$('#infoPanel_' + data[i].name + ' a').html(data[i].info);
					$('#warnPanel_' + data[i].name + ' a').html(data[i].warn);
					$('#errorPanel_' + data[i].name + ' a').html(data[i].error);
					$('#newestMsg_' + data[i].name + '').html(data[i].newest);
				}
				window.setTimeout(update, TIME_30_SECONDS);
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				$('div[id^="infoPanel_"] a').each(function() {
					   $(this).html('<img src="<c:url value="/images/green_loading_28x28.gif"/>" alt="<fmt:message key="projectsOverview.icon.loading"/>" class="icon"/>');
				});
				$('div[id^="warnPanel_"] a').each(function() {
					   $(this).html('<img src="<c:url value="/images/yellow_loading_28x28.gif"/>" alt="<fmt:message key="projectsOverview.icon.loading"/>" class="icon"/>');
				});
				$('div[id^="errorPanel_"] a').each(function() {
					   $(this).html('<img src="<c:url value="/images/red_loading_28x28.gif"/>" alt="<fmt:message key="projectsOverview.icon.loading"/>" class="icon"/>');
				});
				$('div[id^="newestMsg_"]').each(function() {
					   $(this).html('<fmt:message key="projectOverview.loading.error"/>');
				});
				window.setTimeout(update, TIME_120_SECONDS);
			}
		});
	}
	
	function openChartDlg(chartDlgId, projectId) {
		var TIME_3_SECONDS = 3000;
		
		$.ajax({
			tradional: true,
			type: 'POST',
			data: 'project.id=' + projectId,
			url: 'overviewAjaxProjectStats',
			timeout: TIME_3_SECONDS,
			success: function(data) {
				$('#' + chartDlgId).show();
			    $('#' + chartDlgId).draggable();
			    
			    var chartData = [
					['Error', data.error], 
					['Warn', data.warn], 
					['Info', data.info], 
					['None', data.none]
			    ];
			    
				var plot = jQuery.jqplot(chartDlgId, [chartData], { 
					seriesDefaults: {
	               		renderer: jQuery.jqplot.PieRenderer, 
						rendererOptions: {
							showDataLabels: true
						}
					}, 
					legend: { show:true, location: 'e' }
				});
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				//alert('error');
			}
		});
	}
	
	$(document).ready(function() {
	    update();
	});
</script>

<s:form>
	<s:iterator value="projects" status="status" var="p">
	 	<div class="projectOverviewPanel">
	 		<img src="<c:url value="/images/project_32x32.png"/>" 
	 			 alt="<fmt:message key="projectsOverview.icon.project"/>" 
	 			 class="icon" style="float: left;"/>
	 		<s:a action="projects">
	 			<div class="projectOverviewName">${p.name}</div>
	 		</s:a>
 			<img src="<c:url value="/images/chart_16x16.png"/>"
 				 alt="<fmt:message key="projectsOverview.icon.chart"/>" 
 				 class="icon" style="float: right;"
 				 onclick="openChartDlg('modal_${p.name}', '${p.id}');"/>
 			<div id="infoPanel_${p.name}" class="projectOverviewInfoField">
 				<s:a action="messages">
 					<img src="<c:url value="/images/green_loading_28x28.gif"/>" 
 						 alt="<fmt:message key="projectsOverview.icon.loading"/>" class="icon"/>
				</s:a>
 			</div>
 			<div id="warnPanel_${p.name}" class="projectOverviewWarnField">
 				<s:a action="messages">
 					<img src="<c:url value="/images/yellow_loading_28x28.gif"/>" 
 						 alt="<fmt:message key="projectsOverview.icon.loading"/>" class="icon"/>
 				</s:a>
 			</div>
 			<div id="errorPanel_${p.name}" class="projectOverviewErrorField">
 				<s:a action="messages">
 					<img src="<c:url value="/images/red_loading_28x28.gif"/>" 
 						 alt="<fmt:message key="projectsOverview.icon.loading"/>" class="icon"/>
 				</s:a>
 			</div>
 			<div id="newestMsg_${p.name}" class="projectOverviewNewestMsg"></div>
			<div id="modal_${p.name}" class="projectOverviewModalDlg">
				<img src="<c:url value="/images/close_16x16.png"/>" 
					 class="projectOverviewModalDlgClose"
					 onclick="$('#modal_${p.name}').hide();"/>
			</div>
	 	</div>
	</s:iterator>
	<div style="clear:both;"></div>
</s:form>