<%@page import="common.location.LocationTypeENT"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld"%>
<%@taglib prefix="ams" uri="/WEB-INF/AMSTag.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Control Panel</title>
<script type="text/javascript">
	var showLabelMouseEvent = function() {
		var isUnderNeath = false;
		if (event.pageY < $(window).height() - $("#map_canvas").height())
			isUnderNeath = true;
		var txt = "";
		if ($(this).attr("alt") != undefined && $(this).attr("alt").length >= 1)
			txt = $(this).attr("alt");
		else
			txt = $(this).attr("title");
		showMarkerLabel(txt, event.pageX, event.pageY, isUnderNeath);
	};
	var showLabelMouseMove = function() {
		$(this).on('mousemove', showLabelMouseEvent);
		$(this).on('mouseout', function() {
			clearMarkerLabel();
		});
	};
	$(document).ready(
			function() {
				// 						$("#editBoundaryPopup").css("display", "none");
				$("#pathInfoFooter").css("display", "none");
				$("#map_canvas").css("width",
						parseInt($("#mainBodyContents").css("width")));
				$("#map_canvas").css(
						"height",
						parseInt($(window).height())
								- parseInt($(".jqm-header").height())
								- parseInt($("#locPathModeRadiobtn").height())
								- 6);
				$("#actionBar").css("height",
						parseInt($("#locPathModeRadiobtn").height()) - 6);
				$("#map_canvas").trigger("create");
				$(".showLabelMouseOverTrue").each(showLabelMouseMove);
			});
	$(window).bind('load', function() {
		// 		hideLocationInfo();
		getAllLocationTypes();
		setPathTypeButtonIcon();
		getPathTypePanel();
		getLocationTypeDropDown(null);
		getAllMarkers("<%=request.getAttribute("userLocationIds")%>", true);
		$("#parentLocationId").val("<%=request.getAttribute("parentLocationId")%>");
		// 		mapSattelView();
		// 		popErrorMessage("heeeey");
	});
	$(document).on("mobileinit", function() {
		$.mobile.defaultPageTransition = 'slide';
		$.mobile.page.prototype.options.domCache = true;
	});
</script>


<link href="css/location/colorpicker.css" rel="stylesheet">
<link href="css/location/croppie.css" rel="stylesheet">
<link href="css/location/management/management.css" rel="stylesheet">
<link href="css/location/management/location.edit.css" rel="stylesheet">
<link href="css/location/management/path.edit.css" rel="stylesheet">
<link href="css/location/management/polygon.management.css"
	rel="stylesheet">
<link href="css/location/management/toolbox.management.css"
	rel="stylesheet">
<style type="text/css">
#map_canvas div[style*='crosshair'] {
	cursor: url("images/icons/map-markers/mouse-cursors/pin.png") 32 32,
		crosshair !important;
}
</style>
</head>



<ams:message messageEntity="${message}"></ams:message>



<!-- TOP PANEL ICONS TOP PANEL ICONS TOP PANEL ICONS TOP PANEL ICONS TOP PANEL ICONS TOP PANEL ICONS TOP PANEL ICONS TOP PANEL ICONS TOP PANEL ICONS  -->


<div class="ui-block-solo" id="mapSatelViewIcon"
	style="width: inherit; padding: 3px; margin: 2px;"
	onclick="mapSattelView();">
	<img alt="Satellite View" src="images/icons/satellite.png" width="48"
		height="48" id="mapSatelViewImage" class="showLabelMouseOverTrue" />
</div>

<a href="#" data-mini="true" data-role="button" title="New"
	class="showLabelMouseOverTrue" id="createNewButton"
	onclick="createNew(0);"><img width='27' height='27'
	src='images/icons/add.png'>NEW</a>

<a href="#" data-mini="true" data-role="button"
	title="Search for a Place" class="showLabelMouseOverTrue"
	id="searchButton" onclick="openSearchPanel();"><img width='27'
	height='27' src='images/icons/search.png'>SEARCH</a>


<!-- SEARCH PANEL SEARCH PANEL SEARCH PANEL SEARCH PANEL SEARCH PANEL SEARCH PANEL SEARCH PANEL SEARCH PANEL SEARCH PANEL SEARCH PANEL SEARCH PANEL -->


