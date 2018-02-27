function getAngleDirection(angle) {
	if (angle < 0)
		angle = 360 + angle;
	return parseFloat(angle);
}

function getDistanceLeft(distance) {
	var Kilometres = Math.floor(distance / 1000);
	var Metres = Math.round(distance - (Kilometres * 1000));
	var res = "";
	if (Kilometres != 0)
		res = Kilometres + "." + Metres + " (Km) ";
	else
		res = Metres + " (m) ";
	return "" + res;
}

function getTimeLeft(distance) {
	var Hours = 0;
	var Minutes = 0;
	var Seconds = 0;
	if (speed == undefined || speed == null)
		speed = 0.001;
	if (speed > 0) {
		var TotalTime = (distance / 1000) / speed;
		Hours = Math.floor(TotalTime);
		Minutes = Math.floor((TotalTime - Hours) * 60);
		Seconds = Math.round((TotalTime - Hours - Minutes) * 60);
	}
	var String = " ";
	if (Hours > 0)
		String += Hours + "': ";
	String += Minutes + "\": " + Seconds + " s ";
	return String;
}

function getTripInfo() {
	$("#currentLocationInfoContainer").trigger("create");
	var destName = $("#tripRouteLocationNames").val().split("_");
//		getCookie("TripPathLocationsCookie").split("_");
	$("#destinationDescriptionSpan").html(
			"Building " + destName[destName.length - 1]);
	if (distanceToNextPosition != null && markerDepart.getMap() == null)
		distanceToDestination = polylineConstantLength + distanceToNextPosition;
	else
		distanceToDestination = polylineConstantLength;
	$("#distanceLeftInf").html("Distance Left " + getDistanceLeft(distanceToDestination));
	
	// $("#arrivalTimeInf").html(getTimeLeft(distanceToDestination));
}


var ctx;
// function drawCanvasDirection(){
function displayImage(angle) {
	ctx = document.getElementById('directionCanvas').getContext('2d');
	var canvas = document.getElementById('directionCanvas');
	canvas.width = $("#directionShow").width();
	canvas.height = $("#directionShow").height();
	ctx.clearRect(0, 0, document.getElementById('directionCanvas').width,
			document.getElementById('directionCanvas').height);
	var startPointX = 0, startPointY = 0, endPointX = 0, endPointY = 0, quadPointX = 0, quadPointY = 0;
	var width = canvas.width - 10, height = canvas.height - 10;
	if (0 <= angle && angle < 45) {
		startPointX = width / 2;
		startPointY = height;
		endPointX = (width / 2) + (angle * 1.1);
		endPointY = 10;
		quadPointX = width / 2;
		quadPointY = height / 2;
	} else if (45 <= angle && angle < 90) {
		startPointX = width / 2;
		startPointY = height;
		endPointX = width;
		endPointY = 10 + ((angle - 45) * 1.1);
		quadPointX = width / 2;
		quadPointY = height / 2;
	} else if (90 <= angle && angle < 135) {
		startPointX = width / 2 - ((angle - 90) * 0.55);
		startPointY = height;
		endPointX = width;
		endPointY = ((angle - 45) * 1.1);
		quadPointX = width / 2;
		quadPointY = (height / 2) - ((angle - 45) * 1.1);
	} else if (135 <= angle && angle <= 180) {
		startPointX = width / 4;
		startPointY = height;
		endPointX = width - ((angle - 135) * 1.1);
		endPointY = height;
		quadPointX = width / 2;
		quadPointY = (height / 2) - 99;
	} else if (180 < angle && angle < 225) {
		startPointX = (width / 4) * 3;
		startPointY = height;
		endPointX = width / 2 - ((angle - 180));
		endPointY = height;
		quadPointX = width / 2;
		quadPointY = (height / 2) - 99;
	} else if (225 <= angle && angle < 270) {
		startPointX = (3 * (width / 4)) - ((angle - 225) * 0.55) + 10;
		startPointY = height;
		endPointX = 10;
		endPointY = height - ((angle - 225));
		quadPointX = (width / 2);// +((angle - 225) * 0.55)+10 ;
		quadPointY = ((height / 2) - 99) + ((angle - 225) * 1.1);
	} else if (270 <= angle && angle < 315) {
		startPointX = width / 2;
		startPointY = height;
		endPointX = 10;
		endPointY = 10 + ((height / 2) - ((angle - 270) * 1.1));
		quadPointX = width / 2;
		quadPointY = height / 2;
	} else if (315 <= angle && angle <= 360) {
		startPointX = width / 2;
		startPointY = height;
		endPointX = ((angle - 315));
		endPointY = 10;
		quadPointX = width / 2;
		quadPointY = height / 2;
	}
//	var pathstr = "M" + startPointX + "," + startPointY + " Q" + quadPointX
//			+ "," + quadPointY + "," + quadPointX + "," + quadPointY;

	// var path = arrowline(canvas, pathstr, 4000, {
	// stroke : 'black',
	// 'stroke-width' : 8,
	// 'fill-opacity' : 0
	// });
	ctx.strokeStyle = "rgb(248, 182, 36)";
	ctx.lineWidth = 7;
	ctx.lineCap = "round";
	var arrowAngle = Math.atan2(quadPointX - endPointX, quadPointY - endPointY)
			+ Math.PI;
	var arrowWidth = 11;
	ctx.beginPath();
	ctx.moveTo(startPointX, startPointY);
	ctx.quadraticCurveTo(quadPointX, quadPointY, endPointX, endPointY);
	ctx.moveTo(endPointX - (arrowWidth * Math.sin(arrowAngle - Math.PI / 6)),
			endPointY - (arrowWidth * Math.cos(arrowAngle - Math.PI / 6)));
	ctx.lineTo(endPointX, endPointY);
	ctx.lineTo(endPointX - (arrowWidth * Math.sin(arrowAngle + Math.PI / 6)),
			endPointY - (arrowWidth * Math.cos(arrowAngle + Math.PI / 6)));
	ctx.stroke();
	ctx.closePath();
}

function arrowline(canvas, pathstr, duration, attr, callback) {
	ctx.clearRect(0, 0, document.getElementById('directionCanvas').width,
			document.getElementById('directionCanvas').height);
	var guide_path = canvas.path(pathstr).attr({
		stroke : "none",
		fill : "none"
	});
	var path = canvas.path(guide_path.getSubpath(0, 1)).attr(attr);
	var total_length = guide_path.getTotalLength(guide_path);
	var start_time = new Date().getTime();
	var interval_length = 25;

	var interval_id = setInterval(function() {
		var elapsed_time = new Date().getTime() - start_time;
		var this_length = elapsed_time / duration * total_length;
		var subpathstr = guide_path.getSubpath(0, this_length);
		attr.path = subpathstr;
		path.animate(attr, interval_length);

		if (elapsed_time >= duration) {
			clearInterval(interval_id);
			if (callback != undefined)
				callback();
		}
	}, interval_length);
	return path;
}
