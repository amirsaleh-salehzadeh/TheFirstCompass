<html>
<head>
<script type="text/javascript">
	var video = document.getElementById('video');
	var videoSource;
	if (navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
		navigator.mediaDevices.enumerateDevices().then(
				function(devices) {
					devices.forEach(function(device) {
						if (device.label.indexOf('back') >= 2
								|| device.label.indexOf('rear') >= 0
								|| device.label.indexOf('HP') >= 0) {
							// 							alert(device.kind + ": " + device.label + " id = "
							// 									+ device.deviceId);
							videoSource = device.deviceId;
						}
					});
					var constraints = {
						video : {
							optional : [ {
								sourceId : videoSource
							} ]
						}
					};
					navigator.mediaDevices.getUserMedia(constraints).then(
							function(stream) {
								video.src = window.URL.createObjectURL(stream);
								video.play();
								window.stream = stream; // make variable available to console
								video.srcObject = stream;
							});
				});

	}
</script>
</head>
<body>
	<div id="app">
		<video id="video" id="preview" width="100%" height="100%" autoplay
			style="position: relative; width: 100%; height: auto; overflow: hidden;"></video>
	</div>
</body>
<script type="text/javascript">
// var app = new Vue({
// 	  el: '#app',
// 	  data: {
// 	    scanner: null,
// 	    activeCameraId: null,
// 	    cameras: [],
// 	    scans: []
// 	  },
// 	  mounted: function () {
// 	    var self = this;
// 	    self.scanner = new Instascan.Scanner({ video: document.getElementById('preview'), scanPeriod: 5 });
// 	    self.scanner.addListener('scan', function (content, image) {
// 	      self.scans.unshift({ date: +(Date.now()), content: content });
// 	    });
// 	    Instascan.Camera.getCameras().then(function (cameras) {
// 	      self.cameras = cameras;
// 	      if (cameras.length > 0) {
// 	        self.activeCameraId = cameras[0].id;
// 	        self.scanner.start(cameras[0]);
// 	      } else {
// 	        console.error('No cameras found.');
// 	      }
// 	    }).catch(function (e) {
// 	      console.error(e);
// 	    });
// 	  },
// 	  methods: {
// 	    formatName: function (name) {
// 	      return name || '(unknown)';
// 	    },
// 	    selectCamera: function (camera) {
// 	      this.activeCameraId = camera.id;
// 	      this.scanner.start(camera);
// 	    }
// 	  }
// 	});

</script>
</html>