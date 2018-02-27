function setAPathTypeNew() {
	$("#actionBarNextButton").removeClass("disabledBTN").trigger("create");
	$("#actionBarBackButton").removeClass("disabledBTN").trigger("create");
	$(".pathTypeIcon").each(function() {
		if ($(this).hasClass("pathTypeIconSelected")) {
			$(this).removeClass("pathTypeIconSelected");
		}
		$(this).trigger("create");
	});
	$("#editPathTypePopup").css("position", "absolute").trigger("create");
	$("#editPathTypePopup").css(
			"top",
			parseInt($(".jqm-header").height())
					+ parseInt($("#locPathModeRadiobtn").height() + 3) + 'px');
	setTimeout(function() {
		$("#editPathTypePopup").trigger('create').popup('open');
	}, 100);
}
