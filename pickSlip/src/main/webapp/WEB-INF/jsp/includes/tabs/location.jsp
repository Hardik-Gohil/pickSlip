<div class="row">
	<div class="col-md-3">
		<div class="card card-primary">
			<form:form method="POST" action="${contextPath}/location-upload" enctype="multipart/form-data">
				<div class="card-body">
					<div class="form-group">
						<div class="input-group">
							<div class="custom-file">
								<input type="file" class="custom-file-input" name="file" id="exampleInputFile" required="required"> 
								<label class="custom-file-label" for="exampleInputFile">Choose file</label>
							</div>
							<div class="input-group-append">
								<button type="submit" class="btn btn-primary">Upload</button>
							</div>
						</div>
					</div>
				</div>
			</form:form>
		</div>
	</div>
	<c:if test="${not empty uploadLocationFileIsSuccess}">
		<div class="col-md-9">
			<div class="card card-outline ${uploadLocationFileIsSuccess ? 'card-success' : 'card-danger'}">
				<div class="card-header">
					<h3 class="card-title">${uploadLocationFileMessage}</h3>
					<div class="card-tools">
						<button type="button" class="btn btn-tool" data-card-widget="remove">
							<i class="fas fa-times"></i>
						</button>
					</div>
				</div>
				<c:if test="${uploadLocationFileIsSuccess eq false and not empty duplicateItemCodes}">
					<div class="card-body">
						<table class="table table-bordered">
							<tbody>
								<tr>
									<td width="15%">Duplicate Item Codes.</td>
									<td width="85%">${duplicateItemCodes}</td>
								</tr>
							</tbody>
						</table>
					</div>
				</c:if>				
			</div>
		</div>
	</c:if>
</div>

<!-- Main content -->
<div class="row">
	<div class="col-12">
		<div class="card">
			<div class="card-header">
				<h3 class="card-title">Location Master</h3>
				<div style="float: right; margin-right: -0.625rem;">
					<ul class="nav nav-pills ml-auto">
						<li class="nav-item px-2"><a
							href="${contextPath}/export-location-master">
								<button id="" class="btn btn-outline-secondary  btn-sm" type="button">
									<i class="fas fa-file-excel"></i>&nbsp;&nbsp;&nbsp;&nbsp;Export
								</button>
						</a></li>
					</ul>
				</div>
			</div>
			<!-- /.card-header -->
			<div class="card-body">
				<table id="exampleLocation" class="table table-bordered table-striped" style="white-space: nowrap; width: 100%;">
					<thead>
						<tr>
							<th>#</th>
							<th>Item Code</th>
							<th>Location</th>
							<th>Master(PCS)</th>
							<th>Inner(PCS)</th>
						</tr>
					</thead>
				</table>
			</div>
			<!-- /.card-body -->
		</div>
		<!-- /.card -->
	</div>
	<!-- /.col -->
</div>
<!-- /.row -->
<script>
$(document).ready(function() {
    bsCustomFileInput.init();
    var table = $('#exampleLocation').DataTable({
        "ajax": {
            'url': contextPath + "/location-data",
            'method': "GET",
            "dataSrc": ""
        },
        "columns": [{
            "data": "locationMasterId"
        }, {
            "data": "itemCode"
        }, {
            "data": "location"
        }, {
            "data": "masterPcs"
        }, {
            "data": "innerPcs"
        }],
        "order": [
            [0, 'asc']
        ],
        "scrollX": true,
        "lengthMenu": [25, 50, 100, 200],
        "pageLength": 200
    });
});
</script>
