var drawingManager, selectedShape;

function createDrawingManager() {
	var polyOptions = {
		strokeWeight : 2,
		fillOpacity : 0.45,
		editable : true,
		draggable : false
	};
	drawingManager = new google.maps.drawing.DrawingManager({
		drawingMode : google.maps.drawing.OverlayType.POLYGON,
		drawingControl : false,
		drawingControlOptions : {
			style : google.maps.MapTypeControlStyle.HORIZONTAL_BAR,
			position : google.maps.ControlPosition.TOP_CENTER,
			drawingModes : [ 'polygon' ]
		},
		markerOptions : {
			draggable : true,
			editable : true
		},
		polylineOptions : {
			editable : false,
			draggable : false
		},
		rectangleOptions : polyOptions,
		circleOptions : polyOptions,
		polygonOptions : polyOptions,
		map : map
	});
	google.maps.event.addListener(drawingManager, 'overlaycomplete',
			function(e) {
				var newShape = e.overlay;
				newShape.type = e.type;
				$("#boundary").val(getPolygonCoords(newShape));
				$("#actionBarNextButton").attr("onclick", "createNew(4)")
						.trigger("create");
				// $("#actionBarNextButton").html("Next");
				$("#actionBarBackButton").attr("onclick", "createNew(2)");
				$("#actionBarSaveButton").removeClass("disabledBTN").trigger(
						"create");
				$("#actionBarSaveButton").attr("onclick", "saveLocation()");
				selectedShape = newShape;
				map.setOptions({
					draggableCursor : 'crosshair'
				});
				google.maps.event.addListener(newShape, 'bounds_changed',
						function() {
							alert('Bounds changed.');
						});
				if ($("#tempBoundaryColors").val() == "") {
					setBoundaryFillColour("#1E90FF");
					setBoundaryBorderColour("#1E90FF");
					$("#tempBoundaryColors").val("1E90FF,1E90FF");
				} else {
					setBoundaryFillColour(getFillColourValue());
					setBoundaryBorderColour(getBorderColourValue());
				}
				removeDrawingMode();
			});
	drawingManager.setDrawingMode(null);
}

