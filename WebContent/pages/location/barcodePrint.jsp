<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="../../js/jquery.min.js"></script>
<script>
	$(document)
			.ready(
					function() {
						$
								.ajax({
									url : "../../REST/GetLocationWS/GetBarcodeForLocation?locationId="
											+
<%=request.getParameter("locationId")%>
	,
									cache : false,
									success : function(data) {
										$
												.each(
														data,
														function(k, l) {
															if (k == 'i')
																$(
																		"#barcodeContainer")
																		.html(
																				'<img src="'
																						+ l
																						+ '" style="overflow: hidden; background-color: transparent !important;">');
															$("#Description")
																	.html("");
															$("#Description")
																	.append(
																			'<span class="heading">'
																					+ data['t']
																					+ '</span><span class="locationText">'
																					+ data['n']
																					+ '</span>');
														});
										return presentLocation(data['p']);
									}
								});
					});
	function presentLocation(x) {
		if (x['t'].indexOf("sity") > 0 || x['t'].indexOf("ampus") > 0)
			return;
		// 		$.each(x, function(k, l) {
		$("#Description").append(
				'<span class="heading">' + x['t']
						+ '</span><span class="locationText">' + x['n']
						+ '</span>');
		// 		});
		if (x.p != null)
			presentLocation(x['p']);
	}
</script>
<style type="text/css">
body{
overflow-x:hidden;
}
#printPage {
	height: 100%;
	width: 100%;
	display: block;
	overflow:auto;
}

.heading {
	font-weight: bold;
/* 	display: inline-block; */
	font-size: 22pt;
	padding: 7px;
	background-color: rgb(8, 27, 44);
	color: rgb(248, 182, 36);
}

.locationText {
	font-weight: bold;
/* 	display: inline-block; */
	font-size: 18pt;
	color: rgb(8, 27, 44);
	background-color: rgb(248, 182, 36);
	height: 34px;
	padding: 12px;
	padding-bottom: 1px;
}

#Description,#ARMarker {
	background-color: transparent !important;
 	overflow: hidden; 
 	position: absolute;  
}

#barcodeContainer {
/* 	margin: -96px -86px; */
	z-index: 1;
/* 	top: 166px; */
	background-color: transparent;
}

#Description {
	top: 0px;
	z-index: 3;
	width: 100%;
}

#ARMarker {
	z-index: 5;
}
</style>
</head>
<body>
	<div id="printPage">
		<div id="headerContainer" style="height: 100%; z-index: 10">
<!-- 			<div id="ARMarker"> -->
<!-- 				<img src="../../images/logos/nmulogo-s.jpg" alt="marker" /> -->
<!-- 			</div> -->
		<div id="barcodeContainer"></div>
		<div id="Description"></div>
		</div>
	</div>
</body>
</html>