<div data-role="panel" id="locationSearchPanel" data-position="right"
	data-display="overlay" data-swipe-close="false"
	data-dismissible="false"
	class="ui-panel ui-panel-position-right ui-panel-display-overlay ui-body-inherit ui-panel-animate ui-panel-open"
	data-theme="none">
	<div class="ui-block-solo" id="searchFieldDiv">
		<input type="text" id="searchField" placeholder="Find a Location"
			data-role="none"> <span onclick="searchFieldDivClearBTN();"></span>
	</div>
	<div class="ui-block-solo">
		<a data-role="button" href="#" data-rel="close"
			class="pathMenu ui-btn ui-shadow cancel-icon">Cancel</a>
	</div>
	<div class="ui-block-solo" id="resultsListViewDiv">
		<div data-role="content">
			<div data-role="collapsible-set" id="resultsListViewContentDiv"
				data-input="#searchField" data-filter="true" data-collapsed="false">
			</div>
		</div>
	</div>
</div>


<!-- PATH/LOCATION SELECT PATH/LOCATION SELECT PATH/LOCATION  -->


<fieldset data-role="controlgroup" data-mini="true"
	data-type="horizontal" name="optionType" id="locPathModeRadiobtn">
	<div id="settingTabLabel" align="center">MANAGEMENT</div>
	<!-- 	LOCATION -->
	<label for="marker" id="ui-icon-map-marker"><span
		class="inlineIcon showLabelMouseOverTrue"
		id="modeSelection_locationText" title="Manage the Places">CAMPUS</span></label>
	<input type="radio" name="radio-choice" id="marker" value="marker"
		checked="checked" onclick="selectActionType();" alt="View and Manage">
	<!-- 		PATH -->
	<label for="path" id="ui-icon-map-path"><span
		class="ui-alt-icon inlineIcon showLabelMouseOverTrue"
		title="Manage the Paths">PATH</span></label> <input type="radio"
		name="radio-choice" id="path" value="path"
		onclick="selectActionType();">
</fieldset>


<!-- LOCATION INFO LOCATION INFO LOCATION INFO LOCATION INFO LOCATION INFO  -->


<div id="infoDiv" class="ui-block-solo">
	<ul data-role="listview" id="infoListView">
	</ul>
	<!-- 		<label for="locationInfo">Location </label> -->
	<div class="ui-block-solo" style="margin: 0 !important;">
		<label id="locationDescriptionLabel" for="locationInfo"></label>
		<div id="locationInfo"></div>
	</div>
</div>


<!-- ACTION BAR ACTION BAR ACTION BAR ACTION BAR ACTION BAR ACTION BAR ACTION BAR ACTION BAR ACTION BAR ACTION BAR -->



<div id="actionBar" class="ui-grid-b">
	<div id="actionBarTitle" class="ui-block-a">CREATE NEW</div>
	<div id="actionBarMessage" class="ui-block-b"></div>
	<div class="ui-block-c ui-grid-c" id="actionBarButtonGroup">
		<div class="ui-block-a" id="actionBarBackButtonDiv">
			<a style="cursor: pointer;" data-role="button" href="#"
				id="actionBarBackButton" class="ui-btn back-icon" onclick="">Previous</a>
		</div>
		<div class="ui-block-b" id="actionBarNextButtonDiv">
			<a style="cursor: pointer;" data-role="button" href="#"
				id="actionBarNextButton" class="ui-btn next-icon" onclick="">Next</a>
		</div>
		<div class="ui-block-c" id="actionBarSaveButtonDiv">
			<a style="cursor: pointer;" data-role="button" href="#"
				id="actionBarSaveButton" class="ui-btn save-icon" onclick="">Save</a>
		</div>
		<div class="ui-block-d" id="actionBarCancelButtonDiv">
			<a style="cursor: pointer;" data-role="button" href="#"
				id="actionBarCancelButton" class="ui-btn cancel-icon"
				onclick="cancelCreatingNew();">Cancel</a>
		</div>
	</div>
	<div class="ui-block-b">
		<div class="meter">
			<span style="width: 0;"><span></span></span>
		</div>
	</div>
</div>

<div id="actionBarLabel"></div>



