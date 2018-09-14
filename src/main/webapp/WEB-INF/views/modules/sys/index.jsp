<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/common.jsp"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<!-- 
				width=device-width  属性控制设备的宽度。假设您的网站将被带有不同屏幕分辨率的设备浏览，可以确保它能正确呈现在不同设备上。
				initial-scale=1.0 确保网页加载时，以 1:1 的比例呈现，不会有任何的缩放。
				user-scalable=no 可以禁用其缩放（zooming）功能
		-->
<meta name="viewport"
	content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no">
<title>DEMO首页</title>
<style type="text/css">
html, body {
	overflow: hidden;
}

.div_header {
	width: 100%;
	height: 10px;
	/* background: #ee8888; */
}
.div_header .div_logout{
	float:right;
	margin-right:5px;
}

.div_body {
	width: 100%;
	height: 90%;
	margin-top:5px;
	/* background: green; */
}

.div_menu {
	/* width: 150px; */
	width:12%;
	height: 100%;
	background: #c0c0c0;
	float: left;
	overflow-y: auto;
}

.div_main {
	width: 87.6%;
	height: 100%;
	/* background: #8888ee; */
	float: right;
	position: relative;
	border:2px solid #eee;
	margin-left:3px;
}

.div_iframe {
	height: 90%;
	width: 100%;
	/* background: #666eee; */
}

.div_foot {
	height: 10px;
	width: 100%;
	/* background: #eee666; */
	line-height: 50px;
	bottom: 0px;
	right: 0px;
	position: absolute;
}

#main-nav {
	margin-left: 1px;
}

