var pathMarkers = [];
var parentAreaPolygon;

function removeMarker() {
	locationEditPanelClose();
	$("#popupConfirmation_confirmBTN").attr("onclick", "removeLocationFn()");
	showPopupConfirmation('Are you sure you want to remove this property?');
}

function removeEntrance() {
	$("#popupConfirmation_confirmBTN").attr("onclick", "removeEntranceFn()");
	showPopupConfirmation('Are you sure you want to remove this entrance?');
}

var removeEntranceFn = function() {
	var url = "REST/GetLocationWS/RemoveEntrance?entranceIntersectionId="
			+ $("#locationId").val();
	var loadingContent = "Removing Entrance";
	$.ajax({
		url : url,
		cache : false,
		async : true,
		beforeSend : function() {
			ShowLoadingScreen(loadingContent);
		},
		success : function(data) {
			var id = $("#locationId").val();
			for ( var i = 0; i < pathMarkers.length; i++) {
				if (pathMarkers[i].id == id) {
					pathMarkers[i].setMap(null);
					pathMarkers.splice(i, 1);
				}
			}
			for ( var i = 0; i < markers.length; i++) {
				if (markers[i].id == id) {
					markers[i].setMap(null);
					markers.splice(i, 1);
				}
			}
			$(".locationFields").val("");
			// $('#locationEditMenu').popup('close');
			toast("The entrance removed");
		},
		complete : function() {
			HideLoadingScreen();
			hideLocationInfo();
		},
		error : function(xhr, ajaxOptions, thrownError) {
			popErrorMessage("An error occured while removing the marker. "
					+ thrownError);
		}
	});
	$("#popupConfirmation").popup("close");
};

var removeLocationFn = function() {
	var url = "REST/GetLocationWS/RemoveALocation?locationId="
			+ $("#locationId").val();
	var loadingContent = "Removing Proprty";
	$.ajax({
		url : url,
		cache : false,
		async : true,
		beforeSend : function() {
			ShowLoadingScreen(loadingContent);
		},
		success : function(data) {
			var id = $("#locationId").val();
			for ( var i = 0; i < polygons.length; i++) {
				if (polygons[i].id == id) {
					polygons[i].setMap(null);
					polygons.splice(i, 1);
					for ( var int = 0; int < polygonsEdit.length; int++) {
						polygonsEdit[int].setMap(null);
					}
				}
			}
			for ( var i = 0; i < polygons.length; i++) {
				if (polygons[i].id == id) {
					polygons[i].setMap(null);
					polygons.splice(i, 1);
				}
			}
			$(".locationFields").val("");
			// $('#locationEditMenu').popup('close');
			toast("The property removed");
		},
		complete : function() {
			HideLoadingScreen();
			hideLocationInfo();
		},
		error : function(xhr, ajaxOptions, thrownError) {
			popErrorMessage("An error occured while removing the marker. "
					+ thrownError);
		}
	});
	$("#popupConfirmation").popup("close");
};

function showPopupConfirmation(message) {
	$.mobile.changePage("#popupConfirmation");
	$("#popupConfirmation").trigger("create");
	$("#popupConfirmation").popup("open");
	$("#confirmationMessage").html(message);
}

function saveLocation() {
	if (selectedShape != null
			&& ($("#locationId").val() == "" || $("#locationId").val() <= 0)) {
		selectedShape.setMap(null);
		selectedShape = null;
	}
	if ($("#locationName").val() == "") {
		alert("Please select a name for the location");
		return -1;
	}
	if ($("#parentId").val() == "") {
		alert("Please select the parent");
		return -1;
	}
	var url = "REST/GetLocationWS/SaveUpdateLocation";
	$("#boundary").val(
			$("#boundary").val() + ";"
					+ (Math.random() * 0xFFFFFF << 0).toString(16) + ","
					+ (Math.random() * 0xFFFFFF << 0).toString(16));
	$("#boundaryColors").val($("#tempBoundaryColors").val());
	$.ajax({
		url : url,
		cache : false,
		async : true,
		dataType : 'text',
		type : 'POST',
		data : {
			icon : $("#icon").val(),
			locationName : $("#locationName").val(),
			parentId : $("#parentLocationId").val(),
			coordinate : $("#locationGPS").val(),
			locationTypeId : $("#locationTypeId").val(),
			locationId : $("#locationId").val(),
			description : $("#locationDescription").val(),
			plan : "",
			boundary : $("#boundary").val(),
			userName : $("#username").val()
		},
		beforeSend : function() {
			ShowLoadingScreen("Saving Location");
		},
		success : function(data) {
			data = JSON.parse(data);
			$("#locationId").val(data.locationID);
			addMarker(data);
			toast('Saved Successfully');
			if (selectedShape != null)
				selectedShape.setEditable(false);
			setInputsForLocation(data, data.coordinates);
		},
		complete : function() {
			HideLoadingScreen();
			closeAMenuPopup();
		},
		error : function(xhr, ajaxOptions, thrownError) {
			popErrorMessage("An error occured while saving the marker. "
					+ thrownError);
		},
	});
}