<!-- POPUP CONFIRMATION POPUP CONFIRMATION POPUP CONFIRMATION POPUP CONFIRMATION POPUP CONFIRMATION POPUP CONFIRMATION POPUP CONFIRMATION -->



<div data-role="popup" id="popupConfirmation" data-overlay-theme="b"
	data-theme="a" data-dismissible="false" style="max-width: 400px;"
	class="ui-corner-all">
	<div id="popupConfirmation_header" data-role="header" data-theme="b"
		class="ui-corner-top">
		<h1>Confirm Action ?</h1>
	</div>
	<div id="popupConfirmation_content" data-role="content" data-theme="d"
		class="ui-corner-bottom ui-content">
		<div id="confirmationMessage" class="ui-title"></div>
		<div id="confirmationWarning">WARNING, please make a careful decision.</div>
		<div class="ui-grid-a">
			<div class="ui-block-b">
				<a id="popupConfirmation_confirmBTN" href="#" data-role="button"
					data-inline="true" data-transition="flow"
					data-theme="b" onclick="">Confirm</a>
			</div>
			<div class="ui-block-b">
				<a id="popupConfirmation_cancelBTN" href="#" data-role="button"
					data-inline="true" data-rel="back" data-theme="b" onclick="">Cancel</a>
			</div>
		</div>
	</div>
</div>



<!-- HIDDEN INPUTS HIDDEN INPUTS HIDDEN INPUTS HIDDEN INPUTS HIDDEN INPUTS HIDDEN INPUTS HIDDEN INPUTS HIDDEN INPUTS HIDDEN INPUTS HIDDEN INPUTS  -->


<input type="hidden" readonly id="username" value="<%=request.getRemoteUser()%>">
<input type="hidden" readonly id="parentLocationId" value="">
<input type='hidden' readonly class="locationFields" id='locationTypeId'
	value="0">
<input type='hidden' readonly id='userLocationIds'
	value="<%=request.getAttribute("userLocationIds")%>">

<input type='hidden' readonly id='parentLocationTypeId' value="0">
<input type='hidden' readonly class="locationFields" name="locationGPS"
	id="locationGPS">
<input type='hidden' readonly class="locationFields" name="locationId"
	id="locationId">
<input type='hidden' readonly class="locationFields" name="icon"
	id="icon" value="">
<input type='hidden' readonly class="locationFields" name="boundary"
	id="boundary" value="">
<input type='hidden' readonly name="boundaryColors" id="boundaryColors">
<input type='hidden' readonly name="tempBoundaryColors"
	id="tempBoundaryColors">
<input type='hidden' readonly name="isEntranceIntersection"
	id="isEntranceIntersection">



<!-- LOCATION EDIT MENU LOCATION EDIT MENU LOCATION EDIT MENU LOCATION EDIT MENU -->



<div id="locationEditMenu" >
	<ul data-role="listview" style="min-width: 210px;">
