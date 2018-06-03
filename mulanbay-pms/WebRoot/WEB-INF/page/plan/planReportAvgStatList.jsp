<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>计划执行报告</title>
	<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/mulanbay/planCommon.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/planReportAvgStat.js"></script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="center" border="true" title="计划执行报告"
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
							<td>计划:</td>
							<td><input id="userPlanList" class="easyui-combotree" style="width:200px"
									   data-options="editable:true" name="userPlanId"></td>
							</td>
							<td>起始日期:</td>
							<td><input class="easyui-datebox" name="startDate"
									   data-options="showSeconds:false" style="width:100px">--
								<input class="easyui-datebox" name="endDate"
									   data-options="showSeconds:false" style="width:100px"></td>
							</td>
							<td>数据参考年份:</td>
							<td><input id="yearList" class="easyui-combotree" style="width:80px;"
									   data-options="required:true,editable:true" name="year">
							</td>
						</tr>
						<tr>
							<td>过滤全零数据:</td>
							<td>
								<input name="minValue" type="radio" class="easyui-validatebox" required="true" value="0" style="width:20px">是
								<input name="minValue" type="radio" class="easyui-validatebox" checked="checked" required="true" value="" style="width:20px">否
							</td>
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
			<div region="center" border="true" title="计划执行报告列表" split="true"
				 style="background:#E2E377;overflow-y: hidden">
				<div id="grid" fit="true"></div>
			</div>
		</div>
	</div>

	<div id="manual-stat-window" title="手动统计计划报告" class="easyui-window"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div class="yhdiv">
			<form id="manual-stat-form" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>计划:</td>
						<td><input id="planConfigList2" class="easyui-combotree" style="width:220px"
								   data-options="editable:true" name="planConfigId"></td>
						</td>
					</tr>
					<tr>
						<td>起始日期:</td>
						<td><input class="easyui-datebox" name="startDate"
								   data-options="showSeconds:false" style="width:100px">--
							<input class="easyui-datebox" name="endDate"
								   data-options="showSeconds:false" style="width:100px"></td>
						</td>
					</tr>
					<tr>
						<td>统计类型:</td>
						<td>
							<input name="statType" type="radio" class="easyui-validatebox" required="true" value="RE_STAT" style="width:30px">重新统计
							<input name="statType" type="radio" class="easyui-validatebox" checked="checked" required="true" value="STAT_MISS" style="width:30px">统计遗漏
						</td>
					</tr>
				</table>
				<div class="submitForm">
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-stat" onclick="manualStat()">统计数据</a> &nbsp;
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-stat" onclick="cleanSelect()">删除数据</a> &nbsp;
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-cancel" onclick="closeWindow('manual-stat-window')">关闭</a>
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
					<td>
						<form id="stat-form" method="post">
							<table cellpadding="5" class="tableForm">
								<tr>
									<td align=center>名称:</td>
									<td align=center>值</td>
								</tr>
								<tr>
									<td>总次数</td>
									<td><input id="totalcount" class="easyui-numberspinner"
											   name="totalcount" style="width:150px;color:red"></td>
								</tr>
								<tr>
									<td>总时长</td>
									<td><input name="totalHours" id="totalHours" class="easyui-textbox"
											   style="width:150px;color:red"> 小时</td>
								</tr>
								<tr>
									<td>平均每天</td>
									<td><input name="averageValue" id="averageValue" class="easyui-textbox"
											   style="width:150px;color:red"></td>
								</tr>
							</table>
							<div class="submitForm">
								<a href="javascript:void(0)" class="easyui-linkbutton"
								   icon="icon-cancel" onclick="closeWindow('stat-window')">关闭</a>
							</div>
						</form>
					</td>
				</tr>
			</table>

		</div>
	</div>

</body>
</html>
