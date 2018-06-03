<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>用户计划设置</title>
	<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/mulanbay/planCommon.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/userPlan.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/userPlanConfigValuePt.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/operationLogCompare.js"></script>
	<script type="text/javascript" src="/static/js/echarts/echarts-all-3.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/mulanbaychart.js"></script>

</head>
<body class="easyui-layout" data-options="fit:true,border:false">
<div region="center" border="true" title="用户计划设置"
	 style="border-left:0px;border-right:0px; height: 80px">
		<div class="easyui-layout" id="subWrap"
			style="width:100%;height:100%;background:#0A3DA4;"
			data-options="fit:true,border:false">
			<div region="north" border="true" title="查询条件" split="true"
				style="overflow:auto;height:110px;" data-options="selected:false"
				icon="icon-edit" class="yhbackground">
				<form id="search-window" method="post">
					<table cellpadding="5" class="tableForm" style="width: 100%;">
						<tr>
							<td>名称:</td>
							<td><input class="easyui-validatebox" type="text"
									   name="name" style="width:200px;" /></td>
							<td>计划类型:</td>
							<td><select class="easyui-combobox" name="planType"
										style="width:100px;">
								<option value="">全部</option>
								<option value="MONTH">月</option>
								<option value="YEAR">年</option>
								<option value="DAY">天</option>
								<option value="WEEK">周</option>
								<option value="SEASON">季度</option>
							</select></td>
							<td>状态:</td>
							<td>
								<select class="easyui-combobox" name="status"
										style="width:70px;">
									<option value="">全部</option>
									<option value="DISABLE">不可用</option>
									<option value="ENABLE">可用</option>
								</select>
							</td>
						</tr>
						<tr>
							<td>统计最新进度:</td>
							<td>
								<input name="statNow" type="radio" class="easyui-validatebox" required="true" value="true" style="width:30px">是
								<input name="statNow" type="radio" class="easyui-validatebox" checked="checked" required="true" value="false" style="width:30px">否
							</td>
							<td>数据类型:</td>
							<td>
								<select class="easyui-combobox" name="filterType"
										style="width:100px;">
									<option value="ORIGINAL">默认</option>
									<option value="NO_USER">不过滤用户</option>
									<option value="NO_DATE">不过滤时间</option>
									<option value="NONE">完全不过滤</option>
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
		<div style="width:100%;height:500px;">
			<form id="ff" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>选取模板:</td>
						<td colspan="3">
							<input id="planConfigList" class="easyui-combotree" style="width:460px;"
								   data-options="editable:true" name="planConfigId">
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
						<td>用户日历标题:</td>
						<td colspan="3"><input class="easyui-validatebox" type="text"
											   name="calendarTitle" style="width:450px;" />
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
						<td>首次统计日期:</td>
						<td><input class="easyui-datebox" name="firstStatDay" id="firstStatDay"
								   data-options="showSeconds:false" style="width:120px">&nbsp;&nbsp;
							<a href="javascript:getFirstStatDay()"><img src="../static/image/search.png" alt="自动统计"></img></a>
						</td>
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
						<td colspan=3><input class="easyui-validatebox"
											 type="textarea" name="remark" style="width:99%;"></input></td>
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
					   icon="icon-info" onclick="showEditLogCompare('PlanConfig','id')">修改记录</a>
				</div>
			</form>
			<div region="center" border="true" title="配置值列表" split="true"
				 style="background:#E2E377;overflow-y: hidden;height: 200px">
				<div id="gridUserPlanConfigValue" fit="true"></div>
			</div>
		</div>
	</div>

	<div id="eidt-window-pcv" title="配置值" class="easyui-window"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div>
			<form id="ff-plan-config-value" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>年份:</td>
						<td><input class="easyui-numberspinner"
								   name="year" style="width:150px;"
								   data-options="required:true,min:0,precision:0"></td>
					</tr>
					<tr>
						<td>计划次数:</td>
						<td><input class="easyui-numberspinner"
								   name="planCountValue" style="width:150px;"
								   data-options="required:true,min:0,precision:0"></td>
					</tr>
					<tr>
						<td>计划值:</td>
						<td><input class="easyui-numberspinner"
								   name="planValue" style="width:150px;"
								   data-options="required:true,min:0,precision:0"></td>
					</tr>
					<tr>
						<td>备注:</td>
						<td><input class="easyui-textbox" type="text" data-options="multiline:true"
								   name="remark" style="width:150px;height:90px" /></td>
					</tr>
				</table>
				<input type="hidden" id='userPlanId' name="userPlanId"/>
				<input type="hidden" name='createdTime' />
				<input type="hidden" name='id' id="userPlanConfigValueId" />

				<div class="submitForm">
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-ok" onclick="showPlanCommend()">自动推荐</a><br><br>
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-ok" onclick="saveDataUserPlanConfigValue()">保存</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
						icon="icon-cancel" onclick="closeWindowUserPlanConfigValue()">关闭</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-info" onclick="showEditLogCompare('UserPlanConfigValue','userPlanConfigValueId')">修改记录</a>
				</div>
			</form>
		</div>
		<form id="search-window-pcv" method="post">
			<input type="hidden" name='aa' />

		</form>
	</div>

