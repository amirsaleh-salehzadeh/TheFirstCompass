var locationTypeJSONData;

function getAllLocationTypes() {
	var url = "REST/GetLocationWS/GetAllLocationTypes";
	$
			.ajax({
				url : url,
				cache : false,
				async : true,
				success : function(data) {
					setCookie('TripPathTypeIdsCookie', JSON.stringify(data), 1);
				},
				error : function(xhr, ajaxOptions, thrownError) {
					popErrorMessage("An error occured while fetching the location types from the server. "
							+ thrownError);
				}
			});
}

function getLocationTypeDropDown(obj) {
	if (obj == null)
		obj = JSON.parse(getCookie('TripPathTypeIdsCookie'));
	var locationData = "";
	$(obj)
			.each(
					function(k, l) {
						if (l.parent.locationTypeId == $(
								"#parentLocationTypeId").val()) {
							locationData += '<a href="#" title= "'
									+ l.locationType
									+ '" type="radio" onClick="selectThisLocationType(this)" class="locationTypeSelecIcons" name="locationType" value="'
									+ l.locationTypeId + '" id="locationTypeId'
									+ l.locationTypeId + '"><img src="'
									+ getLocationTypeImage(l.locationTypeId)
									+ '" width="48px" height="48px" /></a>';
						} else
							obj = l;
					});
	if (locationData.length > 0) {
		$("#locationType").html(locationData);
		$("#locationType").controlgroup().trigger("create");
		$("#locationType").controlgroup("option", "type", "horizontal");
	}
	if (obj.children != null)
		getLocationTypeDropDown(obj.children);
	else
		return;
}

function getParentLocationTypeId(obj, id) {
	if (obj == null)
		obj = JSON.parse(getCookie('TripPathTypeIdsCookie'));
	$(obj).each(function(k, l) {
		if (l.locationTypeId == id) {
			$("#parentLocationTypeId").val(l.parent.locationTypeId);
			$("#locationTypeId").val(l.locationTypeId);
			// return;
		} else
			obj = l;
	});
	if (obj.children != null)
		getParentLocationTypeId(obj.children, id);
	else
		return;
}

function getLocationTypeName(obj, id) {
	if (obj == null)
		obj = JSON.parse(getCookie('TripPathTypeIdsCookie'));
	$(obj).each(
			function(k, l) {
				if (l.locationTypeId == id) {
					$("#locationTypeLabel").html(l.locationType);
					$("#locationTypeLabelFooter").val(l.locationType);
					$("#modeSelection_locationText").html(
							l.locationType.toUpperCase()).trigger("create");
					return;
				} else
					obj = l;
			});
	if (obj.children != null)
		getLocationTypeName(obj.children, id);
	else
		return "";
}

function selectThisLocationType(ltype) {
	if (ltype != null) {
		$("#locationTypeId").val($(ltype).attr("value"));
	}
	var ltypeTxt = "";
	$(".locationTypeSelecIcons").each(
			function() {
				if ($(this).hasClass("locationTypeSelecIconSelected"))
					$(this).removeClass("locationTypeSelecIconSelected");
				if ($(this).attr("value") == $("#locationTypeId").val()
						|| $("#locationTypeId").val() == "0"
						|| $("#locationTypeId").val() == $(
								"#parentLocationTypeId").val()) {
					$(this).addClass("locationTypeSelecIconSelected").trigger(
							"create");
					ltypeTxt = $(this).attr("title");
					$("#locationTypeId").val($(this).attr("value"));
				}
			});
	getLocationTypeName(null, $("#locationTypeId").val());
	if ($("#locationTypeLabel").html().length <= 1)
		$("#locationTypeLabel").html("Set Location Type");
	if ($("#locationTypeId").val().length >= 1
			&& $("#locationTypeId").val() != 0
			&& $("#actionBar").css("display") != "none") {
		$("#actionBarNextButton").removeClass("disabledBTN");
		$("#actionBarNextButton").attr("disabled", false).trigger("create");
		$("#actionBarMessage").html("Press Next");
		$("#actionBarTitle").html("NEW " + ltypeTxt.toUpperCase());
		$("#editLocationTypePopup").popup("close");
		createNew(1);
	}
}

