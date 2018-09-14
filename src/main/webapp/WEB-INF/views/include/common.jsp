<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%-- <link rel="stylesheet" href="${ctxStatic}/dist/bootstrap/css/bootstrap-theme.min.css" /> --%>
<link rel="stylesheet" href="${ctxStatic}/dist/bootstrap/bootstrap.min.css" />
<%-- <link rel="stylesheet" type="text/css" href="${ctxStatic}/dist/easyui/themes/default/easyui.css"> --%>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/public/directive/page/page.css"/>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/public/tooltip/tooltip.css"/>
<link rel="stylesheet" href="${ctxStatic}/css/common/common.css" />

<script src="${ctxStatic}/dist/jquery/jquery-1.9.1.js"></script>
<script src="${ctxStatic}/dist/jquery/jquery.form.js"></script>
<script src="${ctxStatic}/dist/angularJS/angular.min.js"></script>
<script src="${ctxStatic}/dist/bootstrap/js/bootstrap.min.js"></script>
<script src="${ctxStatic}/public/app.js"></script>
<script src="${ctxStatic}/public/directive/page/page.js"></script>
<script src="${ctxStatic}/public/common.js"></script>
<script src="${ctxStatic}/public/tooltip/tooltip.js"></script>

<script>
	var basePath = '<%=request.getAttribute("basePath")%>';
	
	function getBasePath() {
		return basePath;
	}
	function getBaseAdminPath() {
		return basePath+"console/";
	}
</script>
