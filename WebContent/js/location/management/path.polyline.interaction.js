//function createIntersection() {
//	$("#popupConfirmation_cancelBTN").attr("onclick", "createIntersectionCancelFn()");
//	showPopupConfirmation('Create intersection?');
//}
//
//var createIntersectionCancelFn = function() {
//	return;
//}

function createAPointOnAnExistingPath(path, destinationGPS, polyline) {
//	if (!confirm("create intersection?")) {
//		return;
//	}
	$("#popupConfirmation").on("popupafterclose", function() {
		setTimeout(function() {
			$("#popupConfirmation").unbind("popupafterclose");
			return;
		}, 100);
	});
	showPopupConfirmation('Create intersection?');
	if ($("#pathTypeIds").val().length <= 0) {
		alert("Select Path Type");
		return;
	}
	// All points on the polyline
	var pointsInLine = [];
	$(polyline.getPath().getArray()).each(function(k, l) {
		var point = {
			x : parseFloat(l.lat()),
			y : parseFloat(l.lng())
		};
		pointsInLine.push(point);
	});
	var intersectionpoint = getClosestPointOnLines(destinationGPS, pointsInLine);
	var url = "REST/GetPathWS/CreateAPointOnThePath?pathId=" + path.pathId
			+ "&pointGPS=" + intersectionpoint.x + "," + intersectionpoint.y
			+ "&intersectionEntranceParentId=" + $("#parentLocationId").val()
			+ "&index=" + intersectionpoint.i + "&newPathRoute="
			+ $("#pathLatLng").val() + "&departureId="
			+ $("#departureId").val() + "&pathType=" + $("#pathTypeIds").val()
			+ "&width=" + $("#pathWidth").val() + "&pathName="
			+ $("#pathName").val() + "&description="
			+ $("#pathDescription").val();
	var intersectId = 0;
	$.ajax({
		url : url,
		cache : false,
		async : true,
		beforeSend : function() {
			ShowLoadingScreen("Creating an Intersection on the Path");
		},
		success : function(data) {
			$.each(data, function(k, l) {
				drawApath(l);
				if (k == 0)
					intersectId = l.destination.entrances[0].entranceId;
			});
			for ( var i = 0; i < paths.length; i++) {
				if (paths[i] != null && paths[i].id == path.pathId) {
					paths[i].setMap(null);
					paths.splice(i, 1);
					pathCreateNewPolygons[i].setMap(null);
					pathCreateNewPolygons.splice(i, 1);
				}
			}
			toast("Successful");

			// var intmpIntersectionMarker = new google.maps.Marker({
			// map : map,
			// icon : {
			// path : google.maps.SymbolPath.CIRCLE,
			// scale : 10,
			// strokeColor : 'black',
			// fillOpacity : 1
			// },
			// labelStyle : {
			// opacity : 1.0
			// },
			// position : intersection
			// });
			// else
			// intmpIntersectionMarker.setPosition(intersection);
			// intmpIntersectionMarker.id = intersectId;
			// pathMarkers.push(intmpIntersectionMarker);
			// if ($("#destinationId").val().length > 0) {
			// $("#destination").val("Intersection");
			// $("#destinationId").val(intersectId);
			// $("#destinationGPS").val(
			// destinationGPS.x + "," + destinationGPS.y);
			// if (movingLine != undefined) {
			// movingLine.setMap(null);
			// movingLine = undefined;
			// }
			// updateConstantLine();
			// google.maps.event.clearInstanceListeners(map);
			// saveThePath();
			// } else {
			var intersectionPoint = {
				description : "Intersection",
				entranceId : intersectId,
				gps : intersectionpoint.x + "," + intersectionpoint.y,
				entranceIntersection : false,
				parentId : $("#parentLocationId").val()
			};
			// addAPath(location);
			addEntrance(intersectionPoint);
			// }
		},
		complete : function() {
			HideLoadingScreen();
			createNew(0);
		},
		error : function(xhr, ajaxOptions, thrownError) {
			popErrorMessage("An error occured while creating an intersection. "
					+ thrownError);
		}
	});
}

