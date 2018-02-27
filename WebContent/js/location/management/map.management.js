var map, infoWindow;
var markers = [];
var paths = [];
var pathCreateNewPolygons = [];
var pathPolylines = [];
var polygons = [];
var polygonsEdit = [];
var myStyle = [ {
	featureType : "administrative",
	elementType : "labels",
	stylers : [ {
		visibility : "on"
	} ]
}, {
	featureType : "poi",
	elementType : "labels",
	stylers : [ {
		visibility : "off"
	} ]
}, {
	featureType : "water",
	elementType : "labels",
	stylers : [ {
		visibility : "on"
	} ]
}, {
	featureType : "landscape.natural",
	elementType : "geometry",
	stylers : [ {
		color : "#d0e3b4"
	} ]
}, {
	featureType : "landscape.natural.terrain",
	elementType : "geometry",
	stylers : [ {
		visibility : "on"
	} ]
}, {
	featureType : "poi.business",
	elementType : "all",
	stylers : [ {
		visibility : "off"
	} ]
}, {
	featureType : "poi.medical",
	elementType : "geometry",
	stylers : [ {
		color : "#fbd3da"
	} ]
}, {
	featureType : "poi.park",
	elementType : "geometry",
	stylers : [ {
		color : "#bde6ab"
	} ]
}, {
	featureType : "road",
	elementType : "geometry.stroke",
	stylers : [ {
		visibility : "off"
	} ]
}, {
	featureType : "road.highway",
	elementType : "geometry.fill",
	stylers : [ {
		color : "#ffe15f"
	} ]
}, {
	featureType : "road.highway",
	elementType : "geometry.stroke",
	stylers : [ {
		color : "#efd151"
	} ]
}, {
	featureType : "road.arterial",
	elementType : "geometry.fill",
	stylers : [ {
		color : "#ffffff"
	} ]
}, {
	featureType : "road.local",
	elementType : "geometry.fill",
	stylers : [ {
		color : "black"
	} ]
}, {
	featureType : "water",
	elementType : "geometry",
	stylers : [ {
		color : "#a2daf2"
	} ]
}, {
	"featureType" : "landscape.man_made",
	"elementType" : "geometry",
	"stylers" : [ {
		"hue" : "#ff0000"
	} ]
}, {
	"featureType" : "landscape.man_made",
	"elementType" : "geometry.fill",
	"stylers" : [ {
		"hue" : "#f7f1df"
	}, {
		"visibility" : "on"
	} ]
} ];

function refreshMap(locationTypeId, gpsStr, folderName) {
	var gps = {
		lat : parseFloat(gpsStr.split(",")[0]),
		lng : parseFloat(gpsStr.split(",")[1])
	};
	var icon = 'images/icons/map-markers/' + folderName + '/';
	if (locationTypeId == "1") {
		icon += 'marker-blue.png';
	} else if (locationTypeId == "10") {
		icon += 'parking.png';
	} else if (locationTypeId == "11") {
		icon += 'door.png';
	} else if (locationTypeId == "2") {
		icon += 'area.png';
		map.setCenter(gps);
		map.setZoom(15);
	} else if (locationTypeId == "3") {
		icon += 'buildingss.png';
		map.setCenter(gps);
		// map.setZoom(15);
	} else if (locationTypeId == "4") {
		icon += 'marker-pink.png';
		map.setCenter(gps);
		// map.setZoom(19);
	} else if (locationTypeId == "5") {
		// icon += 'crossroad48.png';
		icon = {
			path : google.maps.SymbolPath.CIRCLE,
			scale : 4,
			fillOpacity : 1
		};
		if (folderName.indexOf("hover") != -1)
			icon = {
				path : google.maps.SymbolPath.CIRCLE,
				scale : 16,
				fillOpacity : 1
			};
		map.setCenter(gps);
		// map.setZoom(15);
	} else
		icon += 'marker-yellow.png';
	return icon;
}

