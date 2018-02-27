var cameraView = undefined;
var scanner = undefined;
function startScanner() {
	cameraView = new Vue(
			{
				el : '#cameraView',
				data : {
					scanner : null,
					activeCameraId : null,
					cameras : [],
					scans : []
				},
				mounted : function() {
					var self = this;
					self.scanner = new Instascan.Scanner({
						video : document.getElementById('videoContent'),
						scanPeriod : 5,
						continuous : true,
						captureImage : false,
						backgroundScan : false,
						refractoryPeriod : 1000
					});
					scanner = self.scanner;
					self.scanner.addListener('scan', function(content, image) {
						getTheBarcodeAR(content);
						self.scans.unshift({
							date : +(Date.now()),
							content : content
						});
					});
					Instascan.Camera
							.getCameras()
							.then(
									function(cameras) {
										self.cameras = cameras;
										if (cameras.length > 1) {
											for ( var i = 0; i < cameras.length; i++) {
												if (cameras[i].name
														.indexOf("back") >= 0
														|| cameras[i].name
																.indexOf("rear") >= 0) {
													self.activeCameraId = cameras[i].id;
													self.scanner
															.start(cameras[i]);
												}
												;
											}
											;
										} else if (cameras.length > 0) {
											self.activeCameraId = cameras[0].id;
											self.scanner.start(cameras[0]);
											// startAR();
										} else {
											console.error('No cameras found.');
										}
									})
					 .catch(function (e) {
					 console.error(e);
					 });
				},
				methods : {
					formatName : function(name) {
						return name || '(unknown)';
					},
					selectCamera : function(camera) {
						this.activeCameraId = camera.id;
						this.scanner.start(camera);
					}
				}
			});
}

function getBCodeInfo(x) {
	var tmpQRInfo = x.split("depId=");
	if(tmpQRInfo.length > 1)
		x = tmpQRInfo[1];
	$("#arrivalMessageContent").html("");
	$
			.ajax({
				url : "REST/GetLocationWS/GetALocation?locationId="
						+ x,
				cache : true,
				success : function(data) {
					var stringIMG = '';
					if (data['icon'] != null && data['icon'].length > 0)
						stringIMG = '<img src="' + data['icon'] + '" width="66" height="66"/>';
					else
						stringIMG = '<img src="images/icons/map-markers/building.png" width="66" height="66"/></br>';
					$("#arrivalMessageContent").append(
							stringIMG + '<span class="heading">' + data['locationType'].locationType
									+ ': </span><span class="locationText">'
									+ data['locationName'] + '</span><br>');
					var barcodePosition = getGoogleMapPosition(data['coordinates']);
					if(marker==null){
						marker =  new google.maps.Marker({
								position : barcodePosition,
								map : map,
								draggable : false
							});
							if (heading == null)
								heading = 90;
							marker.setIcon({
								path : google.maps.SymbolPath.FORWARD_CLOSED_ARROW,
								scale : 6,
								rotation : heading,
								zIndex : 9,
								strokeOpacity : 1,
								fillColor : '#3878c7',
								fillOpacity : 1,
								strokeColor : '#3878c7',
								strokeOpacity : 1,
							});
							marker.addListener('click', function() {
								map.setZoom(18);
								map.setCenter(this.getCenter());
								findMyLocation();
							});
					}else{
						marker.setPosition(barcodePosition);
						marker.setMap(map);
					}
					map.setZoom(18);
					map.panTo(barcodePosition);
					map.setCenter(barcodePosition);
					$("#departureId").val(x);
					getThePath();
//					if (data['locationID'] == x && getCookie("TripPathGPSCookie") != "") {
//						var nextDestId = getCookie("TripPathIdsCookie").split(
//								"_");
//						var tmpStr = "";
//						for ( var int = 0; int < nextDestId.length; int++) {
//							if (x == nextDestId[int]) {
//								if (int == 0)
//									tmpStr = nextDestId[int];
//								else
//									tmpStr += "," + nextDestGPS[int];
//							}
//						}
//						removeTheNextDestination();
//					}
					return presentLocation(data['parent']);					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert(xhr.status);
					alert(thrownError);
				}
			});

}
function presentLocation(x) {
	arrivalMessagePopupOpen();
	selectMapMode();
	$("#arrivalMessageContent").append(
			'<span class="heading">' + x.locationType.locationType
					+ ': </span><span class="locationText">' + x['locationName']
					+ '</span><br>');
	if (x.p != null)
		presentLocation(x['parent']);
}

function hideInfoDiv() {
	$("#barcodeDescription").fadeOut(3000);
}
function getTheBarcodeAR(content) {
	getBCodeInfo(content);
	// arrivalMessagePopupOpen();
	// $("#barcodeDescription").css("display", "block").trigger('create');
	// $("#barcodeDescription").fadeIn(3000);
	// window.setInterval(hideInfoDiv, 5000);
}
