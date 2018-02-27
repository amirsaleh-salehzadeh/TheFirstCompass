var tmpCreateNewlocation;
var newPathInProgress;
function cancelCreatingNew() {
	$('.menuItemPopupClass').popup('close');
//	$("#locationEditMenu").unbind("popupafterclose");
	hideLocationInfo();
	hidePathInfo();
	map.setOptions({
		draggableCursor : 'default'
	});
	$("#actionBar").css("display", "none");
	$("#map_canvas").unbind('mousemove');
	$(".locationFields").val("");
	clearActionBarLabel();
}

function createNew(seq) {
	clearActionBarLabel();
	$("#actionBar").css("display", "inline-block");
	$(".meter").css("display", "block");
	var mapOptions = {
		draggableCursor : "default"
	};
	$("#map_canvas").unbind('mousemove');
	if ($("#locationName").val().length > 0)
		$("#locationLabel").val($("#locationName").val());
	if ($("#locationDescription").val().length > 0)
		$("#locationInfoDescriptionLabel").val($("#locationDescription").val());
	if ($('[name="optionType"] :radio:checked').val() != "marker") {
		$(".meter").css("display", "none");
		$(".pahtFields").val("");
		$("#actionBarButtonGroup").css("display", "none").trigger("create");
		$("#actionBarTitle").html("Create a Path");
		$("#actionBarMessage").html("Select Path Type and Departure Point");
		for ( var int = 0; int < paths.length; int++) {
			paths[int].setMap(null);
		}
		for ( var int = 0; int < pathCreateNewPolygons.length; int++) {
			pathCreateNewPolygons[int].setMap(map);
		}
		newPathInProgress = true;
		openPathEditPanel();
		setMapOnAllMarkers(null);
		setMapOnAllPolygons(null);
		setMapOnAllPathMarkers(map);
		for ( var int = 0; int < polygonsEdit.length; int++) {
			polygonsEdit[int].setMap(map);
		}
		return;
	}
	$(document).keyup(function(e) {
		if (e.keyCode == 27) {
			removeDrawingMode();
			map.setOptions({
				draggableCursor : "default"
			});
			$('img').unbind('mouseover');
			clearActionBarLabel();
		}
	});
	switch (seq) {
	case 0:
		$(".meter > span").width("20%");
		if (tmpCreateNewlocation != null) {
			tmpCreateNewlocation.setMap(null);
			tmpCreateNewlocation = null;
		}
		showLocationInfo();
		$(".SaveCancelBTNPanel").css("display", "none");
		$("#actionBarNextButton").attr("disabled", true);
		$("#actionBarNextButton").addClass("disabledBTN");
		$("#actionBarBackButton").attr("disabled", true);
		$("#actionBarBackButton").addClass("disabledBTN");
		$("#actionBarSaveButton").attr("disabled", true);
		$("#actionBarSaveButton").addClass("disabledBTN");
		$(".locationFields").val("");
		setALocationTypeNew();
		break;
	case 1:
		$(".meter > span").width("40%");
		$("#actionBarNextButtonDiv").css("display", "block");
		mapOptions = {
			draggableCursor : "url('images/icons/map-markers/mouse-cursors/buildingss.png'), auto"
		};
		setAPointOnMap();
		break;
	case 2:
		$(".meter > span").width("60%");
		setLocationInfoNew();
		break;
	case 3:
		$(".meter > span").width("80%");
		$("#editLocationInfoPopup").popup("close");
		$("#actionBarMessage")
				.html(
						"Please draw a boundary around the property. "
								+ "Make sure to place the pins at an accurate geographical position");
		// $("#actionBarSaveButtonDiv").css("display", "block");
		// $("#actionBarNextButton").attr("onclick",
		// "createNew(4)").trigger("create");
		// $("#actionBarBackButton").attr("onclick", "createNew(2)");
		startDrawingMode();
		break;
	case 4:
		$(".meter > span").width("100%");
		saveLocation();
		$("#actionBarNextButton").attr("disabled", true);
		$("#actionBarBackButton").attr("onclick", "createNew(3)");
		editEntrance();
		break;
	}
	map.setOptions(mapOptions);
}

function setLocationInfoNew() {
	$("#actionBarMessage").html(
			"Set a label and description for this property.");
	$("#actionBarBackButtonDiv").css("display", "block");
	// $("#editLocationTypePopup").on("popupafterclose", function() {
	setTimeout(function() {
		$("#editLocationInfoPopup").trigger('create').popup('open');
	}, 100);
	// });
	// $("#editLocationTypePopup").popup("close");
	$("#actionBarNextButton").attr("onclick", "createNew(3)").trigger("create");
	$("#actionBarBackButton").attr("onclick", "createNew(1)");
	// $(".locationSaveNextButton").attr("onclick", "createNew(3)").trigger(
	// "create");
}

function setALocationTypeNew() {
	selectThisLocationType(null);
	$("#actionBarMessage").html("Set a type for this property.");
	setTimeout(function() {
		$('#editLocationTypePopup').popup({
			positionTo : "window",
			transition : "pop",
			history : false
		}).trigger('create').popup('open');
	}, 100);
}

function placeProperty(lat,lng) {
	$("#popupConfirmation_confirmBTN").attr("onclick", "placePropertyFn(" + lat + "," + lng + ")");
	showPopupConfirmation('Are you sure you want to place a property here?');
}

var placePropertyFn = function(lat,lng) {
//	if (confirm("Are u sure u want to place a property here?")) {
		$("#locationGPS").val(lat + "," + lng);
		$("#actionBarNextButton").attr("onclick", "createNew(2)").trigger(
				"create");
		$("#actionBarBackButton").attr("onclick", "createNew(0)");
		$("#actionBarBackButton").removeClass("disabledBTN").trigger(
				"create");
		google.maps.event.clearInstanceListeners(map);
		toast("The property location is set");
		createNew(2);
		$("#popupConfirmation").popup("close");
//	} 
//	else {
//		tmpCreateNewlocation.setMap(null);
//		tmpCreateNewlocation = null;
//		return;
//	}
};

function setAPointOnMap() {
	$("#map_canvas").mousemove(function(event) {
		showActionBarLabel("Pin Property", event.pageX, event.pageY);
	});
	$("#actionBarMessage")
			.html(
					"Place a marker on the map to point the property and then, press next.");
	var mapClickListener = function(event) {
		var lat = event.latLng.lat();
		var lng = event.latLng.lng();
		map.panTo({
			lat : parseFloat(lat),
			lng : parseFloat(lng)
		});
		if (tmpCreateNewlocation != null) {
			tmpCreateNewlocation.setMap(null);
			tmpCreateNewlocation = null;
		}
		tmpCreateNewlocation = new google.maps.Marker({
			map : map,
			position : {
				lat : parseFloat(lat),
				lng : parseFloat(lng)
			}
		});
		placeProperty(lat,lng);
	};
	google.maps.event.addListener(map, "click", mapClickListener);
}

function showActionBarLabel(text, posX, posY) {
	$("#actionBarLabel").html(text);
	$('#actionBarLabel').css("display", "block");
	$('#actionBarLabel').css("position", "absolute");
	$('#actionBarLabel').css("left",
			posX - ($("#actionBarLabel").width() / 2) + 'px');
	$('#actionBarLabel').css("top", posY - 40 + 'px');
	$('#actionBarLabel').trigger("create");
}

function clearActionBarLabel() {
	$('#actionBarLabel').css("display", "none");
}