<!-- 		<li class="unselectable" data-role="list-divider" -->
<!-- 			id="locationEditMenuTitle">Choose an action</li> -->
		<li data-icon="false"><a class="editInfo open" href="#"
			onclick="openALocation();"><img alt=""
				src="images/icons/open.png">Open</a></li>
		<li data-role="list-divider"></li>

		<li data-icon="false"><a class="editInfo location" href="#"
			onclick="openLocationTypePopup();"><img alt=""
				src="images/icons/location.png">Set Location Type</a></li>
				
	<li data-role="collapsible" id="editBoundaryMenuItem" data-iconpos="right">
		<h2 ><div class="imageHolder"><img
				alt="" src="images/icons/polygon.png">Boundary</div></h2 >
				<ul data-role="listview" style="min-width: 210px !important;">
		<li class="unselectable" data-role="list-divider"
			id="locationEditMenuTitle">Boundary Settings</li>
		<li data-icon="false"><a class="editInfo open" href="#"
			onclick="editPolygon();"><img alt="" src="images/icons/edit.png">Edit</a></li>
		<li data-icon="false"><a class="editInfo location" href="#"
			onclick="deletePolygon();"><img alt=""
				src="images/icons/delete-icon.png">Remove</a></li>
		<li data-icon="false"><a class="editInfo location" href="#"
			onclick="showHideColors();"><img alt=""
				src="images/icons/location.png">Color</a></li>
		<li data-role="list-divider"></li>
	</ul>
	</li>
		<li data-icon="false" id="editBoundaryMenuItem"><a
			class="editInfo" href="#" onclick="toast('UNDER CUNSTRUCTION');"><img
				alt="" src="images/icons/pathType/elevator.png">Level</a></li>
		<li data-icon="false" id="editBoundaryMenuItem"><a
			class="editInfo" href="#" onclick="editEntrance();"><img alt=""
				src="images/icons/map-markers/door.png">Add Entrance</a></li>
		<li data-icon="false" id="addBoundaryMenuItem"><a
			class="editInfo" href="#" onclick="openEditBoundaryPopup();"><img
				alt="" src="images/icons/polygon.png">Add Boundary</a></li>

		<li data-role="list-divider"></li>

		<li data-icon="false"><a class="editInfo" href="#"
			onclick="openLocationInfoPopup();"><img alt=""
				src="images/icons/info.png">Info</a></li>
		<li data-icon="false"><a class="editInfo thumbnail" href="#"
			onclick="openIconPopup();"><img alt=""
				src="images/icons/image.png">Thumbnail</a></li>
		<li data-role="list-divider"></li>
		<li data-icon="false"><a href="#" class="editInfo delete"
			onclick="removeMarker();"><img alt=""
				src="images/icons/delete.png">Delete</a></li>
		<li data-role="list-divider"></li>
		<li data-icon="false"><a class="editInfo" href="#"
			onclick="printBarcode($('#locationId').val(),$('#locationName').val());"><img
				alt="" src="images/icons/QRCodeIcon.png">Print QR</a></li>
		<li data-role="list-divider"></li>
		<li><a href="#"  class="editInfo"
			onclick="locationEditPanelClose();"><img alt=""
				src="images/icons/clearInput.png">Cancel</a></li>
	</ul>
</div>


<div data-role="popup" id="locationEntranceIntersectionEditMenu"
	data-mini="true" data-dismissible="false">
	<ul data-role="listview" style="min-width: 210px;">
		<li data-icon="false" id="editBoundaryMenuItem"><a
			class="editInfo" href="#" onclick="openEditBoundaryPopup();"><img
				alt="" src="images/icons/polygon.png">Move</a></li>
		<li data-icon="false" id="editBoundaryMenuItem"><a
			class="editInfo" href="#" onclick="removeEntrance();"><img alt=""
				src="images/icons/pathType/elevator.png">Remove</a></li>
		<li><a href="#" data-rel="back" class="editInfo"
			onclick="hideLocationInfo();"><img alt=""
				src="images/icons/clearInput.png">Cancel</a></li>
	</ul>
</div>

<!-- <div class="ui-grid-a SaveCancelBTNPanel SaveNextBTNPanel" style="" -->
<!-- 	id="locationCreateNewNextPanel"> -->
<!-- 	<div class="ui-block-a"> -->
<!-- 		<a style="cursor: pointer;" data-role="button" href="#" -->
<!-- 			class="ui-btn next-icon locationSaveNextButton" onclick="">Next</a> -->
<!-- 	</div> -->
<!-- 	<div class="ui-block-b"> -->
<!-- 		<a style="cursor: pointer;" data-role="button" href="#" -->
<!-- 			class="ui-btn cancel-icon" onclick="closeAMenuPopup();">Cancel</a> -->
<!-- 	</div> -->
<!-- </div> -->

<!-- LOCATION TYPE POPUP LOCATION TYPE POPUP LOCATION TYPE POPUP LOCATION TYPE POPUP LOCATION TYPE POPUP -->



<div data-role="popup" id="editLocationTypePopup"
	class="menuItemPopupClass">
	<a href="#" data-role="button" data-icon="delete" data-iconpos="notext"
		class="ui-btn-right closeMessageButtonIcon"
		onclick="closeAMenuPopup();">Cancel</a>
	<div class="ui-block-solo editlocationFormRow">
		<label for="locationType" id="locationTypeLabel"></label>
		<div class="ui-field-contain">
			<div data-role="controlgroup" name="locationType" id="locationType"
				data-mini="true"></div>
		</div>
	</div>
	<div class="ui-grid-a editlocationFormRow SaveCancelBTNPanel">
		<div class="ui-block-a">
			<a style="cursor: pointer;" data-role="button" href="#"
				class="pathMenu ui-btn ui-shadow save-icon" onclick="saveLocation()">Save</a>
		</div>
		<div class="ui-block-b">
			<a style="cursor: pointer;" data-role="button" href="#"
				class="pathMenu ui-btn ui-shadow cancel-icon"
				onclick="closeAMenuPopup();">Cancel</a>
		</div>
	</div>
