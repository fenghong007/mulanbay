<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>调度管理</title>
<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/mulanbay/taskTrigger.js"></script>
	<script type="text/javascript" src="/static/js/echarts/echarts-all-3.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/mulanbaychart.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/planCommon.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/userPlanForBuss.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/operationLogCompare.js"></script>

</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="center" border="true" title="调度管理"
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
							<td>调度类型:</td>
							<td>
								<select class="easyui-combobox" name="triggerType"
										style="width:160px;height:23px" >
									<option value="">全部</option>
									<option value="NOW">立刻</option>
									<option value="SECOND">秒</option>
									<option value="MINUTE">分钟</option>
									<option value="HOUR">小时</option>
									<option value="DAY">天</option>
									<option value="WEEK">周</option>
									<option value="MONTH">季度</option>
									<option value="SEASON">季度</option>
									<option value="YEAR">年</option>
									<option value="CRON">自定义</option>
								</select>
							</td>
							<td>状态:</td>
							<td>
								<select class="easyui-combobox" name="triggerStatus"
										 style="width:120px;height:23px" >
									<option value="">全部</option>
									<option value="ENABLE">启用</option>
									<option value="DISABLE">未启用</option>
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
			<div region="center" border="true" title="调度列表" split="true"
				style="background:#E2E377;overflow-y: hidden">
				<div id="grid" fit="true"></div>
			</div>
		</div>
	</div>

	<div id="eidt-window" title="调度信息" class="easyui-window" style="overflow:auto;height:500px;"
		data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div class="yhdiv">
			<form id="ff" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>调度名称:</td>
						<td colspan=3>
							<input class="easyui-textbox" name="name" style="width:510px;height:30px" />
					</tr>
					<tr>
						<td>调度类:</td>
						<td colspan=3>
							<input class="easyui-textbox" name="taskClass" style="width:510px;height:30px" />
					</tr>
					<tr>
						<td>分组:</td>
						<td><input class="easyui-validatebox" type="text"
								   name="groupName" style="width:160px;" />
						</td>
						<td>超时时间:</td>
						<td><input class="easyui-spinner"
								   name="timeout" style="width:160px;"
								   data-options="required:true,min:1">秒</td>
					</tr>
					<tr>
						<td>部署点:</td>
						<td><input class="easyui-validatebox" type="text"
								   name="deployId" style="width:160px;" />
						</td>
						<td>支持分布式:</td>
						<td>
							<input name="distriable" type="radio" class="easyui-validatebox" required="true" value="true" style="width:30px">是
							<input name="distriable" type="radio" class="easyui-validatebox" required="true" value="false" style="width:30px" checked="checked" >否
						</td>
					</tr>
					<tr>
						<td>重做类型:</td>
						<td>
							<select class="easyui-combobox" name="redoType"
									style="width:160px;height:23px" >
								<option value="CANNOT">不能重做</option>
								<option value="MUNUAL_REDO">手动重做</option>
								<option value="AUTO_REDO">自动重做</option>
								<option value="ALL_REDO">手动、自动重做</option>
							</select>
						</td>
						<td>最大支持重做次数:</td>
						<td><input class="easyui-spinner"
								   name="allowedRedoTimes" style="width:160px;"
								   data-options="required:true,min:1"></td>
					</tr>
					<tr>
						<td>调度周期:</td>
						<td>每
							<input class="easyui-spinner"
								   name="triggerInterval" style="width:70px;"
								   data-options="required:true,min:1">
							<select class="easyui-combobox" name="triggerType"
									style="width:70px;" >
								<option value="NOW">立刻</option>
								<option value="SECOND">秒</option>
								<option value="MINUTE">分钟</option>
								<option value="HOUR">小时</option>
								<option value="DAY">天</option>
								<option value="WEEK">周</option>
								<option value="MONTH">月</option>
								<option value="SEASON">季度</option>
								<option value="YEAR">年</option>
								<option value="CRON">自定义</option>
							</select>一次
						</td>
						<td>CRON表达式:</td>
						<td><input class="easyui-validatebox" type="text"
								   name="cronExpression" style="width:160px;" />
					</tr>
					<tr>
						<td>调度参数:</td>
						<td colspan=3>
							<input class="easyui-textbox" name="triggerParas" style="width:510px;height:30px" />
					</tr>
					<tr>
						<td>允许执行的时间段:</td>
						<td colspan=3>
							<input class="easyui-textbox" name="execTimePeriods" style="width:510px;height:30px" />
					</tr>
					<tr>
						<td>时间偏移量:</td>
						<td><input class="easyui-spinner"
								   name="offsetDays" style="width:160px;"
								   data-options="required:true">天</td>
						<td>调度状态:</td>
						<td>
							<input name="triggerStatus" type="radio" class="easyui-validatebox" required="true" value="ENABLE" style="width:30px" checked="checked">启用
							<input name="triggerStatus" type="radio" class="easyui-validatebox" required="true" value="DISABLE" style="width:30px" >关闭
						</td>
					</tr>
					<tr>
						<td>首次执行时间:</td>
						<td><input class="easyui-datetimebox" name="firstExecuteTime"
								   data-options="showSeconds:true" style="width:160px"></td>
						<td>下一次执行时间:</td>
						<td><input class="easyui-datetimebox" name="nextExecuteTime"
								   data-options="showSeconds:true" style="width:160px"></td>
					</tr>
					<tr>
						<td>检查唯一性:</td>
						<td>
							<input name="checkUnique" type="radio" class="easyui-validatebox" required="true" value="true" style="width:30px" checked="checked">是
							<input name="checkUnique" type="radio" class="easyui-validatebox" required="true" value="false" style="width:30px" >否
						</td>
						<td>唯一性类型:</td>
						<td>
							<input name="uniqueType" type="radio" class="easyui-validatebox" required="true" value="IDENTITY" style="width:30px" checked="checked">执行唯一标识
							<input name="uniqueType" type="radio" class="easyui-validatebox" required="true" value="BUSS_DATE" style="width:30px" >运营日期
						</td>
					</tr>
					<tr>
						<td>记录日志:</td>
						<td>
							<input name="loggable" type="radio" class="easyui-validatebox" required="true" value="true" style="width:30px" checked="checked">是
							<input name="loggable" type="radio" class="easyui-validatebox" required="true" value="false" style="width:30px" >否
						</td>
						<td>消息提醒:</td>
						<td>
							<input name="notifiable" type="radio" class="easyui-validatebox" required="true" value="true" style="width:30px" checked="checked">是 &nbsp;&nbsp;
							<input name="notifiable" type="radio" class="easyui-validatebox" required="true" value="false" style="width:30px" >否
						</td>
					</tr>
					<tr>
						<td>调度子任务编号:</td>
						<td colspan=3>
							<input class="easyui-textbox" name="subTaskCodes" style="width:510px;height:30px" />
					</tr>
					<tr>
						<td>调度子任务名称:</td>
						<td colspan=3>
							<input class="easyui-textbox" name="subTaskNames" style="width:510px;height:30px" />
					</tr>
					<tr>
						<td>备注:</td>
						<td colspan=3>
							<input class="easyui-textbox" type="text" data-options="multiline:true"
								   name="remark" style="width:510px;height:60px" />
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
					   icon="icon-info" onclick="showEditLogCompare('Diet','id')">修改记录</a>
				</div>
			</form>
		</div>
	</div>

	<div id="stat-window" title="统计信息（基于当前查询条件）" class="easyui-window"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div class="yhdiv">
			<table>
				<tr>
					<td>
						<div region="center" border="true" title="结果" split="true">
							<div id="container"
								 style="min-width: 500px; height: 450px; margin: 0 auto"></div>
						</div>
					</td>
				</tr>
			</table>

		</div>
	</div>

	<div id="manual-exec-window" title="手动执行调度" class="easyui-window"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div class="yhdiv">
			<form id="manual-exec-form" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>调度器:</td>
						<td colspan="3"><input id="taskTriggerList" class="easyui-combotree" style="width:320px"
								   data-options="editable:true" name="taskTriggerId"></td>
					</tr>
					<tr>
						<td>业务日期:</td>
						<td><input class="easyui-datebox" name="bussDate"
								   data-options="showSeconds:false" style="width:110px">
						</td>
						<td>同步执行:</td>
						<td>
							<input name="isSync" type="radio" class="easyui-validatebox" required="true" value="true" style="width:30px">是
							<input name="isSync" type="radio" class="easyui-validatebox" checked="checked" required="true" value="false" style="width:30px">否
						</td>
					</tr>
				</table>
				<div class="submitForm">
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-stat" onclick="manualExec()">执行</a> &nbsp;
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-cancel" onclick="closeWindow('manual-exec-window')">关闭</a>
				</div>
			</form>
		</div>
	</div>
	<%@ include file="../plan/userPlanInclude.jsp"%>
	<%@ include file="../log/operationLogCompareInclude.jsp"%>

</body>
</html>
