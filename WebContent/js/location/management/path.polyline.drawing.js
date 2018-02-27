function setMapOnAllPolylines(value) {
	for ( var i = 0; i < paths.length; i++) {
		paths[i].setMap(value);
	}
	for ( var i = 0; i < pathPolylines.length; i++) {
		pathPolylines[i].setMap(value);
	}
}

var tmpModifiedPathPolygon;
function changePathWith(slider) {
	var pathRouteTMP;
	if (movingLine != null)
		return;
	if (($("#pathId").val() == 0 || $("#pathId").val().length <= 0)
			&& pathPolylineConstant != null) {
		pathRouteTMP = pathPolylineConstant.getPath();
		// return;
	} else
		for ( var i = 0; i < pathPolylines.length; i++) {
			if (pathPolylines[i].id == $("#pathId").val()) {
				pathRouteTMP = pathPolylines[i].getPath();
			}
		}
	var pathCoorPolygon = [];
	for ( var i = 0; i < pathRouteTMP.getArray().length; i++) {
		pathCoorPolygon.push([ parseFloat(pathRouteTMP.getArray()[i].lng()),
				parseFloat(pathRouteTMP.getArray()[i].lat()) ]);
	}
	if (tmpModifiedPathPolygon != null) {
		tmpModifiedPathPolygon.setPath(measurePolygonForAPath(pathCoorPolygon,
				$(slider).val()));
	} else
		tmpModifiedPathPolygon = new google.maps.Polygon({
			paths : measurePolygonForAPath(pathCoorPolygon, $(slider).val()),
			map : map,
			strokeColor : "#027B2C",
			strokeWeight : 2,
			fillColor : "#081B2C",
			zIndex : 10
		});
	// if (pathPolylineConstant != null)
	// pathPolylineConstant.setPath(measurePolygonForAPath(pathCoorPolygon,
	// $(slider).val()));
}

function measurePolygonForAPath(coorPoly, width) {
	var distance = parseFloat(width) / 222240, geoInput = {
		type : "LineString",
		coordinates : coorPoly
	};
	var geoReader = new jsts.io.GeoJSONReader(), geoWriter = new jsts.io.GeoJSONWriter();
	var geometry = geoReader.read(geoInput).buffer(distance);
	var polygon = geoWriter.write(geometry);
	var oLanLng = [];
	var oCoordinates;
	oCoordinates = polygon.coordinates[0];
	for ( var i = 0; i < oCoordinates.length; i++) {
		var oItem;
		oItem = oCoordinates[i];
		oLanLng.push(new google.maps.LatLng(oItem[1], oItem[0]));
	}
	return oLanLng;
}

