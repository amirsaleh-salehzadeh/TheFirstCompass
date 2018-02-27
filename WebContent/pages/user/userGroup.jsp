<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="javax.management.relation.RoleList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib prefix="ams" uri="/WEB-INF/AMSTag.tld"%>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html>
<body>
	<form id="dataFilterGridMainPage" action="user.do">
		<ams:message messageEntity="${message}"></ams:message>
		<input type="hidden" name="userName"
			value="<%=request.getParameter("userName")%>"> <input
			type="hidden" name="reqCode" value="userGroupsSave"> Groups
		for user "
		<bean:write name="userENT" property="userName" />
		"
		<div>
			<html:text property="groupName" name="groupENT"
				styleId="searchKeyInput" onkeyup="searchForGroup()"
				title="Search for a group"></html:text>
		</div>
		<label style="width: 100%">Select All <input type="checkbox"
			id="checkAllGroups" onclick="selectAllGroups()">
		</label> <a href="#" data-role="button" data-inline="true"
			onclick="callAnAction('user.do?reqCode=userEdit&userName=<%=request.getParameter("userName")%>')">Back</a>
		<a href="#" data-role="button" data-inline="true"
			onclick="saveTheForm()">Save</a>
		<table data-role="table" id="table-column-toggle"
			class="ui-responsive table-stroke">
			<tbody>
				<logic:iterate id="groupsListIteration" indexId="rowId"
					name="groupsList" type="common.security.GroupENT">

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
						<td><label><input type="checkbox" name="userGroupID"
								class="groupCheckBoxes"
								value="<%=groupsListIteration.getGroupID()%>"
								<logic:iterate id="userGroupIds"
									name="userGroups" type="common.security.GroupENT">
										<%if (groupsListIteration.getGroupID() == userGroupIds
							.getGroupID()) {%>
										checked="checked" <%}%> 
								</logic:iterate>
								data-inline="true"><%=groupsListIteration.getGroupName()%>
						</label></td>
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
<head>
<script type="text/javascript">
	$(document).ready(function() {
		refreshPlaceHolders();
	});
	function selectAllGroups() {

		$('.groupCheckBoxes').prop('checked',
				$("#checkAllGroups").is(':checked')).checkboxradio('refresh');

	}
	function searchForGroup() {
		var str = "user.do?reqCode=userGroupView&userName="
				+
<%=request.getParameter("userName")%>
	+ "&groupName="
				+ $('#searchKeyInput').val();
		callAnAction(str);

	}
</script>
</head>
</html>