</div>



<!-- LOCATION INFO POPUP LOCATION INFO POPUP LOCATION INFO POPUP LOCATION INFO POPUP LOCATION INFO POPUP -->



<div data-role="popup" id="editLocationInfoPopup"
	class="menuItemPopupClass">
	<a href="#" data-role="button" data-theme="a" data-icon="delete"
		data-iconpos="notext" class="ui-btn-right closeMessageButtonIcon"
		onclick="closeAMenuPopup();">Cancel</a>
	<div class="ui-block-solo editlocationFormRow">
		<label for="locationName" id="markerLabel">Label</label> <input
			class="pathMenu locationFields" type="text" placeholder="Label"
			name="locationName" id="locationName" value="">
	</div>
	<div class="ui-block-solo editlocationFormRow">
		<label for="locationDescription">Description</label>
		<textarea type="text" placeholder="Description"
			name="locationDescription" id="locationDescription"
			class="locationFields" value="" rows="5"></textarea>
	</div>
	<div class="ui-grid-a editlocationFormRow SaveCancelBTNPanel">
		<div class="ui-block-a">
			<a style="cursor: pointer;" data-role="button" href="#"
				class="pathMenu ui-btn ui-shadow save-icon" onclick="saveLocation()">Save</a>
		</div>
		<div class="ui-block-b">
			<a style="cursor: pointer;" data-role="button" href="#"
				class="pathMenu ui-btn ui-shadow cancel-icon"
				onclick="hideLocationInfo();">Cancel</a>
		</div>
	</div>
</div>



<!-- EDIT ICON POPUP EDIT ICON POPUP EDIT ICON POPUP EDIT ICON POPUP EDIT ICON POPUP EDIT ICON POPUP EDIT ICON POPUP -->



<div data-role="popup" id="editIconPopup" data-position-to="window"
	style="padding: 7px 7px 7px 7px;" class="menuItemPopupClass">
	<a href="#" data-role="button" data-icon="delete" data-iconpos="notext"
		class="ui-btn-right closeMessageButtonIcon"
		onclick="closeAMenuPopup();">Cancel</a>
	<div class="pathMenu editlocationFormRow" id="IconCollapsible">
		<label>Icon</label>
		<div class="ui-block-solo" id="iconDiv">
			<span>Upload file for icon</span> <input class="pathMenu" type="file"
				id="upload" value="Choose Image" accept="image/*">
			<div id="main-cropper"></div>
			<div id="iconCropDiv">
				<img id="croppedIcon" src="" alt="" />
			</div>
			<button class="cropIcon pathMenu" id="cropIcon">Crop Icon</button>
		</div>
	</div>
	<div class="ui-grid-a editlocationFormRow SaveCancelBTNPanel">
		<div class="ui-block-a">
			<a style="cursor: pointer;" data-role="button" href="#"
				class="pathMenu ui-btn ui-shadow save-icon" onclick="saveLocation()">Save</a>
		</div>
		<div class="ui-block-b">
			<a style="cursor: pointer;" data-role="button" href="#"
				class="pathMenu ui-btn ui-shadow cancel-icon"
				onclick="closeAMenuPopup();">Cancel</a>
		</div>
	</div>
</div>



<!-- EDIT BOUNDARY POPUP EDIT BOUNDARY POPUP EDIT BOUNDARY POPUP EDIT BOUNDARY POPUP EDIT BOUNDARY POPUP EDIT BOUNDARY POPUP  -->



