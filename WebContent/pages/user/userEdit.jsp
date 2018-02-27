<%@page import="java.io.PrintWriter"%>
<%@page import="common.user.UserENT"%>
<%@page import="java.util.List"%>
<%@page import="common.PopupENT"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib prefix="ams" uri="/WEB-INF/AMSTag.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
	$(document).ready(function() {
		refreshPlaceHolders();
		$("#dob").attr("type", "date").trigger("create");
	});
</script>
<style type="text/css">
.flipswitchGender.ui-flipswitch .ui-btn.ui-flipswitch-on {
	text-indent: -3.9em;
	color: rgb(248, 182, 36) !important;
	text-shadow: none !important;
	    font-family: sans-serif;
}

.flipswitchGender.ui-flipswitch .ui-flipswitch-off {
	text-indent: 0.5em;
	color: rgb(248, 182, 36) !important;
	text-shadow: none !important;
	    font-family: sans-serif;
}

.flipswitchGender.ui-flipswitch {
	background-color: #FF0080;
	color: rgb(248, 182, 36) !important;
	width: 6.875em;
	text-indent: 2em;
	text-shadow: none !important;
}

.flipswitchGender.ui-flipswitch.ui-flipswitch-active {
	background-color: #2E64FE;
	color: rgb(248, 182, 36) !important;
	padding-left: 5em;
	width: 1.875em;
	text-indent: 2em;
	text-shadow: none !important;
}

.flipswitchActive.ui-flipswitch .ui-btn.ui-flipswitch-on {
	text-indent: -3.9em;
	text-shadow: none !important;
}

.flipswitchActive.ui-flipswitch .ui-flipswitch-off {
	text-indent: 0.5em;
	text-shadow: none !important;
}

.flipswitchActive.ui-flipswitch {
	background-color: #8A0808;
	color:rgb(248, 182, 36) !important;
	width: 6.875em;
	font-weight: normal;
	text-indent: 2em;
}