#main-nav.ul_tabs.nav-stacked>li>a {
	padding: 10px 8px;
	font-size: 12px;
	font-weight: 600;
	color: #4A515B;
	background: #E9E9E9;
	background: -moz-linear-gradient(top, #FAFAFA 0%, #E9E9E9 100%);
	background: -webkit-gradient(linear, left top, left bottom, color-stop(0%, #FAFAFA),
		color-stop(100%, #E9E9E9));
	background: -webkit-linear-gradient(top, #FAFAFA 0%, #E9E9E9 100%);
	background: -o-linear-gradient(top, #FAFAFA 0%, #E9E9E9 100%);
	background: -ms-linear-gradient(top, #FAFAFA 0%, #E9E9E9 100%);
	background: linear-gradient(top, #FAFAFA 0%, #E9E9E9 100%);
	filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#FAFAFA',
		endColorstr='#E9E9E9');
	-ms-filter:
		"progid:DXImageTransform.Microsoft.gradient(startColorstr='#FAFAFA', endColorstr='#E9E9E9')";
	border: 1px solid #D5D5D5;
	border-radius: 4px;
}

#main-nav.ul_tabs.nav-stacked>li>a>span {
	color: #4A515B;
}

#main-nav.ul_tabs.nav-stacked>li.active>a, #main-nav.ul_tabs.nav-stacked>li>a:hover
	{
	color: #FFF;
	background: #3C4049;
	background: -moz-linear-gradient(top, #4A515B 0%, #3C4049 100%);
	background: -webkit-gradient(linear, left top, left bottom, color-stop(0%, #4A515B),
		color-stop(100%, #3C4049));
	background: -webkit-linear-gradient(top, #4A515B 0%, #3C4049 100%);
	background: -o-linear-gradient(top, #4A515B 0%, #3C4049 100%);
	background: -ms-linear-gradient(top, #4A515B 0%, #3C4049 100%);
	background: linear-gradient(top, #4A515B 0%, #3C4049 100%);
	filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#4A515B',
		endColorstr='#3C4049');
	-ms-filter:
		"progid:DXImageTransform.Microsoft.gradient(startColorstr='#4A515B', endColorstr='#3C4049')";
	border-color: #2B2E33;
}

#main-nav.ul_tabs.nav-stacked>li.active>a, #main-nav.ul_tabs.nav-stacked>li>a:hover>span
	{
	color: #FFF;
}

#main-nav.ul_tabs.nav-stacked>li {
	margin-bottom: 4px;
}

/*定义二级菜单样式*/
.secondmenu a {
	font-size: 10px;
	color: #4A515B;
	text-align: center;
}

.navbar-static-top {
	background-color: #212121;
	margin-bottom: 5px;
}

.navbar-brand {
	background: url('') no-repeat 10px 8px;
	display: inline-block;
	vertical-align: middle;
	padding-left: 50px;
	color: #fff;
}
</style>
<script type="text/javascript">
	$(document).ready(function() {
		$.ajax({
			"url" : getBasePath() + "andy/bill/print",
			"type" : "POST",
			"dataType" : "json",
			"success" : function(data) {

			},
			"error" : function(XMLHttpRequest, textStatus, errorThrown) {
				//alert("操作失败");
			}
		});
		$(window).resize(function(){
			// 设置头部高度
			var win_h = $(window).height();
			var body_h = $(".div_body").height();
			var head_h = win_h - body_h - parseFloat($(".div_body").css("margin-top"));
			$(".div_header").height(head_h).css("line-height",(head_h)+"px");
			$(".div_header").find("[zylh]").each(function() {
				var deviant = parseInt($(this).attr("zylh"));
				$(this).height(head_h-deviant).css("line-height",(head_h-deviant)+"px");
			});
			$(".div_header").find("[zymid]").each(function() {
				var margin = (head_h - $(this).height())/2;
				$(this).css("margin-top", margin + "px");
				$(this).css("margin-bottom", margin + "px");
			});
			// 设置底部高度
			var main_h = $(".div_main").height();
			var iframe_h = $(".div_iframe").height();
			var foot_h = main_h-iframe_h;
			$(".div_foot").height(foot_h).css("line-height",(foot_h)+"px");
			$(".div_foot").find(".lh").height(head_h).css("line-height",foot_h+"px");
			$(".div_foot").find("[zylh]").each(function() {
				var deviant = parseInt($(this).attr("zylh"));
				$(this).height(foot_h-deviant).css("line-height",(foot_h-deviant)+"px");
			});
			$(".div_foot").find("[zymid]").each(function() {
				var margin = (foot_h - $(this).height())/2;
				$(this).css("margin-top", margin + "px");
				$(this).css("margin-bottom", margin + "px");
			});
			// 设置主题宽度
			// var menu_w = $(".div_menu").width();
			// var win_w = $(".div_body").width();
			// var main_style_w = parseFloat($(".div_main").css("margin-right"))+parseFloat($(".div_main").css("border-left-width"))+parseFloat($(".div_main").css("border-right-width"))+parseFloat($(".div_main").css("margin-left"));
			// var menu_style_w = parseFloat($(".div_menu").css("margin-right"))+parseFloat($(".div_menu").css("border-left-width"))+parseFloat($(".div_menu").css("border-right-width"))+parseFloat($(".div_menu").css("margin-left"));
			// $(".div_main").width(win_w-menu_w-main_style_w-menu_style_w-3);
		});
		$(window).resize();
		$("#apply").collapse();
	})
</script>

</head>
<body>
	<div class="div_header">
		<%-- <img zylh="5" zymid="true" src="${ctxStatic}/img/index/whitecloud.png" style="margin-left:10px;" /> --%>
		<h2 zylh="5" zymid="true" style="margin-left:10px; float:left;">DEMO系统</h2>
		<div class="div_logout">
			<em>欢迎您：<shiro:principal/> </em>
			<a href="javascript:void(0);">退出</a>
		</div>
	</div>

	<!-- 主体 -->
	<div class="div_body">
		<div class="div_menu">
			<ul id="main-nav" class="nav ul_tabs nav-stacked" style="">
				<li><a class="nav-header"  href="javascript:void(0);" data-toggle="collapse"> 首页 </a></li>
				<li><a href="#apply" class="nav-header" 
					data-toggle="collapse"> 申报数据 </a>
					<ul id="apply" class="nav nav-list secondmenu collapse"
						style="height: 0px;">
						<li><a href="${ctx}/menu/bill" target="iframe_main">总运单</a></li>
						<li><a href="javascript:void(0);">分运单</a></li>
					</ul></li>
				<li><a href="#errorHandle" class="nav-header collapsed"
					data-toggle="collapse">异常处理 </a>
					<ul id="errorHandle" class="nav nav-list secondmenu collapse"
						style="height: 0px;">
						<li><a href="javascript:void(0);">未处理异常</a></li>
						<li><a href="javascript:void(0);">已处理异常</a></li>
					</ul></li>
				<li><a href="#report" class="nav-header collapsed"
					data-toggle="collapse"> 理货报告</a>
					<ul id="report" class="nav nav-list secondmenu collapse"
						style="height: 0px;">
						<li><a href="javascript:void(0);">报告调阅</a></li>
						<li><a href="javascript:void(0);">报告日志</a></li>
					</ul></li>

				<li><a href="javascript:void(0);"> 数据交换 </a></li>
				<li><a href="javascript:void(0);"> 关于系统 </a></li>

			</ul>

		</div>
		<div class="div_main">
			<div class="div_iframe">
				<iframe width="100%" height="100%" id="iframe_main"
					name="iframe_main" src="${ctx}/menu/bill" frameborder="no" border="0"
					marginwidth="0" marginheight="0"></iframe>
			</div>
			<div class="div_foot" style="text-align: center;" >
				<span class="span_copyright" zylh="0" zymid >Copyright © 2016 中云智慧（北京）科技有限公司</span>
			</div>
		</div>
	</div>
</body>
</html>
