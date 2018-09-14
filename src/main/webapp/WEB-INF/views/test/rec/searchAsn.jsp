<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/common.jsp"%>

<!DOCTYPE html>
<html ng-app="app">
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no">
<title>测试Asn单列表查询</title>
<script type="text/javascript">
	app.controller("asn",function($scope, $http) {
		$scope.showList = function() {
			$scope.getPageByJson('${basePath}asn/list');
		};
	})
</script>
</head>
<body ng-controller="asn">
	<input type="button" value="请求" ng-click="showList()" />
	<div class="containt" >
		<table cellspacing="0" id="table_bl_billlist"
			class="table table-hover zytable">
			<thead>
				<tr>
					<th>货主</th>
					<th>仓库</th>
					<th>ASN单号</th>
					<th>订单类型</th>
					<th>单据来源</th>
					<!-- <th>PO单号<br/>相关单号1<br/>相关单号2</th> -->
				</tr>
			</thead>
			<tbody>
				<tr ng-repeat="item in pageData">
					<td ng-bind="item.ownerComment"></td>
					<td ng-bind="item.wareHouseComment"></td>
					<td ng-bind="item.asn.asnNo"></td>
					<td ng-bind="item.asn.docType"></td>
					<td ng-bind="item.asn.dataFrom"></td>
				</tr>
			</tbody>
		</table>
	</div>
	<page></page>
</body>
</html>