.flipswitchActive.ui-flipswitch.ui-flipswitch-active {
	background-color:#3eb249;
	color: rgb(248, 182, 36) !important;
	padding-left: 5em;
	width: 1.875em;
	text-indent: 2em;
}
</style>
</head>
<body>
	<ams:message messageEntity="${message}"></ams:message>
	<div id='formContainer'>
		<form id="dataFilterGridMainPage" action="user.do">
			<input type="hidden" name="reqCode" value="userSaveUpdate">
			<html:hidden name="userENT" property="userName" styleId="userName" />
			
			<bean:define id="genderValue" name="userENT" property="gender"
				type="java.lang.Boolean" />
			<bean:define id="activeValue" name="userENT" property="active"
				type="java.lang.Boolean" />
			<bean:define id="selectedValueClient" name="userENT"
				property="clientID" type="java.lang.Integer"></bean:define>
			<bean:define id="selectedValueEthnic" name="userENT"
 				property="ethnicID" type="java.lang.Integer"></bean:define> 
			<bean:define id="selectedValueTitle" name="userENT" 
				property="titleID" type="java.lang.Integer"></bean:define> 

			<%
				if ((request.getParameter("reqCode")).equals("userView")) {
			%>

			<div class="ui-block-solo">
				Username:
				<bean:write name="userENT" property="userName" />
			</div>
			<div class="ui-block-solo">
				<label>Client: <bean:write name="userENT"
						property="clientID" />
				</label>
			</div>
			<div class="ui-block-solo" style="display: none;">
				<label>Title: <bean:write name="userENT" property="title.title" />
				</label>
			</div>
			<div class="ui-block-solo">
				<label>Name: <bean:write name="userENT" property="name" />
				</label>
			</div>
			<div class="ui-block-solo">
				<label>Surname: <bean:write name="userENT"
						property="surName" />
				</label>
			</div>
			<div class="ui-block-solo">
				<label>Date of Birth: <bean:write name="userENT"
						property="dateOfBirth" />
				</label>
			</div>
			<div class="ui-block-solo">
				<label>Gender: <%
					if (genderValue) {
				%> Male <%
					} else {
				%> Female <%
					}
						;
				%>
				</label>
			</div>
			<div class="ui-block-solo" style="display: none;">
				<label>Ethnicity: <bean:write name="userENT"
						property="ethnic" /> 
				</label>
			</div>
			<div class="ui-block-solo">
				<label>Registration Date: <bean:write name="userENT"
						property="registerationDate" />
				</label>
			</div>
			<div class="ui-block-solo">
				<label>Status: <%
					if (activeValue) {
				%> Active <%
					} else {
				%> Inactive <%
					}
						;
				%>
				</label>
			</div>
			<fieldset class="ui-grid-a">
				<div class="ui-block-a">
					<a href="#" data-role="button" data-mini="true" class="cancel-icon"
						onclick="callAnAction('user.do?reqCode=userManagement');">Cancel</a>
				</div>
				<div class="ui-block-b">
					<a href="#" data-role="button" data-mini="true" class="edit-icon"
						onclick="callAnAction('user.do?reqCode=userEdit&userName=<bean:write name="userENT" property="userName" />');">Edit
						User</a>
				</div>
			</fieldset>
			<%
				} else if ((request.getParameter("reqCode")).equals("userEdit")) {
			%>

			<div class="ui-block-solo">
				<label>Username: <html:text name="userENT"
						property="userName" title="Username" disabled="true" />
				</label>
			</div>
			<div class="ui-block-solo">
				<label>Client: <ams:dropDown dropDownItems="${clientENTs}"
						selectedVal="<%=selectedValueClient.toString()%>" name="clientID"
						title=""></ams:dropDown>
				</label>
			</div>
			<div class="ui-block-solo">
				<label>Name: <html:text name="userENT" property="name"
						title="Name" />
				</label>
			</div>
			<div class="ui-block-solo">
				<label>Surname: <html:text name="userENT" property="surName"
						title="Surname" />
				</label>
			</div>
			<div class="ui-block-solo">
				<label>Date of Birth: <html:text name="userENT"
						property="dateOfBirth" title="Date of Birth" styleId="dob" />
				</label>
			</div>
			<div class="ui-block-solo">
				<label>Gender: <input type="checkbox" data-role="flipswitch"
					name="gender" id="flipswitchGender" data-on-text="Male"
					data-off-text="Female" data-wrapper-class="flipswitchGender"
					data-inline="true" <%if (genderValue) {%> checked="" <%}
				;%> />
				</label>
			</div>
			<div class="ui-block-solo">
				<label>Registration Date: <bean:write name="userENT"
						property="registerationDate" /></label>
			</div>
			<div class="ui-block-solo">
				<label>Status: <input type="checkbox" data-role="flipswitch"
					data-inline="true" name="active" id="flipswitchActivation"
					selected="selected" data-on-text="Active" data-off-text="Inactive"
					data-wrapper-class="flipswitchActive" <%if (activeValue) {%>
					checked="" <%}
				;%> />
				</label>
			</div>
			<div class=ui-grid-c>
				<div class=ui-block-a>
					<a href="#" data-role="button" data-mini="true" class="cancel-icon"
						onclick="callAnAction('user.do?reqCode=userManagement');">Cancel</a>
				</div>
				<div class=ui-block-b>
					<a href="#" data-role="button" data-mini="true" class="role-icon"
						onclick="callAnAction('user.do?reqCode=userRoleView&userName=<bean:write name="userENT" property="userName"
					/>')">Roles</a>
				</div>
				<div class=ui-block-c>
					<a href="#" data-role="button" data-mini="true" class="group-icon"
						onclick="callAnAction('user.do?reqCode=userGroupView&userName=<bean:write name="userENT" property="userName"
					/>')">Groups</a>
				</div>
				<div class=ui-block-d>
					<a href="#" data-role="button" data-mini="true" class="save-icon"
						onclick="saveTheForm();">Save</a>
				</div>
			</div>
			<%
				} else {
			%>			
			<html:text name="userENT" property="userName" title="Username" />
			<ams:dropDown dropDownItems="${clientENTs}"
				selectedVal="<%=selectedValueClient.toString()%>" name="clientID"
				title=""></ams:dropDown>
			<html:text name="userENT" property="name" title="Name" />
			<html:text name="userENT" property="surName" title="Surname" />
			<label for="dateOfBirth"
				style="white-space: nowrap !important; width: 100%">Date of
				Birth: </label>
			<html:text name="userENT" property="dateOfBirth"
				title="Date of Birth" styleId="dob" />
			<div>
				<input type="checkbox" data-role="flipswitch" name="gender"
					id="flipswitchGender" data-on-text="Male" data-off-text="Female"
					data-wrapper-class="flipswitchGender" data-inline="true"
					<%if (genderValue.booleanValue() == true) {%> checked=""
					<%}
				;%> />
			</div>
			<div>
				<input type="checkbox" data-role="flipswitch" data-inline="true"
					name="active" id="flipswitchActivation" selected="selected"
					data-on-text="Active" data-off-text="Inactive"
					data-wrapper-class="flipswitchActive"
					<%if (activeValue.booleanValue() == true) {%> checked=""
					<%}
				;%> />
			</div>
			<html:text name="userENT" property="password" title="Password" />
			<div class=ui-grid-a>
				<div class=ui-block-a>
					<a href="#" data-role="button" data-mini="true" class="cancel-icon"
						onclick="callAnAction('user.do?reqCode=userManagement');">Cancel</a>
				</div>
				<div class=ui-block-b>
					<a href="#" data-role="button" data-mini="true" class="save-icon"
						onclick="saveTheForm();">Save</a>
				</div>
			</div>
			<%
				}
				;
			%>
		</form>
	</div>
</body>

</html>