<div id="editBoundaryPopup" data-role="popup" data-position-to="window"
	style="padding: 7px 7px 7px 7px;" class="menuItemPopupClass"
	data-dismissible="false">
	<a href="#" data-role="button" data-icon="delete" data-iconpos="notext"
		class="ui-btn-right closeMessageButtonIcon"
		onclick="hideLocationInfo();">Cancel</a>
	<!-- 	<div class="ams"> -->
	<!-- 		<img src='images/icons/cursor-pointer.png' -->
	<!-- 			class="pathMenu showLabelMouseOverTrue" width="48" height="48" -->
	<!-- 			title="Normal Mode" onclick="removeDrawingMode()"> <img -->
	<!-- 			src='images/icons/polygon-select.png' width="48" height="48" -->
	<!-- 			class="pathMenu showLabelMouseOverTrue" title="Drawing Mode" -->
	<!-- 			onclick="startDrawingMode()" /> <img src='images/icons/edit.png' -->
	<!-- 			width="48" height="48" class="pathMenu showLabelMouseOverTrue" -->
	<!-- 			title="Edit Boundary Points" /> <img -->
	<!-- 			src='images/icons/delete-icon.png' width="48" height="48" -->
	<!-- 			class="pathMenu showLabelMouseOverTrue" title="Delete Boundary" -->
	<!-- 			onclick="deletePolygon()" /> -->
	<!-- 		<div id="boundaryColour" title="Edit Boundary Colours" -->
	<!-- 			class="pathMenu showLabelMouseOverTrue" onclick="showHideColors();"></div> -->
	<!-- 	</div> -->
	<!-- 	<div class="ui-block-c">	 -->
	<ul data-role="listview" style="min-width: 210px !important;">
		<li class="unselectable" data-role="list-divider"
			id="locationEditMenuTitle">Boundary Settings</li>
		<li data-icon="false"><a class="editInfo open" href="#"
			onclick="editPolygon();"><img alt="" src="images/icons/edit.png">Edit</a></li>
		<li data-icon="false"><a class="editInfo location" href="#"
			onclick="deletePolygon();"><img alt=""
				src="images/icons/delete-icon.png">Remove</a></li>
		<li data-icon="false"><a class="editInfo location" href="#"
			onclick="showHideColors();"><img alt=""
				src="images/icons/location.png">Color</a></li>
		<li><a href="#" class="editInfo"
			onclick="closeAMenuPopup();$('#locationSaveCancelPanel').css('display','none');hideLocationInfo();"><img
				alt="" src="images/icons/clearInput.png">Cancel</a></li>
	</ul>
	<!-- 	</div> -->
	<div id="boundaryColorFieldset" style="display: none;">
		<span>Fill Colour</span>
		<div id="colorSelectorFill">
			<div style="background-color: #00ff00"></div>
		</div>
		<span>Border Colour</span>
		<div id="colorSelectorBorder">
			<div style="background-color: #0000ff"></div>
		</div>
		<img src='images/icons/checkMark.png' width="48" height="48"
			class="pathMenu showLabelMouseOverTrue" title="Apply Colour"
			onclick="applyBoundaryColour()" /> <img src='images/icons/undo.png'
			class="pathMenu showLabelMouseOverTrue" width="48" height="48"
			title="Undo Colour Change" onclick="undoColourChange()">
	</div>
</div>
<div class="ui-grid-a toolBar" id="locationSaveCancelPanel"
	style="display: none">
	<div class="ui-block-a">
		<a style="cursor: pointer;" data-role="button" href="#"
			class="ui-btn save-icon" onclick="saveLocation()">Save</a>
	</div>
	<div class="ui-block-b">
		<a style="cursor: pointer;" data-role="button" href="#"
			class="ui-btn cancel-icon"
			onclick="closeAMenuPopup();$('#locationSaveCancelPanel').css('display','none');hideLocationInfo();">Cancel</a>
	</div>
</div>


<!-- LOCATION EDIT PANEL LOCATION EDIT PANEL LOCATION EDIT PANEL LOCATION EDIT PANEL LOCATION EDIT PANEL  -->



<!-- 	<div class="ui-block-solo editlocationFormRow" -->
<!-- 		onclick="" id="calendarIcon"> -->
<!-- 		<img src="images/icons/calendar.png" id="editIconIcon" width="48" -->
<!-- 			height="48" style="cursor: pointer;" />Schedule Access  -->
<!-- 	</div> -->