function drawPolygons(location) {
	var arrayBoundary = getArrayBoundary(location.boundary).split("_");
	var CoordinatesArray = new Array();
	for ( var i = 0; i < arrayBoundary.length; i++) {
		CoordinatesArray.push(new google.maps.LatLng(arrayBoundary[i]
				.split(",")[0], arrayBoundary[i].split(",")[1]));
	}
	var boundaryColour = getBoundaryColour(location.boundary);
	var FillColour;
	var BorderColour;
	if (boundaryColour == "") {
		FillColour = "#1E90FF";
		BorderColour = "#1E90FF";
	} else {
		FillColour = '#' + boundaryColour[0];
		BorderColour = '#' + boundaryColour[1];
	}

	var DRAWPolygon = new google.maps.Polygon({
		paths : CoordinatesArray,
		strokeColor : BorderColour,
		strokeWeight : 2,
		fillColor : FillColour,
		title : location.locationName + " "
				+ location.locationType.locationType
	});
	CoordinatesArray.push(new google.maps.LatLng(
			arrayBoundary[0].split(",")[0], arrayBoundary[0].split(",")[1]));
	var editPolygon = new google.maps.Polyline({
		path : CoordinatesArray,
		strokeOpacity : 1,
		strokeColor : "#FF0000",
		fillColor : "#FF0000",
		strokeWeight : 2,
		title : location.locationName + " "
				+ location.locationType.locationType
	});
	editPolygon.id = location.locationID;
	editPolygon.setMap(null);
	polygonsEdit.push(editPolygon);
	DRAWPolygon.setMap(map);
	DRAWPolygon.id = location.locationID;
	google.maps.event.addListener(DRAWPolygon, 'click', function(event) {
		if ($('[name="optionType"] :radio:checked').val() != "marker") {
			return;
		}
		showLocationInfo();
		selectedShape = this;
		this.setMap(map);
		var area = google.maps.geometry.spherical.computeArea(DRAWPolygon
				.getPath())
				+ "";
		area = area.split(".")[0];
		var tmpArea = "";
		for ( var int = area.length - 1; int >= 0; int--) {
			tmpArea += area[area.length - 1 - int];
			if (int % 3 == 0 && int != 0)
				tmpArea += ",";
		}
		$("#locationAera").val(tmpArea);
		setInputsForLocation(location, location.coordinates);
		for ( var int = 0; int < polygons.length; int++) {
			if (polygons[int].id != $("#locationId").val())
				polygons[int].setMap(null);
		}
		for ( var int = 0; int < polygonsEdit.length; int++) {
			if (polygonsEdit[int].id == $("#locationId").val())
				polygonsEdit[int].setMap(null);
			else
				polygonsEdit[int].setMap(map);
		}
		locationEditPanelOpen(location.locationName,
				location.locationType.locationType);
		google.maps.event.addListener(this.getPath(), "insert_at", function(e) {
			var newBoundaryString = this.getArray()[0].lat() + ","
					+ this.getArray()[0].lng();
			for ( var int2 = 1; int2 < this.getArray().length; int2++) {
				newBoundaryString += "_" + this.getArray()[int2].lat() + ","
						+ this.getArray()[int2].lng();
			}
			$("#boundary").val(newBoundaryString);
		});
		google.maps.event.addListener(this.getPath(), "set_at",
				function(i, obj) {
					var pathLtnLngZ = $("#boundary").val().split("_");
					pathLtnLngZ[i] = this.getArray()[i].lat() + ","
							+ this.getArray()[i].lng();
					$("#boundary").val(pathLtnLngZ.join("_"));
				});
		$('#boundaryColour').css('background-color', FillColour);
		$('#boundaryColour').css('border-color', BorderColour)
				.trigger("create");
		$('#colorSelectorFill div').css('background-color', FillColour);
		$('#colorSelectorBorder div').css('background-color', BorderColour);
		$('#colorSelectorFill').ColorPickerSetColor(FillColour);
		$('#colorSelectorBorder').ColorPickerSetColor(BorderColour);
	});

	google.maps.event.addListener(DRAWPolygon, 'mousemove', function(event) {
		if (event.xa != null)
			showMarkerLabel(location.locationName, event.xa.x, event.xa.y,
					false);
	});
	google.maps.event.addListener(DRAWPolygon, 'mouseout', function(event) {
		clearMarkerLabel();
	});
	polygons.push(DRAWPolygon);
}

function undoColourChange() {
	$("#tempBoundaryColors").val($("#boundaryColors").val());
	$('#boundaryColour').css('background-color', getFillColourValue());
	$('#boundaryColour').css('border-color', getBorderColourValue());
	$('#colorSelectorFill div').css('background-color', getFillColourValue());
	$('#colorSelectorBorder div').css('background-color',
			getBorderColourValue());
	$('#colorSelectorFill').ColorPickerSetColor(getFillColourValue());
	$('#colorSelectorBorder').ColorPickerSetColor(getBorderColourValue());
	setBoundaryFillColour(getFillColourValue());
	setBoundaryBorderColour(getBorderColourValue());
}

function getArrayBoundary(boundary) {
	var locationWithColourArray = boundary.split(";");
	var arrayBoundary = locationWithColourArray[0];
	return arrayBoundary;
}

function getBoundaryColour(boundary) {
	var locationWithColourArray = boundary.split(";");
	if (boundary.length <= 1 || locationWithColourArray.length <= 1)
		return "#000000,#FFFFFF";
	var boundaryColour = locationWithColourArray[1].split(",");
	return boundaryColour;
}

function showHideColors() {

	if ($("#boundaryColorFieldset").css("display") == "none") {
		$("#boundaryColorFieldset").css("display", "block");
	} else {
		$("#boundaryColorFieldset").css("display", "none");
	}
	locationEditPanelClose();
}

function deletePolygon() {
	locationEditPanelClose();
	$("#popupConfirmation_confirmBTN").attr("onclick", "removePolygonFn()");
	showPopupConfirmation('Are you sure you want to remove the boundary?');
}
var removePolygonFn = function() {
	var id = $("#locationId").val();
	for ( var i = 0; i < polygons.length; i++) {
		if (polygons[i].id == id) {
			$("#boundary").val("");
			polygons[i].setMap(null);
			polygons.splice(i, 1);
			saveLocation();
			hideLocationInfo();
			$("#popupConfirmation").popup("close");
			return;
		}
	}
	$("#popupConfirmation").popup("close");
};

