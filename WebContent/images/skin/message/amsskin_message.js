var browser = navigator.appName;
var ExtraSpace = 10;
var WindowLeftEdge = 0;
var WindowTopEdge = 0;
var WindowWidth = 0;
var WindowHeight = 0;
var WindowRightEdge = 0;
var WindowBottomEdge = 0;

$(document).ready(
		function() {
			$('a.btn-ok, #dialog-overlay, #dialog-box').click(function() {
				closePopupMessage();
			});
			$(window).resize(function() {
				if (!$('#dialog-box').is(':hidden'))
					popupMessage();
			});
			if ($("#successDescription").html() != ""
					|| $("#errorDescription").html() != "")
				popupMessage();
		});

$(document).ajaxComplete(
		function() {
			$('a.btn-ok, #dialog-overlay, #dialog-box').click(function() {
				closePopupMessage();
			});
			$(window).resize(function() {
				if (!$('#dialog-box').is(':hidden'))
					popupMessage();
			});
			if ($("#successDescription").html() != ""
					|| $("#errorDescription").html() != "")
				popupMessage();
		});

function popSuccessMessage(message) {
	if (message.length <= 1)
		return;
	$("#dialog-message").html(
			"<label id='successDescription' style='color:green;'>" + message
					+ "</label>");
	popupMessage();
}

function popErrorMessage(message) {
	if (message.length <= 1)
		return;
	$("#dialog-message").html(
			"<label id='errorDescription' style='color:red;'>" + message
					+ "</label>");
	popupMessage();
}

function popupMessage() {
	var maskHeight = $(window).height();
	var maskWidth = $(window).width();
	var dialogTop = (maskHeight / 3) - ($('#dialog-box').height());
	var dialogLeft = (maskWidth / 2) - ($('#dialog-box').width() / 2);
	$('#dialog-overlay').css({
		height : maskHeight,
		width : maskWidth
	}).show();
	$('#dialog-box').css({
		top : dialogTop,
		left : dialogLeft
	}).show();
}

function closePopupMessage() {
	$("#dialog-message").html("");
	$('#dialog-overlay, #dialog-box').hide();
	return false;
}
