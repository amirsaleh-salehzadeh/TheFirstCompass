<%@page import="hibernate.security.SecurityDAO"%>
<html>
<style type="text/css">
body {
	-moz-user-select: none;
	-khtml-user-select: none;
	-webkit-user-select: none;
	user-select: none;
	-ms-user-select: none
}
</style>
<ul class="jqm-list" data-role="listview">

	<li class="userMenu"
		data-filtertext="form checkboxradio widget radio input radio buttons controlgroups">
		<a id="btnHeading" href="../"
		data-ajax="false">Home</a>
	</li>
	<%		String uname = request.getRemoteUser();
	        if (SecurityDAO.isUserAuthorised(uname, "SuperAdmin")) {
	%>
	<li id="liMenu" data-role="collapsible" data-enhanced="true"
		data-collapsed-icon="carat-d" data-expanded-icon="carat-u"
		data-iconpos="right" data-inset="false"
		class=" ui-collapsible ui-collapsible-collapsed">
		<h3 class="ui-collapsible-heading ui-collapsible-heading-collapsed">
			<a id="btnHeading" href="#"
				class="ui-collapsible-heading-toggle ui-btn ui-btn-icon-right ui-btn-inherit ui-icon-carat-d">
				System Administration<span class="ui-collapsible-heading-status">
					click to expand contents</span>
			</a>
		</h3>
		<div id="divMenu"
			class=" ui-collapsible-content ui-body-inherit ui-collapsible-content-collapsed"
			aria-hidden="true">
			<ul id="ulMenu">
				<li id="liMenu" class="userMenu"
					data-filtertext="form checkboxradio widget radio input radio buttons controlgroups"><a
					id="btnMenu" class="userMenu ui-btn"
					href="t_user.do?reqCode=userManagement" data-ajax="false">User</a></li>
				<%
					if (SecurityDAO.isUserAuthorised(uname, "RoleManagement")) {
				%>
				<li id="liMenu"
					data-filtertext="form checkboxradio widget checkbox input checkboxes controlgroups"><a
					id="btnMenu" class="userMenu ui-btn"
					href="t_security.do?reqCode=roleManagement" data-ajax="false">Roles</a></li>
				<%
					}
						if (SecurityDAO.isUserAuthorised(uname, "GroupManagement")) {
				%>
				<li id="liMenu"
					data-filtertext="form checkboxradio widget radio input radio buttons controlgroups"><a
					id="btnMenu" class="userMenu ui-btn"
					href="t_security.do?reqCode=groupManagement" data-ajax="false">Groups</a></li>
				<%
					}
				%>
			</ul>
		</div>
	</li>
	<%
		}
		if (SecurityDAO.isUserAuthorised(uname, "SuperAdmin")
				|| SecurityDAO.isUserAuthorised(uname, "ClientSuperAdmin")) {
	%>
	<li class="userMenu"
		data-filtertext="form checkboxradio widget radio input radio buttons controlgroups">
		<a id="btnHeading" href="t_location.do?reqCode=pathManagement"
		data-ajax="false">Location/Path Management</a>
	</li>
	<%
		}
	%>
	<li id="liMenu" data-role="collapsible" data-enhanced="true"
		data-collapsed-icon="carat-d" data-expanded-icon="carat-u"
		data-iconpos="right" data-inset="false"
		class="userMenu ui-collapsible ui-collapsible-themed-content ui-collapsible-collapsed">
		<h3 class="ui-collapsible-heading ui-collapsible-heading-collapsed">
			<a id="btnHeading" href="#"
				class="ui-collapsible-heading-toggle ui-btn ui-btn-icon-right ui-btn-inherit ui-icon-carat-d">
				Personal<span class="ui-collapsible-heading-status"> click to
					expand contents</span>
			</a>
		</h3>
		<div id="divMenu"
			class="userMenu ui-collapsible-content ui-body-inherit ui-collapsible-content-collapsed"
			aria-hidden="true">
			<ul id="ulMenu">
				<li id="liMenu"
					data-filtertext="form checkboxradio widget radio input radio buttons controlgroups"><a
					id="btnMenu" class="userMenu ui-btn"
					href="t_user.do?reqCode=userManagement" data-ajax="false">Profile</a></li>
				<li id="liMenu"
					data-filtertext="form checkboxradio widget checkbox input checkboxes controlgroups"><a
					id="btnMenu" class="userMenu ui-btn"
					href="t_security.do?reqCode=roleManagement" data-ajax="false">Privacy</a></li>
				<li id="liMenu"
					data-filtertext="form checkboxradio widget radio input radio buttons controlgroups"><a
					id="btnMenu" class="userMenu ui-btn"
					href="t_security.do?reqCode=groupManagement" data-ajax="false">Notification</a></li>
			</ul>
		</div>
	</li>
	<li class="userMenu"
		data-filtertext="form checkboxradio widget radio input radio buttons controlgroups">
		<a id="btnHeading" href="logOut.do" data-ajax="false">Logout</a>
	</li>

</ul>
<div data-role="popup" id="popupDialogDeleteConfirmation"
	data-overlay-theme="b" data-theme="a" data-dismissible="false"
	style="max-width: 400px;" class="ui-corner-all">
	<div data-role="header" data-theme="b" class="ui-corner-top">
		<h1>Delete Item ?</h1>
	</div>
	<div data-role="content" data-theme="d"
		class="ui-corner-bottom ui-content">
		<h3 class="ui-title">Are you sure you want to delete this Item ?</h3>
		<p>This action cannot be undone.</p>
		<a href="#" data-role="button" data-inline="true" data-rel="back"
			data-theme="c">Cancel</a> <a href="#" data-role="button"
			data-inline="true" data-rel="back" data-transition="flow"
			data-theme="b" rel="external" onclick="deleteConfirmed();">Delete</a>
	</div>
</div>
</html>