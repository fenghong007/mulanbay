<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>加班记录管理</title>
<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/mulanbay/workOvertime.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/planCommon.js"></script>
	<script type="text/javascript" src="/static/js/echarts/echarts-all-3.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/mulanbaychart.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/userPlanForBuss.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/operationLogCompare.js"></script>

</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="center" border="true" title="加班记录信息"
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
							<td>所在公司:</td>
							<td><input id="companyList" class="easyui-combotree"
								data-options="editable:true" name="companyId"></td>
							<td>加班时长:</td>
							<td><input id="minHours" class="easyui-numberbox"
									   name="minHours" style="width:60px;"
									   data-options="min:0,precision:1">--
								<input id="maxHours" class="easyui-numberbox"
									   name="maxHours" style="width:60px;"
									   data-options="min:0,precision:1">
							</td>
							</td>
							<td>起始日期:</td>
							<td><input class="easyui-datebox" name="startDate"
									   data-options="showSeconds:false" style="width:110px">--
								<input class="easyui-datebox" name="endDate"
									   data-options="showSeconds:false" style="width:110px"></td>
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
			<div region="center" border="true" title="加班记录列表" split="true"
				style="background:#E2E377;overflow-y: hidden">
				<div id="grid" fit="true"></div>
			</div>
		</div>
	</div>

	<div id="eidt-window" title="加班信息" class="easyui-window"
		data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div class="yhdiv">
			<form id="ff" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>所在公司:</td>
						<td><input id="companyList2" class="easyui-combotree"
							name="companyId" style="width:160px;"
							data-options="required:true" missingMessage="所在部门"></td>
						<td>加班日期:</td>
						<td><input class="easyui-datebox" name="workDate"
								   data-options="showSeconds:false" style="width:155px"></td>
					</tr>
					<tr>
						<td>加班开始时间:</td>
						<td><input class="easyui-datetimebox" name="workStartTime" id="workStartTime"
								   data-options="showSeconds:true" style="width:155px"></td>
						<td>加班结束时间:</td>
						<td><input class="easyui-datetimebox" name="workEndTime" id="workEndTime"
								   data-options="showSeconds:true" style="width:155px"></td>
					</tr>
					<tr>
						<td>加班时长:</td>
						<td><input id="hours" class="easyui-numberbox"
								   name="hours" style="width:160px;"
								   data-options="required:true,min:0,precision:1"></td>
					</tr>
					<tr>
						<td>备注:</td>
						<td colspan=3><input class="easyui-validatebox"
							type="textarea" name="remark" style="width:99%;"></input></td>
					</tr>
				</table>
				<input type="hidden" name='id' id="id" />
				<input type="hidden" name='createdTime' />
				<div class="submitForm">
					<a href="javascript:void(0)" class="easyui-linkbutton"
						icon="icon-ok" onclick="saveData()">保存</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
						icon="icon-cancel" onclick="closeWindow()">取消</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-info" onclick="showEditLogCompare('WorkOvertime','id')">修改记录</a>
				</div>
			</form>
		</div>
	</div>

	<div id="stat-window" title="统计信息（基于当前查询条件）" class="easyui-window"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div class="yhdiv">
			<form id="stat-form" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td align=center>名称:</td>
						<td align=center>值</td>
					</tr>
					<tr>
						<td>总次数</td>
						<td><input id="totalcount" class="easyui-numberspinner"
								   name="totalcount" style="width:155px;color:red"></td>
					</tr>
					<tr>
						<td>总加班时长</td>
						<td><input name="totalHours" id="totalHours" class="easyui-textbox"
								   style="width:155px;color:red"> 小时</td>
					</tr>
					<tr>
						<td>平均每天加班</td>
						<td><input name="averageHours" id="averageHours"
								   class="easyui-textbox" style="width:155px;color:red"> 小时</td>
					</tr>
				</table>
				<div class="submitForm">
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-cancel" onclick="closeWindow('stat-window')">关闭</a>
				</div>
			</form>
		</div>
	</div>
	<%@ include file="../plan/userPlanInclude.jsp"%>
	<%@ include file="../log/operationLogCompareInclude.jsp"%>

</body>
</html>