var str = "";
function getAllMarkers(parentId, refreshMarkers) {
	if(parentId == null || parentId.length <= 0)
		return;
	$("input[name='radio-choice']").checkboxradio();
	$("input[name='radio-choice']").checkboxradio('disable');
	setMapOnAllPolylines(null);
	if (!refreshMarkers) {// && markers.length > 0
		setMapOnAllPathMarkers(null);
		setMapOnAllMarkers(map);
		$("input[name='radio-choice']").checkboxradio('enable');
		hideLocationInfo();
		return;
	}
	var url = "REST/GetLocationWS/GetAllLocationsForUser?parentLocationId="
			+ parentId + "&locationTypeId=&userName=" + $("#username").val();
	$
			.ajax({
				url : url,
				cache : true,
				async : true,
				beforeSend : function() {
					ShowLoadingScreen("Fetching locations");
					setMapOnAllMarkers(null);
					setMapOnAllPolygons(null);
					for ( var int = 0; int < polygonsEdit.length; int++) {
						polygonsEdit[int].setMap(null);
					}
					if (parentAreaPolygon != null) {
						parentAreaPolygon.setMap(null);
						parentAreaPolygon = null;
					}
				},
				success : function(data) {
					str = "";
					markers = [];
					polygons = [];
					paths = [];
					pathPolylines = [];
					polygonsEdit = [];
					$.each(data, createMarkersOnMap);
					setMapOnAllPathMarkers(null);
					setMapOnAllMarkers(map);
					setMapOnAllPolygons(map);
				},
				complete : function() {
					HideLoadingScreen();
					$("input[name='radio-choice']").checkboxradio('enable');
					// $(".locationFields").val("");
					hideLocationInfo();
				},
				error : function(xhr, ajaxOptions, thrownError) {
					$("input[name='radio-choice']").checkboxradio('enable');
					popErrorMessage("An error occured while fetching the markers from the server. "
							+ thrownError);
				}
			});
}

function getIntoALocation(parentId) {
	$("input[name='radio-choice']").checkboxradio();
	$("input[name='radio-choice']").checkboxradio('disable');
	setMapOnAllPolylines(null);
	var url = "REST/GetLocationWS/GetChildrenOfALocation?parentLocationId="
			+ parentId + "&locationTypeId=&userName=" + $("#username").val();
	$
			.ajax({
				url : url,
				cache : true,
				async : true,
				beforeSend : function() {
					ShowLoadingScreen("Fetching locations");
					setMapOnAllMarkers(null);
					setMapOnAllPolygons(null);
					for ( var int = 0; int < polygonsEdit.length; int++) {
						polygonsEdit[int].setMap(null);
					}
					if (parentAreaPolygon != null) {
						parentAreaPolygon.setMap(null);
						parentAreaPolygon = null;
					}
				},
				success : function(data) {
					// console.log(data.length);
					str = "";
					markers = [];
					polygons = [];
					// pathMarkers = [];
					paths = [];
					pathPolylines = [];
					polygonsEdit = [];
					$.each(data, createMarkersOnMap);
					setMapOnAllPathMarkers(null);
					setMapOnAllMarkers(map);
					setMapOnAllPolygons(map);
				},
				complete : function() {
					HideLoadingScreen();
					$("input[name='radio-choice']").checkboxradio('enable');
					$(".locationFields").val("");
				},
				error : function(xhr, ajaxOptions, thrownError) {
					$("input[name='radio-choice']").checkboxradio('enable');
					popErrorMessage("An error occured while fetching the markers from the server. "
							+ thrownError);
				}
			});
}