function getGoogleMapPosition(gps) {
	var res = new google.maps.LatLng(parseFloat(gps.split(',')[0]),
			parseFloat(gps.split(',')[1]));
	return res;
}

// function animateCircle(line) {
// var count = 0;
// window.setInterval(function() {
// count = (count + 1) % 200;
// var icons = line.get('icons');
// icons[0].offset = (count / 2) + '%';
// line.set('icons', icons);
// }, 50);
// }
var myLatLng;
var successGetCurrentPosition = function(position) {
	isLocationAvailable = true;
	myLatLng = {
		lat : position.coords.latitude,
		lng : position.coords.longitude
	};
	$("#from").val(position.coords.latitude + "," + position.coords.longitude);
};
var errorHandler = function(errorObj) {
	errorMessagePopupOpen("MapBuddy cannot access your location system. <br>"
			+ "Please enable it in the browser settings to allow MapBuddy to navigate you to a destination <br>");
	navigator.permissions.query({
		name : 'geolocation'
	}).then(function(p) {
		p.onchange = function() {
			updatePermission('geolocation', this.state);
		};
	});
};

function handleLocationError(browserHasGeolocation, pos) {
	errorMessagePopupOpen(browserHasGeolocation ? 'Error: The Geolocation service failed.'
			: 'Error: Your browser doesn\'t support geolocation.');
	navigator.permissions.query({
		name : 'geolocation'
	}).then(function(p) {
		p.onchange = function() {
			updatePermission('geolocation', this.state);
		};
	});

}
function initMap() {
	myLatLng = {
		lat : -33.5343803,
		lng : 24.2683424
	};
	
	if (myLatLng == null)
		myLatLng = {
			lat : -33.5343803,
			lng : 24.2683424
		};
	map = new google.maps.Map(document.getElementById('map_canvas'), {
		fullscreenControl : false,
		streetViewControl : false,
		mapTypeControl : false,
		scrollwheel : true,
		disableDoubleClickZoom : true,
		gestureHandling : 'greedy',
		zoom : 15,
		center : myLatLng
	// styles: myStyle
	});
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(successGetCurrentPosition,
				errorHandler, {
					enableHighAccuracy : true,
					maximumAge : 1000
				});
	} else {
		handleLocationError(false, map.getCenter());
	}
	map.mapTypes.set('mystyle', new google.maps.StyledMapType(myStyle, {
		name : 'My Style'
	}));
	map.setMapTypeId('mystyle');
	map.setCenter(myLatLng);
	map.controls[google.maps.ControlPosition.RIGHT_TOP].push(document
			.getElementById('locationsUnderAType'));
	// map.controls[google.maps.ControlPosition.TOP_CENTER].push(document
	// .getElementById('editBoundaryPopup'));
	createDrawingManager();
	createColorPicker();

	google.maps.LatLng.prototype.kmTo = function(a) {
		var e = Math, ra = e.PI / 180;
		var b = this.lat() * ra, c = a.lat() * ra, d = b - c;
		var g = this.lng() * ra - a.lng() * ra;
		var f = 2 * e.asin(e.sqrt(e.pow(e.sin(d / 2), 2) + e.cos(b) * e.cos(c)
				* e.pow(e.sin(g / 2), 2)));
		return f * 6378.137;
	};

	google.maps.Polyline.prototype.inKm = function(n) {
		var a = this.getPath(n), len = a.getLength(), dist = 0;
		for ( var i = 0; i < len - 1; i++) {
			dist += a.getAt(i).kmTo(a.getAt(i + 1));
		}
		return dist;
	};

	google.maps.event.addDomListener(window, 'resize', function() {
		$("#map_canvas").css("width",
				parseInt($("#mainBodyContents").css("width")));
		$("#map_canvas").css(
				"height",
				parseInt($(window).height())
						- parseInt($(".jqm-header").height())
						- parseInt($("#locPathModeRadiobtn").height()) - 6);
		$(".jqm-demos").trigger("create");
		google.maps.event.trigger(map, "resize");
	});
}