function getLocationTypeImage(locationTypeId) {
	var icon = 'images/icons/map-markers/';
	if (locationTypeId == "1") {
		icon += 'marker-blue.png';
	} else if (locationTypeId == "10") {
		icon += 'parking.png';
	} else if (locationTypeId == "2") {
		icon += 'area.png';
	} else if (locationTypeId == "3") {
		icon += 'building.png';
	} else if (locationTypeId == "4") {
		icon += 'marker-pink.png';
	} else if (locationTypeId == "5") {
		icon = 'images/icons/path.png';
	} else
		icon = 'images/icons/pathType/stairs.png';
	return icon;
}
function locationEditPanelClose(){
	$("#locationEditMenu").css("display", "none");
	
	$("#map_canvas").css("width",
			$(window).width());
	$("#map_canvas").css("left", 0);
}
function locationEditPanelOpen(title, info) {
	if ($("#boundary").val().length > 16) {
		$("#addBoundaryMenuItem").css("display", "none").trigger("create");
		$("#editBoundaryMenuItem").css("display", "block").trigger("create");
	} else {
		$("#addBoundaryMenuItem").css("display", "block").trigger("create");
		$("#editBoundaryMenuItem").css("display", "none").trigger("create");
	}
	$("#locationInfo").html('');
//	$("#locationEditMenuTitle").html(title + " (" + info + ")");
	$("#locationInfo").prepend(title);
	$("#locationDescriptionLabel").html(info);
//	$('#locationEditMenu').popup("option", {
//		x : event.pageX,
//		y : event.pageY
//	});
//	$("#locationEditMenu").popup("open").trigger("create");
	
	
	$("#locationEditMenu").css("display", "block");
//	$("#locationEditMenu").css(
//			"top",
//			parseInt(parseInt($(".jqm-header").height())
//					+ parseInt($("#locPathModeRadiobtn").height()) + 2))
//			.trigger("create");
	$("#map_canvas").css("left", $("#locationEditMenu").width()+32);
	$("#map_canvas").css("width",
			$(window).width() - $("#locationEditMenu").width());
	$("#map_canvas").css("right", "0px").trigger("create");
	google.maps.event.trigger(map, "resize");
}
function locationEntranceIntersectionEditPanelOpen(title, info) {
	$("#locationInfo").prepend(title);
	$("#locationDescriptionLabel").html(info);
	$('#locationEntranceIntersectionEditMenu').popup("option", {
		x : event.pageX,
		y : event.pageY
	});
	$("#locationEntranceIntersectionEditMenu").popup("open").trigger("create");
}

function openALocation() {
	locationEditPanelClose();
//	$("#locationEditMenu").popup("close");
	$("#parentLocationId").val($("#locationId").val());
	getIntoALocation($("#locationId").val());
	
}

function openLocationTypePopup() {

	selectThisLocationType(null);
	
//	$("#locationEditMenu").on("popupafterclose", function() {
		setTimeout(function() {
			$("#editLocationTypePopup").trigger('create').popup('open');
		}, 100);
//	});
//	$("#locationEditMenu").popup("close");
		locationEditPanelClose();
		$("#map_canvas").css("left", 0);
	
}

function openLocationInfoPopup() {

//	$('#editLocationInfoPopup').popup("option", {
//		x : event.pageX,
//		y : event.pageY
//	});
	// $("#mainBodyContents").trigger('create');
//	$("#locationEditMenu").on("popupafterclose", function() {
		setTimeout(function() {
			$("#editLocationInfoPopup").trigger('create').popup('open');
		}, 100);
//	});
//	$("#locationEditMenu").popup("close");
		locationEditPanelClose();
	// $("#mainBodyContents").trigger('create');
		
}

function closeAMenuPopup() {
	if (tmpEntrancePolygon != null) {
		tmpEntrancePolygon.setMap(null);
		tmpEntrancePolygon = null;
	}
	if (entranceMarker != null) {
		entranceMarker.setMap(null);
		entranceMarker = null;
	}
	$('.menuItemPopupClass').popup('close');
//	$("#locationEditMenu").unbind("popupafterclose");
	map.setOptions({
		draggableCursor : 'default'
	});
	$("#map_canvas").unbind('mousemove');
	$('#locationSaveCancelPanel').css('display', 'none');
}

