function getPathTypeColorCode(pathTypeIds) {
	if (pathTypeIds == null || pathTypeIds.length <= 0)
		return "#000000";
	if (pathTypeIds.indexOf("3") > -1)
		return "#A569BD";
	if (pathTypeIds.indexOf("6") > -1 && pathTypeIds.indexOf("5") == -1)
		return "#3498DB";
	if (pathTypeIds.indexOf("5") > -1)
		return "#566573";
	if (pathTypeIds.indexOf("2") > -1)
		return "#2ECC71";
	if (pathTypeIds.indexOf("1") > -1)
		return "#FF5733";
	if (pathTypeIds.indexOf("9") > -1)
		return "#FFFFFF";
}

function setPathTypeButtonIcon() {
	$(".pathTypeIcon").each(function() {
		var pathTypeId = $(this).attr("alt");
		if (pathTypeId == "1")
			$(this).attr("src", "images/icons/pathType/grass.png");
		else if (pathTypeId == "2") {
			$(this).attr("src", "images/icons/pathType/normalSpeed.png");
		} else if (pathTypeId == "3") {
			$(this).attr("src", "images/icons/pathType/stairs.png");
		} else if (pathTypeId == "4")
			$(this).attr("src", "images/icons/pathType/elevator.png");
		else if (pathTypeId == "5")
			$(this).attr("src", "images/icons/pathType/car.png");
		else if (pathTypeId == "6")
			$(this).attr("src", "images/icons/pathType/wheelchair.png");
		else if (pathTypeId == "7")
			$(this).attr("src", "images/icons/pathType/escalator.png");
		else if (pathTypeId == "8")
			$(this).attr("src", "images/icons/pathType/indoor.png");
		else if (pathTypeId == "9")
			$(this).attr("src", "images/icons/pathType/bicycle.png");
		else
			$(this).attr("src", "images/icons/pathType/cursor-pointer.png");
	});
}

function selectIcon(id) {
	var pathTypeIds = [];
	$(".pathTypeIcon").each(function() {
		var pathTypeId = $(this).attr("alt");
		if ($(this).hasClass("pathTypeIconSelected")) {
			pathTypeIds.push($(this).attr("alt"));
		}
		if (pathTypeId == id) {
			if (!$(this).hasClass("pathTypeIconSelected")) {
				$(this).addClass("pathTypeIconSelected");
				pathTypeIds.push(id);
			} else {
				$(this).removeClass("pathTypeIconSelected");
				pathTypeIds.splice(pathTypeIds.indexOf(id), 1);
			}
			$(this).trigger("create");
		}
	});
	$("#pathTypeIds").val(pathTypeIds.join(","));
}
