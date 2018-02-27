<%@page import="javax.management.relation.RoleList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib prefix="ams" uri="/WEB-INF/AMSTag.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<body>
	<form id="dataFilterGridMainPage" action="user.do">
		<ams:message messageEntity="${message}"></ams:message>
		<input type="hidden" name="userName"
			value="<%=request.getParameter("userName")%>"> <input
			type="hidden" name="reqCode" value="userRolesSave"> Roles for
		user "<bean:write name="userENT" property="userName" />"
		<div>
			<html:text property="roleName" name="roleENT"
				styleId="searchKeyInput" onkeyup="searchForRole()"
				title="Search for a role"></html:text>
		</div>
		<div class="ui-grid-solo">
			<label style="width: 100%">Select All <input type="checkbox"
				id="checkAllRoles" onclick="selectAllRoles()">
			</label>
		</div>
		<div class=ui-block-a>
			<a href="#" data-role="button" data-icon="delete"
				onclick="callAnAction('user.do?reqCode=userEdit&userName=<%=request.getParameter("userName")%>');">Cancel/Back</a>
		</div>
		<div class=ui-block-b>
			<a href="#" data-role="button" data-icon="check" data-theme="b"
				onclick="saveTheForm();">Save</a>
		</div>
		<table data-role="table" id="table-column-toggle"
			class="ui-responsive table-stroke">
			<tbody>
				<logic:iterate id="roleListIteration" indexId="rowId"
					name="rolesList" type="common.security.RoleENT">
					<%
						int counter;
							int idcount;
							counter = rowId % 3;
							if (counter == 0) {
					%>
					<tr>
						<%
							}
						%>
						<td><label><input type="checkbox" name="userRoleName"
								class="roleCheckBoxes"
								value="<%=roleListIteration.getRoleName()%>"
								<logic:iterate id="userRoleNames"
									name="userRoles" type="common.security.RoleENT">
										<%if (roleListIteration.getRoleName().equalsIgnoreCase(
							userRoleNames.getRoleName())) {%>
										checked="checked" <%}%>
								</logic:iterate>
								data-inline="true"> <%=roleListIteration.getRoleName()%></label>
						</td>
						<%
							if (counter == 2) {
						%>
					</tr>
					<%
						}
					%>

				</logic:iterate>
			</tbody>
		</table>
	</form>
</body>
<script type="text/javascript">
	$(document).ready(function() {
		refreshPlaceHolders();
	});
	function selectAllRoles() {
		$('.roleCheckBoxes')
				.prop('checked', $("#checkAllRoles").is(':checked'))
				.checkboxradio('refresh');

	}
	function searchForRole() {
		var str = "security.do?reqCode=userRoleView&userName="
				+
<%=request.getParameter("userName")%>
	+ "&roleName="
				+ $('#searchKeyInput').val();
		callAnAction(str);

	}
</script>
</html>