function setMapOnAllPolygons(value) {
	for ( var i = 0; i < polygons.length; i++) {
		polygons[i].setMap(value);
	}

}

function startDrawingMode() {
	drawingManager.setDrawingMode(google.maps.drawing.OverlayType.POLYGON);

}

function removeDrawingMode() {
	if (drawingManager != null)
		drawingManager.setDrawingMode(null);
	map.setOptions({
		draggableCursor : 'default'
	});
}

function editPolygon() {
	locationEditPanelClose();
	if (selectedShape.type !== 'marker') {
		for ( var i = 0; i < polygons.length; i++) {
			polygons[i].setEditable(false);
		}
		selectedShape.setEditable(true);
	}
	closeAMenuPopup();
	// if ($("#locationId").val().length >= 0) {
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
	// }
}

function createColorPicker() {
	$('#colorSelectorFill').ColorPicker({
		color : '#0000ff',
		onShow : function(colpkr) {
			$(colpkr).fadeIn(500);
			return false;
		},
		onHide : function(colpkr) {
			$(colpkr).fadeOut(500);
			return false;
		},
		onChange : function(hsb, hex, rgb) {
			$('#colorSelectorFill div').css('backgroundColor', '#' + hex);
			$('#boundaryColour').css('background-color', '#' + hex);
			updateFillColourValue(hex);
		}
	});
	$('#colorSelectorBorder').ColorPicker({
		color : '#0000ff',
		onShow : function(colpkr) {
			$(colpkr).fadeIn(500);
			return false;
		},
		onHide : function(colpkr) {
			$(colpkr).fadeOut(500);
			return false;
		},
		onChange : function(hsb, hex, rgb) {
			$('#colorSelectorBorder div').css('backgroundColor', '#' + hex);
			$('#boundaryColour').css('border-color', '#' + hex);
			updateBorderColourValue(hex);
		}
	});
}

function applyBoundaryColour() {
	if (selectedShape) {
		setBoundaryFillColour(getFillColourValue());
	}
	if (selectedShape) {
		setBoundaryBorderColour(getBorderColourValue());
	}
}

function getPolygonCoords(shape) {
	var len = shape.getPath().getLength();
	var coordinates = "";
	for ( var i = 0; i < len; i++) {
		coordinates = coordinates + shape.getPath().getAt(i).lat() + ","
				+ shape.getPath().getAt(i).lng();
		if (i !== len - 1) {
			coordinates = coordinates + "_";
		}
	}
	return coordinates;
}

function setBoundaryFillColour(HexFillColour) {
	var polygonOptions = drawingManager.get('polygonOptions');
	polygonOptions.fillColor = HexFillColour;
	drawingManager.set('polygonOptions', polygonOptions);
	selectedShape.set('fillColor', HexFillColour);
}

function setBoundaryBorderColour(HexBorderColour) {
	var polygonOptions = drawingManager.get('polygonOptions');
	polygonOptions.strokeColor = HexBorderColour;
	drawingManager.set('polygonOptions', polygonOptions);
	selectedShape.set('strokeColor', HexBorderColour);
}

function updateFillColourValue(FillColour) {
	var boundaryColour = $("#tempBoundaryColors").val().split(",");
	var BorderColour = boundaryColour[1];
	$("#tempBoundaryColors").val(FillColour + "," + BorderColour);
}

function updateBorderColourValue(BorderColour) {
	var boundaryColour = $("#tempBoundaryColors").val().split(",");
	var FillColour = boundaryColour[0];
	$("#tempBoundaryColors").val(FillColour + "," + BorderColour);
}

function getFillColourValue() {
	var boundaryColour = $("#tempBoundaryColors").val().split(",");
	var FillColour = boundaryColour[0];
	return '#' + FillColour;
}

function getBorderColourValue() {
	var boundaryColour = $("#tempBoundaryColors").val().split(",");
	var BorderColour = boundaryColour[1];
	return '#' + BorderColour;
}