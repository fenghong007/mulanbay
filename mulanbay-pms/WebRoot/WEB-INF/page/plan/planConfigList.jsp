<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>计划设置</title>
	<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/mulanbay/planCommon.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/planConfig.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/statValueConfigPt.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/operationLogCompare.js"></script>

</head>
<body class="easyui-layout" data-options="fit:true,border:false">
<div region="center" border="true" title="计划设置"
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
			<div region="center" border="true" title="计划配置列表" split="true"
				 style="background:#E2E377;overflow-y: hidden">
				<div id="grid" fit="true"></div>
			</div>
		</div>
	</div>

	<div id="eidt-window" title="计划配置信息" class="easyui-window"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div style="width:100%;height:500px;">
			<form id="ff" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>选取模板:</td>
						<td colspan="3">
							<input id="planConfigList" class="easyui-combotree" style="width:460px;"
								   data-options="editable:true" name="tmpPlanConfigId">
						</td>
					</tr>
					<tr>
						<td>名称:</td>
						<td colspan="3"><input class="easyui-validatebox" type="text"
											   name="name" style="width:450px;" />
						</td>
					</tr>
					<tr>
						<td>标题:</td>
						<td colspan="3"><input class="easyui-validatebox" type="text"
											   name="title" style="width:450px;" />
						</td>
					</tr>
					<tr>
						<td>查询类型:</td>
						<td><select class="easyui-combobox" name="sqlType"
									style="width:158px;">
							<option value="SQL">SQL</option>
							<option value="HQL">HQL</option>
						</select></td>
						<td>计划类型:</td>
						<td><select class="easyui-combobox" name="planType"
									style="width:158px;">
							<option value="MONTH">月</option>
							<option value="YEAR">年</option>
							<option value="DAY">天</option>
							<option value="WEEK">周</option>
							<option value="SEASON">季度</option>
						</select></td>
					</tr>
					<tr>
						<td>查询语句:
							<a href="javascript:showSqlHelp()" title="提示"><img src="../static/image/help.png"></img></a>
						</td>
						<td colspan=3>
							<input class="easyui-textbox" type="text" data-options="multiline:true"
								   name="sqlContent" style="width:450px;height:80px" />
						</td>
					</tr>
					<tr>
						<td>时间字段:</td>
						<td><input class="easyui-textbox" type="text"
								   name="dateField" style="width:155px;" />
						</td>
						<td>用户字段:</td>
						<td><input class="easyui-textbox" type="text"
								   name="userField" style="width:155px;" />
						</td>
					</tr>
					<tr>
						<td>比较类型:</td>
						<td>
							<input name="compareType" type="radio" class="easyui-validatebox" required="true" value="MORE" style="width:30px">大于
							<input name="compareType" type="radio" class="easyui-validatebox" checked="checked" required="true" value="LESS" style="width:30px">小于
						</td>
						<td>状态:</td>
						<td>
							<input name="status" type="radio" class="easyui-validatebox" required="true" value="ENABLE" style="width:30px">可用
							<input name="status" type="radio" class="easyui-validatebox" checked="checked" required="true" value="DISABLE" style="width:30px">不可用
						</td>
					</tr>
					<tr>
						<td>排序号:</td>
						<td><input class="easyui-numberspinner" type="text"
								   name="orderIndex" style="width:155px;" data-options="required:true,min:0,precision:0"/>
						</td>
						<td>单位:</td>
						<td><input class="easyui-textbox" type="text"
								   name="unit" style="width:155px;" />
						</td>
					</tr>
					<tr>
						<td>默认次数:</td>
						<td><input class="easyui-numberspinner" type="text"
								   name="defaultPlanCountValue" style="width:155px;" data-options="required:true,min:0,precision:0"/>
						</td>
						<td>默认值:</td>
						<td><input class="easyui-numberspinner" type="text"
								   name="defaultPlanValue" style="width:155px;" data-options="required:true,min:0,precision:0" />
						</td>
					</tr>
					<tr>
						<td>关联的业务:</td>
						<td>
							<input id="relatedBeansList" class="easyui-combotree" style="width:150px;"
								   data-options="editable:true" name="relatedBeans">
						</td>
						<td>首次统计日期:</td>
						<td><input class="easyui-datebox" name="firstStatDay" id="firstStatDay"
								   data-options="showSeconds:false" style="width:120px">&nbsp;&nbsp;
							<a href="javascript:getFirstStatDay()"><img src="../static/image/search.png" alt="自动统计"></img></a>
						</td>
					</tr>
					<tr>
						<td>等级:</td>
						<td><input class="easyui-numberspinner" type="text"
								   name="level" style="width:155px;" data-options="required:true,min:0,precision:0"/>
						</td>
						<td>积分奖励:</td>
						<td><input class="easyui-numberspinner"
								   name="rewardPoint" style="width:155px;"
								   data-options="precision:0">
						</td>
					</tr>
					<tr>
						<td>业务KEY:</td>
						<td><input class="easyui-textbox" type="text"
								   name="bussKey" style="width:155px;" />
						</td>
						</td>
					</tr>
					<tr>
						<td>用户日历标题:</td>
						<td colspan="3"><input class="easyui-validatebox" type="text"
											   name="defaultCalendarTitle" style="width:450px;" />
						</td>
					</tr>
					<tr>
						<td>备注:</td>
						<td colspan=3><input class="easyui-textbox" type="text" data-options="multiline:true"
											 type="textarea" name="remark" style="width:450px;height:40px"></input></td>
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
			<div region="center" border="true" title="配置选项值列表" split="true"
				 style="background:#E2E377;overflow-y: hidden;height: 200px">
				<div id="gridStatValueConfig" fit="true"></div>
			</div>
		</div>
	</div>

	<div id="eidt-window-pcv" title="配置选项值" class="easyui-window"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div>
			<form id="ff-stat-value-config-value" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>名称:</td>
						<td colspan="3"><input class="easyui-validatebox" type="text"
											   name="name" style="width:450px;" />
						</td>
					</tr>
					<tr>
						<td>值类型:</td>
						<td><select class="easyui-combobox" name="valueClass"
									style="width:158px;" >
							<option value="LONG">LONG</option>
							<option value="INTEGER">INTEGER</option>
							<option value="SHORT">SHORT</option>
							<option value="STRING">STRING</option>
						</select></td>
						<td>排序号:</td>
						<td><input class="easyui-numberspinner"
								   name="orderIndex" style="width:150px;" value="1"
								   data-options="required:true,min:0,precision:0"></td>
					</tr>
					<tr>
						<td>SQL语句:</td>
						<td colspan=3>
							<input class="easyui-textbox" type="text" data-options="multiline:true"
								   name="sqlContent" style="width:450px;height:80px" />
						</td>
					</tr>
					<tr>
						<td>级联类型:</td>
						<td><select class="easyui-combobox" name="casCadeType"
									style="width:158px;" >
							<option value="NOT_CASCADE">不级联</option>
							<option value="CASCADE_NEXT">级联下一层</option>
							<option value="BE_CASCADED">级联上一层</option>
						</select></td>
						<td>用户字段:</td>
						<td><input class="easyui-textbox" type="text"
								   name="userField" style="width:155px;" />
						</td>
					</tr>
					<tr>
						<td>提示信息:</td>
						<td colspan="3"><input class="easyui-validatebox" type="text"
											   name="promptMsg" style="width:450px;" />
						</td>
					</tr>
				</table>
				<input type="hidden" id='statValueConfigFid' name="fid"/>
				<input type="hidden" name='createdTime' />
				<input type="hidden" name='type' id="type" value="PLAN" />
				<input type="hidden" name='id' id="statValueConfigId" />

				<div class="submitForm">
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-ok" onclick="showPlanCommend()">自动推荐</a><br><br>
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-ok" onclick="saveDataStatValueConfig()">保存</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
						icon="icon-cancel" onclick="closeWindowStatValueConfig()">关闭</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-info" onclick="showEditLogCompare('StatValueConfig','statValueConfigId')">修改记录</a>
				</div>
			</form>
		</div>
		<form id="search-window-pcv" method="post">
			<input type="hidden" name='aa' />

		</form>
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
</div>
<%@ include file="../log/operationLogCompareInclude.jsp"%>

</body>
</html>
