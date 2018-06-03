<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>梦想管理</title>
<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/mulanbay/dream.js"></script>
	<script type="text/javascript" src="/static/js/echarts/echarts-all-3.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/mulanbaychart.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/planCommon.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/userPlanForBuss.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/operationLogCompare.js"></script>

</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="center" border="true" title="梦想信息"
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
							<td>梦想名称:</td>
							<td><input class="easyui-validatebox" type="text"
									   name="name" style="width:320px;" /></td>
							<td>类型:</td>
							<td>
								<!--必须要带中挂号，否则带多个情况下，只传statuses，不传中挂号了-->
								<select class="easyui-combobox" name="statuses[]"
										multiple="true" style="width:160px;height:23px" data-options="value:['CREATED','STARTED','PAUSED']">
									<option value="CREATED">新创建</option>
									<option value="STARTED">进行中</option>
									<option value="FINISHED">已实现</option>
									<option value="PAUSED">暂停中</option>
									<option value="GIVEDUP">已放弃</option>
								</select>
							</td>
							<td>进度:</td>
							<td><input class="easyui-numberbox"
									   name="minRate" style="width:110px;"
									   data-options="min:0,precision:0">
								--
								<input class="easyui-numberbox"
									   name="maxRate" style="width:110px;"
									   data-options="min:0,precision:0">
							</td>
						</tr>
						<tr>
							<td>起始日期:</td>
							<td>
								<select class="easyui-combobox" name="dateQueryType"
										style="width:100px;">
									<option value="proposedDate">期望实现日期</option>
									<option value="finishedDate">完成日期</option>
									<option value="deadline">最晚截止日期</option>
									<option value="createdTime">创建日期</option>
								</select>
								<input class="easyui-datebox" name="startDate"
									   data-options="showSeconds:false" style="width:100px">--
								<input class="easyui-datebox" name="endDate" value="${endDate}"
									   data-options="showSeconds:false" style="width:100px">
							</td>
							<td>排序方式:</td>
							<td><select class="easyui-combobox" name="sortField"
										style="width:90px;">
								<option value="proposedDate">期望实现日期</option>
								<option value="createdTime">创建时间</option>
								<option value="status">状态</option>
								<option value="difficulty">困难程度</option>
								<option value="importantLevel">重要程度</option>
								<option value="rate">进度</option>
								<option value="finishedDate">实现日期</option>
								<option value="deadline">最晚截止日期</option>
							</select>
								<select class="easyui-combobox" name="sortType"
										style="width:65px;">
									<option value="asc">顺序</option>
									<option value="desc">倒序</option>
								</select></td>
							<td colspan="2" align="center">
								<a href="javascript:void(0)" class="easyui-linkbutton"
								   icon="icon-search" onclick="showAll()">查询</a> <a
									href="javascript:void(0)" class="easyui-linkbutton"
									icon="icon-cancel" onclick="resetSearchWindow()">重置</a>
							</td>

						</tr>
					</table>

				</form>
			</div>
			<div region="center" border="true" title="梦想列表" split="true"
				style="background:#E2E377;overflow-y: hidden">
				<div id="grid" fit="true"></div>
			</div>
		</div>
	</div>

	<div id="eidt-window" title="梦想信息" class="easyui-window"
		data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div class="yhdiv">
			<form id="ff" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>梦想名称:</td>
						<td colspan=3><input class="easyui-validatebox"
											 type="textarea" name="name" style="width:99%;"></input></td>
					</tr>
					<tr>
						<td>最小资金:</td>
						<td><input class="easyui-spinner"
								   name="minMoney" style="width:155px;"
								   data-options="min:0,precision:0">(元)</td>
						<td>最大资金:</td>
						<td><input class="easyui-spinner"
								   name="maxMoney" style="width:155px;"
								   data-options="min:0,precision:0">(元)</td>
					</tr>
					<tr>
						<td>困难程度:</td>
						<td><input class="easyui-spinner"
								   name="difficulty" style="width:155px;"
								   data-options="required:true,min:0,max:10,precision:0">(0-10)</td>
						<td>重要程度:</td>
						<td><input class="easyui-spinner"
								   name="importantLevel" style="width:155px;"
								   data-options="required:true,min:0,max:5,precision:1">(0.0-5.0)</td>
					</tr>
					<tr>
						<td>预计完成天数:</td>
						<td><input class="easyui-spinner"
								   name="expectDays" style="width:155px;"
								   data-options="required:true,min:0,precision:0"></td>
						<td>进度(%):</td>
						<td><input class="easyui-spinner"
								   name="rate" style="width:155px;"
								   data-options="required:true,min:0,max:100,precision:0">(0-100)</td>
					</tr>
					<tr>
						<td>期望实现日期:</td>
						<td><input class="easyui-datebox" name="proposedDate"
								   data-options="required:true,showSeconds:false" style="width:155px"></td>
						<td>最晚截止日期:</td>
						<td><input class="easyui-datebox" name="deadline"
								   data-options="showSeconds:false" style="width:155px">
						</td>
					</tr>
					<tr>
						<td>实现日期:</td>
						<td><input class="easyui-datebox" name="finishedDate"
								   data-options="showSeconds:false" style="width:155px"></td>
						<td>状态:</td>
						<td><select class="easyui-combobox" name="status" id="statusList"
									style="width:158px;">
							<option value="CREATED">新创建</option>
							<option value="STARTED">进行中</option>
							<option value="FINISHED">已实现</option>
							<option value="PAUSED">暂停中</option>
							<option value="GIVEDUP">已放弃</option>
						</select></td>
					</tr>
					<tr>
						<td>关联计划:</td>
						<td>
							<input id="userPlanList" class="easyui-combotree" style="width:155px;"
								   data-options="editable:true" name="userPlan.Id">
						</td>
						<td>计划期望值:
							<a href="javascript:showPlanValueHelp()" title="提示"><img src="../static/image/help.png"></img></a>
						</td>
						<td><input class="easyui-spinner"
								   name="planValue" style="width:155px;"
								   data-options="min:0,precision:0"></td>
					</tr>
					<tr>
						<td>备注:</td>
						<td colspan=3>
							<input class="easyui-textbox" type="text" data-options="multiline:true"
								   name="remark" style="width:485px;height:60px" />
							</td>
					</tr>
					<tr>
						<td>期望时间修改历史:</td>
						<td colspan=3>
							<input class="easyui-textbox" type="text" data-options="multiline:true"
								   name="dateChangeHistory" style="width:485px;height:60px" />
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
					   icon="icon-info" onclick="showEditLogCompare('Dream','id')">修改记录</a>
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
	<%@ include file="../plan/userPlanInclude.jsp"%>
	<%@ include file="../log/operationLogCompareInclude.jsp"%>

</body>
</html>
