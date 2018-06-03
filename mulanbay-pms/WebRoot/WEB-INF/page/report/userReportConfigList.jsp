<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>用户计划设置</title>
	<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/mulanbay/planCommon.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/userReportConfig.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/operationLogCompare.js"></script>

</head>
<body class="easyui-layout" data-options="fit:true,border:false">
<div region="center" border="true" title="用户计划设置"
	 style="border-left:0px;border-right:0px; height: 80px">
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
									   name="name" style="width:200px;" /></td>
							<td>状态:</td>
							<td>
								<select class="easyui-combobox" name="status"
										style="width:70px;">
									<option value="">全部</option>
									<option value="DISABLE">不可用</option>
									<option value="ENABLE">可用</option>
								</select>
							</td>
							<td align="center" colspan="2">
								<a href="javascript:void(0)" class="easyui-linkbutton"
								   icon="icon-search" onclick="showAll()">查询</a> <a
									href="javascript:void(0)" class="easyui-linkbutton"
									icon="icon-cancel" onclick="resetSearchWindow()">重置</a>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div region="center" border="true" title="用户计划列表" split="true"
				 style="background:#E2E377;overflow-y: hidden">
				<div id="grid" fit="true"></div>
			</div>
		</div>
	</div>

	<div id="eidt-window" title="用户计划配置信息" class="easyui-window"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div class="yhdiv">
			<form id="ff" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>选取模板:</td>
						<td colspan="3">
							<input id="reportConfigList" class="easyui-combotree" style="width:460px;"
								   data-options="editable:true" name="reportConfigId">
						</td>
					</tr>
					<tr>
						<td>类型选择:</td>
						<td colspan="3">
							<div id="selectList"></div>
						</td>
					</tr>
					<tr>
						<td>标题:</td>
						<td colspan="3"><input class="easyui-validatebox" type="text"
											   name="title" style="width:450px;" />
						</td>
					</tr>
					<tr>
						<td>状态:</td>
						<td>
							<input name="status" type="radio" class="easyui-validatebox" required="true" value="ENABLE" style="width:30px">可用
							<input name="status" type="radio" class="easyui-validatebox" checked="checked" required="true" value="DISABLE" style="width:30px">不可用
						</td>
						<td>排序号:</td>
						<td><input class="easyui-numberspinner" type="text"
								   name="orderIndex" style="width:155px;" data-options="required:true,min:0,precision:0"/>
						</td>
					</tr>
					<tr>
						<td>消息提醒:</td>
						<td>
							<input name="remind" type="radio" class="easyui-validatebox" required="true" value="true" style="width:30px">是
							<input name="remind" type="radio" class="easyui-validatebox" checked="checked" required="true" value="false" style="width:30px">否
							<a href="javascript:void(0)" class="easyui-linkbutton"
							   icon="icon-ok" onclick="openRemindWindow()">配置</a></a>
						</td>
					</tr>
					<tr>
						<td>备注:</td>
						<td colspan=3><input class="easyui-textbox"  data-options="multiline:true"
											 type="textarea" name="remark" style="width:460px;height:40px"></input></td>
					</tr>
				</table>
				<input type="hidden" name='id' id="id"/>
				<input type="hidden" name='createdTime' />
				<div class="submitForm">
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-ok" onclick="saveData()">保存</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
						icon="icon-cancel" onclick="closeWindow()">关闭</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-info" onclick="showEditLogCompare('UserReportConfig','id')">修改记录</a>
				</div>
			</form>
		</div>
	</div>

<div id="eidt-user-plan-remind-window" title="用户计划提醒配置信息" class="easyui-window"
	 data-options="modal:true,closed:true,iconCls:'icon-info'">
	<div style="width:100%;height:auto;">
		<form id="ff-user-plan-remind" method="post">
			<table cellpadding="5" class="tableForm">
				<tr>
					<td>提醒时间:</td>
					<td>
						<input class="easyui-timespinner" name="remindTime" labelPosition="top" value="08:30" style="width:160px;">
					</td>
				</tr>
				<tr>
					<td>备注:</td>
					<td colspan=3><input class="easyui-validatebox"
										 type="textarea" name="remark" style="width:99%;"></input></td>
				</tr>
			</table>
			<input type="hidden" name='id' id="userReportRemindId"/>
			<input type="hidden" name='createdTime' />
			<input type="hidden" name='userReportConfigId' id="userReportConfigIdInRemind"/>
			<div class="submitForm">
				<a href="javascript:void(0)" class="easyui-linkbutton"
				   icon="icon-ok" onclick="saveRemindData()">保存</a>
				<a href="javascript:void(0)" class="easyui-linkbutton"
				   icon="icon-cancel" onclick="closeRemindWindow()">关闭</a>
				<a href="javascript:void(0)" class="easyui-linkbutton"
				   icon="icon-info" onclick="showEditLogCompare('UserReportRemind','userReportRemindId')">修改记录</a>
			</div>
		</form>
	</div>
</div>

</div>
<%@ include file="../log/operationLogCompareInclude.jsp"%>

</body>
</html>
