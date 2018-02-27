<%@page import="java.util.List"%>
<%@page import="common.PopupENT"%>
<%@page import="java.util.ArrayList"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld"%>
<%@taglib prefix="ams" uri="/WEB-INF/AMSTag.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script>
	$(document).ready(function() {
		refreshPlaceHolders();
		refreshGrid();
	});
</script>
</head>
<body>
	<form id="dataFilterGridMainPage" action="security.do">
		<ams:message messageEntity="${message}"></ams:message>
		<input type="hidden" name="reqCode" id="reqCode" value="roleManagement">
		<input type="hidden" name="reqCodeGrid" id="reqCodeGrid" value="">
		<div class="ui-grid-solo" id="searchFilters">
					<html:text name="roleLST" property="searchRole.roleName"
						onkeyup="refreshGrid();" title="Role Name" ></html:text>
		</div>
		<div class="ui-grid-solo">
			<bean:define id="totalRows" name="roleLST" property="totalItems"
				type="java.lang.Integer"></bean:define>
			<bean:define id="first" name="roleLST" property="first"
				type="java.lang.Integer"></bean:define>
			<bean:define id="currentPage" name="roleLST" property="currentPage"
				type="java.lang.Integer"></bean:define>
			<bean:define id="pageSize" name="roleLST" property="pageSize"
				type="java.lang.Integer"></bean:define>
			<bean:define id="totalPages" name="roleLST" property="totalPages"
				type="java.lang.Integer"></bean:define>
			<ams:ajaxPaginate currentPage="<%=currentPage%>"
				pageSize="<%=pageSize%>" totalRows="<%=totalRows%>" align="center"
				columns="roleName,DT_RowId,roleCategory,comment"
				popupID="roleManagementSettingMenu"
				popupGridSettingItems="${gridMenuItem}"
				popupMenuSettingItems="${settingMenuItem}">
				<table id="gridList" class="mainGrid display cell-border dt-body-center"
					cellspacing="0" width="100%" data-mini="true">
					<thead>
						<tr>
							<th><input type="checkbox" id="checkAllHead"></th>
							<th data-priority="1">Role</th>
							<th data-priority="2">Category</th>
							<th data-priority="3">Comment</th>
						</tr>
					</thead>
					<tfoot>
						<tr>
							<th><input type="checkbox" id="checkAllFoot"></th>
							<th data-priority="1">Role</th>
							<th data-priority="2">Category</th>
							<th data-priority="3">Comment</th>
						</tr>
					</tfoot>
				</table>
			</ams:ajaxPaginate>
		</div>
	</form>
</body>
</html>