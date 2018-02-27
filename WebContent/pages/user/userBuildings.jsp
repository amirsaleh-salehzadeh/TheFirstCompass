<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib prefix="ams" uri="/WEB-INF/AMSTag.tld"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet"
	href="TreeViewPlugin/trunk/src/jquery.treefilter.css">
</head>
<body>
	<form id="dataFilterGridMainPage" action="user.do">
		<ams:message messageEntity="${message}"></ams:message>
		<input type="hidden" name="groupID"
			value="<%=request.getParameter("groupID")%>"> <input
			type="hidden" name="reqCode" value="saveUpdateGroupRole">
		Locations allowed for group "<span
			style="font-weight: bold; text-shadow: none; font-style: italic;"><bean:write
				name="groupENT" property="groupName" /></span>"
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
		<input type="search" id="my-search"
			placeholder="Search for a Building">

		<!-- insert heirachy here -->


		<div id="allLocations" class="ui-block-a">
			<ul id="my-tree">
				<li style="padding: 2px 4px 4px 4px">Nelson Mandela University
					<ul id="loc360">
					</ul>
				</li>
			</ul>
		</div>
		<script src="TreeViewPlugin/trunk/src/jquery.treefilter.js"></script>
		<script type="text/javascript"
			src="js/location/management/location.access.js"></script>
		<script type="text/javascript">
			$(function() {
				getAllLocationsTree(360);
				$("#my-tree").trigger("create");
			});
		</script>
		<!-- end of heirachy -->
	</form>
</body>

</html>

