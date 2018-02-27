function refreshPlaceHolders() {
	$('input[type=text]').each(function() {
		$(this).attr("placeholder", $(this).attr("title"));
	});
	$('textarea').each(function() {
		$(this).attr("placeholder", $(this).attr("title"));
	});
}

function callAnAction(url) {
	$('#gridMenuSetting').popup("destroy");
	$("#mainBodyContents").html("");
	$.ajax({
		url : url,
		cache : false,
		success : function(data) {
			$("#mainBodyContents").html(data).trigger("create");
			// $(document).trigger("create");
			$(".ui-popup-active").css("display", "none");
			refreshPlaceHolders();
			if ($("#reqCodeGrid").val() != undefined) {
				refreshGrid();
			}
			return true;
		}
	});
}

function saveTheForm() {
	var url = $("#dataFilterGridMainPage").attr("action");
	url += "?" + $("#dataFilterGridMainPage").serialize();
	$.ajax({
		url : url,
		cache : false,
		success : function(data) {
			$("#mainBodyContents").html("");
			$("#mainBodyContents").html(data).trigger("create");
			$(document).trigger("create");
			$(".ui-popup-active").css("display", "none");
			refreshPlaceHolders();
			refreshGrid();
			return true;
		},
		error : function() {
			return;
		}
	});
}

function deleteAnItem(id, reqCode) {
	$("#reqCode").val(reqCode);
	$("#deleteID").val(id);
	showPopupDialogDeleteConfirmation(reqCode);
}

function deleteConfirmed() {
	var url = $("#dataFilterGridMainPage").attr("action");
	url += "?" + $("#dataFilterGridMainPage").serialize();
	$.ajax({
		url : url,
		cache : false,
		success : function(data) {
			// $("#mainBodyContents").html(data);
			// $(document).trigger("create");
			$(".ui-popup-active").css("display", "none");
			// $("input.AMSpaginationBTN").each(function() {
			// if ($(this).attr('title') == $("#hiddenPage").val()) {
			// $(this).prop('disabled', true).trigger("create");
			// $(this).button("refresh");
			// }
			// });
			refreshPlaceHolders();
			refreshGrid();
			return true;
		}
	});
}

function deleteSelectedItems(reqCode) {
	$("#tempReqCode").val(reqCode);
	var ids = $('.gridCheckBoxes:checked').map(function() {
		return $(this).attr("id");
	}).get().join(',');
	deleteAnItem(ids, reqCode);
}

function showPopupDialogDeleteConfirmation(reqCode) {
	$.mobile.changePage("#popupDialogDeleteConfirmation");
	$("#popupDialogDeleteConfirmation").trigger("create");
	$("#popupDialogDeleteConfirmation").popup("open");
}