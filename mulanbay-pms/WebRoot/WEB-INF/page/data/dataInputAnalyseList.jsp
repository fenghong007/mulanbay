<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>数据录入分析管理</title>
	<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/mulanbay/dataInputAnalyse.js"></script>
	<script type="text/javascript" src="/static/js/echarts/echarts-all-3.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/mulanbaychart.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/operationLogCompare.js"></script>

</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="center" border="true" title="数据录入分析管理"
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
								   icon="icon-search" onclick="showAll()">查询</a>
								<a href="javascript:void(0)" class="easyui-linkbutton"
									icon="icon-cancel" onclick="resetSearchWindow()">重置</a>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div region="center" border="true" title="数据录入分析管理" split="true"
				 style="background:#E2E377;overflow-y: hidden">
				<div id="grid" fit="true"></div>
			</div>
		</div>
	</div>

	<div id="eidt-window" title="数据录入分析管理" class="easyui-window"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div class="yhdiv">
			<form id="ff" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>名称:</td>
						<td><input class="easyui-validatebox" type="text"
								   name="name" style="width:158px;" />
						</td>
					</tr>
					<tr>
						<td>表名:</td>
						<td><input class="easyui-validatebox" type="text"
								   name="tableName" style="width:158px;" />
						</td>
					</tr>
					<tr>
						<td>业务字段:</td>
						<td><input class="easyui-validatebox" type="text"
								   name="bussField" style="width:158px;" />
						</td>
					</tr>
					<tr>
						<td>录入字段:</td>
						<td><input class="easyui-validatebox" type="text"
								   name="inputField" style="width:158px;" />
						</td>
					</tr>
					<tr>
						<td>用户字段:</td>
						<td><input class="easyui-validatebox" type="text"
								   name="userField" style="width:158px;" />
						</td>
					</tr>
					<tr>
						<td>状态:</td>
						<td>
							<input name="status" type="radio" class="easyui-validatebox" checked="checked" required="true" value="ENABLE" style="width:30px">可用
							<input name="status" type="radio" class="easyui-validatebox" required="true" value="DISABLE" style="width:30px">不可用
						</td>
					</tr>
					<tr>
						<td>备注:</td>
						<td><input class="easyui-textbox" type="text" data-options="multiline:true"
											 type="textarea" name="remark" style="width:158px;height:60px"></input></td>
						</td>
					</tr>
				</table>
				<input type="hidden" name='id' id="id"/>
				<input type="hidden" name='createdTime' />
				<div class="submitForm">
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-ok" onclick="saveData()">保存</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
						icon="icon-cancel" onclick="closeWindow()">取消</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-info" onclick="showEditLogCompare('DataInputAnalyse','id')">修改记录</a>
				</div>
			</form>
		</div>
	</div>

	<div id="stat-window" title="数据录入延迟统计" class="easyui-window"
		 style="width:850px;height:550px;"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div region="north" border="true" title="查询条件" split="true"
			 style="overflow:auto;height:100px;" data-options="selected:false"
			 icon="icon-edit" class="yhbackground">
			<form id="stat-search-form" method="post">
				<table cellpadding="5" class="tableForm" style="width: 100%;">
					<tr>
						<td>数据录入表:</td>
						<td><input id="dataInputAnalyseList" class="easyui-combotree" style="width:130px"
								   data-options="editable:true" name="dataInputAnalyseId"></td>
						<td>数据视图:</td>
						<td><select class="easyui-combobox" name="statType"
									style="width:130px;">
							<option value="DAY">时间</option>
							<option value="TABLE">表</option>
						</select>
						</td>
						<td>
							时间段:
						</td>
						<td>
							<input class="easyui-datebox" name="startDate"
								   data-options="showSeconds:false" style="width:100px">--
							<input class="easyui-datebox" name="endDate"
								   data-options="showSeconds:false" style="width:100px">
						</td>
					</tr>
					<tr>
						<td>录入延迟类型:</td>
						<td><select class="easyui-combobox" name="compareValue"
									style="width:130px;">
							<option value="">所有</option>
							<option value="n<0">超前</option>
							<option value="n=0">当天</option>
							<option value="n>0">超过一天</option>
							<option value="1<=n<=3">1-3天</option>
							<option value="3<n<=7">3天到一个星期内</option>
							<option value="n<=7">一星期内</option>
							<option value="7<n<=30">一个星期到一个月内</option>
							<option value="n>30">超过一个月</option>
						</select>
						</td>
						<td>选择用户:</td>
						<td><input id="userList" class="easyui-combotree" style="width:130px"
								   data-options="editable:true" name="userId"></td>
						<td align="center" colspan="2">
							<a href="javascript:void(0)" class="easyui-linkbutton"
							   icon="icon-search" onclick="stat()">查询</a>
							<a href="javascript:void(0)" class="easyui-linkbutton"
							   icon="icon-cancel" onclick="resetSearchWindowById('stat-search-form')">重置</a>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div region="center" border="true" title="统计" split="true"
			 style="overflow-y: hidden;width:auto;height:auto;">
			<div region="center" border="true" title="结果" split="true">
				<div id="container"
					 style="min-width: 500px; height: 400px; margin: 0 auto"></div>
			</div>
		</div>
	</div>
	<%@ include file="../log/operationLogCompareInclude.jsp"%>

</body>
</html>