var tmpIntersectionMarker = undefined;
// var pathSelected = false;
function drawApath(l) {
	var pathCoor = [];
	var pathCoorPolygon = [];
	pathCoorPolygon.push([
			parseFloat(l.departure.entrances[0].coordinates.split(',')[1]),
			parseFloat(l.departure.entrances[0].coordinates.split(',')[0]) ]);
	pathCoor.push(getGoogleMapPosition(l.departure.entrances[0].coordinates));
	if (l.pathRoute != null && l.pathRoute.length > 0) {
		var tm = l.pathRoute.split("_");
		for ( var i = 0; i < tm.length; i++) {
			pathCoorPolygon.push([ parseFloat(tm[i].split(',')[1]),
					parseFloat(tm[i].split(',')[0]) ]);
			pathCoor.push(getGoogleMapPosition(tm[i]));
		}
	}

	pathCoorPolygon.push([
			parseFloat(l.destination.entrances[0].coordinates.split(',')[1]),
			parseFloat(l.destination.entrances[0].coordinates.split(',')[0]) ]);
	pathCoor.push(getGoogleMapPosition(l.destination.entrances[0].coordinates));
	var polygon = new google.maps.Polygon({
		paths : measurePolygonForAPath(pathCoorPolygon, l.width),
		strokeColor : "#081B2C",
		strokeOpacity : 0,
		fillColor : getPathTypeColorCode(l.pathType),
		fillOpacity : .66,
		customInfo : l.width + ";" + l.pathType,
		zIndex : 2
	});

	var polyTMP = new google.maps.Polygon({
		paths : measurePolygonForAPath(pathCoorPolygon, l.width),
		strokeColor : "#000000",
		strokeWeight : 1,
		fillOpacity : .66,
		fillColor : "#CCCCCC",
		zIndex : 666
	});

	var pathPolyline = new google.maps.Polyline({
		path : pathCoor,
		strokeColor : '#081B2C',
		strokeOpacity : 0.6,
		strokeWeight : pathWidthScale,
		customInfo : l.width + ";" + l.pathType,
		zIndex : 2
	});
	pathPolyline.id = l.pathId;
	polygon.id = l.pathId;
	polyTMP.id = l.pathId;
//	if (pathTypeIds.indexOf("9") > -1)
		polygon.addListener('click', function(event) {
			// if (pathSelected) {
			// hidePathInfo();
			// pathSelected = false;
			// } else {
			var lat = event.latLng.lat();
			var lng = event.latLng.lng();
			openPathEditPanel();
			selectAPath(l);
			openPathTypePopup();
			// pathSelected = true;
			// }
		});

	polyTMP.addListener('click', function(event) {
		if (!newPathInProgress || $("#departureId").val().length == 0)
			return;
		var lat = event.latLng.lat();
		var lng = event.latLng.lng();
		if ($('#destinationId').val().length <= 0
				&& $('#departureId').val().length > 0) {
			createAPointOnAnExistingPath(l, {
				x : parseFloat(lat),
				y : parseFloat(lng)
			}, pathPolyline);
		}
	});
	polyTMP.addListener('mousemove', function(event) {
		if (!newPathInProgress || $("#departureId").val().length == 0)
			return;
		if ($('#destinationId').val().length <= 0
				&& $('#departureId').val().length > 0) {
			updateMovingLine(event);
			var lat = event.latLng.lat();
			var lng = event.latLng.lng();
			var pos = {
				lat : parseFloat(lat),
				lng : parseFloat(lng)
			};
			if (tmpIntersectionMarker == null)
				tmpIntersectionMarker = new google.maps.Marker({
					map : map,
					icon : {
						path : google.maps.SymbolPath.CIRCLE,
						scale : 10,
						color : '#FFFFFF',
						fillOpacity : 1
					},
					position : pos,
					zIndex : 778,
					title : "Create New Intersetion"
				});
			else
				tmpIntersectionMarker.setPosition(pos);
		}
	});
	polyTMP.addListener('mouseover', function(event) {
		if (!newPathInProgress || $("#departureId").val().length == 0)
			return;
		if ($('#destinationId').val().length <= 0
				&& $('#departureId').val().length > 0) {
			updateMovingLine(event);
			var lat = event.latLng.lat();
			var lng = event.latLng.lng();
			var pos = {
				lat : parseFloat(lat),
				lng : parseFloat(lng)
			};
			if (tmpIntersectionMarker == null)
				tmpIntersectionMarker = new google.maps.Marker({
					map : map,
					icon : {
						path : google.maps.SymbolPath.CIRCLE,
						scale : 10,
						color : '#FFFFFF',
						fillOpacity : 1
					},
					position : pos,
					zIndex : 778,
					title : "Create New Intersetion"
				});
			else
				tmpIntersectionMarker.setPosition(pos);
			google.maps.event.addListener(tmpIntersectionMarker, "click",
					function(event) {
						openPathTypePopup();
						createAPointOnAnExistingPath(l, {
							x : parseFloat(lat),
							y : parseFloat(lng)
						}, pathPolyline);
					});
		}
	});
	polyTMP.addListener('mouseout', function(event) {
		if (tmpIntersectionMarker != null) {
			tmpIntersectionMarker.setMap(null);
			tmpIntersectionMarker = null;
		}
	});
	pathCreateNewPolygons.push(polyTMP);
	pathPolylines.push(pathPolyline);
	paths.push(polygon);
}

$(document).keyup(function(e) {
	if (e.keyCode == 27) {
		cancelCreatingNew();
	} else if (e.keyCode == 46) {
		removePath();
	}
});

