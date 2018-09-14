<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/common.jsp"%>

<!DOCTYPE html>
<html ng-app="app">
<head>
    <base href="/">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>app</title>
	<link href="${ctxStatic}/css/styles.454d9a.css" rel="stylesheet">
</head>

<body>
    <div ui-view style="height:100%"></div>
	<script type="text/javascript" src="${ctxStatic}/js/vendor.454d9a.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/app.454d9a.js"></script>
</body>

</html>
