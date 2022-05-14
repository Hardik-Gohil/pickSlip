<div class="row">
	<div class="col-md-3">
		<div class="card card-primary">
			<form:form method="POST" action="${contextPath}/pick-slip-upload" enctype="multipart/form-data">
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
	<c:if test="${not empty uploadPickSlipFileMessage}">
		<div class="col-md-9">
			<div class="card card-outline ${uploadPickSlipFileIsSuccess ? 'card-success' : 'card-danger'}">
				<div class="card-header">
					<h3 class="card-title">${uploadPickSlipFileMessage}</h3>
					<div class="card-tools">
						<button type="button" class="btn btn-tool" data-card-widget="remove">
							<i class="fas fa-times"></i>
						</button>
					</div>
				</div>
				<c:if test="${uploadPickSlipFileIsSuccess}">
					<div class="card-body">
						<table class="table table-bordered">
							<tbody>
								<tr>
									<td width="15%">New Added Pick Slip Nos.</td>
									<td width="85%">${newAddedPickSlipNos}</td>
								</tr>
								<tr>
									<td width="15%">Already Exists Pick Slip Nos.</td>
									<td width="85%">${alreadyExistsPickSlipNos}</td>
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
				<h3 class="card-title">PICKSLIP STATUS REPORT</h3>
				<div style="float: right; margin-right: -0.625rem;">
					<ul class="nav nav-pills ml-auto">
						<li class="nav-item px-2">
							<button id="tab1-btn-select-all" class="btn btn-outline-secondary  btn-sm" type="button">Deselect All</button>
						</li>
						<li class="nav-item px-2">
							<button id="tab1-btn-print-all" class="btn btn-outline-secondary  btn-sm" type="button"><i class="fas fa-print"></i>&nbsp;&nbsp;&nbsp;&nbsp;Print All</button>
						</li>
						<li class="nav-item px-2">
							<button id="tab1-btn-archive-all" class="btn btn-outline-secondary  btn-sm" type="button"><i class="fas fa-file-archive"></i>&nbsp;&nbsp;&nbsp;&nbsp;Archive All</button>
						</li>
						<li class="nav-item px-2">
							<button id="tab1-btn-show-all-children" class="btn btn-outline-secondary  btn-sm" type="button"><i class="fas fa-plus"></i>&nbsp;&nbsp;&nbsp;&nbsp;Expand All</button>
						</li>
						<li class="nav-item px-2">
							<button id="tab1-btn-hide-all-children" class="btn btn-outline-secondary  btn-sm" type="button"><i class="fas fa-minus"></i>&nbsp;&nbsp;&nbsp;&nbsp;Collapse All</button>
						</li>
						<li class="nav-item px-2">
							<a href="${contextPath}/export-pick-slip-data?systemStatus=0"><button id="" class="btn btn-outline-secondary  btn-sm" type="button"><i class="fas fa-file-excel"></i>&nbsp;&nbsp;&nbsp;&nbsp;Export</button></a>
						</li>
					</ul>
				</div>
			</div>
			<!-- /.card-header -->
			<div class="card-body">
				<table id="tab1-datatable" class="table table-bordered table-striped" style="white-space: nowrap; width: 100%;">
					<thead>
						<tr>
							<th></th>
							<th></th>
							<th>Pick Slip no</th>
							<th>Customer Name</th>
							<th>Product Category</th>
							<th>City</th>
							<th>Delivery No</th>
							<th>Pick Slip  Date</th>
							<th>Pick Slip Gen Time</th>
							<th>Pick Slip Updated Date</th>
							<th>Pick Slip Uploaded Date Time</th>
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
/* Formatting function for row details - modify as you need */
function format(d) {
    var returnStr = "No Record Found";
    if (Array.isArray(d.pickSlipItems)) {
        returnStr = "<div class='card-body' style='padding: 5px 0px 0px 5px;'><table class='table table-bordered table-striped' style='width: auto;margin-left: 1%;'><thead><tr style='background: white;'><th>#</th><th>Ordered Item</th><th>Location</th><th>Sub Inventory</th><th>Picked Qty</th><th>Weight(In KGS)</th><th>Volume (In CFT)</th><th>Unit Price</th><th>Amount</th></thead></tr><tbody>";

        for (let i = 0; i < d.pickSlipItems.length; i++) {
            returnStr += "<tr><td>" + (i + 1) + "</td>";
            returnStr += "<td>" + d.pickSlipItems[i].orderedItem + "</td>";
            returnStr += "<td>" + d.pickSlipItems[i].location + "</td>";
            returnStr += "<td>" + d.pickSlipItems[i].subInventory + "</td>";
            returnStr += "<td>" + d.pickSlipItems[i].pickedQty + "</td>";
            returnStr += "<td>" + d.pickSlipItems[i].weight + "</td>";
            returnStr += "<td>" + d.pickSlipItems[i].volume + "</td>";
            returnStr += "<td>" + d.pickSlipItems[i].unitPrice + "</td>";
            returnStr += "<td>" + d.pickSlipItems[i].amount + "</td></tr>";
        }
        returnStr += "</tbody></table></div>";
    }
    return returnStr;
}

