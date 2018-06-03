<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>音乐练习记录管理</title>
<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/easyui/datagrid-detailview.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/musicPractice.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/musicPracticeTunePt.js"></script>
	<script type="text/javascript" src="/static/js/echarts/echarts-all-3.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/mulanbaychart.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/planCommon.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/userPlanForBuss.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/operationLogCompare.js"></script>

</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="center" border="true" title="音乐练习记录信息"
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
							<td>乐器:</td>
							<td><input id="musicInstrumentList" class="easyui-combotree"
									   data-options="editable:true" name="musicInstrumentId"></td>
							</td>
							<td>练习时长:</td>
							<td><input id="minMinutes" class="easyui-numberbox"
									   name="minMinutes" style="width:100px;"
									   data-options="min:0,precision:0">--
								<input id="maxMinutes" class="easyui-numberbox"
									   name="maxMinutes" style="width:100px;"
									   data-options="min:0,precision:0">
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
			<div region="center" border="true" title="口琴练习记录列表" split="true"
				style="background:#E2E377;overflow-y: hidden">
				<div id="grid" fit="true"></div>
			</div>
		</div>
	</div>

	<div id="eidt-window" title="音乐练习信息" class="easyui-window"
		data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div class="yhdiv">
			<form id="ff" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>练习开始时间:</td>
						<td><input class="easyui-datetimebox" name="practiceStartTime" id="practiceStartTime"
								   data-options="showSeconds:true" style="width:155px"></td>
						<td>练习结束时间:</td>
						<td><input class="easyui-datetimebox" name="practiceEndTime" id="practiceEndTime"
								   data-options="showSeconds:true" style="width:155px"></td>
					</tr>
					<tr>
						<td>练习时长:</td>
						<td><input id="minutes" class="easyui-numberspinner"
								   name="minutes" style="width:155px;"
								   data-options="required:true,min:0,precision:0"></td>
						<td>乐器:</td>
						<td><input id="musicInstrumentList2" class="easyui-combotree"
								   data-options="editable:true" style="width:155px" name="musicInstrumentId"></td>
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
					<div region="center" border="true" title="口琴练习曲子列表" split="true"
						 style="background:#E2E377;overflow-y: hidden;height: 200px">
						<div id="gridTune" fit="true"></div>
					</div>
				<div class="submitForm">
					<a href="javascript:void(0)" class="easyui-linkbutton"
						icon="icon-ok" onclick="saveData()">保存</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
						icon="icon-cancel" onclick="closeWindow()">关闭</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-info" onclick="showEditLogCompare('MusicPractice','id')">修改记录</a>
				</div>
			</form>
		</div>
	</div>

	<div id="eidt-window-tune" title="音乐练习曲子信息" class="easyui-window"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div class="yhdiv">
			<form id="ff-tune" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>练习类型:</td>
						<td>
							<select class="easyui-combobox" name="tuneType" id="tuneTypeList"
									style="width:155px;">
								<option value="TUNE">曲子</option>
								<option value="TECH">技术</option>
							</select>
						</td>
					</tr>
					<tr>
						<td>曲子/技术名称:</td>
						<td>
							<input id="tuneList" class="easyui-combobox" style="width: 155px;"
								   data-options="editable:true" name="tune" />
					</tr>
					<tr>
						<td>练习次数/分钟:</td>
						<td><input id="times" class="easyui-numberspinner"
								   name="times" style="width:155px;"
								   data-options="required:true,min:0,precision:0"></td>
					</tr>
					<tr>
						<td>水平:</td>
						<td>
							<select class="easyui-combobox" name="level" id="level"
									style="width:155px;">
								<option value="PRACTICE">练习</option>
								<option value="SKILLED">熟练</option>
								<option value="RECORD">录音</option>
								<option value="PERFORMANCE">演奏</option>
							</select>
						</td>
					</tr>
					<tr>
						<td>备注:</td>
						<td><input class="easyui-validatebox"
											 type="textarea" name="remark" style="width:99%;"></input></td>
					</tr>
				</table>
				<input type="hidden" id='musicPracticeId' name="musicPracticeId"/>
				<input type="hidden" id='musicPracticeTuneId' name="id"/>
				<div class="submitForm">
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-ok" onclick="saveDataTune()">保存</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
						icon="icon-cancel" onclick="closeWindowTune()">关闭</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-info" onclick="showEditLogCompare('MusicPracticeTune','musicPracticeTuneId')">修改记录</a>
				</div>
			</form>
		</div>
	</div>
	<form id="search-window-tune" method="post">
	</form>

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
	<%@ include file="../plan/userPlanInclude.jsp"%>
	<%@ include file="../log/operationLogCompareInclude.jsp"%>

</body>
<script type="text/javascript">
    // 饼图
    function createMyPieData(data){
        createDoublePieData(data);
    }

</script>
</html>