<div id="locationInfoFooter" class="ui-grid-d pathManagementInfoFooter">
	<div class="ui-block-a">
		<label for="locationLabel">Label</label><input type="text"
			placeholder="Location Label" name="locationLabel" id="locationLabel"
			value="" readonly>
	</div>
	<div class="ui-block-b">
		<label for="locationInfoDescriptionLabel">Description</label><input
			type="text" placeholder="Location Description"
			name="locationInfoDescriptionLabel" id="locationInfoDescriptionLabel"
			value="" readonly>
	</div>
	<div class="ui-block-c">
		<label for="locationThumbnail">Thumbnail</label><input type="text"
			placeholder="Location Thumbnail" name="locationThumbnail"
			id="locationThumbnail" value="" readonly>
	</div>
	<div class="ui-block-d">
		<label for="locationTypeLabelFooter">Location Type</label><input
			type="text" placeholder="Width" name="locationTypeLabelFooter"
			id="locationTypeLabelFooter" value="0" readonly>
	</div>
	<div class="ui-block-e">
		<label for="locationAera">Area (Square Meter)</label> <input
			type="text" placeholder="Aera" name="locationAera" id="locationAera"
			value="0" readonly>
	</div>
</div>





<!-- EDIT PATH MENU EDIT PATH MENU EDIT PATH MENU EDIT PATH MENU EDIT PATH MENU EDIT PATH MENU EDIT PATH MENU -->



<input type='hidden' readonly class="pahtFields" name="pathId"
	id="pathId">
<input type='hidden' readonly class="pahtFields" id="pathLatLng">
<input type='hidden' readonly class="pahtFields" name="pathTypeIds"
	id="pathTypeIds">
<input type='hidden' readonly class="pahtFields" name="destinationId"
	id="destinationId">
<input type='hidden' readonly class="pahtFields" name="destinationGPS"
	id="destinationGPS">
<input type='hidden' readonly class="pahtFields" name="departureId"
	id="departureId">
<input type='hidden' readonly class="pahtFields" name="departureGPS"
	id="departureGPS">



<div id="pathEditMenu">
	<ul data-role="listview" data-mini="true">
		<li data-icon="false" class="editInfo" onclick="openPathTypePopup();"
			id="pathTypePopupMenuBTN"><img alt=""
			src="images/icons/path.png">Path Type</li>
		<li data-icon="false"><label for="pathWidth">Path Width
				(Meters)</label> <input type="range" placeholder="Label" name="pathWidth"
			id="pathWidth" value="5" min="1" max="50"
			onchange="changePathWith(this);"></li>
		<li data-role="list-divider"></li>
		<li data-icon="false" class="editInfo" onclick="hideLocationInfo();"><input
			class="pathMenu" type="text" placeholder="Label" name="pathName"
			id="pathName" value=""></li>
		<li data-icon="false" class="editInfo"><textarea type="text"
				placeholder="Description" name="pathDescription"
				id="pathDescription" value="" rows="5"></textarea></li>
		<li data-role="list-divider"></li>

		<li data-icon="false" onclick="saveThePath();" class="editInfo"><img
			alt="" src="images/icons/save.png">Save</li>
		<li data-icon="false" class="editInfo" onclick="removePath();"><img
			alt="" src="images/icons/delete-icon.png">Delete</li>
		<li data-icon="false" onclick="cancelCreatingNew();" class="editInfo"><img
			alt="" src="images/icons/clearInput.png">Cancel</li>
	</ul>
</div>





<div id="pathInfoFooter" class="ui-grid-d pathManagementInfoFooter">
	<div class="ui-block-a">
		<label for="departure">Departure</label><input type="text"
			placeholder="From (Departure)" name="departure" id="departure"
			value="" readonly>
	</div>
	<div class="ui-block-b">
		<label for="destination">Destination</label><input type="text"
			placeholder="To (Destination)" name="destination" id="destination"
			value="" readonly>
	</div>
	<div class="ui-block-c">
		<label for="destination">Label</label><input type="text"
			placeholder="Path Label" name="destination" id="pathLabel" value=""
			readonly>
	</div>
	<div class="ui-block-d">
		<label for="destination">Width (Meter)</label><input type="text"
			placeholder="Width" name="destination" id="pathWidthLabel" value="0"
			readonly>
	</div>
	<div class="ui-block-e">
		<label for="pathLength">Length</label> <span id="pathLength"></span>
	</div>
