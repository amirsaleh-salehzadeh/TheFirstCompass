<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<title>Map Buddy | Nelson Mandela University</title>
<link rel="stylesheet"
	href="css/themes/default/jquery.mobile-1.4.5.min.css">
<link rel="stylesheet" href="css/jquery-mobile/jqm-demos.css">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="css/location/login.css">
<%if(request.getRemoteUser()!=null) {%>
<script type="text/javascript">
window.replace.location("t_location.do?reqCode=pathManagement");
</script>
<%} %>
</head>
<body class="login">
	<div id="loginPage" data-role="page">
		<div id="loginContents">
			<form name="loginForm" method="POST" action="j_security_check">
				<img id="logo" src="images/MandelaUniversity_logo_B.png">
					<input type="text" name="j_username" id="uNameEmail" value=""
						placeholder="Username/Email" />
					<input type="password" name="j_password" id="loginPass" value=""
						placeholder="Password" />
					<button id="loginBtn" class="ui-btn" type="submit">Login</button>
					<button id="loginBtn" class="ui-btn" type="button">Register</button>
<!-- 					<label>Haven't got an account yet? </label> <label>Register -->
<!-- 					<a href="t_location.do?reqCode=register">Here!</a> -->
<!-- 				</label> -->
			</form>
		</div>
	</div>
	<!-- /page -->
</body>
</html>