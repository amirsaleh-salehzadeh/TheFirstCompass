function userMedia() {
	return navigator.getUserMedia = navigator.getUserMedia
			|| navigator.webkitGetUserMedia || navigator.mozGetUserMedia
			|| navigator.msGetUserMedia || null;
}

var cameraObjs = [];
var cameraObj = undefined;
function gotDevices(deviceInfos) {
	  for (var i = 0; i !== deviceInfos.length; ++i) {
	    var deviceInfo = deviceInfos[i];
	    if (deviceInfo.kind === 'videoinput') {
	    	cameraObjs.push(deviceInfo);
	    } else {
	      console.log('Found ome other kind of source/device: ', deviceInfo);
	    }
	  }
	}

	function getStream() {
	  if (window.stream) {
	    window.stream.getTracks().forEach(function(track) {
	      track.stop();
	    });
	  }
	  cameraObj = cameraObjs[0]; 
	  if(cameraObjs.length>1){
		  cameraObj = cameraObjs[1];
	  }
	  var constraints = {
	    video: {
	      optional: [{
	        sourceId: cameraObj.deviceId
	      }]
	    }
	  };
	  navigator.mediaDevices.getUserMedia(constraints).
	      then(gotStream).catch(handleError);
	}

	function gotStream(stream) {
	  window.stream = stream; // make stream available to console
	  document.getElementById('videoContent').srcObject = stream;
	  track = stream.getVideoTracks()[0];
		// Start the video
	  document.getElementById('videoContent').play();
	  $('#videoContent').width(parseInt($(window).width()));
	}

	function handleError(error) {
	  console.log('Error: ', error);
	}

var track;
function startCamera() {
 startScanner();
}

function stopCamera() {
	cameraView = undefined;
	if(scanner != undefined)
		scanner.stop();
	$('#videoContent').src = "";
}