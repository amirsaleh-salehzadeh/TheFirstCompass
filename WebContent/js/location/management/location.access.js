function getAllLocationsTree(parentId) {
	var url = "REST/GetLocationWS/GetLocationWithChildren?locationID="
			+ parentId + "&userName="+$("#username").val();
	$.ajax({
		url : url,
		cache : true,
		async : true,
		beforeSend : function() {
			ShowLoadingScreen("Loading Locations");
		},
		success : function(data) {
			str = "<li><div class='tf-div'>"
					+ '<input type="checkbox" name="locationIDs" id="'
					+ data.locationID + '" data-mini="true">'
					+ data.locationName + "</div><ul id='loc" + data.locationID
					+ "'></ul></li>";
			$("ul#loc" + data.parentId).append(str);
			generateTree(data.childrenENT);
			initiateTree();
		},
		complete : function() {
			HideLoadingScreen();
		},
		error : function(xhr, ajaxOptions, thrownError) {
			alert(xhr.status);
			alert(thrownError);
			alert("getAllLocationsTree");
		}

	});
}

function generateTree(data) {
	$.each(data, function(k, l) {
		// if ((l.locationType.locationTypeId == 2)
		// || (l.locationType.locationTypeId == 3)
		// || (l.locationType.locationTypeId == 7)) {
		str = "<li><div class='tf-div'>"
				+ '<input type="checkbox" name="locationIDs" id="'
				+ l.locationID + '" value="' + l.locationID
				+ '" data-mini="true">' + l.locationName + "</div><ul id='loc"
				+ l.locationID + "'></ul></li>";
		$("ul#loc" + l.parentId).append(str);
		// }
		if (l.childrenENT != null)
			generateTree(l.childrenENT);
		// else
		// return;
	});

}

function initiateTree() {
	var tree = new treefilter($("#loc360"), {
		searcher : $("input#my-search")
	});
	var userLIDs = $("#userLocationIds").val().split(",");
	$('[name="locationIDs"]').each(function() {
		for ( var int = 0; int < userLIDs.length; int++) {
			if ($(this).attr("id") == userLIDs[int]) {
				// $(this).checkboxradio();
				$(this).prop('checked', true);// .checkboxradio('refresh')
			}
		}
	});
}
function ShowLoadingScreen(loadingContent) {
	if (loadingContent == null) {
		loadingContent = "Please Wait";
	}
	$("#loadingOverlay").css("display", "block");
	$("#loadingContent").css("display", "block");
	$(".markerLoading").css('display', 'block').trigger("create");
	$("#loadingContent").html("Loading. . ." + "</br>" + loadingContent);
}
function HideLoadingScreen() {
	$("#loadingOverlay").css("display", "none");
	$(".markerLoading").css('display', 'none');
	$("#loadingContent").css("display", "none");
}