</div>


<!-- MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP MAP  -->



<div id="map_canvas"></div>



<!-- LOADING LOADING LOADING LOADING LOADING LOADING LOADING LOADING LOADING LOADING LOADING LOADING LOADING  -->



<div id="loadingOverlay">
	<div class="markerLoading" style="display: none;"></div>
	<span id="loadingContent" style="display: none;"></span>
</div>



<!-- MARKER LABEL MARKER LABEL MARKER LABEL MARKER LABEL MARKER LABEL MARKER LABEL MARKER LABEL -->



<div id="googleMapMarkerLabel" class="labelStyleClass"></div>



<!-- PATH TYPE POPUP PATH TYPE POPUP PATH TYPE POPUP vPATH TYPE POPUP PATH TYPE POPUP PATH TYPE POPUP PATH TYPE POPUP PATH TYPE POPUP -->



<div id="editPathTypePopup"
	style="padding: 7px 7px 7px 7px; position: absolute; display: none;">
	<!-- 	<a href="#" data-role="button" data-icon="delete" data-iconpos="notext" -->
	<!-- 		class="ui-btn-right closeMessageButtonIcon" -->
	<!-- 		onclick="$('#editPathTypePopup').popup('close');">Cancel</a> -->
	<logic:iterate id="pathTIteration" name="pathTypes"
		type="common.location.PathTypeENT">
		<img src='images/icons/cursor-pointer.png' class="pathTypeIcon"
			alt="<%=pathTIteration.getPathTypeId()%>" width="48" height="48"
			title="<%=pathTIteration.getPathType()%>"
			onclick="selectIcon('<%=pathTIteration.getPathTypeId()%>');">
	</logic:iterate>
	<!-- 	<div class="ui-grid-a editlocationFormRow SaveCancelBTNPanel"> -->
	<!-- 		<div class="ui-block-a"> -->
	<!-- 			<a style="cursor: pointer;" data-role="button" href="#" -->
	<!-- 				class="pathMenu ui-btn ui-shadow save-icon" onclick="saveThePath()">Save</a> -->
	<!-- 		</div> -->
	<!-- 		<div class="ui-block-b"> -->
	<!-- 			<a style="cursor: pointer;" data-role="button" href="#" -->
	<!-- 				class="pathMenu ui-btn ui-shadow cancel-icon" -->
	<!-- 				onclick="closeAMenuPopup();">Cancel</a> -->
	<!-- 		</div> -->
	<!-- 	</div> -->
</div>

<script src="js/croppie.js"></script>
<script src="js/colorpicker.js"></script>
<script src="js/leanModal.min.js"></script>
<script src="js/jquery.Jcrop.min.js"></script>
<script type="text/javascript"
	src="js/location/management/path.data.transaction.js"></script>
<script type="text/javascript"
	src="js/location/management/path.polyline.drawing.js"></script>
<script type="text/javascript"
	src="js/location/management/path.polyline.interaction.js"></script>
<script type="text/javascript" src="js/location/management/path.type.js"></script>
<script type="text/javascript"
	src="js/location/management/map.management.js"></script>
<script type="text/javascript"
	src="js/location/management/location.create.new.js"></script>
<script type="text/javascript"
	src="js/location/management/image.thumbnail.croppie.js"></script>
<script src="https://openlayers.org/en/v4.5.0/build/ol.js"></script>
<script
	src="https://cdn.rawgit.com/bjornharrtell/jsts/gh-pages/1.4.0/jsts.min.js"></script>
<script type="text/javascript"
	src="js/location/management/marker.polygon.js"></script>
<script type="text/javascript"
	src="js/location/management/management.general.js"></script>
<script type="text/javascript"
	src="js/location/management/marker.management.js"></script>
<script type="text/javascript"
	src="js/location/management/panel.edit.management.js"></script>
<script async defer
	src="https://maps.googleapis.com/maps/api/js?key=AIzaSyABLdskfv64ZZa0mpjVcTMsEAXNblL9dyE&libraries=drawing&callback=initMap"
	type="text/javascript"></script>
</html>