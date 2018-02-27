<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib prefix="ams" uri="/WEB-INF/AMSTag.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
	function completeMe() {
		var $ul = $("#autocomplete"), $input = $("#roleCategory"), value = $input.val(), html = "";
		$ul.fadeIn();
		$ul.html("");
		$ul
				.html("<li><div class='ui-loader'><span class='ui-icon ui-icon-loading'></span></div></li>");
		$ul.css("width", $input.css("width"));
		$ul.listview("refresh");
		$.ajax({
			url : "REST/GetSecurityWS/GetAllRoleCategories?filterTxt=" + value,
			data : {
				q : $input.val()
			}
		}).then(function(response) {
			$.each(response, function(i, val) {
				html += "<li onclick=\"addValue('" + val + "');\" >" + val + "</li>";
			});
			$ul.html(html);
			$ul.listview("refresh");
			$ul.trigger("updatelayout");
		});
	}
	function addValue(x) {
		$("#roleCategory").val(x);
		$("#autocomplete").fadeOut();
	}
</script>
</head>
<body>
<div id='formContainer'>
	<ams:message messageEntity="${message}"></ams:message>
	<form id="dataFilterGridMainPage" action="security.do"
		autocomplete="off">
		<input type="hidden" name="reqCode" value="saveUpdateRole">
		<div class="ui-block-solo">
			<html:text name="roleENT" property="roleName" title="Role Name" />
			<html:hidden name="roleENT" property="roleName" styleId="roleName" />
		</div>
		<div class="ui-block-solo">
			<html:text name="roleENT" property="roleCategory"
				styleId="roleCategory" title="Category" onkeyup="completeMe()" />
			<ul id="autocomplete" data-role="listview" data-inset="true"
				data-filter="true" class="autocomplete-list"
				data-input="#roleCategory"></ul>
		</div>
		<div class="ui-block-solo">
			<html:textarea name="roleENT" property="comment" styleId="comment"
				title="Comment" />
		</div>
		<div class=ui-grid-a>
			<div class=ui-block-a>
				<a href="#" data-role="button" data-mini="true"
					class="cancel-icon"
					onclick="callAnAction('security.do?reqCode=roleManagement');">Cancel</a>
			</div>
			<div class=ui-block-b>
				<a href="#" data-role="button" class="save-icon"
					data-mini="true" onclick="saveTheForm();">Save</a>
			</div>
		</div>
	</form>
	</div>
</body>

</html>