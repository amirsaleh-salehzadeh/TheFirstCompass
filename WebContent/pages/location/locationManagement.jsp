<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib prefix="logic" uri="/WEB-INF/struts-logic.tld"%>
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
	<form id="dataFilterGridMainPage" action="location.do">
		<ams:message messageEntity="${message}"></ams:message>
		<input type="hidden" name="reqCode" id="reqCode" value="locationManagement">
		<input type="hidden" name="reqCodeGrid" id="reqCodeGrid" value="">
		<div class="ui-grid-a" id="searchFilters"> 
			<fieldset class="ui-grid-a">
				<div class="ui-block-a">
					<html:text name="locationLST" property="searchLocation.locationName"
						onkeyup="refreshGrid();" title="Location Name"></html:text>
				</div>
				<div class="ui-block-b">
					<html:text name="locationENT" property="searchLocation.locationType"
						onkeyup="refreshGrid();" title="Location Type"></html:text>
				</div>
				<div class="ui-block-c">
					<bean:define id="locationTypeIdSelectedVal" name="locationENT" property="searchLocation.locationTypeId"></bean:define>
					<ams:dropDown dropDownItems="${clientENTs}" name="locationTypeId"
						selectedVal="<%=locationTypeIdSelectedVal.toString() %>" onChange="refreshGrid()" title="Location Type"></ams:dropDown>
				</div>
			</fieldset>
		</div>
		<div class="ui-grid-solo">
			<bean:define id="totalRows" name="locationLST" property="totalItems"
				type="java.lang.Integer"></bean:define>
			<bean:define id="first" name="locationLST" property="first"
				type="java.lang.Integer"></bean:define>
			<bean:define id="currentPage" name="locationLST" property="currentPage"
				type="java.lang.Integer"></bean:define>
			<bean:define id="pageSize" name="locationLST" property="pageSize"
				type="java.lang.Integer"></bean:define>
			<bean:define id="totalPages" name="locationLST" property="totalPages"
				type="java.lang.Integer"></bean:define>
			<ams:ajaxPaginate currentPage="<%=currentPage%>"
				pageSize="<%=pageSize%>" totalRows="<%=totalRows%>" align="center"
				columns="locationID,locationname,address,gps,username"
				popupID="locationManagementSettingMenu"
				popupGridSettingItems="${gridMenuItem}"
				popupMenuSettingItems="${settingMenuItem}">
				<table id="gridList" class="display cell-border dt-body-center"
					cellspacing="0" width="100%">
					<thead>
						<tr>
							<th><input type="checkbox" id="checkAllHead"></th>
							<th data-priority="1">Location</th>
							<th data-priority="2">Address</th>
							<th data-priority="3">GPS</th>
							<th data-priority="4">Username</th>
						</tr>
					</thead>
					<tfoot>
						<tr>
							<th><input type="checkbox" id="checkAllFoot"></th>
							<th data-priority="1">Location</th>
							<th data-priority="2">Address</th>
							<th data-priority="3">GPS</th>
							<th data-priority="4">Username</th>
						</tr>
					</tfoot>
				</table>
			</ams:ajaxPaginate>
		</div>
	</form>
</body>
</html>