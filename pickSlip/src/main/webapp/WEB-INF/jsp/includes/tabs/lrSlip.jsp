<div class="row">
	<div class="col-md-3">
		<div class="card card-primary">
			<form:form method="POST" action="${contextPath}/lr-slip-upload-print" enctype="multipart/form-data">
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
</div>
<script>
$(document).ready(function() {
    bsCustomFileInput.init();
});
</script>

