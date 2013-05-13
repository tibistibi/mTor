<%@ include file="/common/taglibs.jsp" %>

<head>
    <title><fmt:message key="login.title"/></title>
    <meta name="menu" content="Login"/>
</head>

<script type="text/javascript">
	function codeQRScannedAjaxPolling() {
		var TIME_2_SECONDS = 2000;
		var TIME_5_SECONDS = 5000;
		var TIME_30_SECONDS = 30000;
		
		$.ajax({
			tradional: true,
			type: 'POST',
			url: 'codeQRScannedAjaxAction',
			timeout: TIME_2_SECONDS,
			success: function(data) {
				if (data) {
					$('#divQRScannerLaser').addClass('loginQRScannerLaser');
					window.setTimeout(function() { window.location.href = '<s:url action="codeQRLoginAction"/>' }, TIME_5_SECONDS);
				} else {
					window.setTimeout(codeQRScannedAjaxPolling, TIME_2_SECONDS);
				}
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				window.setTimeout(codeQRScannedAjaxPolling, TIME_30_SECONDS);
			}
		});
	}
	
	$(document).ready(function() {
		codeQRScannedAjaxPolling();
	});
</script>

<body id="login">

	<form	id="loginForm" action="<c:url value='/j_security_check'/>" method="post"
	      	onsubmit="saveUsername(this);return validateForm(this)" 
	      	autocomplete="off">
		
		<div class="loginMainPanel">
			<div class="loginUserPwdPanel">
				<h2 class="loginUserPwdLabel">
			        <fmt:message key="login.heading"/>
			    </h2>
				<input type="text" name="j_username" id="j_username" class="input-block-level"
				       placeholder="<fmt:message key="label.username"/>" required tabindex="1">
				<div>
					<input type="password" class="input-block-level" name="j_password" id="j_password" tabindex="2"
					       placeholder="<fmt:message key="label.password"/>" style="position: relative; float: left; width: 75%;" required/>
					<button type="submit" class="btn btn-small btn-primary" name="login" tabindex="4" style="position: relative; float: right; width: 20%;">
					    <fmt:message key='button.login'/>
					</button>
					<div style="clear: both;"></div>
				</div>
				<c:if test="${appConfig['rememberMeEnabled']}">
					<label class="checkbox" for="rememberMe">
					    <input type="checkbox" class="checkbox" name="_spring_security_remember_me" id="rememberMe" tabindex="3"/>
						<fmt:message key="login.rememberMe"/>
				    </label>
				</c:if>
				<c:if test="${param.error != null}">
					<div class="alert alert-error fade in">
					    <fmt:message key="errors.password.mismatch"/>
					</div>
				</c:if>
			</div>
			<div class="loginSeparationPanel"></div>
			<div class="loginQRPanel">
				<h2 class="loginQRLabel">
			        <fmt:message key="login.qr.scan"/>
			    </h2>
			    <div id="divQRScannerLaser"></div>
				<img src="<s:url action='codeQRGenerationAction'/>"/>
			</div>
		</div>
		<div style="clear: both;"></div>
				
	</form>
	
</body>