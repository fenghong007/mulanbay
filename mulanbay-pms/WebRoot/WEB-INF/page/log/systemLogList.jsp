<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>系统日志管理</title>
	<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/mulanbay/systemLog.js"></script>

</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="center" border="true" title="系统日志管理"
		style="border-left:0px;border-right:0px; height: 188px">
		<div class="easyui-layout" id="subWrap"
			style="width:100%;height:100%;background:#0A3DA4;"
			data-options="fit:true,border:false">
			<div region="north" border="true" title="查询条件" split="true"
				style="overflow:auto;height:110px;" data-options="selected:false"
				icon="icon-edit" class="yhbackground">
				<form id="search-window" method="post">
					<table cellpadding="5" class="tableForm" style="width: 100%;">
						<tr>
							<td>起始日期:</td>
							<td><input class="easyui-datebox" name="startDate"
									   data-options="showSeconds:false" style="width:110px">--
								<input class="easyui-datebox" name="endDate"
									   data-options="showSeconds:false" style="width:110px"></td>
							</td>
							<td>关键字:</td>
							<td><input class="easyui-validatebox" type="text"
									   name="name" style="width:150px;" /></td>
							<td>请求参数:</td>
							<td><input class="easyui-validatebox" type="text"
									   name="paras" style="width:150px;" /></td>
						</tr>
						<tr>
							<td>功能点:</td>
							<td><input id="systemFunctionList" class="easyui-combotree" style="width:240px"
									   data-options="editable:true" name="functionId"></td>
							<td>日志级别:</td>
							<td><select class="easyui-combobox" name="logLevel"
										style="width:155px;">
								<option value="">全部</option>
								<option value="NORMAL">普通</option>
								<option value="WARNING">警告</option>
								<option value="ERROR">异常</option>
								<option value="FATAL">致命</option>
							</select>
							<td colspan="2" align="center">
								<a href="javascript:void(0)" class="easyui-linkbutton"
								   icon="icon-search" onclick="showAll()">查询</a> <a
									href="javascript:void(0)" class="easyui-linkbutton"
									icon="icon-cancel" onclick="resetSearchWindow()">重置</a>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div region="center" border="true" title="系统日志管理" split="true"
				 style="background:#E2E377;overflow-y: hidden">
				<div id="grid" fit="true"></div>
			</div>
		</div>
	</div>

	<div id="paras-window" title="请求参数信息" class="easyui-window"
		 style="width:500px;height:400px;padding:10px;"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<table cellpadding="5">
			<tr>
				<td valign="top">
					<table id="parasContent" title="请求参数信息" class="easyui-treegrid"
						   style="width:450px;height:auto"
						   data-options="rownumbers: true,idField: 'id',treeField: 'name',tools:'#parasContenttt'">
						<thead>
						<tr>
							<th data-options="field:'name'" width="200">项</th>
							<th data-options="field:'value'" width="250">值</th>
						</tr>
						</thead>
					</table>
				</td>
			</tr>
		</table>
	</div>
	<div id="parasContenttt">
		<a href="javascript:void(0)" class="icon-add" onclick="javascript:$('#parasContent').treegrid('expandAll')"></a>
		<a href="javascript:void(0)" class="icon-edit" onclick="javascript:$('#parasContent').treegrid('collapseAll')"></a>
	</div>


</body>
</html>
