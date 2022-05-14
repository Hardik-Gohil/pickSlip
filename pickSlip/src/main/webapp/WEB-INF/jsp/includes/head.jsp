<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!-- Google Font: Source Sans Pro -->
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,400i,700&display=fallback">
<!-- Font Awesome -->
<link rel="stylesheet" href="${contextPath}/resources/plugins/fontawesome-free/css/all.min.css">
<!-- icheck bootstrap -->
<link rel="stylesheet" href="${contextPath}/resources/plugins/icheck-bootstrap/icheck-bootstrap.min.css">
<!-- iCheck for checkboxes and radio inputs -->
<!-- Ionicons -->
<link rel="stylesheet" href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css">
<!-- SweetAlert2 -->
<link rel="stylesheet" href="${contextPath}/resources/plugins/sweetalert2-theme-bootstrap-4/bootstrap-4.min.css">
<!-- DataTables -->
<link rel="stylesheet" href="${contextPath}/resources/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css">
<link rel="stylesheet" href="${contextPath}/resources/plugins/datatables-responsive/css/responsive.bootstrap4.min.css">
<link rel="stylesheet" href="${contextPath}/resources/plugins/datatables-buttons/css/buttons.bootstrap4.min.css">
<!-- Theme style -->
<link rel="stylesheet" href="${contextPath}/resources/dist/css/adminlte.min.css">

  
<!-- jQuery -->
<script src="${contextPath}/resources/plugins/jquery/jquery.min.js"></script>
<!-- Bootstrap 4 -->
<script src="${contextPath}/resources/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<!-- bs-custom-file-input -->
<script src="${contextPath}/resources/plugins/bs-custom-file-input/bs-custom-file-input.min.js"></script>
<!-- DataTables  & Plugins -->
<script src="${contextPath}/resources/plugins/datatables/jquery.dataTables.min.js"></script>
<script src="${contextPath}/resources/plugins/datatables-bs4/js/dataTables.bootstrap4.min.js"></script>
<%-- <script src="${contextPath}/resources/plugins/datatables-responsive/js/dataTables.responsive.min.js"></script> --%>
<%-- <script src="${contextPath}/resources/plugins/datatables-responsive/js/responsive.bootstrap4.min.js"></script> --%>
<%-- <script src="${contextPath}/resources/plugins/datatables-buttons/js/dataTables.buttons.min.js"></script> --%>
<%-- <script src="${contextPath}/resources/plugins/datatables-buttons/js/buttons.bootstrap4.min.js"></script> --%>
<%-- <script src="${contextPath}/resources/plugins/jszip/jszip.min.js"></script> --%>
<%-- <script src="${contextPath}/resources/plugins/pdfmake/pdfmake.min.js"></script> --%>
<%-- <script src="${contextPath}/resources/plugins/pdfmake/vfs_fonts.js"></script> --%>
<%-- <script src="${contextPath}/resources/plugins/datatables-buttons/js/buttons.html5.min.js"></script> --%>
<%-- <script src="${contextPath}/resources/plugins/datatables-buttons/js/buttons.print.min.js"></script> --%>
<%-- <script src="${contextPath}/resources/plugins/datatables-buttons/js/buttons.colVis.min.js"></script> --%>
<!-- AdminLTE App -->
<script src="${contextPath}/resources/dist/js/adminlte.min.js"></script>	
<!-- SweetAlert2 -->
<script src="${contextPath}/resources/plugins/sweetalert2/sweetalert2.min.js"></script>

<script type="text/javascript">
var contextPath = '<c:out value="${contextPath}"/>';
</script>