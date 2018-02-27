DEBUG = true;
function startAR() {
	var video = document.getElementById('videoContent');
	var canvas = document.createElement('canvas');
	canvas.width = $("#cameraView").css('width');
	canvas.height = $("#cameraView").css('height');
	canvas.id = "ARPres";
	var ctx = canvas.getContext('2d');
	var debugCanvas = document.createElement('canvas');
	debugCanvas.width = 320;
	debugCanvas.height = 240;
	debugCanvas.id = 'debugCanvas';
	document.getElementById('cameraView').appendChild(debugCanvas);
	var raster = new NyARRgbRaster_Canvas2D(canvas);
	var param = new FLARParam($("#cameraView").css('width'), $("#cameraView")
			.css('height'));
	var resultMat = new NyARTransMatResult();
	var detector = new FLARMultiIdMarkerDetector(param, 80);
	detector.setContinueMode(true);
	var glCanvas = document.createElement('canvas');
	glCanvas.width = $("#cameraView").css('width');
	glCanvas.height = $("#cameraView").css('height');
	glCanvas.style.zIndex = 20001;
	glCanvas.id = "ARPresGL";
	// document.body.appendChild(glCanvas);
	document.getElementById('cameraView').appendChild(glCanvas);

	var display = new Magi.Scene(glCanvas);
	// get the camera matrix from param and copy it to given 16-elem
	// Float32Array
	// 100 is near plane, 10000 is far plane
	param.copyCameraMatrix(display.camera.perspectiveMatrix, 100, 10000);
	display.camera.useProjectionMatrix = true;
	var videoTex = new Magi.FlipFilterQuad();
	// use the detect canvas as the video texture to keep video and detection in
	// sync
	videoTex.material.textures.Texture0 = new Magi.Texture();
	videoTex.material.textures.Texture0.image = canvas;
	videoTex.material.textures.Texture0.generateMipmaps = false;
	display.scene.appendChild(videoTex);
	var times = [];
	var pastResults = {};
	var lastTime = 0;
	var cubes = {};
	// video frame loop
	display.scene.addFrameListener(function() {
		console.log(canvas.width);
		if (video.paused)
			return;
		if (window.paused)
			return;
		if (video.currentTime == lastTime)
			return;
		lastTime = video.currentTime;
		ctx.drawImage(video, 0, 0, $("#cameraView").css('width'), $(
				"#cameraView").css('height')); // draw video to canvas
		var dt = new Date().getTime();
		canvas.changed = true;
		videoTex.material.textures.Texture0.changed = true;
		videoTex.material.textures.Texture0.upload();
		var t = new Date();
		var detected = detector.detectMarkerLite(raster, 170);
		for ( var idx = 0; idx < detected; idx++) {
			var id = detector.getIdMarkerData(idx);
			var currId;
			if (id.packetLength > 4) {
				currId = -1;
			} else {
				currId = 0;
				for ( var i = 0; i < id.packetLength; i++) {
					currId = (currId << 8) | id.getPacketData(i);
				}
			}
			if (!pastResults[currId]) {
				pastResults[currId] = {};
			}
			detector.getTransformMatrix(idx, resultMat);
			pastResults[currId].age = 0;
			pastResults[currId].transform = Object.asCopy(resultMat);
			if (idx == 0)
				times.push(new Date() - t);
		}
		// create cubes on top of the results
		for ( var i in pastResults) {
			var r = pastResults[i];
			if (r.age > 5)
				delete pastResults[i];
			r.age++;
		}
		for ( var i in cubes)
			cubes[i].display = false;
		for ( var i in pastResults) {
			if (!cubes[i]) {
				var pivot = new Magi.Node();
				pivot.transform = mat4.identity();
				pivot.setScale(80);
				var cube;
				cube = new Magi.Cube();
				cube.setZ(-0.125);
				cube.scaling[2] = 0.25;
				pivot.appendChild(cube);
				var txt = new Magi.Text(i.toString());
				txt.setColor('black');
				txt.setFontSize(48);
				txt.setAlign(txt.centerAlign, txt.bottomAlign).setZ(-0.6).setY(
						-0.34).setScale(1 / 80);
				cube.appendChild(txt);
				pivot.cube = cube;
				pivot.txt = txt;
				display.scene.appendChild(pivot);
				cubes[i] = pivot;
			}
			cubes[i].display = true;
			cubes[i].txt.setText(i.toString());
			var mat = pastResults[i].transform;
			// set transform matrix for the cube
			// using a copy of the resultMat we got back above
			var cm = cubes[i].transform;
			cm[0] = mat.m00;
			cm[1] = -mat.m10;
			cm[2] = mat.m20;
			cm[3] = 0;
			cm[4] = mat.m01;
			cm[5] = -mat.m11;
			cm[6] = mat.m21;
			cm[7] = 0;
			cm[8] = -mat.m02;
			cm[9] = mat.m12;
			cm[10] = -mat.m22;
			cm[11] = 0;
			cm[12] = mat.m03;
			cm[13] = -mat.m13;
			cm[14] = mat.m23;
			cm[15] = 1;
		}
		if (detected == 0)
			times.push(new Date() - t);
		if (times.length > 100) {
			if (window.console)
				console.log(times.reduce(function(s, i) {
					return s + i;
				}) / times.length)
			times.splice(0);
		}
	});
}