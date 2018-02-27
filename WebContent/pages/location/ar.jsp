<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
<script src="js/jquery.js"></script>
<script src="js/jquery.mobile-1.4.5.min.js"></script>
<link rel="stylesheet"
	href="css/themes/default/jquery.mobile-1.4.5.min.css">
<link rel="stylesheet"
	href="css/location/path.navigation.search.list.css">
<link rel="stylesheet" href="css/location/message.dialog.css">

<script type="text/javascript">
	$(document).ready(function() {
		// 			$("#camera").width = $(window).width;
		// 			$("#camera").height = $(window).height;
		getAllLocations();
	});
</script>
<style type="text/css">
#camera {
	position: fixed;
	right: 0;
	bottom: 0;
	left: 0;
	width: 100%;
	height: 100%;
	/* 	z-index: 1000; */
}

#marker {
	position: absolute;
	z-index: 11;
}

#searchLeftSideBTN {
	position: absolute;
	right: 3px;
	top: 3px;
}
</style>
<meta charset=utf-8>
<title>Augmented Reality Viewer</title>
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, user-scalable=no">
</head>
<body>
	<input type="button" class="zoomBTN" id="searchLeftSideBTN"
		onclick="searchIconClick();" data-theming="none">
	<video id="camera"></video>
	<input id="destinationId"
		value="<%=request.getParameter("destinationId")%>" type="hidden">
	<input id="aaa" type="text" style="position: absolute;">
	<img alt="" src="images/icons/map-markers/marker-blue.png" id="marker">
	<div data-role="popup" id="popupSearchResult" class="ui-content"
		data-position-to="window">
		<div class="ui-block-solo">
			<div class="ui-block-solo popupSearchResultCloseBTNContainer"
				id="destinationButtonGroup">
				<a data-role="button" href="#" id="popupSearchResultCloseBTN"
					onclick="$('#popupSearchResult').popup('close');;" class="closePopupMessage"><img
					src="images/icons/clearInput.png" alt=""
					class="closeMessageButtonIcon" />Close</a>
			</div>
		</div>
		<div class="ui-block-solo" id="searchFieldDiv">
			<input type="text" id="searchField" placeholder="Find a Place"
				data-role="none"> <span onclick="searchFieldDivClearBTN();"></span>
		</div>
		<div class="ui-block-solo" id="resultsListViewDiv">
			<div data-role="content">
				<div data-role="collapsible-set" id="resultsListViewContentDiv"
					data-input="#searchField" data-filter="true" data-inset="true"
					data-collapsed="false" data-icon='false'></div>
			</div>
		</div>
	</div>
	<script src="js/location/camera/ar/requestanimationframe.js"></script>
	<script src="js/location/camera/ar/ar.js"></script>
	<script src="js/location/camera/ar/script.js"></script>
	<script type="text/javascript"
	src="js/location/camera/ar.search.panel.js"></script>
</body>
</html>