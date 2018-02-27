var is_keyboard = false;
var is_landscape = false;
var initial_screen_size = window.innerHeight;
var popupopen;

/* Android */
window.addEventListener("resize", function() {
	is_keyboard = (window.innerHeight < initial_screen_size);
	is_landscape = (screen.height < screen.width);
	updateViews();
}, false);

function closePopup() {
	popupopen = false;
	if ($("#destinationId").val().length > 0)
		hideBottomPanel();
	$('#popupErrorMessage').css("display", "none");
	$('#popupArrivalMessage').popup('close');
	$('#popupSearchResult').popup('close');
	$('#popupSelectDeparture').popup('close');
}

function errorMessagePopupOpen(content) {
	$("#errorMessageContent").html(content);
	popupopen = true;
	hideBottomPanel();
	$('#popupErrorMessage').css("height", $(window).height());
	$('#popupErrorMessage').css("display", "block").trigger("create");
}

function arrivalMessagePopupOpen() {
	$('#popupArrivalMessage').popup().trigger('create');
	$('#popupArrivalMessage').popup({
		history : false,
		transition : "turn"
	});
	$('#popupArrivalMessage').popup('open').trigger('create');
	popupopen = true;
}

function searchIconClick() {
	if ($("#destinationId").val().length > 0
			&& $("#destinationId").val().length)
		selectDepartureModePopupOpen();
	else
		searchResultPopupOpen();
}

function searchResultPopupOpen() {
	if (markerDepart != null)
		markerDepart.setMap(null);
	$('#popupSearchResult').popup().trigger('create');
	$('#popupSearchResult').popup({
		history : false,
		transition : "turn"
	});
	// setTimeout(function() {
	$('#popupSearchResult').popup('open').trigger('create');
	// }, 100);
	searchFieldDivClearBTN();
	hideBottomPanel();
	popupopen = true;
	$("#searchField").focus();
	// }
}

function searchForNewPlace() {
	removeTrip();
	$("#popupSelectDeparture").on("popupafterclose", function() {
		setTimeout(function() {
			searchResultPopupOpen();
			$("#popupSelectDeparture").unbind("popupafterclose");
		}, 100);
	});
	$("#popupSelectDeparture").popup("close");
}

function searchResultPopupOpenForDeparture() {
	$("#popupSelectDeparture").on("popupafterclose", function() {
		setTimeout(function() {
			if (markerDepart != null)
				markerDepart.setMap(null);
			$('#popupSearchResult').popup().trigger('create');
			$('#popupSearchResult').popup({
				history : false,
				transition : "turn"
			});
			$('#popupSearchResult').popup('open').trigger('create');
			searchFieldDivClearBTN();
			hideBottomPanel();
			popupopen = true;
			$("#popupSelectDeparture").unbind("popupafterclose");
			$("#searchField").focus();
		}, 100);
	});
	$("#popupSelectDeparture").popup("close");

}

function selectDepartureModePopupOpen() {
	if (markerDepart != null)
		markerDepart.setMap(null);
	$('#popupSelectDeparture').popup().trigger('create');
	$('#popupSelectDeparture').popup({
		history : false,
		transition : "turn"
	});
	$('#popupSelectDeparture').popup('open').trigger('create');
}

function updateViews() {
	if (is_keyboard) {
		$("#popupSearchResult").height(window.innerHeight - 30);
	} else {
		$("#popupSearchResult").height(window.innerHeight - 30);
	}

}