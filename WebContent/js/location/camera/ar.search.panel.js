var locationTypeJSONData;
var markers = [];
function getAllLocations() {
	var url = "REST/GetLocationWS/SearchForALocation?username="
			+ $("#username").val() + "&locationType=Building&locationName=";
	$
			.ajax({
				url : url,
				cache : true,
				async : false,
				success : function(data) {
					// $.each(data, function(p, z) {
					$
							.each(
									data.childrenENT,
									function(k, l) {
										var children = l.childrenENT;
										var col = $("<div/>", {
											"data-role" : "collapsible",
											"data-collapsed" : true,
											"data-inset" : true,
											"class" : "parentCollapsibleLV",
											"data-icon" : false,
											"data-collapsed-icon" : false,
											"data-expanded-icon" : false
										}).trigger("create");
										$("<h3/>", {
											text : l.locationName + " Campus",
											"class" : "listItemParentItem"

										}).appendTo(col).trigger("create");
										var list_items = '';
										$
												.each(
														children,
														function(x, y) {
															list_items += "<li id='"
																	+ y.locationID
																	+ "_"
																	+ y.coordinates
																	+ "_"
																	+ y.locationType.locationType
																	+ "' onclick='selectDestination(this, \""
																	+ y.locationType.locationType
																	+ " "
																	+ y.locationName
																	+ "\")' class='resultsListViewContentLI' >"
																	+ "<a href='#' class='resultsListViewContent'>";
															var src = "images/icons/map-markers/building.png";
															if (y.icon != null
																	&& y.icon.length > 0)
																src = y.icon;
															list_items += "<img src='"
																	+ src
																	+ "' class='listViewIcons' style='visibility: hidden;' data-icon='false'><h2 data-icon='false'>"
																	+ y.locationType.locationType
																	+ " "
																	+ y.locationName
																	+ "</h2><p>";
															var desc = "&nbsp;";
															if (y.description != null)
																desc = y.description;
															list_items += desc
																	+ " "
																	+ l.locationName
																	+ " Campus</p></a></li>";
														});
										var list = $("<ul/>", {
											"data-role" : "listview",
											"id" : "listview" + l.locationID,
											"data-input" : "#searchField",
											"data-filter" : true,
											"data-icon" : false
										});
										$(list).append(list_items).trigger(
												"create");
										$(list).appendTo(col).trigger("create");
										$("#resultsListViewContentDiv").append(
												col).collapsibleset().trigger(
												"create");
									});
					searchResultPopupOpen();
				},
				error : function(xhr, ajaxOptions, thrownError) {
					errorMessagePopupOpen(ajaxOptions + ": " + thrownError);
				},
				complete : function(xhr, txtStatus) {

				}
			});
}
var ar;
function selectDestination(destination, content) {
	$("#destinationId").val($(destination).attr("id").split("_")[0]);
	ar = new AugmentedRealityViewer(function(here, callback) {
		var url = "REST/GetLocationWS/GetALocationAR?locationId="+$("#destinationId").val();
		$.ajax({
			url : url,
			cache : false,
			async : false,
			success : function(datal) {
				callback(datal);
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert("An error occured while removing the marker. "
						+ thrownError);
			}
		});
	});
	ar.setViewer(document.getElementById('camera'));
	$('#popupSearchResult').popup('close');
}

function searchResultPopupOpen() {
	$('#popupSearchResult').popup().trigger('create');
	$('#popupSearchResult').popup({
		history : false,
		transition : "turn"
	});
	// setTimeout(function() {
	$('#popupSearchResult').popup('open').trigger('create');
	// }, 100);
	$("#searchField").focus();
	// }
}


