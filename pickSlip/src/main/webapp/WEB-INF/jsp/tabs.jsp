<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Pick Slip Tabs</title>
<%@include file="includes/head.jsp"%>
</head>
<body class="hold-transition sidebar-mini">
	<div class="wrapper">
		<section class="content">
			<div class="card card-primary card-tabs">
				<div class="card-header p-0 pt-1"
					style="border-top-left-radius: 0rem; border-top-right-radius: 0rem">
					<ul class="nav nav-tabs" id="custom-tabs-one-tab" role="tablist">
						<li class="nav-item">
							<a class="nav-link <c:if test="${activeTab eq '1'}">active</c:if>" id="custom-tabs-one-pick-slip-tab" href="${contextPath}/tab/1" role="tab">Pick Slip</a>
						</li>
						<li class="nav-item">
							<a class="nav-link <c:if test="${activeTab eq '2'}">active</c:if>" id="custom-tabs-one-archived-pick-slip-tab" href="${contextPath}/tab/2" role="tab">Archived Pick Slip</a>
						</li>
						<li class="nav-item">
							<a class="nav-link <c:if test="${activeTab eq '3'}">active</c:if>" id="custom-tabs-one-location-master-tab" href="${contextPath}/tab/3" role="tab">Location Master</a>
						</li>
						<li class="nav-item">
							<a class="nav-link <c:if test="${activeTab eq '4'}">active</c:if>" id="custom-tabs-one-lr-slip-tab" href="${contextPath}/tab/4" role="tab">Bajaj- LR</a>
						</li>						
						<li class="nav-item  ml-auto"><a class="nav-link"
							href="${contextPath}/logout"><i class="fas fa-power-off"></i></a>
						</li>
					</ul>
				</div>
				<div class="card-body">
					<div class="tab-content" id="custom-tabs-one-tabContent">
						<c:if test="${activeTab eq '1'}">
							<div class="tab-pane fade show active" id="custom-tabs-one-pick-slip" role="tabpanel" aria-labelledby="custom-tabs-one-pick-slip-tab">
								<%@include file="includes/tabs/pickSlip.jsp"%>
							</div>
						</c:if>
						<c:if test="${activeTab eq '2'}">
							<div class="tab-pane fade show active" id="custom-tabs-one-archived-pick-slip" role="tabpanel" aria-labelledby="custom-tabs-one-archived-pick-slip-tab">
								<%@include file="includes/tabs/archivedPickSlip.jsp"%>
							</div>
						</c:if>
						<c:if test="${activeTab eq '3'}">
							<div class="tab-pane fade show active" id="custom-tabs-one-location-master" role="tabpanel" aria-labelledby="custom-tabs-one-location-master-tab">
								<%@include file="includes/tabs/location.jsp"%>
							</div>
						</c:if>
						<c:if test="${activeTab eq '4'}">
							<div class="tab-pane fade show active" id="custom-tabs-one-lr-slip" role="tabpanel" aria-labelledby="custom-tabs-one-lr-slip-tab">
								<%@include file="includes/tabs/lrSlip.jsp"%>
							</div>
						</c:if>						
					</div>
				</div>
			</div>
		</section>
	</div>
	<div class="card card-primary" style="display: none;">
		<form:form method="POST" action="${contextPath}/pick-slip-update-status" id="updatePickSlipStatusform">
			<div class="card-body">
				<div class="form-group">
					<div class="input-group">
						<div class="custom-file">
							<input type="text" name="pickSlipNos" id="updateStatusPickSlipNos" required="required"> 
							<input type="text" name="status" id="status" required="required">
							<input type="text" name="tabId" id="tabId" required="required">
						</div>
						<div class="input-group-append">
							<button type="submit" class="btn btn-primary">Submit</button>
						</div>
					</div>
				</div>
			</div>
		</form:form>
	</div>
</body>
</html>