function openIconPopup() {
	$('#editIconPopup').popup("option", {
		x : event.pageX,
		y : event.pageY
	});
//	$("#locationEditMenu").on("popupafterclose", function() {
		setTimeout(function() {
			$("#editIconPopup").popup('open').trigger('create');
		}, 100);
//	}).trigger('create');
//	$("#locationEditMenu").popup("close");
		locationEditPanelClose();
}

function openEditBoundaryPopup() {
	locationEditPanelClose();
	if ($("#boundary").val().length <= 16) {
		startDrawingMode();
//		$("#locationEditMenu").popup("close");
		
		$("#locationSaveCancelPanel").css("display", "inline-block").trigger(
				"create");
		$("#locationSaveCancelPanel").css(
				"top",
				parseInt(parseInt($(".jqm-header").height())
						+ parseInt($("#locPathModeRadiobtn").height()) + 3));
		$("#locationSaveCancelPanel").css(
				"left",
				parseInt(parseInt($(window).width() / 2)
						- parseInt($("#locationSaveCancelPanel").width() / 2)))
				.trigger("create");
		return;
	}

	$('#editBoundaryPopup').popup("option", {
		x : event.pageX,
		y : event.pageY
	});
//	$("#locationEditMenu").on("popupafterclose", function() {
		setTimeout(function() {
			$("#editBoundaryPopup").trigger('create').popup('open');
		}, 100);
//	});
//	$("#locationEditMenu").popup("close");
		
}

function createEntrance() {
	$("#popupConfirmation_confirmBTN").attr("onclick", "createEntranceFn()");
	showPopupConfirmation('Are you sure you want to create the entrance?');
}

var createEntranceFn = function() {
	$("#locationGPS").val(
			entranceMarker.getPosition().lat() + ","
					+ entranceMarker.getPosition().lng());
	if ($("#locationId").val().length > 0)
		$("#parentLocationId").val($("#locationId").val());
	$("#isEntranceIntersection").val("true");
	saveEntrance(0);
	$("#popupConfirmation").popup("close");
};

var tmpEntrancePolygon, entranceMarker;
function editEntrance() {
	var pointsInLine = [];
	var bndPos = $("#boundary").val().split("_");
	var coordinatesArray = [];
	for ( var int = 0; int < bndPos.length; int++) {
		var LatAndLng = bndPos[int].split(",");
		var point = {
			x : parseFloat(LatAndLng[0]),
			y : parseFloat(LatAndLng[1])
		};
		pointsInLine.push(point);
		var LatLng = new google.maps.LatLng(LatAndLng[0], LatAndLng[1]);
		coordinatesArray.push(LatLng);
	}
	coordinatesArray.push(new google.maps.LatLng(bndPos[0].split(",")[0],
			bndPos[0].split(",")[1]));
	pointsInLine.push({
		x : parseFloat(bndPos[0].split(",")[0]),
		y : parseFloat(bndPos[0].split(",")[1])
	});
	setMapOnAllPolygons(null);
	for ( var int = 0; int < polygonsEdit.length; int++) {
		polygonsEdit[int].setMap(null);
	}
	tmpEntrancePolygon = new google.maps.Polyline({
		path : coordinatesArray,
		strokeColor : "#000000",
		strokeWeight : 1,
		map : map
	});
	if (entranceMarker == null)
		entranceMarker = new google.maps.Marker({
			icon : refreshMap(11, "", "normal"),
			zIndex : 666,
			map : map
		});
	google.maps.event.addListener(map, 'mousemove', function(ev) {
		var destGPS = {
			x : ev.latLng.lat(),
			y : ev.latLng.lng()
		};
		var intersectionpoint = getClosestPointOnLines(destGPS, pointsInLine);
		var pos = {
			lat : parseFloat(intersectionpoint.x),
			lng : parseFloat(intersectionpoint.y)
		};
		entranceMarker.setPosition(pos);
	});
	// $("#parentLocationId").val($("#locationId").val());
	// $("#locationId").val("0");
	$("#description").val("Entrance");
	google.maps.event.addListener(entranceMarker, 'click', function(ev) {
		createEntrance();
	});
	google.maps.event.addListener(map, 'click', function(ev) {
		createEntrance();
	});
	// $("#locationSaveCancelPanel").css("display", "inline-block").trigger(
	// "create");
	// $("#locationSaveCancelPanel").css(
	// "top",
	// parseInt(parseInt($(".jqm-header").height())
	// + parseInt($("#locPathModeRadiobtn").height()) + 3))
	// .trigger("create");
	// $("#locationSaveCancelPanel").css(
	// "left",
	// parseInt(parseInt($(window).width() / 2)
	// - parseInt($("#locationSaveCancelPanel").width() / 2)))
	// .trigger("create");
//	$("#locationEditMenu").popup("close");
	locationEditPanelClose();
}

