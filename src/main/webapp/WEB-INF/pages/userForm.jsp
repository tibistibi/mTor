<%@ include file="/common/taglibs.jsp" %>

<head>
    <title><fmt:message key="userProfile.title"/></title>
    <meta name="menu" content="UserMenu"/>
</head>

<c:set var="delObject" scope="request"><fmt:message key="userList.user"/></c:set>
<script type="text/javascript">var msgDelConfirm =
   "<fmt:message key="delete.confirm"><fmt:param value="${delObject}"/></fmt:message>";
</script>

<div class="span2">
    <h2><fmt:message key="userProfile.heading"/></h2>
    <c:choose>
        <c:when test="${param.from == 'list'}">
            <p><fmt:message key="userProfile.admin.message"/></p>
        </c:when>
        <c:otherwise>
            <p><fmt:message key="userProfile.message"/></p>
        </c:otherwise>
    </c:choose>
</div>
<div class="span7">
    <s:form name="userForm" action="saveUser" method="post" validate="true" cssClass="well form-horizontal" autocomplete="off">
        <s:hidden key="user.id"/>
        <s:hidden key="user.version"/>
        <input type="hidden" name="from" value="${param.from}"/>

        <c:if test="${cookieLogin == 'true'}">
            <s:hidden key="user.password"/>
            <s:hidden key="user.confirmPassword"/>
        </c:if>
        <s:if test="user.version == null">
            <input type="hidden" name="encryptPass" value="true"/>
        </s:if>
        <s:textfield key="user.username" required="true" onchange="usernameChanged()"/>

        <c:if test="${cookieLogin != 'true'}">
            <s:password key="user.password" showPassword="true" required="true"
                        onchange="passwordChanged(this)"/>

            <s:password key="user.confirmPassword" required="true"
                        showPassword="true" onchange="passwordChanged(this)"/>
        </c:if>

        <s:textfield key="user.passwordHint" required="true"/>
        <s:textfield key="user.firstName" required="true"/>
        <s:textfield key="user.lastName" required="true"/>
        <s:select list="statusList" key="user.statusThreshold"></s:select>
        <s:hidden key="user.email" />
        <s:hidden key="user.phoneNumber"/>
        <s:hidden key="user.website"/>
         
        <security:authorize ifAllGranted="ROLE_ADMIN">
			<s:select id="projects" name="projects" key="user.assign.projects" 
					  list="projectList" multiple="true" 
					  listKey="id" listValue="name"   
					  value="assignedProjects"/>
		</security:authorize>
		
        <fieldset>
<%--             <legend class="accordion-heading">
                <a data-toggle="collapse" href="#collapse-address"><fmt:message key="user.address.address"/></a>
            </legend> --%>
            <div id="collapse-address" class="accordion-body collapse">
                <s:textfield key="user.address.address"/>
                <s:textfield key="user.address.city"/>
                <s:textfield key="user.address.province"/>
                <s:textfield key="user.address.postalCode"/>
                <s:set name="country" value="user.address.country" scope="page"/>
                <fieldset class="control-group">
                    <label for="user.address.country">
                        <fmt:message key="user.address.country"/>
                    </label>

                    <div class="controls">
                        <appfuse:country name="user.address.country" prompt="" default="${country}"/>
                    </div>
                </fieldset>
            </div>
        </fieldset>
<c:choose>
    <c:when test="${param.from == 'list'}">
        <fieldset class="control-group">
            <label class="control-label"><fmt:message key="userProfile.accountSettings"/></label>
            <div class="controls">
                <label class="checkbox inline">
                    <s:checkbox key="user.enabled" id="user.enabled" theme="simple" fieldValue="true"/>
                    <fmt:message key="user.enabled"/>
                </label>
                <label class="checkbox inline">
                    <s:checkbox key="user.accountExpired" id="user.accountExpired" theme="simple" fieldValue="true"/>
                    <fmt:message key="user.accountExpired"/>
                </label>
                <label class="checkbox inline">
                    <s:checkbox key="user.accountLocked" id="user.accountLocked" theme="simple" fieldValue="true"/>
                    <fmt:message key="user.accountLocked"/>
                </label>
                <br/>
                <label class="checkbox inline">
                    <s:checkbox key="user.credentialsExpired" id="user.credentialsExpired" theme="simple" fieldValue="true"/>
                    <fmt:message key="user.credentialsExpired"/>
                </label>
            </div>
        </fieldset>
        <fieldset class="control-group">
        	<s:select id="userRoles" name="userRoles" key="userProfile.assignRoles" 
        			  list="roleList" multiple="true"
        			  listKey="name" listValue="name"
        			  value="assignedRoles"/>
        </fieldset>
    </c:when>
    <c:otherwise>
        <fieldset class="control-group">
            <label class="control-label"><fmt:message key="user.roles"/>:</label>
            <div class="controls readonly">
                <s:iterator value="user.roleList" status="status">
                    <s:property value="label"/><s:if test="!#status.last">,</s:if>
                    <input type="hidden" name="userRoles" value="<s:property value="value"/>"/>
                </s:iterator>
            </div>
            <s:hidden name="user.enabled" value="%{user.enabled}"/>
            <s:hidden name="user.accountExpired" value="%{user.accountExpired}"/>
            <s:hidden name="user.accountLocked" value="%{user.accountLocked}"/>
            <s:hidden name="user.credentialsExpired" value="%{user.credentialsExpired}"/>
        </fieldset>
    </c:otherwise>
</c:choose>
        <div id="actions" class="form-actions">
            <s:submit type="button" cssClass="btn btn-primary" method="save" key="button.save" theme="simple">
                <i class="icon-ok icon-white"></i>
                <fmt:message key="button.save"/>
            </s:submit>
            <c:if test="${param.from == 'list' and not empty user.id}">
                <s:submit type="button" cssClass="btn btn-danger" method="delete" key="button.delete"
                    onclick="return confirmMessage(msgDelConfirm)" theme="simple">
                    <i class="icon-trash"></i>
                    <fmt:message key="button.delete"/>
                </s:submit>
            </c:if>
            <s:submit type="button" cssClass="btn" method="cancel" key="button.cancel" theme="simple">
                <i class="icon-remove"></i>
                <fmt:message key="button.cancel"/>
            </s:submit>
        </div>
    </s:form>
</div>

<c:set var="scripts" scope="request">
<script type="text/javascript">
    function passwordChanged(passwordField) {
        if (passwordField.name == "user.password") {
            var origPassword = "<s:property value="user.password"/>";
        } else if (passwordField.name == "user.confirmPassword") {
            var origPassword = "<s:property value="user.confirmPassword"/>";
        }

        if (passwordField.value != origPassword) {
            createFormElement("input", "hidden", "encryptPass", "encryptPass",
                    "true", passwordField.form);
        }
    }
    
    function usernameChanged(){
    	 document.forms['userForm'].elements["user.password"].value = '';
    	 document.forms['userForm'].elements["user.confirmPassword"].value = '';
    }
    
</script>
</c:set>
<script type="text/javascript">
    $(document).ready(function() {
        $("input[type='text']:visible:enabled:first", document.forms['userForm']).focus();
    });      
</script>
