<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>操作日志管理</title>
	<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/mulanbay/operationLog.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/operationLogCompare.js"></script>

</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="center" border="true" title="系统功能管理"
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
							<td>功能点类型:</td>
							<td><select class="easyui-combobox" name="functionType"
										style="width:155px;">
								<option value="">全部</option>
								<option value="CREATE">新增</option>
								<option value="EDIT">修改</option>
								<option value="DELETE">删除</option>
								<option value="SEARCH">查询</option>
								<option value="STAT">统计</option>
								<option value="OTHER">其他</option>
							</select>
							</td>
							<td colspan="2">主键值:&nbsp;&nbsp;<input class="easyui-validatebox" type="text"
																   name="idValue" style="width:150px;" />
								&nbsp;&nbsp;
								<a href="javascript:void(0)" class="easyui-linkbutton"
								   icon="icon-search" onclick="showAll()">查询</a> <a
										href="javascript:void(0)" class="easyui-linkbutton"
										icon="icon-cancel" onclick="resetSearchWindow()">重置</a>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div region="center" border="true" title="操作日志管理" split="true"
				 style="background:#E2E377;overflow-y: hidden">
				<div id="grid" fit="true"></div>
			</div>
		</div>
	</div>

	<div id="contentDetail-window" title="信息详情" class="easyui-window"
		 style="width:500px;height:400px;padding:10px;"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<table cellpadding="5">
			<tr>
				<td valign="top">
					<table id="contentDetail" title="信息" class="easyui-treegrid"
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
		<a href="javascript:void(0)" class="icon-add" onclick="javascript:$('#contentDetail').treegrid('expandAll')"></a>
		<a href="javascript:void(0)" class="icon-edit" onclick="javascript:$('#contentDetail').treegrid('collapseAll')"></a>
	</div>

	<div id="setOperationLogFunctionId-window" title="设置管理功能点" class="easyui-window"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div class="yhdiv">
			<form id="setOperationLogFunctionId-form" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>设置类型:</td>
						<td>
							<input name="needReSet" type="radio" class="easyui-validatebox" checked="checked" required="true" value="false" style="width:30px">设置遗漏的
							<input name="needReSet" type="radio" class="easyui-validatebox" required="true" value="true" style="width:30px">全部重新设置
						</td>
					</tr>
				</table>
				<div class="submitForm">
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-stat" onclick="setOperationLogFunctionId()">设置</a> &nbsp;
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-cancel" onclick="closeWindow('setOperationLogFunctionId')">关闭</a>
				</div>
			</form>
		</div>
	</div>

	<div id="bean-detail-window" title="操作对象详情(基于数据库中最新数据)" class="easyui-window"
		 style="width:500px;height:400px;padding:10px;"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<table cellpadding="5">
			<tr>
				<td valign="top">
					<table id="beanDetailContent" title="操作对象详情" class="easyui-treegrid"
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

	<%@ include file="../log/operationLogCompareInclude.jsp"%>

</body>
</html>
