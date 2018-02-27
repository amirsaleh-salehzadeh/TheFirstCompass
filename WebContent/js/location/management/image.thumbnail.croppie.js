function createCroppie() {
	$("#main-cropper")
			.append(
					"<span>Use the scroll wheel of your mouse to resize the image</span>");
	var basic = $('#main-cropper').croppie({
		viewport : {
			width : 128,
			height : 128
		},
		boundary : {
			width : 200,
			height : 200
		},
		showZoomer : false
	});
	basic.croppie('bind', {
		url : 'images/NMMU_logo.png'
	});
}

function readFile(input) {
	if (input.files && input.files[0]) {
		var reader = new FileReader();

		reader.onload = function(e) {
			$('#main-cropper').croppie('bind', {
				url : e.target.result
			});
		};

		reader.readAsDataURL(input.files[0]);
	} else {
		swal("Sorry - you're browser doesn't support the FileReader API");
	}
}

$("#upload").change(function() {
	$("#main-cropper").empty();
//	$("#iconCropDiv").empty();
	createCroppie();
	readFile(this);
});

$('#cropIcon').on(
		'click',
		function(ev) {
			$('#main-cropper').croppie('result', {
				type : 'canvas',
				size : 'viewport',
//				format : 'png'
				format : 'jpeg'
			}).then(
					function(resp) {
						$("#croppedIcon").attr('src', resp);
						$("#icon").val($("#croppedIcon").attr('src'));
					});
			$("#main-cropper").empty();
		});