function showLocationInfo() {
//	$("#locationEditMenu").popup("close");
	$("#locationInfoFooter").css("width", $("#map_canvas").width());
	$("#locationInfoFooter").css("right", "0px");
	$("#locationInfoFooter").css("bottom", "0px");
	$("#locationInfoFooter").css("position", "absolute");
	$("#locationInfoFooter").css("display", "inline-block").trigger("create");
}

function hideLocationInfo() {
	$("#locationInfoFooter").css("display", "none");
	$(".locationFields").val("");
	$("#locationLabel").val("");
	$("#locationInfoDescriptionLabel").val("");
	$("#locationThumbnail").val("");
	$("#locationTypeLabelFooter").val("");
	$("#locationAera").html("");
	$("#locationLabel").val("");
	closeAMenuPopup();
	removeDrawingMode();
	$('#locationSaveCancelPanel').css('display', 'none');
	if (tmpCreateNewlocation != null) {
		tmpCreateNewlocation.setMap(null);
		tmpCreateNewlocation = null;
	}
	if (selectedShape != null
			&& ($("#locationEditMenu").val() != "0" || $("#locationEditMenu")
					.val().length <= "0")) {
		selectedShape.setMap(null);
		selectedShape = null;
	}
	for ( var i = 0; i < polygons.length; i++) {
		polygons[i].setEditable(false);
		polygons[i].setMap(map);
	}
	for ( var int = 0; int < polygonsEdit.length; int++) {
		polygonsEdit[int].setMap(null);
	}
	if (tmpEntrancePolygon != null) {
		tmpEntrancePolygon.setMap(null);
		tmpEntrancePolygon = null;
	}
	if (entranceMarker != null) {
		entranceMarker.setMap(null);
		entranceMarker = null;
	}
	$('.menuItemPopupClass').popup('close');
//	$("#locationEditMenu").unbind("popupafterclose");
	map.setOptions({
		draggableCursor : 'default'
	});
	$("#map_canvas").unbind('mousemove');
	$('#locationSaveCancelPanel').css('display', 'none');
}

function openPathEditPanel() {
	$("#pathEditMenu").css("display", "block");
	$("#pathEditMenu").css(
			"top",
			parseInt(parseInt($(".jqm-header").height())
					+ parseInt($("#locPathModeRadiobtn").height()) + 2))
			.trigger("create");
	$("#map_canvas").css("left", $("#pathEditMenu").width());
	$("#map_canvas").css("width",
			$(window).width() - $("#pathEditMenu").width());
	$("#map_canvas").css("right", "0px").trigger("create");
	google.maps.event.trigger(map, "resize");
	openPathTypePopup();
	$("#pathInfoFooter").css("left", $("#pathEditMenu").width());
	$("#pathInfoFooter").css("width", $("#map_canvas").width());
	$("#pathInfoFooter").css("right", "0px");
	$("#pathInfoFooter").css("bottom", "0px");
	$("#pathInfoFooter").css("position", "absolute");
	$("#pathInfoFooter").css("display", "inline-block").trigger("create");
	hideLocationInfo();
}

function openPathTypePopup() {
	$("#pathTypePopupMenuBTN").attr("onclick", "closePathTypePopup()");
	$(".pathTypeIcon").each(function() {
		if ($(this).hasClass("pathTypeIconSelected")) {
			$(this).removeClass("pathTypeIconSelected");
		}
		$(this).trigger("create");
	});
	$("#editPathTypePopup").css(
			"top",
			parseInt(parseInt($(".jqm-header").height())
					+ parseInt($("#locPathModeRadiobtn").height()) + 3))
			.trigger("create");
	$("#editPathTypePopup").css("left", $("#pathEditMenu").width());
	$("#pathInfoFooter").css("width", $("#map_canvas").width()).trigger("create");
	$("#editPathTypePopup").css('display', "block").trigger('create');
	var ptids = $("#pathTypeIds").val().split(",");
	for ( var int = 0; int < ptids.length; int++)
		$(".pathTypeIcon").each(function() {
			var pathTypeId = $(this).attr("alt");
			if (pathTypeId == ptids[int]) {
				if (!$(this).hasClass("pathTypeIconSelected")) {
					$(this).addClass("pathTypeIconSelected");
				} else {
					$(this).removeClass("pathTypeIconSelected");
				}
				$(this).trigger("create");
			}
		});
}

