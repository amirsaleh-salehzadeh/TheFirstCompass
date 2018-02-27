<html>
<head>
<title>Basic QR-code reader example - Version 1.0.1</title>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css">
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-md-6">
				<div id="example"
					class="thumbnail embed-responsive embed-responsive-4by3">
				</div>
				<div class="boxWrapper auto">
					<div id="hiddenImg"></div>
					<div id="qrContent" class="alert alert-info" role="alert">
						<p>No QR Code in sight.</p>
					</div>
				</div>
				<div class="col-md-6">
					<form>
						<label for="videoSource">Select Camera</label> <select
							id="videoSource"><option selected>Default
								Camera</option></select>
					</form>
				</div>

			</div>
		</div>
	</div>

	<script src="js/location/camera/jquery.min.js"></script>

	<script src="js/location/camera/say-cheese.js"></script>

	<script src="js/location/camera/qr/grid.js"></script>
	<script src="js/location/camera/qr/version.js"></script>
	<script src="js/location/camera/qr/detector.js"></script>
	<script src="js/location/camera/qr/formatinf.js"></script>
	<script src="js/location/camera/qr/errorlevel.js"></script>
	<script src="js/location/camera/qr/bitmat.js"></script>
	<script src="js/location/camera/qr/datablock.js"></script>
	<script src="js/location/camera/qr/bmparser.js"></script>
	<script src="js/location/camera/qr/datamask.js"></script>
	<script src="js/location/camera/qr/rsdecoder.js"></script>
	<script src="js/location/camera/qr/gf256poly.js"></script>
	<script src="js/location/camera/qr/gf256.js"></script>
	<script src="js/location/camera/qr/decoder.js"></script>
	<script src="js/location/camera/qr/qrcode.js"></script>
	<script src="js/location/camera/qr/findpat.js"></script>
	<script src="js/location/camera/qr/alignpat.js"></script>
	<script src="js/location/camera/qr/databr.js"></script>

	<script src="js/location/camera/effects_saycheese.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			go();
		});
	</script>
</body>
</html>