function selectAPath(path) {
	url = "REST/GetPathWS/SavePath?fLocationId=" + $("#departureId").val()
			+ "&tLocationId=" + $("#destinationId").val() + "&pathType="
			+ $("#pathType").val() + "&pathRoute=" + $("#pathLatLng").val();
	$("#departure").val(path.departure.entrances[0].description);
	$("#departureId").val(path.departure.entrances[0].entranceId);
	$("#departureGPS").val(path.departure.entrances[0].coordinates);
	$("#pathLatLng").val(path.pathRoute);
	var ptids = path.pathType.split(",");
	for ( var int = 0; int < ptids.length; int++) {
		selectIcon(ptids[int] + "");
	}
	$("#pathTypeIds").val(path.pathType);
	$("#pathLength").html(getTheLength(path.distance));
	$("#destination").val(path.destination.entrances[0].description);
	$("#destinationId").val(path.destination.entrances[0].entranceId);
	$("#destinationGPS").val(path.destination.entrances[0].coordinates);
	$("#pathWidth").val(path.width);
	$("#pathId").val(path.pathId);
	$("#pathWidth").slider("refresh");
	$("#pathWidth").trigger("create");
	if (paths != null)
		for ( var i = 0; i < paths.length; i++) {
			if (paths[i] != null) {
				if (paths[i].id == path.pathId) {
					paths[i].setOptions({
						strokeColor : 'red',
						strokeWeight : 1,
						strokeOpacity : 1
					});
				} else {
					paths[i].setOptions({
						strokeOpacity : 0
					});
				}
				paths[i].setMap(null);
				paths[i].setMap(map);
			}
		}
}

function getClosestPointOnLines(pXy, aXys) {
	var fTo, minDist, fFrom, x, y, i, dist;
	if (aXys.length > 1) {
		for ( var n = 1; n < aXys.length; n++) {
			if (aXys[n].x != aXys[n - 1].x) {
				var a = (aXys[n].y - aXys[n - 1].y)
						/ (aXys[n].x - aXys[n - 1].x);
				var b = aXys[n].y - a * aXys[n].x;
				dist = Math.abs(a * pXy.x + b - pXy.y) / Math.sqrt(a * a + 1);
			} else
				dist = Math.abs(pXy.x - aXys[n].x)
			var rl2 = Math.pow(aXys[n].y - aXys[n - 1].y, 2)
					+ Math.pow(aXys[n].x - aXys[n - 1].x, 2);
			var ln2 = Math.pow(aXys[n].y - pXy.y, 2)
					+ Math.pow(aXys[n].x - pXy.x, 2);
			var lnm12 = Math.pow(aXys[n - 1].y - pXy.y, 2)
					+ Math.pow(aXys[n - 1].x - pXy.x, 2);
			var dist2 = Math.pow(dist, 2);
			var calcrl2 = ln2 - dist2 + lnm12 - dist2;
			if (calcrl2 > rl2)
				dist = Math.sqrt(Math.min(ln2, lnm12));
			if ((minDist == null) || (minDist > dist)) {
				if (calcrl2 > rl2) {
					if (lnm12 < ln2) {
						fTo = 0;
						fFrom = 1;
					} else {
						fFrom = 0;
						fTo = 1;
					}
				} else {
					fTo = ((Math.sqrt(lnm12 - dist2)) / Math.sqrt(rl2));
					fFrom = ((Math.sqrt(ln2 - dist2)) / Math.sqrt(rl2));
				}
				minDist = dist;
				i = n;
			}
		}
		var dx = aXys[i - 1].x - aXys[i].x;
		var dy = aXys[i - 1].y - aXys[i].y;
		x = aXys[i - 1].x - (dx * fTo);
		y = aXys[i - 1].y - (dy * fTo);
	}
	return {
		'x' : x,
		'y' : y,
		'i' : i,
		'fTo' : fTo,
		'fFrom' : fFrom
	};
}