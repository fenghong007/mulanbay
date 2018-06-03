<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>出差管理</title>
	<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/mulanbay/userRewardPointRecord.js"></script>
	<script type="text/javascript" src="/static/js/echarts/echarts-all-3.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/mulanbaychart.js"></script>

</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="center" border="true" title="出差统计"
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
							<td>起始日期:</td>
							<td><input class="easyui-datebox" name="startDate"
									   data-options="showSeconds:false" style="width:110px">--
								<input class="easyui-datebox" name="endDate"
									   data-options="showSeconds:false" style="width:110px"></td>
							</td>
							<td>来源:</td>
							<td>
								<select class="easyui-combobox" name="rewardSource"
										style="width:80px;height:23px" >
									<option value="">全部</option>
									<option value="OPERATION">操作</option>
									<option value="NOTIFY">提醒</option>
									<option value="PLAN">计划</option>
								</select>
							</td>
							<td>主键值:</td>
							<td><input class="easyui-validatebox" type="text"
									   name="sourceId" style="width:80px;" /></td>
							<td>积分:</td>
							<td>
								<select class="easyui-combobox" name="rewardsCompareType"
										style="width:70px;" >
									<option value="GT">大于</option>
									<option value="GTE">大于等于</option>
									<option value="LT">小于</option>
									<option value="LTE">小于等于</option>
									<option value="EQ">等于</option>
								</select>
								<input class="easyui-numberspinner"
									   name="rewards" style="width:80px;"
									   data-options="min:0,precision:0">
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
			<div region="center" border="true" title="用户积分奖励列表" split="true"
				 style="background:#E2E377;overflow-y: hidden">
				<div id="grid" fit="true"></div>
			</div>
		</div>
	</div>

	<div id="userRewardPoint-value-stat-window" title="积分统计" class="easyui-window"
		 style="width:950px;height:550px;"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div region="north" border="true" title="查询条件" split="true"
			 style="overflow:auto;height:80px;" data-options="selected:false"
			 icon="icon-edit" class="yhbackground">
			<form id="userRewardPoint-value-stat-search-form" method="post">
				<table cellpadding="5" class="tableForm" style="width: 100%;">
					<tr>
						<td>起始日期:</td>
						<td>
							<input class="easyui-datebox" name="startDate"
								   data-options="showSeconds:false" style="width:100px">--
							<input class="easyui-datebox" name="endDate"
								   data-options="showSeconds:false" style="width:100px">
						</td>
						<td>统计分类:</td>
						<td><select class="easyui-combobox" name="dateGroupType"
									style="width:120px;">
							<option value="">默认</option>
							<option value="MONTH">月</option>
							<option value="DAY">天</option>
							<option value="DAYCALENDAR">天(日历)</option>
							<option value="WEEK">周</option>
							<option value="YEAR">年</option>
							<option value="YEARMONTH">年月</option>
						</select>
						</td>
					</tr>
					<tr>
						<td>来源:</td>
						<td>
							<select class="easyui-combobox" name="rewardSource"
									style="width:80px;height:23px" >
								<option value="OPERATION">操作</option>
								<option value="NOTIFY">提醒</option>
								<option value="PLAN">计划</option>
							</select>
						主键值:
						<input class="easyui-validatebox" type="text"
								   name="sourceId" style="width:80px;" />
						<td align="center" colspan="2">
							<a href="javascript:void(0)" class="easyui-linkbutton"
							   icon="icon-search" onclick="statUserRewardPointValue()">统计</a>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div region="center" border="true" title="统计" split="true"
			 style="overflow-y: hidden;width:auto;height:auto;">
			<div region="center" border="true" title="结果" split="true">
				<div id="container"
					 style="min-width: 500px; height: 450px; margin: 0 auto"></div>
			</div>
		</div>
	</div>

	<div id="userRewardPoint-source-stat-window" title="积分统计" class="easyui-window"
		 style="width:1100px;height:550px;"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div region="north" border="true" title="查询条件" split="true"
			 style="overflow:auto;height:80px;" data-options="selected:false"
			 icon="icon-edit" class="yhbackground">
			<form id="userRewardPoint-source-stat-search-form" method="post">
				<table cellpadding="5" class="tableForm" style="width: 100%;">
					<tr>
						<td>起始日期:</td>
						<td>
							<input class="easyui-datebox" name="startDate"
								   data-options="showSeconds:false" style="width:100px">--
							<input class="easyui-datebox" name="endDate"
								   data-options="showSeconds:false" style="width:100px">
						</td>
						<td>来源:</td>
						<td>
							<select class="easyui-combobox" name="rewardSource"
									style="width:80px;height:23px" >
								<option value="">全部</option>
								<option value="OPERATION">操作</option>
								<option value="NOTIFY">提醒</option>
								<option value="PLAN">计划</option>
							</select>
						</td>
						<td>排序:</td>
						<td>
							<select class="easyui-combobox" name="orderBy"
									style="width:80px;height:23px" >
								<option value="VALUE">积分</option>
								<option value="COUNTS">次数</option>
							</select>
						</td>
						<td align="center" colspan="2">
							<select class="easyui-combobox" name="chartType"
									style="width:70px;">
								<option value="BAR">柱状图</option>
								<option value="PIE">饼图</option>
							</select>
							<a href="javascript:void(0)" class="easyui-linkbutton"
							   icon="icon-search" onclick="statUserRewardPointSource()">统计</a>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div region="center" border="true" title="统计" split="true"
			 style="overflow-y: hidden;width:auto;height:auto;">
			<div region="center" border="true" title="结果" split="true">
				<div id="sourceStatContainer"
					 style="min-width: 500px; height: 450px; margin: 0 auto"></div>
			</div>
		</div>
	</div>

</body>
</html>
