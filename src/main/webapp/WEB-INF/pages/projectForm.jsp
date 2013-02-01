<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="projectDetail.title"/></title>
    <meta name="heading" content="<fmt:message key='projectDetail.heading'/>"/>
</head>

<c:set var="delObject" scope="request"><fmt:message key="projectList.project"/></c:set>
<script type="text/javascript">var msgDelConfirm =
   "<fmt:message key="delete.confirm"><fmt:param value="${delObject}"/></fmt:message>";
</script>

<div class="span2">
    <h2><fmt:message key="projectDetail.heading"/></h2>
    <fmt:message key="projectDetail.message"/>
</div>

<div class="span7">
    <s:form id="projectForm" action="saveProject" method="post" validate="true" cssClass="well form-horizontal">
            <s:hidden key="project.id"/>
        <!-- todo: change this to read the identifier field from the other pojo -->
        <s:select key="project.company" name="project.company.id" list="companyList" listKey="id" listValue="id"></s:select>
        <s:textfield key="project.name" required="true" maxlength="255" />
        <label for="projectUsers" class="control-label">Assign to user:</label>
        <select id="projectUsers" name="projectUsers" multiple="true" style="margin-left:20px;">
            <c:forEach items="${userList}" var="user">
            <option value="${user.getId()}">${user.getFullName()}</option>
            </c:forEach>
        </select>

        <div id="actions" class="form-actions">
            <s:submit type="button" cssClass="btn btn-primary" method="save" key="button.save" theme="simple">
                <i class="icon-ok icon-white"></i> <fmt:message key="button.save"/>
            </s:submit>
            <c:if test="${not empty project.id}">
                <s:submit type="button" cssClass="btn btn-warning" method="delete" key="button.delete"
                    onclick="return confirmMessage(msgDelConfirm)" theme="simple">
                    <i class="icon-trash icon-white"></i> <fmt:message key="button.delete"/>
                </s:submit>
            </c:if>
            <s:submit type="button" cssClass="btn" method="cancel" key="button.cancel" theme="simple">
                <i class="icon-remove"></i> <fmt:message key="button.cancel"/>
            </s:submit>
        </div>
    </s:form>
</div>

<script type="text/javascript">
    $(document).ready(function() {
        $("input[type='text']:visible:enabled:first", document.forms['projectForm']).focus();
    });
</script>
