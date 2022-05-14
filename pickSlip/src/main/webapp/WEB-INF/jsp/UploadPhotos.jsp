<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Upload Photos</title>
<link href="${contextPath}/resources/plugins/uppy/uppy.min.css" rel="stylesheet">
<%@include file="includes/head.jsp"%>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<div class="card">
					<div class="card-header">
						<h3 class="card-title">Pick Slip Details</h3>
					</div>
					<!-- /.card-header -->
					<div class="card-body">
						<table class="table table-bordered table-sm">
							<tbody>
								<tr>
									<td>Sales Order No</td>
									<td>${pickSlip.orderNumber}</td>
								</tr>
								<tr>
									<td>Pick Slip Date</td>
									<td>${not empty pickSlip.pickSlipDate ? pickSlip.pickSlipDate.format(localDateFormatter) : ''}</td>
								</tr>
								<tr>
									<td>Delivery No</td>
									<td>${pickSlip.deliveryNo}</td>
								</tr>
								<tr>
									<td>Pick Slip No</td>
									<td>${pickSlip.pickSlipNo}</td>
								</tr>
								<tr>
									<td>Pick Slip Updated Date</td>
									<td>${not empty pickSlip.pickSlipUpdatedDate ? pickSlip.pickSlipUpdatedDate.format(localDateTimeFormatter) : ''}</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div id="drag-drop-area"></div>
					<!-- /.card -->
				</div>
				<!-- /.card -->
			</div>
			<!-- /.col -->
		</div>
		<!-- /.row -->
	</div>
	<script src="${contextPath}/resources/plugins/uppy/uppy.min.js"></script>
	<script>
		const uppy = new Uppy.Core();
		uppy.use(Uppy.Dashboard, {
			target : '#drag-drop-area',
			inline : true,
			width : "100%"
		})
// 		uppy.use(Uppy.Webcam, {
// 			target : Uppy.Dashboard,
// 			modes : [ 'picture' ],
// 			videoConstraints : {
// 				facingMode : 'environment'
// 			}
// 		})
		//   uppy.use(Uppy.Compressor, {quality: 0.6, limit: 10,})
		//   uppy.use(Uppy.XHRUpload, { formData: true, bundle: true, endpoint: contextPath + '/upload-photos?pickSlipNo=${pickSlipDataDto.pickSlipNo}' }) 
		uppy.use(Uppy.XHRUpload, {
			fieldName : 'files[]',
			endpoint : contextPath + '/upload-photos?pickSlipNo=${pickSlip.pickSlipNo}'
		})
	</script>
</body>
</html>