<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<title>Find It | Nelson Mandela University</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="css/location/login.css">
<script src="js/jquery.mobile-1.4.5.min.js"></script>
<style type="text/css">
button {
	width: 100%;
}
</style>
</head>
<body class="login">
	<div id="loginPage" data-role="page">
		<div id="loginContents">
			<form name="loginForm" method="POST" action="j_security_check">
				<img id="logo" src="images/MandelaUniversity_logo_B.png">
				<div class="ui-block-solo">
					<input type="text" name="j_username" id="uNameEmail" value=""
						placeholder="Username/Email" />
				</div>
				<div class="ui-block-solo">
					<input type="password" name="j_password" id="loginPass" value=""
						placeholder="Password" />
				</div>

				<div class="ui-block-solo">
					<button id="loginBtn" class="ui-btn" type="submit">Login</button>
				</div>
				<label style="color: red;">Invalid username or password </label>
			</form>
		</div>
	</div>
	<!-- /page -->
</body>
</html>