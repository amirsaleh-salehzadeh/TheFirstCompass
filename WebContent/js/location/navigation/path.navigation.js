var map, marker, markerDest, markerDepart;
var pathPolylineConstant, pathPolygonConstant, pathPolylineTrack, polylineConstantLength;
var walkingTimer, speed, speedTimer, heading, walkingWatchID, speedWatchID, altitude, isLocationAvailable;
var distanceToNextPosition, distanceToDestination, angleToNextDestination;
var paths = [];
var locationPolygons = [];
var locationPolylines = [];
var ajaxCallSearch;

function toast(msg) {
	$(
			"<div class='ui-loader ui-body-e ui-corner-all'><h3>" + msg
					+ "</h3></div>").css({
		display : "block",
		opacity : 1,
		position : "fixed",
		"background-color" : "rgb(8, 27, 44)",
		padding : "7px",
		color : "rgb(248, 182, 36)",
		"text-align" : "center",
		"text-shadow" : "none",
		width : "270px",
		"border" : "3px solid rgb(248, 182, 36)",
		left : ($(window).width() - 284) / 2,
		top : $(window).height() / 2
	}).appendTo($.mobile.pageContainer).delay(2000).fadeOut(1000, function() {
		$(this).remove();
	});
}

function KeyPress(e) {
	var eventKeys = window.event ? event : e;
	if (popupopen == false) {
		if (eventKeys.keyCode == 27) {
			return false;
		}
	} else {
		if (eventKeys.keyCode == 27) {
			closePopup();
		}
	}
	if (eventKeys.keyCode == 86 && eventKeys.ctrlKey && eventKeys.shiftKey) {
		if ($("#visitorCounter").css("display") == "none") {
			$("#visitorCounter").css("display", "block");
			$("#visitorCounter").css("display");
		} else {
			$("#visitorCounter").css("display", "none");
			$("#visitorCounter").css("display");
		}
	}
}

