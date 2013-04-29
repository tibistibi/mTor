<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="gCMClientDetail.title"/></title>
    <meta name="heading" content="<fmt:message key='gCMClientDetail.heading'/>"/>
</head>

<c:set var="delObject" scope="request"><fmt:message key="gCMClientList.gCMClient"/></c:set>
<script type="text/javascript">var msgDelConfirm =
   "<fmt:message key="delete.confirm"><fmt:param value="${delObject}"/></fmt:message>";
</script>

<div class="span2">
    <h2><fmt:message key="gCMClientDetail.heading"/></h2>
    <fmt:message key="gCMClientDetail.message"/>
</div>

<div class="span7">
    <s:form id="gCMClientForm" action="saveGCMClient" method="post" validate="true" cssClass="well form-horizontal">
            <s:hidden key="gCMClient.id"/>
        <s:textfield key="gCMClient.device" required="false" maxlength="255" />
        <s:textfield key="gCMClient.gcmRegistrationId" required="false" maxlength="255" />
        <!-- todo: change this to read the identifier field from the other pojo -->
        <!--s:select name="gCMClient.user.id" list="userList" listKey="id" listValue="id"--><!--/s:select -->

        <div id="actions" class="form-actions">
            <s:submit type="button" cssClass="btn btn-primary" method="save" key="button.save" theme="simple">
                <i class="icon-ok icon-white"></i> <fmt:message key="button.save"/>
            </s:submit>
            <c:if test="${not empty gCMClient.id}">
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
        $("input[type='text']:visible:enabled:first", document.forms['gCMClientForm']).focus();
    });
</script>