$(document).ready(function() {
    bsCustomFileInput.init();
    var table = $('#tab1-datatable').DataTable({
        "ajax": {
            'contentType': 'application/json',
            'url': contextPath + "/get-pick-slips?systemStatus=0",
            'method': "POST",
            'data': function(d) {
                return JSON.stringify(d);
            }
        },
        "columns": [{
                "className": 'dt-control border-right-0',
                "orderable": false,
                "searchable": false,
                "data": "",
                "defaultContent": ''
            },
            {
                "className": 'border-left-0',
                "orderable": false,
                "searchable": false,
                "data": "",
                "defaultContent": '<i class="fa fa-print"></i>&nbsp;&nbsp;<i class="fa fa-file-archive" aria-hidden="true"></i>&nbsp;&nbsp;<i class="fa fa-download" aria-hidden="true"></i></i>&nbsp;&nbsp;<input type="checkbox" class="checkboxAll" checked="true" name="pick-slip-checkbox"/></div>'
            },
            {
                "data": "pickSlipNo"
            },
            {
                "data": "customerName",
                "searchable": false
            },
            {
                "data": "productCategory",
                "searchable": false
            },
            {
                "data": "city",
                "searchable": false
            },
            {
                "data": "deliveryNo",
                "searchable": false
            },
            {
                "data": "pickSlipDate",
                "searchable": false
            },
            {
                "data": "pickslipGenTime",
                "searchable": false
            },
            {
                "data": "pickSlipUpdatedDate",
                "searchable": false
            },
            {
                "data": "systemCreatedOn",
                "searchable": false
            }
        ],
        "order": [
            [10, 'desc'],
            [2, 'asc']
        ],
        "scrollX": true,
        "serverSide": true,
        "language": {
            "search": "Search Pick Slip no:"
        },
        "lengthMenu": [25, 50, 100, 200],
        "pageLength": 200
    });

    // Add event listener for opening and closing details
    $('#tab1-datatable tbody').on('click', 'td.dt-control', function() {
        var tr = $(this).closest('tr');
        var row = table.row(tr);

        if (row.child.isShown()) {
            // This row is already open - close it
            row.child.hide();
            tr.removeClass('shown');
        } else {
            // Open this row
            row.child(format(row.data())).show();
            tr.addClass('shown');
        }
    });

    // Handle click on "Expand All" button
    $('#tab1-btn-show-all-children').on('click', function() {
        // Enumerate all rows
        table.rows().every(function() {
            // If row has details collapsed
            if (!this.child.isShown()) {
                // Open this row
                this.child(format(this.data())).show();
                $(this.node()).addClass('shown');
            }
        });
    });

    // Handle click on "Collapse All" button
    $('#tab1-btn-hide-all-children').on('click', function() {
        // Enumerate all rows
        table.rows().every(function() {
            // If row has details expanded
            if (this.child.isShown()) {
                // Collapse row details
                this.child.hide();
                $(this.node()).removeClass('shown');
            }
        });
    });

    $('#tab1-datatable').on('click', 'tbody .fa-file-archive', function() {
        var data_row = table.row($(this).closest('tr')).data();
        $("#updateStatusPickSlipNos").val(data_row["pickSlipNo"]);
        $("#status").val("1");
        $("#tabId").val("1");
        Swal.fire({
            title: 'Are you sure, you want to archive selected Pick Slip(s) ?',
            text: "You will be able to revert this!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, archive it!'
        }).then((result) => {
            if (result.isConfirmed) {
                $("#updatePickSlipStatusform").submit();
            }
        });
    })

    $('#tab1-btn-archive-all').on('click', function() {
        var array = [];
        table.rows().every(function(rowIdx, tableLoop, rowLoop) {
            var data = this.data();
            var row = $(this.node());
            var checkbox = row.find('input[type="checkbox"]');
            if (checkbox.is(':checked')) {
                array.push(data["pickSlipNo"]);
            }
        });
        if (array.length === 0) {
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'Please Select At Least One Pick Slip'
            });
        } else {
            $("#updateStatusPickSlipNos").val(array);
            $("#status").val("1");
            $("#tabId").val("1");
            Swal.fire({
                title: 'Are you sure, you want to archive selected Pick Slip(s) ?',
                text: "You will be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, archive it!'
            }).then((result) => {
                if (result.isConfirmed) {
                    $("#updatePickSlipStatusform").submit();
                }
            });
        }
    });


    $('#tab1-datatable').on('click', 'tbody .fa-print', function() {
        var data_row = table.row($(this).closest('tr')).data();
        window.open(contextPath + "/print?pickSlipNo=" + data_row["pickSlipNo"], "_blank");
    })

    $('#tab1-btn-print-all').on('click', function() {
        var array = [];
        table.rows().every(function(rowIdx, tableLoop, rowLoop) {
            var data = this.data();
            var row = $(this.node());
            var checkbox = row.find('input[type="checkbox"]');
            if (checkbox.is(':checked')) {
                array.push(data["pickSlipNo"]);
            }
        });
        if (array.length === 0) {
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'Please Select At Least One Pick Slip'
            });
        } else {
            window.open(contextPath + "/print?pickSlipNo=" + array, "_blank");
        }
    });

    $('#tab1-btn-select-all').on('click', function() {
        var btnValue = $(this).text();
        if (btnValue == "Select All") {
            table.rows().every(function(rowIdx, tableLoop, rowLoop) {
                var data = this.data();
                var row = $(this.node());
                var checkbox = row.find('input[type="checkbox"]');
                $(checkbox).prop('checked', true);
            });
            $(this).text("Deselect  All");
        } else {
            table.rows().every(function(rowIdx, tableLoop, rowLoop) {
                var data = this.data();
                var row = $(this.node());
                var checkbox = row.find('input[type="checkbox"]');
                $(checkbox).prop('checked', false);
            });
            $(this).text("Select All");
        }
    });

    $('#tab1-datatable').on('click', 'tbody .fa-download', function() {
        var data_row = table.row($(this).closest('tr')).data();
        window.location = contextPath + "/downloadZip?pickSlipNo=" + data_row["pickSlipNo"];
    })
});
</script>