function closePathTypePopup() {
	$("#pathTypePopupMenuBTN").attr("onclick", "openPathTypePopup()");
	$("#editPathTypePopup").css('display', "none").trigger('create');
}

function hidePathInfo() {
	newPathInProgress = false;
	if (paths != null) {
		for ( var i = 0; i < paths.length; i++) {
			if (paths[i] != null) {
				paths[i].setOptions({
					strokeColor : '#081B2C'
				});
				paths[i].setMap(map);
			} else
				pathTypeIds.splice(i, 1);
		}
	}
//	pathSelected = false;
	$("#pathInfoFooter").css("display", "none");
	$("#pathEditMenu").css("display", "none");
	$("#map_canvas").css("width", $(window).width());
	$("#map_canvas").css("left", "0px");
	closePathTypePopup();
	$("#actionBar").css("display", "none");
	google.maps.event.clearInstanceListeners(map);
	if (movingLine != undefined) {
		movingLine.setMap(null);
		movingLine = undefined;
	}
	if (pathPolylineConstant != undefined) {
		pathPolylineConstant.setMap(null);
		pathPolylineConstant = undefined;
	}
	removeDrawingMode();
	$(".pahtFields").val("");
	for ( var int = 0; int < pathCreateNewPolygons.length; int++) {
		if (pathCreateNewPolygons[int] != null)
			pathCreateNewPolygons[int].setMap(null);
	}
}

function openPathInfoPopup() {
	$('#editPathInfoPopup').popup("option", {
		x : event.pageX,
		y : event.pageY
	});
	$("#editPathInfoPopup").trigger('create').popup('open');
}

function openSearchPanel() {
	getLocationsListToSearch();
	$("#locationSearchPanel").panel("open");
}

function getLocationsListToSearch() {
	$("#resultsListViewContentDiv").empty();
	var url = "REST/GetLocationWS/SearchForALocation?clientName="+$("#username").val()+"&locationType=Building&locationName=";
	$.ajax({
		url : url,
		cache : true,
		async : true,
		success : function(data) {
			// $.each(data, function(p, z) {
			$.each(data.childrenENT, function(k, l) {
				var children = l.childrenENT;
				var col = $("<div/>", {
					"data-role" : "collapsible",
					"data-collapsed" : true,
					"class" : "parentCollapsibleLV",
					"data-icon" : false
				});
				var title = $("<h3/>", {
					text : l.locationName + " Campus"
				}).appendTo(col);
				var list_items = '';
				$.each(children, function(x, y) {
					list_items += "<li id='" + y.locationID + "_" + y.coordinates + "_"
							+ y.locationType.locationType
							+ "' onclick='selectDestination(this, \""
							+ y.locationType.locationType + " "
							+ y.locationName + "\")' data-icon='false'>"
							+ "<a href='#' class='resultsListViewContent'>";
					var src = "images/icons/map-markers/building.png";
					if (y.icon != null)
						src = y.icon;
					list_items += "<img src='" + src
							+ "' class='listViewIcons'><h2>"
							+ y.locationType.locationType + " "
							+ y.locationName + "</h2><p>";
					var desc = "&nbsp;";
					if (y.description != null)
						desc = y.description;
					list_items += desc + ", " + l.locationName
							+ " Campus</p></a></li>";
				});
				var list = $("<ul/>", {
					"data-role" : "listview",
					"id" : "listview" + l.locationID,
					// "data-inset" : true,
					"data-input" : "#searchField",
					"data-filter" : true,
					"data-icon" : false
				});
				$(list).append(list_items);
				$(list).appendTo(col).trigger("create");
				$("#resultsListViewContentDiv").append(col).collapsibleset()
						.trigger("create");
			});
		},
		error : function(xhr, ajaxOptions, thrownError) {
			errorMessagePopupOpen(ajaxOptions + ": " + thrownError);
		}
	});
}

function searchFieldDivClearBTN() {
	$("#searchField").val("");
	$("#resultsListView").listview("refresh");
}