<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/common.jsp"%>

<!DOCTYPE html>
<html ng-app="app">
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no">
<title>app</title>

<script type="text/javascript">
	app.controller("login", function($scope, $http,$compile,tooltip) {
		$scope.itemIndex = 0;
		$scope.add = function() {
			$http({
                method: 'POST',
                url: "${basePath}console/login/doLogin",
                data: $scope.wms,
                headers: {
                    'Content-Type': "application/json;charset=UTF-8"
                }
            }).success(function (data) {
            	if ( data.status == 1 ) {
            		alert("保存成功");
            	} else {
            		alert("保存失败，错误码："+data.message);
            	}
            }).error(function(data, status, headers, config) {  
                //处理错误  
                if ( data.status == 0 ) {
                	alert(data.message);
            	}
            });
		};
	});
</script>
</head>

<body ng-controller="login" >
	<div class="container">
		<div class="row">
			<div class="col-md-9 text-right">
				<input type="button" class="btn btn-warning" value="保存" ng-click="add()" />
			</div>
		</div>
		<div class="row input-group-sm">
			<div class="col-md-3">
				<span>企业名称：</span><input class="form-control" ng-model="wms.orgId" type="text" ng-trim="true" />
			</div>
			<div class="col-md-3">
				<span>登录帐号：</span><input class="form-control" ng-model="wms.username" type="text" ng-trim="true" />
			</div>
			<div class="col-md-3">
				<span>登录密码：</span><input class="form-control" ng-model="wms.password" type="text" ng-trim="true" />
			</div>
		</div>
</body>
</html>