function updateMovingLine(event) {
	// var pointPath = event.latLng;
	// var tmpPathCoor = [];
	// tmpPathCoor.push(lastOne);
	// tmpPathCoor.push(pointPath);
	// if (movingLine == null) {
	// movingLine = new google.maps.Polyline({
	// path : tmpPathCoor,
	// strokeColor : '#111',
	// strokeOpacity : 0.1,
	// zIndex : 1,
	// map : map
	// });
	// google.maps.event.addListener(movingLine, "click", function(event) {
	// addAPathInnerConnection(event);
	// });
	// google.maps.event.addListener(movingLine, "mousemove", function(event) {
	// updateMovingLine(event);
	// });
	// } else
	// movingLine.setPath(tmpPathCoor);
	// var km = movingLine.inKm();
	// if (pathPolylineConstant != null)
	// km += pathPolylineConstant.inKm();
	// km = km * 1000;
	// $("#pathLength").html(getTheLength(km));
	// movingLine.setMap(null);
	var pathCoorPolygon = [];
	pathCoorPolygon
			.push([ parseFloat(lastOne.lng()), parseFloat(lastOne.lat()) ]);
	pathCoorPolygon.push([ parseFloat(event.latLng.lng()),
			parseFloat(event.latLng.lat()) ]);
	if (movingLine == null) {
		movingLine = new google.maps.Polygon({
			paths : measurePolygonForAPath(pathCoorPolygon, $("#pathWidth")
					.val()),
			strokeColor : "#00FF00",
			strokeWeight : 1,
			fillColor : "#282B1C",
			zIndex : 10,
			map : map
		});
		google.maps.event.addListener(movingLine, "click", function(event) {
			addAPathInnerConnection(event);
		});
		google.maps.event.addListener(movingLine, "mousemove", function(event) {
			updateMovingLine(event);
		});
	} else
		movingLine.setPaths(measurePolygonForAPath(pathCoorPolygon, $(
				"#pathWidth").val()));
	// movingLine.setMap(null);
	// var km = movingLine.inKm();
	// if (pathPolylineConstant != null)
	// km += pathPolylineConstant.inKm();
	// km = km * 1000;
	// $("#pathLength").html(getTheLength(km));
}

function updateConstantLine() {
	var nextDestGPS = $("#pathLatLng").val().split("_");
	polylineConstantLength = 0;
	var tmpPathCoor = [];
	tmpPathCoor.push([ parseFloat($("#departureGPS").val().split(",")[1]),
			parseFloat($("#departureGPS").val().split(",")[0]) ]);
	if ($("#pathLatLng").val().length > 1)
		for ( var i = 0; i < nextDestGPS.length; i++) {
			tmpPathCoor.push([ parseFloat(nextDestGPS[i].split(",")[1]),
					parseFloat(nextDestGPS[i].split(",")[0]) ]);
			lastOne = getGoogleMapPosition(nextDestGPS[i]);
		}
	if ($("#destinationGPS").val().length > 0)
		tmpPathCoor.push([
				parseFloat($("#destinationGPS").val().split(",")[1]),
				parseFloat($("#destinationGPS").val().split(",")[0]) ]);
	if (tmpPathCoor.length <= 1)
		return;
	if (pathPolylineConstant != undefined)
		pathPolylineConstant.setMap(null);
	if (pathPolylineConstant == null)
		pathPolylineConstant = new google.maps.Polygon({
			paths : measurePolygonForAPath(tmpPathCoor, $("#pathWidth").val()),
			strokeColor : "#00FF00",
			strokeWeight : 1,
			fillColor : "#282B1C",
			zIndex : 10,
			map : map
		});
	else
		pathPolylineConstant.setPaths(measurePolygonForAPath(tmpPathCoor, $(
				"#pathWidth").val()));
	pathPolylineConstant.setMap(map);
}

// DRAW A TEMPORARY PATH WITH JOINTS
function addAPathInnerConnection(event) {
	var lat = event.latLng.lat();
	var lng = event.latLng.lng();
	if ($("#pathLatLng").val().length > 1)
		$("#pathLatLng").val($("#pathLatLng").val() + "_" + lat + "," + lng);
	else
		$("#pathLatLng").val(lat + "," + lng);
	updateConstantLine();
	// var bounds = new google.maps.LatLngBounds();
	// bounds.extend(marker.getPosition());
	mapDrawingClickCounter++;
}

function getTheLength(distance) {
	var Kilometres = Math.floor(distance / 1000);
	var Metres = Math.round(distance - (Kilometres * 1000));
	var res = "";
	if (Kilometres != 0)
		res = Kilometres + "." + Metres + " (Km) ";
	else
		res = Metres + " (m) ";
	return res;
}
