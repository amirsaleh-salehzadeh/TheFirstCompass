<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="javax.management.relation.RoleList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib prefix="ams" uri="/WEB-INF/AMSTag.tld"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html>
<script type="text/javascript">
	$(document).ready(function() {
		refreshPlaceHolders();
	});
	function changeUsernamePasswordValidation() {
		saveTheForm();
	}
</script>
<body>
	<div id='formContainer'>
		<form id="dataFilterGridMainPage" action="user.do">
			<input type="hidden" name="reqCode" value="saveNewPassword">
			<ams:message messageEntity="${message}"></ams:message>
			<input type="hidden" name="userName"
				value="<%=request.getParameter("userName")%>">

			<bean:write name="userENT" property="userName"/>
			<input type="password" name="oldPass" placeholder="Old Password" />
			<input type="password" name="newPW" id="newPW" value=""
				placeholder="New Password"> <input type="password"
				name="newPWCheck" id="newPWCheck" value=""
				placeholder="Re-enter New Password">
			<div class=ui-grid-a>
				<div class=ui-block-a>
					<a data-role="button" class="cancel-icon" data-mini="true"
						href="t_user.do?reqCode=userManagement" data-ajax="false">Cancel</a>
				</div>
				<div class=ui-block-b>
					<a href="#" data-role="button" class="save-icon" data-mini="true"
						onclick="changeUsernamePasswordValidation();
					">Save</a>
				</div>
			</div>
		</form>
	</div>
</body>
</html>