function createMarkersOnMap(k, l) {
	if (k == 0) {
		getMarkerInfo(l);
		$("#parentLocationId").val(l.parentId);
		if (l.parent.boundary != null && l.parent.boundary.length > 3) {
			var bnd = l.parent.boundary.split(";")[0].split("_");
			var coordinatesArray = [];
			for ( var i = 0; i < bnd.length; i++) {
				coordinatesArray.push(getGoogleMapPosition(bnd[i]));
			}
			coordinatesArray.push(getGoogleMapPosition(bnd[0]));
			parentAreaPolygon = new google.maps.Polyline({
				path : coordinatesArray,
				strokeColor : "#FF0000",
				strokeOpacity : 1,
				strokeWeight : 1,
				zIndex : 1,
				map : map

			});
		}
		getParentLocationTypeId(null, l.locationType.locationTypeId);
		getLocationTypeDropDown(null);
	}
	addMarker(l);
}

function getMarkerInfo(location) {
	var c = 0;
	do {
		// if (location.parent.locationId > 0) {
		// str += "<li onclick='getIntoALocation(\""
		// + location.parent.locationID + "\")'>&nbsp;> "
		// + location.parent.locationName + " "
		// + location.parent.locationType.locationType + "</li>" + str;
		// } else
		if (c > 0)
			str = "<li onclick='getAllMarkers(\"" + $("#userLocationIds").val()
					+ "\", true)'>" + location.parent.locationName + " - </li>"
					+ str;
		else
			str = "<li onclick='getAllMarkers(\"" + location.parentId
					+ "\", true)'>" + location.parent.locationName + " - </li>"
					+ str;
		location = location.parent;
		c++;
	} while (location.parent != null);
	$("#infoListView").html(str).trigger("create").listview("refresh");
}

function addMarker(l) {
	var marker = new google.maps.Marker({
		icon : refreshMap(l.locationType.locationTypeId, l.coordinates, "normal"),
		// hovericon : refreshMap(l.locationType.locationTypeId, l.gps,
		// "hover"),
		// originalicon : refreshMap(l.locationType.locationTypeId, l.gps,
		// "normal"),
		draggable : true,
		zIndex : 1777,
		map : map,
		title : l.locationName + " " + l.locationType.locationType
	});
	// google.maps.event.addListener(marker, "mouseover", function() {
	// this.setIcon(this.hovericon);
	// });
	// google.maps.event.addListener(marker, "mouseout", function() {
	// this.setIcon(this.originalicon);
	// });
	var pos = {
		lat : parseFloat(l.coordinates.split(",")[0]),
		lng : parseFloat(l.coordinates.split(",")[1])
	};
	marker.id = l.locationID;
	marker.setPosition(pos);
	if (l.boundary != null && l.boundary.length > 16) {
		drawPolygons(l);
		// marker.setVisible(false);
		if (l.entrances != null && l.entrances.length > 0)
			for ( var int = 0; int < l.entrances.length; int++) {
				addEntrance(l.entrances[int]);
			}
		// marker.setVisible(false);
	}
	// else {
	// marker.setMap(map);
	// }

	marker.addListener('dragend', function(point) {
		//$("#locationGPS").val(point.latLng.lat() + "," + point.latLng.lng());
		$("#popupConfirmation").on("popupafterclose", function() {
			setTimeout(function() {
				marker.setPosition({
					lat : parseFloat(l.coordinates.split(",")[0]),
					lng : parseFloat(l.coordinates.split(",")[1])
				});
				$("#popupConfirmation").unbind("popupafterclose");
			}, 100);
		});
		$("#popupConfirmation_confirmBTN").on('click', function() {
			$("#popupConfirmation").unbind("popupafterclose");
			dragLocationMarker(l, point);
			$("#popupConfirmation").popup("close");
		});
		showPopupConfirmation('Are you sure you want to move this property?');
	});
	google.maps.event.addListener(marker, 'click', function(point) {
		showLocationInfo();
		locationEditPanelOpen(l.locationName, l.locationType.locationType);
		setInputsForLocation(l, l.coordinates);
	});
	if (markers.indexOf(marker) <= 0)
		markers.push(marker);
}

