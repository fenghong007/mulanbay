<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>数据库清理</title>
	<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/mulanbay/databaseClean.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/operationLogCompare.js"></script>

</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="center" border="true" title="数据库清理统计"
		style="border-left:0px;border-right:0px; height: 188px">
		<div class="easyui-layout" id="subWrap"
			style="width:100%;height:100%;background:#0A3DA4;"
			data-options="fit:true,border:false">
			<div region="north" border="true" title="查询条件" split="true"
				style="overflow:auto;height:70px;" data-options="selected:false"
				icon="icon-edit" class="yhbackground">
				<form id="search-window" method="post">
					<table cellpadding="5" class="tableForm" style="width: 100%;">
						<tr>
							<td>名称:</td>
							<td><input class="easyui-validatebox" type="text"
									   name="name" style="width:235px;" /></td>
							<td>状态:</td>
							<td>
								<select class="easyui-combobox" name="status"
										style="width:155px;">
									<option value="">全部</option>
									<option value="DISABLE">不可用</option>
									<option value="ENABLE">可用</option>
								</select>
							</td>
							<td align="center">
								<a href="javascript:void(0)" class="easyui-linkbutton"
								   icon="icon-search" onclick="showAll()">查询</a> <a
									href="javascript:void(0)" class="easyui-linkbutton"
									icon="icon-cancel" onclick="resetSearchWindow()">重置</a>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div region="center" border="true" title="数据库清理列表" split="true"
				 style="background:#E2E377;overflow-y: hidden">
				<div id="grid" fit="true"></div>
			</div>
		</div>
	</div>

	<div id="eidt-window" title="数据库清理信息" class="easyui-window"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div class="yhdiv">
			<form id="ff" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>名称:</td>
						<td colspan="3"><input class="easyui-validatebox" type="text"
											   name="name" style="width:440px;" />
						</td>
					</tr>
					<tr>
						<td>表名:</td>
						<td><input class="easyui-validatebox" type="text"
								   name="tableName" style="width:160px;" /></td>
						<td>时间字段:</td>
						<td><input class="easyui-validatebox" type="text"
								   name="dateField" style="width:160px;" /></td>
					</tr>
					<tr>
						<td>保留天数:</td>
						<td><input class="easyui-numberspinner"
								   name="days" style="width:155px;" data-options="required:true,min:0,precision:0"/>
						</td>
						<td>删除类型:</td>
						<td>
							<input name="cleanType" type="radio" class="easyui-validatebox" checked="checked" required="true" value="DATE_COMPARE" style="width:30px">按时间条件
							<input name="cleanType" type="radio" class="easyui-validatebox" required="true" value="TRUNCATE" style="width:30px">全表删除
						</td>
					</tr>
					<tr>
						<td>附加条件:</td>
						<td colspan="3"><input class="easyui-validatebox" type="text"
											   name="extraCondition" style="width:440px;" />
						</td>
					</tr>
					<tr>
						<td>排序号:</td>
						<td><input class="easyui-numberspinner" type="text"
								   name="orderIndex" style="width:158px;" data-options="required:true,min:0,precision:0"/>
						</td>
						<td>状态:</td>
						<td>
							<input name="status" type="radio" class="easyui-validatebox" required="true" value="ENABLE" style="width:30px">可用
							<input name="status" type="radio" class="easyui-validatebox" checked="checked" required="true" value="DISABLE" style="width:30px">不可用
						</td>
					</tr>
				</table>
				<input type="hidden" name='id' id="id" />
				<input type="hidden" name='createdTime' />
				<input type="hidden" name='lastCleanTime' />
				<input type="hidden" name='lastCleanCounts' />
				<div class="submitForm">
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-ok" onclick="saveData()">保存</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
						icon="icon-cancel" onclick="closeWindow()">取消</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-info" onclick="showEditLogCompare('BuyType','id')">修改记录</a>
				</div>
			</form>
		</div>
	</div>
	<%@ include file="../log/operationLogCompareInclude.jsp"%>

</body>
</html>
