<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib prefix="ams" uri="/WEB-INF/AMSTag.tld"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
</head>
<body>
	<form id="dataFilterGridMainPage" action="security.do">
		<ams:message messageEntity="${message}"></ams:message>
		<input type="hidden" name="groupID"
			value="<%=request.getParameter("groupID")%>"> <input
			type="hidden" name="reqCode" value="saveUpdateGroupRole">
		Roles for group "<span
			style="font-weight: bold; text-shadow: none; font-style: italic;"><bean:write
				name="groupENT" property="groupName" /></span>"
		<!-- 
		<div class="ui-grid-solo">
			<div class="ui-block-a">
				<input type="text" name="searchKey" placeholder="Search for a role">
			</div>
		</div>
		-->
		<div>
			<html:text property="groupName" name="groupENT"
				styleId="searchKeyInput" onkeyup="searchForRole()"
				title="Search for a role"></html:text>
		</div>
		<div class=ui-grid-a>
			<div class=ui-block-a>
				<a href="#" data-role="button" class="cancel-icon" data-mini="true"
					onclick="callAnAction('security.do?reqCode=groupEdit&groupID=<%=request.getParameter("groupID")%>');">Cancel</a>
			</div>
			<div class=ui-block-b>
				<a href="#" data-role="button" class="save-icon" data-mini="true"
					onclick="saveTheForm();">Save</a>
			</div>
		</div>
			<label>Select All <input type="checkbox"
				id="checkAllRoles" onclick="selectAllRoles()" data-inline="true"
				data-mini="true">
			</label>
		<table data-role="table" class="ui-responsive table-stroke">
			<thead>
				<tr>
					<th></th>
					<th></th>
					<th></th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<logic:iterate id="roleListIteration" indexId="rowId" name="roleLST"
					type="common.security.RoleENT">
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
						<td><label><input type="checkbox"
								value="<%=roleListIteration.getRoleName()%>"
								<logic:iterate id="groupRoleNames"
									name="groupENTRoles" type="common.security.RoleENT">
										<%if (roleListIteration.getRoleName().equalsIgnoreCase(
												groupRoleNames.getRoleName())) {%>
										checked="checked" <%}%>
								</logic:iterate>
								name="groupRoleName" data-mini="true" class="groupCheckBoxes"><%=roleListIteration.getRoleName()%></label></td>
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
		$('.groupCheckBoxes').prop('checked', $("#checkAllRoles").is(':checked')).checkboxradio(
				'refresh');
	}
	function searchForRole() {
		var str = "security.do?reqCode=groupRoleView&groupID="
				+
<%=request.getParameter("groupID")%>
	+ "&roleName=" + $('#searchKeyInput').val();
		callAnAction(str);

	}
</script>
</html>