function dragLocationMarker(l, point) {
	setInputsForLocation(l, l.coordinates);
	$("#locationGPS").val(point.latLng.lat() + "," + point.latLng.lng());
	saveLocation();
}

function saveEntrance(entranceId) {
	var lName = "Entrance";
	if ($("#isEntranceIntersection").val() != "true")
		lName = "Intersection";
	if (parseInt($("#locationId").val()) <= 0
			|| $("#locationId").val().length == 0) {
		$("#isEntranceIntersection").val("true");
		var url = "REST/GetLocationWS/SaveUpdateLocation";
		$("#boundary").val(
				$("#boundary").val() + ";"
						+ (Math.random() * 0xFFFFFF << 0).toString(16) + ","
						+ (Math.random() * 0xFFFFFF << 0).toString(16));
		$("#boundaryColors").val($("#tempBoundaryColors").val());
		$
				.ajax({
					url : url,
					cache : false,
					async : false,
					dataType : 'text',
					type : 'POST',
					data : {
						icon : $("#icon").val(),
						locationName : $("#locationName").val(),
						parentId : $("#parentLocationId").val(),
						coordinate : $("#locationGPS").val(),
						locationTypeId : $("#locationTypeId").val(),
						locationId : 0,
						description : $("#locationDescription").val(),
						plan : "",
						boundary : $("#boundary").val(),
						userName : $("#username").val()
					},
					beforeSend : function() {
						ShowLoadingScreen("Saving Location");
					},
					success : function(data) {
						var dataJson = JSON.parse(data);
						addMarker(dataJson);
						// toast('Saved Successfully');
						if (selectedShape != null)
							selectedShape.setEditable(false);
						url = "REST/GetLocationWS/CreateTFCEntrance?entranceId="
								+ 0
								+ "&username="+$("#username").val()+"&parentId="
								+ dataJson.locationID
								+ "&locationName="
								+ lName
								+ "&isEntrance="
								+ $("#isEntranceIntersection").val()
								+ "&coordinate="
								+ entranceMarker.getPosition().lat()
								+ ","
								+ entranceMarker.getPosition().lng();
						$
								.ajax({
									url : url,
									cache : false,
									async : false,
									beforeSend : function() {
										ShowLoadingScreen("Saving Entrance");
									},
									success : function(dataEntrance) {
										google.maps.event
												.clearInstanceListeners(map);
										// closeAMenuPopup();
										$('#locationSaveCancelPanel').css(
												'display', 'none');
										addEntrance(dataEntrance);
										toast('Saved Successfully');
									},
									complete : function() {
										HideLoadingScreen();
									},
									error : function(xhr, ajaxOptions,
											thrownError) {
										popErrorMessage("An error occured while saving the marker. "
												+ thrownError);
									},
								});
					},
					complete : function() {
						HideLoadingScreen();
						cancelCreatingNew();
						return;
					},
					error : function(xhr, ajaxOptions, thrownError) {
						popErrorMessage("An error occured while saving the marker. "
								+ thrownError);
					},
				});
	} else {
		var url = "REST/GetLocationWS/CreateTFCEntrance?entranceId="
				+ entranceId + "&username="+$("#username").val()+"&parentId="
				+ $("#parentLocationId").val() + "&locationName=" + lName
				+ "&coordinate=" + $("#locationGPS").val() + "&isEntrance="
				+ $("#isEntranceIntersection").val();
		$.ajax({
			url : url,
			cache : false,
			async : true,
			beforeSend : function() {
				ShowLoadingScreen("Saving Entrance");
			},
			success : function(data) {
				google.maps.event.clearInstanceListeners(map);
				$('#locationSaveCancelPanel').css('display', 'none');
				addEntrance(data);
				toast('Saved Successfully');
			},
			complete : function() {
				HideLoadingScreen();
				hideLocationInfo();
			},
			error : function(xhr, ajaxOptions, thrownError) {
				popErrorMessage("An error occured while saving the marker. "
						+ thrownError);
			},
		});
	}
}