<div id="eidt-user-plan-remind-window" title="用户计划提醒配置信息" class="easyui-window"
	 data-options="modal:true,closed:true,iconCls:'icon-info'">
	<div style="width:100%;height:auto;">
		<form id="ff-user-plan-remind" method="post">
			<table cellpadding="5" class="tableForm">
				<tr>
					<td>开始提醒设置:</td>
					<td>当时间过去<input class="easyui-numberspinner"
							   name="formTimePassedRate" style="width:100px;"
							   data-options="required:true,max:100,min:0,precision:0">
						%时开始提醒
					</td>

				</tr>
				<tr>
					<td>计划完整时提醒:</td>
					<td>
						<input name="finishedRemind" type="radio" class="easyui-validatebox" checked="checked" required="true" value="true" style="width:30px">是
						<input name="finishedRemind" type="radio" class="easyui-validatebox" required="true" value="false" style="width:30px">否
					</td>
				</tr>
				<tr>
					<td>提醒周期:</td>
					<td>每
						<input class="easyui-spinner"
							   name="triggerInterval" style="width:70px;"
							   data-options="required:true,min:1">
						<select class="easyui-combobox" name="triggerType" id="triggerTypeList"
								style="width:70px;" >
							<option value="DAY">天</option>
							<option value="WEEK">周</option>
							<option value="MONTH">月</option>
						</select>一次
					</td>
				</tr>
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
			<input type="hidden" name='id' id="userPlanRemindId"/>
			<input type="hidden" name='createdTime' />
			<input type="hidden" name='userPlanId' id="userPlanIdInRemind"/>
			<div class="submitForm">
				<a href="javascript:void(0)" class="easyui-linkbutton"
				   icon="icon-ok" onclick="saveRemindData()">保存</a>
				<a href="javascript:void(0)" class="easyui-linkbutton"
				   icon="icon-cancel" onclick="closeRemindWindow()">关闭</a>
				<a href="javascript:void(0)" class="easyui-linkbutton"
				   icon="icon-info" onclick="showEditLogCompare('UserPlanRemind','userPlanRemindId')">修改记录</a>
			</div>
		</form>
	</div>
</div>

	<div id="plan-commend-window" title="推荐值统计" class="easyui-window"
	 data-options="modal:true,closed:true,iconCls:'icon-info'">
	<div class="yhdiv">
		<form id="plan-commend-form" method="post">
			<table cellpadding="5" class="tableForm">
				<tr>
					<td>采样日期:</td>
					<td><input class="easyui-datebox" name="startDate"
							   data-options="showSeconds:false" style="width:100px">--
						<input class="easyui-datebox" name="endDate"
							   data-options="showSeconds:false" style="width:100px"></td>
					</td>
				</tr>
			</table>
			<div class="submitForm">
				<a href="javascript:void(0)" class="easyui-linkbutton"
				   icon="icon-stat" onclick="planCommend()">推荐</a> &nbsp;
				<a href="javascript:void(0)" class="easyui-linkbutton"
				   icon="icon-cancel" onclick="closeWindow('plan-commend-window')">关闭</a>
			</div>
		</form>
	</div>

	<div id="plan-analyse-stat-window" title="统计" class="easyui-window"
			 style="width:860px;height:480px;"
			 data-options="modal:true,closed:true,iconCls:'icon-info'">
			<div region="north" border="true" title="查询条件" split="true"
				 style="overflow:auto;height:50px;" data-options="selected:false"
				 icon="icon-edit" class="yhbackground">
				<form id="plan-analyse-search-form" method="post">
					<table cellpadding="5" class="tableForm" style="width: 100%;">
						<tr>
							<td>
								起始日期:
							</td>
							<td>
								<input class="easyui-datebox" name="startDate"
									   data-options="showSeconds:false" style="width:100px">--
								<input class="easyui-datebox" name="endDate"
									   data-options="showSeconds:false" style="width:100px">
							</td>
							<td>选择计划:</td>
							<td><input id="userPlanAnalyseList" class="easyui-combotree" style="width:180px"
									   data-options="editable:true" name="id"></td>
							</td>
							<td align="center" colspan="2">
								<a href="javascript:void(0)" class="easyui-linkbutton"
								   icon="icon-search" onclick="analyseUserPlan()">统计</a>
							</td>
						</tr>
					</table>
					<input type="hidden" name='status' value="2"/>
				</form>
			</div>
			<div region="center" border="true" title="统计" split="true"
				 style="overflow-y: hidden;width:auto;height:auto;">
				<div class="yhdiv">
					<table>
						<tr>
							<td>
								<div region="center" border="true" title="结果" split="true">
									<div id="planCountAnalyseContainer"
										 style="min-width: 400px; height: 450px; margin: 0 auto"></div>
								</div>
							</td>
							<td>
								<div region="center" border="true" title="结果" split="true">
									<div id="planValueAnalyseContainer"
										 style="min-width: 400px; height: 450px; margin: 0 auto"></div>
								</div>
							</td>
						</tr>
					</table>
				</div>
			</div>
		</div>


</div>
<%@ include file="../log/operationLogCompareInclude.jsp"%>

</body>
</html>