// starting a trip, fetches the path to destination, creates a trip and draw
// polylines
function getThePath() {
	var GPSCook = getCookie("TripPathIdsCookie");
	if (GPSCook != "") {
		$("#destinationId").val(
				getCookie("TripPathIdsCookie").split("_")[getCookie(
						"TripPathIdsCookie").split("_").length - 1]);
		if ($("#departureId").val().length <= 0)
			$("#departureId").val(getCookie("TripPathIdsCookie").split("_")[0]);
		if ($("#pathType").val().length <= 0)
			setPathType(getCookie("TripPathTypeCookie"), null);
	}
	if ($("#destinationId").val() == $("#departureId").val()) {
		if (markerDepart != null) {
			markerDepart.setMap(null);
		}
		errorMessagePopupOpen("The departure and destination can not be the same.");
		removeTrip();
		return;
	}
	var url = "REST/GetPathWS/GetADirectionFromTo?clientName=NMMU&departureId="
			+ $("#departureId").val() + "&destinationId="
			+ $("#destinationId").val() + "&from=" + $("#from").val() + "&to="
			+ $("#to").val() + "&pathType=" + $("#pathType").val();
	var dataLength = 0;
	$
			.ajax({
				url : url,
				cache : true,
				async : true,
				beforeSend : function() {
					showBottomPanel();
					$("#locationInf").html('');
					$(".spinnerLoading").css('display', 'block').trigger(
							"create");
				},
				success : function(data) {
					var pathIds = "";
					var pathWidths = "";
					var pathGPSs = "";
					var pathLocations = "";
					dataLength = data.length;
					if (dataLength == 0) {
						if ($("#pathType").val() == 6) {
							errorMessagePopupOpen("There is no route yet for your specific request. <br/>"
									+ "You can try this alternative path."
									+ "<br/>We recorded your selection and will update our database accordingly.");
							setCookie('TripPathTypeCookie', 2, 1);
							$(".pathTypeMethodButton").css("background-color",
									"rgb(8, 27, 44)");
							setPathType(2, null);
							var from = $("#from").val();
							var to = $("#destinationId").val();
							var dep = $("#departureId").val();
							removeTrip();
							findMyLocation();
							$("#from").val(from);
							$("#destinationId").val(to);
							$("#departureId").val(dep);
							getThePath();
						} else {
							errorMessagePopupOpen("There are currently no routes for this selection in the system. "
									+ "<br/>We recorded your selection and will update our database accordingly.");
						}
					} else {
						$
								.each(
										data,
										function(k, l) {
											if (k == 0) {
												pathIds = l.depL.id + "_"
														+ l.desL.id;
												pathWidths = l.width;
												if (l.pathRoute != null
														&& l.pathRoute.length > 0) {
													pathGPSs += l.depL.g
															.replace(" ", "")
															+ "_"
															+ l.pathRoute
															+ "_"
															+ l.desL.g.replace(
																	" ", "");
												} else {
													pathGPSs += l.depL.g
															.replace(" ", "")
															+ "_"
															+ l.desL.g.replace(
																	" ", "");
												}
												pathLocations += l.depL.n + "_"
														+ l.desL.n;
											} else {
												if (l.pathRoute != null
														&& l.pathRoute.length > 0) {
													pathGPSs += "_"
															+ l.pathRoute
															+ "_"
															+ l.desL.g.replace(
																	" ", "");
												} else {
													pathGPSs += "_"
															+ l.desL.g.replace(
																	" ", "");
												}
												pathIds += "_" + l.depL.id
														+ "_" + l.desL.id;
												pathWidths += "_" + l.width;
												pathLocations += "_" + l.desL.n;
												$("#destinationDef").html(
														l.desL.n);
											}
										});
						// if ($("#to").val().length > 2) {
						// var destPoint = getGoogleMapPosition($("#to").val());
						// if (markerDest != null)
						// markerDest.setMap(null);
						// markerDest = new google.maps.Marker(
						// {
						// position : destPoint,
						// map : map,
						// icon : 'images/icons/map-markers/marker-orange.png'
						// });
						// }
						$("#departureId").val(pathIds.split("_")[0]);
						$("#destinationId")
								.val(
										pathIds.split("_")[pathIds.split("_").length - 1]);
						setCookie('TripPathIdsCookie', pathIds, 1);
						setCookie('TripPathWidthsCookie', pathWidths, 1);
						setCookie('TripPathGPSCookie', pathGPSs, 1);
						$("#tripRouteGPSCoordinates").val(pathGPSs);
						setCookie('TripPathLocationsCookie', pathLocations, 1);
						$("#tripRouteLocationNames").val(pathLocations);
						setCookie('TripPathTypeCookie', $("#pathType").val(), 1);
						if (markerDest != null)
							markerDest.setMap(map);
						else
							markerDest = new google.maps.Marker(
									{
//										position : getGoogleMapPosition(pathGPSs
//												.split("_")[pathGPSs.split("_").length - 1]),
										map : map,
										icon : 'images/icons/map-markers/marker-orange.png',
										zIndex : 12
									});
						markerDest.setPosition(getGoogleMapPosition(pathGPSs
								.split("_")[pathGPSs.split("_").length - 1]));
						markerDepart.setMap(map);
						markerDepart
								.setPosition(getGoogleMapPosition(pathGPSs.split("_")[0]));
						resetWalking();
						drawConstantPolyline();
						hideBottomPanel();
						setTimeout(showViewItems(), 1500);
						getTripInfo();
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					setPathType(2, null);
					hideBottomPanel();
					removeTrip();
					errorMessagePopupOpen(thrownError);
				}
			});
}

function getNextPosition() {
	// var tripGPSs = getCookie("TripPathGPSCookie").split("_");
	var tripGPSs = $("#tripRouteGPSCoordinates").val().split("_");
	var closestDest = 0;
	var closestId = 0;
	var curGPS = marker.getPosition();
	if (tripGPSs.length > 1)
		for ( var int = 0; int < tripGPSs.length; int++) {
			var dist = getDistance(curGPS.lat() + "," + curGPS.lng(),
					tripGPSs[int]);
			if (int == 0)
				closestDest = dist;
			if (dist > 5)
				continue;
			if (dist < closestDest) {
				closestDest = dist;
				closestId = int;
			}
		}
	return closestId;
}

function removeTheNextDestination(closestId) {
	var tripIds = getCookie("TripPathIdsCookie").split(",");
	var tripGPSs = $("#tripRouteGPSCoordinates").val().split("_");
	// getCookie("TripPathGPSCookie").split("_");
	var tripLocations = $("#tripRouteLocationNames").val().split("_");
	// getCookie("TripPathLocationsCookie").split("_");
	var tripPathWidths = getCookie("TripPathWidthsCookie").split("_");
	var removeVarGPS = "";
	var removeVarIDS = "";
	var removeVarNames = "";
	var removePathWidths = "";
	if (tripGPSs.length > 1)
		for ( var int = closestId; int < tripGPSs.length - 1; int++) {
			if (int == closestId) {
				removeVarGPS = tripGPSs[int + 1];
				removeVarIDS = tripIds[int + 1];
				removeVarNames = tripLocations[int + 1];
				removePathWidths = tripPathWidths[int + 1];
			} else {
				removeVarGPS += "_" + tripGPSs[int + 1];
				removeVarIDS += "," + tripIds[int + 1];
				removeVarNames += "_" + tripLocations[int + 1];
				removePathWidths += "_" + tripPathWidths[int + 1];
			}
		}
	setCookie("TripPathIdsCookie", getCookie("TripPathIdsCookie").replace(
			removeVarIDS + ",", ""), 1);
	setCookie("TripPathGPSCookie", removeVarGPS, 1);
	$("#tripRouteGPSCoordinates").val(removeVarGPS);
	setCookie("TripPathLocationsCookie", $("#tripRouteLocationNames").val()
			.replace(removeVarNames + "_", ""), 1);
	$("#tripRouteLocationNames").val(
			$("#tripRouteLocationNames").val()
					.replace(removeVarNames + "_", ""));
	setCookie("TripPathWidthsCookie", getCookie("TripPathWidthsCookie")
			.replace(removePathWidths + "_", ""), 1);
	if (pathPolylineTrack != undefined)
		pathPolylineTrack.setMap(null);
	if (pathPolylineConstant != undefined)
		pathPolylineConstant.setMap(null);
	pathPolylineTrack = null;
	pathPolylineConstant = null;
	// if (getCookie("TripPathGPSCookie").length > 1)
	// drawConstantPolyline();
	if ($("#tripRouteGPSCoordinates").val().length > 1)
		drawConstantPolyline();

}

function drawConstantPolyline() {
	var tmpPathCoor = [];
	var nextDestGPS = $("#tripRouteGPSCoordinates").val().split("_");
	// getCookie("TripPathGPSCookie").split("_");
	polylineConstantLength = 0;
	var bounds = new google.maps.LatLngBounds();
	var pathCoorPolygon = [];
	if (nextDestGPS.length > 1)
		for ( var i = 0; i < nextDestGPS.length; i++) {
			if (i < nextDestGPS.length - 1)
				polylineConstantLength += getDistance(nextDestGPS[i],
						nextDestGPS[i + 1]);
			tmpPathCoor.push(getGoogleMapPosition(nextDestGPS[i]));
			bounds.extend(getGoogleMapPosition(nextDestGPS[i]));
			pathCoorPolygon.push([ parseFloat(nextDestGPS[i].split(',')[1]),
					parseFloat(nextDestGPS[i].split(',')[0]) ]);
		}
	else
		return;
	bounds.getCenter();
	map.fitBounds(bounds);
	if (pathPolygonConstant == null)
		pathPolygonConstant = new google.maps.Polygon({
			fillColor : "#F7AF23",
//			fillColor : "#081B2C",
			fillOpacity : 1,
			zIndex : 4,
			strokeOpacity : 0,
			map : map
		});
	// else
	pathPolygonConstant.setPaths(measurePolygonForAPath(pathCoorPolygon, 5));
	var lineSymbol2 = {
		path : google.maps.SymbolPath.FORWARD_CLOSED_ARROW,
		scale : 5,
		strokeColor : '#F7AF23',
//		strokeColor : '#081B2C',
		zIndex : 9,
		strokeOpacity : 1
	};
	// if (pathPolylineConstant != undefined)
	// pathPolylineConstant.setMap(null);
	if (pathPolylineConstant == null)
		pathPolylineConstant = new google.maps.Polyline({
			geodesic : true,
			icons : [ {
				icon : lineSymbol2,
				offset : '100%',
				repeat : '222px'
			} ],
			strokeColor : '#F7AF23',
			strokeOpacity : 0.3,
			strokeWeight : 1,
			zIndex : 11,
			map : map
		});
	// else
	pathPolylineConstant.setPath(tmpPathCoor);
	// pathPolylineConstant.setMap(map);
	// paths.push(pathPolylineConstant);
	animateArrow(pathPolylineConstant);
	resetWalking();
	$('#searchLeftSideBTN').css("display", "none");
}

function updatePolyLine(currentPos, altitude) {
	var pointPath = new google.maps.LatLng(parseFloat(currentPos.lat),
			parseFloat(currentPos.lng));
	var tmpPathCoor = [];
	var nextDestGPS = $("#tripRouteGPSCoordinates").val().split("_");
	// getCookie("TripPathGPSCookie").split("_");
	// //////////////////////////////////////////////////////////////////////////////////////
	var nextPoitionIndex = getNextPosition();
	var nextPosition = getGoogleMapPosition(nextDestGPS[nextPoitionIndex]);
	distanceToNextPosition = google.maps.geometry.spherical
			.computeDistanceBetween(pointPath, nextPosition);
	if (nextPoitionIndex > 0 || distanceToNextPosition < 10) {
		removeTheNextDestination(nextPoitionIndex);
	}
	tmpPathCoor.push(pointPath);
	tmpPathCoor.push(nextPosition);

	var headingTo1st = google.maps.geometry.spherical.computeHeading(pointPath,
			nextPosition);
	if (nextDestGPS.length > 1) {
		var secondNextPosition = getGoogleMapPosition(nextDestGPS[1]);
		var headingTo2st = google.maps.geometry.spherical.computeHeading(
				nextPosition, secondNextPosition);
		angleToNextDestination = headingTo2st - headingTo1st;
		heading = angleToNextDestination;
		$("#distanceToNextPoint").html("In " + getDistanceLeft(distanceToNextPosition));
		displayImage(getAngleDirection(angleToNextDestination));
	}
	if (pathPolylineTrack == null) {
		var lineSymbol = {
			path : 'M 0,-1 0,1',
			strokeOpacity : .5,
			scale : 2
		};
		pathPolylineTrack = new google.maps.Polyline({
			path : tmpPathCoor,
			icons : [ {
				icon : lineSymbol,
				offset : '0',
				repeat : '20px'
			} ],
			strokeColor : 'white',
			strokeOpacity : 0,
			strokeWeight : 0,
			map : map
		});
		// paths.push(pathPolylineTrack);
		animateArrow(pathPolylineTrack);
	} else
		pathPolylineTrack.setPath(tmpPathCoor);
	pathPolylineTrack.setMap(null);
	pathPolylineTrack.setMap(map);
}

function resetWalking() {
	if (walkingWatchID != undefined) {
		navigator.geolocation.clearWatch(walkingWatchID);
		walkingWatchID = null;
	}
	walkToDestination();
}

function findMyLocation() {
	if (isLocationAvailable == null || isLocationAvailable == true)
		if (navigator.geolocation) {
			navigator.geolocation.getCurrentPosition(successGetCurrentPosition,
					errorHandler, {
						enableHighAccuracy : true,
						maximumAge : 1000
					});
		} else {
			handleLocationError(false, map.getCenter());
		}
}

function setOnMyLocation() {
	findMyLocation();
	if (marker != null) {
		map.setZoom(17.3);
//		map.panTo(marker.getPosition());
		map.setCenter(marker.getPosition());
	}
}

function walkToDestination() {
	if (walkingWatchID != undefined) {
		navigator.geolocation.clearWatch(walkingWatchID);
		walkingWatchID = null;
	}
	if (isLocationAvailable == null || isLocationAvailable == true)
		if (navigator.geolocation) {
			walkingWatchID = navigator.geolocation.watchPosition(
					successTrackingHandler, errorHandler, {
						enableHighAccuracy : true
					});
		} else {
			handleLocationError(false, map.getCenter());
		}
}

function removeTrip() {
	// for ( var i = 0; i < paths.length; i++) {
	// if (paths[i] != undefined)
	// paths[i].setMap(null);
	// }
	if (pathPolylineTrack != undefined)
		pathPolylineTrack.setMap(null);
	if (pathPolylineConstant != undefined)
		pathPolylineConstant.setMap(null);
	if (pathPolygonConstant != undefined)
		pathPolygonConstant.setMap(null);
	pathPolylineTrack = null;
	pathPolylineConstant = null;
	pathPolygonConstant = null;
	if (walkingWatchID != undefined) {
		navigator.geolocation.clearWatch(walkingWatchID);
		walkingWatchID = null;
	}
	setCookie('TripIdCookie', "", 0);
	setCookie('TripPathIdsCookie', "", 0);
	setCookie('TripPathGPSCookie', "", 0);
	setCookie('TripPathLocationsCookie', "", 0);
	setCookie('TripPathWidthsCookie', "", 0);
	setCookie('TripPathTypeCookie', "", 0);

	$("#from").val("");
	$("#departureId").val("");
	$("#to").val("");
	$("#destinationId").val("");
	$("#to").val("");
	$("#tripLocations").val("");
	$("#tripRouteGPSCoordinates").val("");
	$("#destinationName").val("");
	$('#searchLeftSideBTN').css("display", "block");
	if (markerDepart != null)
		markerDepart.setMap(null);
	if (markerDest != null)
		markerDest.setMap(null);
	markerDest = null;
	// markerDepart = null;
	distanceToDestination = 0;
	findMyLocation();
	clearSearchBTN();
	showViewItems();
}

var myLatLng = {
	lat : -34.009211,
	lng : 25.669051
};

var errorHandler = function(errorObj) {
	errorMessagePopupOpen("MapBuddy cannot access your location system. <br>"
			+ "Please enable it in the browser settings to allow MapBuddy to navigate you to a destination <br>");
	isLocationAvailable = false;
	navigator.permissions.query({
		name : 'geolocation'
	}).then(function(p) {
		updatePermission('geolocation', p.state);

		p.onchange = function() {
			updatePermission('geolocation', this.state);
		};
	});
};

function handleLocationError(browserHasGeolocation, pos) {
	errorMessagePopupOpen(browserHasGeolocation ? 'Error: The Geolocation service failed.'
			: 'Error: Your browser doesn\'t support geolocation.');
	isLocationAvailable = false;
	navigator.permissions.query({
		name : 'geolocation'
	}).then(function(p) {
		updatePermission('geolocation', p.state);

		p.onchange = function() {
			updatePermission('geolocation', this.state);
		};
	});

}
function updatePermission(name, state) {
	console.log('update permission for ' + name + ' with ' + state);
}

function isFullScreen() {
	return (document.fullScreenElement && document.fullScreenElement !== null)
			|| document.mozFullScreen || document.msFullScreen
			|| document.webkitIsFullScreen;
}

function requestFullScreen(element) {
	if (element.requestFullscreen)
		element.requestFullscreen();
	else if (element.msRequestFullscreen)
		element.msRequestFullscreen();
	else if (element.mozRequestFullScreen)
		element.mozRequestFullScreen();
	else if (element.webkitRequestFullscreen)
		element.webkitRequestFullscreen();
}

function exitFullScreen() {
	if (document.exitFullscreen)
		document.exitFullscreen();
	else if (document.msExitFullscreen)
		document.msExitFullscreen();
	else if (document.mozCancelFullScreen)
		document.mozCancelFullScreen();
	else if (document.webkitExitFullscreen)
		document.webkitExitFullscreen();
}

function animateArrow(line) {
	var count = 0;
	window.setInterval(function() {
		count = (count + 1) % 200;
		var icons = line.get('icons');
		icons[0].offset = (count / 2) + '%';
		line.set('icons', icons);
	}, 100);
}

function animateCircle(line) {
	var count = 0;
	window.setInterval(function() {
		count = (count + 1) % 200;
		var icons = line.get('icons');
		icons[0].offset = (count / 2) + '%';
		line.set('icons', icons);
	}, 100);
}

var successTrackingHandler = function(position) {
	isLocationAvailable = true;
	var currentPos = {
		lat : position.coords.latitude,
		lng : position.coords.longitude
	};
	$("#from").val(position.coords.latitude + "," + position.coords.longitude);
	if (position.coords.heading != null) {
		heading = position.coords.heading;
	}
	// if (position.coords.speed != null) {
	// speed = position.coords.speed;
	// if (speed != null)
	// speed = Math.round(speed);
	// }
	altitude = position.coords.altitude;
	if (marker == null) {
		marker = new google.maps.Marker({
			position : currentPos,
			map : map,
			draggable : false
		});
		if (heading == null)
			heading = 90;
		marker.setIcon({
			path : google.maps.SymbolPath.FORWARD_CLOSED_ARROW,
			scale : 6,
			rotation : heading,
			zIndex : 9,
			strokeOpacity : 1,
			fillColor : '#3878c7',
			fillOpacity : 1,
			strokeColor : '#3878c7',
			strokeOpacity : 1,
		});
		marker.addListener('click', function() {
			map.setZoom(18);
			map.setCenter(this.getPosition());
			resetWalking();
		});
	} else {
		marker.setPosition(currentPos);
		if (heading == null)
			heading = 90;
		marker.setIcon({
			path : google.maps.SymbolPath.FORWARD_CLOSED_ARROW,
			scale : 6,
			rotation : heading,
			zIndex : 9,
			strokeOpacity : 1,
			fillColor : '#3878c7',
			fillOpacity : 1,
			strokeColor : '#3878c7',
			strokeOpacity : 1,
		});
	}
	updatePolyLine(currentPos, altitude);
};

// var circleMarker;
var successGetCurrentPosition = function(position) {
	isLocationAvailable = true;
	var currentPos = {
		lat : position.coords.latitude,
		lng : position.coords.longitude
	};
	$("#from").val(position.coords.latitude + "," + position.coords.longitude);
	if (marker == null) {
		marker = new google.maps.Marker({
			position : currentPos,
			map : map,
			draggable : false
		});
		if (heading == null)
			heading = 90;
		marker.setIcon({
			path : google.maps.SymbolPath.FORWARD_CLOSED_ARROW,
			scale : 6,
			rotation : heading,
			zIndex : 9,
			strokeOpacity : 1,
			fillColor : '#3878c7',
			fillOpacity : 1,
			strokeColor : '#3878c7',
			strokeOpacity : 1,
		});
		marker.addListener('click', function() {
			map.setZoom(18);
			map.setCenter(this.getPosition());
			findMyLocation();
		});
	} else {
		if (heading == null)
			heading = 90;
		marker.setIcon({
			path : google.maps.SymbolPath.FORWARD_CLOSED_ARROW,
			scale : 6,
			rotation : heading,
			zIndex : 9,
			strokeOpacity : 1,
			fillColor : '#3878c7',
			fillOpacity : 1,
			strokeColor : '#3878c7',
			strokeOpacity : 1,
		});
		marker.setPosition(currentPos);
	}
};

$(document).ready(function() {
	showHideLeftSideMenu();
	document.onkeydown = KeyPress;
});

function setPathType(pathTypeId, div) {
	$("#pathType").val(pathTypeId);
	$(".pathTypeMethodButton").css("background-color", "rgb(8, 27, 44)");
	if (div == null) {
		var tmpType = getCookie("TripPathTypeCookie");
		if (tmpType == 1)
			$("#dirtRoadType").css("background-color", "rgb(248, 182, 36)");
		else if (pathTypeId == 6)
			$("#wheelChairType").css("background-color", "rgb(248, 182, 36)");
		else
			$("#walkwayType").css("background-color", "rgb(248, 182, 36)");
		return;
	} else
		$(div).css("background-color", "rgb(248, 182, 36)");
	var from = $("#from").val();
	var to = $("#destinationId").val();
	var dep = $("#departureId").val();
	removeTrip();
	findMyLocation();
	$("#from").val(from);
	$("#destinationId").val(to);
	$("#departureId").val(dep);
	getThePath();
}