function addEntrance(l) {
	$("#locationGPS").val("");
	var intersectionEntrance = 5;
	if (l.entranceIntersection)
		intersectionEntrance = 11;
	var entrance = new google.maps.Marker({
		icon : refreshMap(intersectionEntrance, l.coordinates, "normal"),
		hovericon : refreshMap(intersectionEntrance, l.coordinates, "hover"),
		originalicon : refreshMap(intersectionEntrance, l.coordinates, "normal"),
		draggable : true,
		zIndex : 1777,
		map : map,
		title : l.description + l.entranceId
	});
	google.maps.event.addListener(entrance, "mouseover", function() {
		this.setIcon(this.hovericon);
	});
	google.maps.event.addListener(entrance, "mouseout", function() {
		this.setIcon(this.originalicon);
	});
	google.maps.event.addListener(entrance, "drag", function() {
		$("#locationId").val(l.entranceId);
		$("#isEntranceIntersection").val(l.entranceIntersection);
	});

	var pos = {
		lat : parseFloat(l.coordinates.split(",")[0]),
		lng : parseFloat(l.coordinates.split(",")[1])
	};
	entrance.id = l.entranceId;
	entrance.addListener('dragend', function(point) {
		var parentPolygon = null;
		for ( var i = 0; i < polygons.length; i++) {
			if (polygons[i].id == l.parentId)
				parentPolygon = polygons[i];
		}
		var oldPoint = {
			x : parseFloat(point.latLng.lat()),
			y : parseFloat(point.latLng.lng())
		};
		pointsInLine = [];
		if (l.entranceIntersection) {
			$(parentPolygon.getPath().getArray()).each(function(k, l) {
				var point = {
					x : parseFloat(l.lat()),
					y : parseFloat(l.lng())
				};
				pointsInLine.push(point);
			});
			pointsInLine.push({
				x : parseFloat(parentPolygon.getPath().getArray()[0].lat()),
				y : parseFloat(parentPolygon.getPath().getArray()[0].lng())
			});
			var intersectionpoint = getClosestPointOnLines(oldPoint,
					pointsInLine);
			this.setPosition({
				lat : parseFloat(intersectionpoint.x),
				lng : parseFloat(intersectionpoint.y)
			});
			$("#locationGPS").val(
					intersectionpoint.x + "," + intersectionpoint.y);
		} else
			$("#locationGPS").val(
					entrance.getPosition().lat() + ","
							+ entrance.getPosition().lng());
		setTimeout(function() {
			moveMarker(entrance, l);
			// if (confirm("Are you sure you want to move the marker?")) {
			// // $("#locationId").val(l.entranceId);
			// $("#locationName").val(l.description);
			// $("#parentLocationId").val(l.parentId);
			// saveEntrance(l.entranceId);
			// } else {
			// entrance.setPosition({
			// lat : parseFloat(l.gps.split(",")[0]),
			// lng : parseFloat(l.gps.split(",")[1])
			// });
			// }
			// ;
		}, 200);

	});
	entrance.setPosition(pos);
	google.maps.event.addListener(entrance, 'click',
			function(point) {
				if ($('[name="optionType"] :radio:checked').val() == "marker") {
					hideLocationInfo();
					showLocationInfo();
					locationEntranceIntersectionEditPanelOpen(l.description,
							"Entrance");
					$("#locationId").val(l.entranceId);
				} else {
					addAPath(l);
				}
			});
	if (pathMarkers.indexOf(entrance) <= 0) {
		pathMarkers.push(entrance);
	}
	if (markers.indexOf(entrance) <= 0 && l.entranceIntersection) {
		markers.push(entrance);
	}
}

function moveMarker(entrance, l) {
	// $("#popupConfirmation_cancelBTN").attr("onclick", "moveMarkerCancelFn(" +
	// entrance +"," + l + ")");
	$("#popupConfirmation").on("popupafterclose", function() {
		setTimeout(function() {
			entrance.setPosition({
				lat : parseFloat(l.coordinates.split(",")[0]),
				lng : parseFloat(l.coordinates.split(",")[1])
			});
			$("#popupConfirmation").unbind("popupafterclose");
		}, 100);
	});
	$("#popupConfirmation_confirmBTN").attr(
			"onclick",
			"moveMarkerFn('" + l.description + "', '" + l.parentId + "', '"
					+ l.entranceId + "')");
	showPopupConfirmation('Are you sure you want to move the entrance?');
}

var moveMarkerFn = function(description, parentId, entranceId) {
	$("#popupConfirmation").unbind("popupafterclose");
	// $("#locationId").val(l.entranceId);
	$("#locationName").val(description);
	$("#parentLocationId").val(parentId);
	saveEntrance(entranceId);
	$("#popupConfirmation").popup("close");
};

// var moveMarkerCancelFn = function(entrance,l) {
// entrance.setPosition({
// lat : parseFloat(l.gps.split(",")[0]),
// lng : parseFloat(l.gps.split(",")[1])
// });
// };

function setMapOnAllMarkers(value) {
	for ( var i = 0; i < markers.length; i++) {
		markers[i].setMap(value);
	}
}

function setMapOnAllPathMarkers(value) {
	for ( var i = 0; i < pathMarkers.length; i++) {
		pathMarkers[i].setMap(value);
	}
}

function setInputsForLocation(location, gps) {
	$("#upload").val("");
	$("#main-cropper").empty();
	google.maps.event.clearInstanceListeners(map);
	$("#locationName").val(location.locationName);
	$("#locationGPS").val(location.coordinates);
	if (location.boundary != null && location.boundary.length > 16)
		$("#boundary").val(getArrayBoundary(location.boundary));
	if (location.boundary == null) {
		$("#tempBoundaryColors").val("1E90FF,1E90FF");
		$("#boundaryColors").val("1E90FF,1E90FF");
	} else {
		$("#tempBoundaryColors").val(getBoundaryColour(location.boundary));
		$("#boundaryColors").val(getBoundaryColour(location.boundary));
	}
	$("#locationId").val(location.locationID);
	$("#croppedIcon").attr("src", location.icon);
	$("#icon").val(location.icon);
	$("#locationLabel").val(location.locationName);
	$("#locationInfoDescriptionLabel").val(location.description);
	$("#locationDescription").val(location.description);
	$("#locationThumbnail").val(location.icon);
	$("#locationTypeLabel").val(location.locationType.locationType);
	var icn = location.icon;
	if (icn != null && icn.length > 5)
		$("#editIconIcon").attr("src", location.icon);
	else
		$("#editIconIcon").attr("src", "images/icons/image.png");
	$("#parentLocationId").val(location.parentId);
	$("#locationTypeId").val(location.locationType.locationTypeId);
}

function showMarkerLabel(text, posX, posY, isPresentUnderneath) {
	$("#googleMapMarkerLabel").html(text);
	$('#googleMapMarkerLabel').css("display", "block");
	$('#googleMapMarkerLabel').css("position", "absolute").trigger("create");
	if (isPresentUnderneath) {
		$('#googleMapMarkerLabel').css("top", posY + 25 + 'px');
		if (posX - $("#googleMapMarkerLabel").width() < 0) {
			$('#googleMapMarkerLabel').css("left", '4px');
		} else if (posX + $("#googleMapMarkerLabel").width() > $(window)
				.width()) {
			// $('#googleMapMarkerLabel').css("right", '3px');
			$('#googleMapMarkerLabel').css(
					"left",
					$(window).width() - $("#googleMapMarkerLabel").width() - 22
							+ 'px');
		} else
			$('#googleMapMarkerLabel').css("left",
					posX - ($("#googleMapMarkerLabel").width() / 2) + 'px');
	} else {
		if (posY < $(window).height() / 2) {
			$('#googleMapMarkerLabel').css("top", posY + 66 + 'px');
		} else {
			$('#googleMapMarkerLabel').css("top", posY - 66 + 'px');
		}
		if (posX - ($("#googleMapMarkerLabel").width() / 2) < 0) {
			$('#googleMapMarkerLabel').css("left", '4px');
		} else if ($(window).width() - posX < $("#googleMapMarkerLabel")
				.width()) {
			// $('#googleMapMarkerLabel').css("right", '0px');
			$('#googleMapMarkerLabel').css(
					"left",
					$(window).width() - $("#googleMapMarkerLabel").width() - 22
							+ 'px');
		} else
			$('#googleMapMarkerLabel').css("left",
					posX - ($("#googleMapMarkerLabel").width() / 2) + 'px');
	}
	$('#googleMapMarkerLabel').trigger("create");
}

function clearMarkerLabel() {
	$('#googleMapMarkerLabel').css("display", "none");
}
