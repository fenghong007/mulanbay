<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>锻炼记录管理</title>
<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/mulanbay/sportExercise.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/planCommon.js"></script>
	<script type="text/javascript" src="/static/js/echarts/echarts-all-3.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/mulanbaychart.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/userPlanForBuss.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/bodyAbnormalRecordAnalyse.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/operationLogCompare.js"></script>

</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="center" border="true" title="锻炼记录信息"
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
							<td>运动类型:</td>
							<td><input id="sportTypeList" class="easyui-combotree" style="width:100px"
								data-options="editable:true" name="sportTypeId"></td>
							</td>
							<td>起始日期:</td>
							<td><input class="easyui-datebox" name="startDate"
									   data-options="showSeconds:false" style="width:100px">--
								<input class="easyui-datebox" name="endDate"
									   data-options="showSeconds:false" style="width:100px"></td>
							</td>
							<td>筛选最佳:</td>
							<td>
								<input name="containBest" type="radio" class="easyui-validatebox" required="true" value="true" style="width:15px"  >是
								<input name="containBest" type="radio" class="easyui-validatebox" required="true" value="false" style="width:15px" checked="checked">否
							</td>
							<td>排序方式:</td>
							<td><select class="easyui-combobox" name="sortField"
										style="width:80px;">
								<option value="EXERCISEDATE">锻炼日期</option>
								<option value="KILOMETRES">公里数</option>
								<option value="MINUTES">锻炼时间</option>
								<option value="SPEED">平均速度</option>
								<option value="MAXSPEED">最佳速度</option>
								<option value="PACE">平均配速</option>
								<option value="MAXPACE">最佳配速</option>
								<option value="MAXHEARTRATE">最大心率</option>
								<option value="AVERAGEHEART">平均心率</option>
							</select>
								<select class="easyui-combobox" name="sortType"
										style="width:70px;">
									<option value="desc">倒序</option>
									<option value="asc">顺序</option>
								</select></td>
							<td colspan="2" align="center">
								<a href="javascript:void(0)" class="easyui-linkbutton"
								   icon="icon-search" onclick="showAll()">查询</a> <a
									href="javascript:void(0)" class="easyui-linkbutton"
									icon="icon-cancel" onclick="resetAndInitSearchWindowYear()">重置</a>
							</td>
						</tr>
					</table>

				</form>
			</div>
			<div region="center" border="true" title="锻炼记录列表" split="true"
				style="background:#E2E377;overflow-y: hidden">
				<div id="grid" fit="true"></div>
			</div>
		</div>
	</div>

	<div id="eidt-window" title="锻炼信息" class="easyui-window"
		data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div class="yhdiv">
			<form id="ff" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>运动类型:</td>
						<td><input id="sportTypeList2" class="easyui-combotree"
							name="sportTypeId" style="width:160px;"
							data-options="required:true" missingMessage="运动类型"></td>
						<td>锻炼日期:</td>
						<td><input class="easyui-datetimebox" name="exerciseDate"
								   data-options="showSeconds:true" style="width:160px"></td>
					</tr>
					<tr>
						<td>公里数:</td>
						<td><input class="easyui-numberbox"
								   name="kilometres" style="width:160px;"
								   data-options="min:0,precision:2">公里</td>
						<td>锻炼时长:</td>
						<td><input class="easyui-numberspinner"
								   name="minutes" style="width:160px;"
								   data-options="min:0,precision:0">分钟</td>
					</tr>
					<tr>
						<td>平均配速:</td>
						<td><input class="easyui-numberbox"
								   name="pace" style="width:160px;"
								   data-options="min:0,precision:2">分钟/公里</td>
						<td>最佳配速:</td>
						<td><input class="easyui-numberbox"
								   name="maxPace" style="width:160px;"
								   data-options="min:0,precision:2">分钟/公里</td>
					</tr>
					<tr>
						<td>平均速度:</td>
						<td><input class="easyui-numberbox"
								   name="speed" style="width:160px;"
								   data-options="min:0,precision:2">公里/小时</td>
						<td>最佳速度:</td>
						<td><input class="easyui-numberbox"
								   name="maxSpeed" style="width:160px;"
								   data-options="min:0,precision:2">公里/小时</td>
					</tr>
					<tr>
						<td>最大心率:</td>
						<td><input class="easyui-numberspinner"
								   name="maxHeartRate" style="width:160px;"
								   data-options="min:0,precision:0">次</td>
						<td>平均心率:</td>
						<td><input class="easyui-numberspinner"
								   name="averageHeartRate" style="width:160px;"
								   data-options="min:0,precision:0">次</td>
					</tr>
					<tr>
						<td>备注:</td>
						<td colspan=3><input class="easyui-validatebox"
							type="textarea" name="remark" style="width:99%;"></input></td>
					</tr>
				</table>
				<input type="hidden" name='id' id="id"/>
				<input type="hidden" name='createdTime' />
				<input type="hidden" name='mileageBest' />
				<input type="hidden" name='fastBest' />
				<div class="submitForm">
					<a href="javascript:void(0)" class="easyui-linkbutton"
						icon="icon-ok" onclick="saveData()">保存</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
						icon="icon-cancel" onclick="closeWindow()">取消</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-info" onclick="showEditLogCompare('SportExercise','id')">修改记录</a>
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
						<td>总时长</td>
						<td><input name="totalMinutes" id="totalMinutes" class="easyui-textbox"
								   style="width:155px;color:red">分钟</td>
					</tr>
					<tr>
						<td>总公里数</td>
						<td><input name="totalKilometres" id="totalKilometres"
								   class="easyui-textbox" style="width:155px;color:red">km</td>
					</tr>
				</table>
				<div class="submitForm">
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-cancel" onclick="closeWindow('stat-window')">关闭</a>
				</div>
			</form>
		</div>
	</div>

	<div id="multiStat-window" title="最佳记录统计（基于当前查询条件）" class="easyui-window"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div class="yhdiv">
			<form id="multiStat-form" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td align=center>类别:</td>
						<td align=center>最大值</td>
						<td align=center>最小值</td>
						<td align=center>平均值</td>
					</tr>
					<tr>
						<td align="center">
							里程(公里)
						</td>
						<td><input class="easyui-numberbox" data-options="min:0,precision:2"
								   name="maxKilometres" style="width:100px;color:red">
							<a href="javascript:showMultiSportExerciseInfo('KILOMETRES','MAX')"><img src="../static/image/info.png"></img></a>
						</td>
						<td><input class="easyui-numberbox" data-options="min:0,precision:2"
								   name="minKilometres" style="width:100px;color:red">
							<a href="javascript:showMultiSportExerciseInfo('KILOMETRES','MIN')"><img src="../static/image/info.png"></img></a>
						</td>
						<td><input class="easyui-numberbox" data-options="min:0,precision:2"
								   name="avgKilometres" style="width:100px;color:red">
						</td>
					</tr>
					<tr>
						<td align="center">
							锻炼时间(分钟)
						</td>
						<td>
							<input class="easyui-numberbox"
								   name="maxMinutes" style="width:100px;color:red">
							<a href="javascript:showMultiSportExerciseInfo('MINUTES','MAX')"><img src="../static/image/info.png"></img></a>
						</td>
						<td>
							<input class="easyui-numberbox"
								   name="minMinutes" style="width:100px;color:red">
							<a href="javascript:showMultiSportExerciseInfo('MINUTES','MIN')"><img src="../static/image/info.png"></img></a>
						</td>
						<td>
							<input class="easyui-numberbox"
								   name="avgMinutes" style="width:100px;color:red">
						</td>
					</tr>
					<tr>
						<td align="center">
							平均速度(公里/小时)
						</td>
						<td>
							<input class="easyui-numberbox" data-options="min:0,precision:2"
								   name="maxSpeed" style="width:100px;color:red">
							<a href="javascript:showMultiSportExerciseInfo('SPEED','MAX')"><img src="../static/image/info.png"></img></a>
						</td>
						<td>
							<input class="easyui-numberbox" data-options="min:0,precision:2"
								   name="minSpeed" style="width:100px;color:red">
							<a href="javascript:showMultiSportExerciseInfo('SPEED','MIN')"><img src="../static/image/info.png"></img></a>
						</td>
						<td>
							<input class="easyui-numberbox" data-options="min:0,precision:2"
								   name="avgSpeed" style="width:100px;color:red">
						</td>
					</tr>
					<tr>
						<td align="center">
							最佳速度(公里/小时)
						</td>
						<td>
							<input class="easyui-numberbox" data-options="min:0,precision:2"
								   name="maxMaxSpeed" style="width:100px;color:red">
							<a href="javascript:showMultiSportExerciseInfo('MAXSPEED','MAX')"><img src="../static/image/info.png"></img></a>
						</td>
						<td>
							<input class="easyui-numberbox" data-options="min:0,precision:2"
								   name="minMaxSpeed" style="width:100px;color:red">
							<a href="javascript:showMultiSportExerciseInfo('MAXSPEED','MIN')"><img src="../static/image/info.png"></img></a>
						</td>
						<td>
							<input class="easyui-numberbox" data-options="min:0,precision:2"
								   name="avgMaxSpeed" style="width:100px;color:red">
						</td>
					</tr>
					<tr>
						<td align="center">
							平均配速(分钟/公里)
						</td>
						<td>
							<input class="easyui-numberbox" data-options="min:0,precision:2"
								   name="maxPace" style="width:100px;color:red">
							<a href="javascript:showMultiSportExerciseInfo('PACE','MAX')"><img src="../static/image/info.png"></img></a>
						</td>
						<td>
							<input class="easyui-numberbox" data-options="min:0,precision:2"
								   name="minPace" style="width:100px;color:red">
							<a href="javascript:showMultiSportExerciseInfo('PACE','MIN')"><img src="../static/image/info.png"></img></a>
						</td>
						<td>
							<input class="easyui-numberbox" data-options="min:0,precision:2"
								   name="avgPace" style="width:100px;color:red">
						</td>
					</tr>
					<tr>
						<td align="center">
							最佳配速(分钟/公里)
						</td>
						<td>
							<input class="easyui-numberbox" data-options="min:0,precision:2"
								   name="maxMaxPace" style="width:100px;color:red">
							<a href="javascript:showMultiSportExerciseInfo('MAXPACE','MAX')"><img src="../static/image/info.png"></img></a>
						</td>
						<td>
							<input class="easyui-numberbox" data-options="min:0,precision:2"
								   name="minMaxPace" style="width:100px;color:red">
							<a href="javascript:showMultiSportExerciseInfo('MAXPACE','MIN')"><img src="../static/image/info.png"></img></a>
						</td>
						<td>
							<input id="maxPace" class="easyui-numberbox" data-options="min:0,precision:2"
								   name="avgMaxPace" style="width:100px;color:red">
						</td>
					</tr>
					<tr>
						<td align="center">
							最高心率(次/分钟)
						</td>
						<td>
							<input class="easyui-numberbox"
								   name="maxMaxHeartRate" style="width:100px;color:red">
							<a href="javascript:showMultiSportExerciseInfo('MAXHEARTRATE','MAX')"><img src="../static/image/info.png"></img></a>
						</td>
						<td>
							<input class="easyui-numberbox"
								   name="minMaxHeartRate" style="width:100px;color:red">
							<a href="javascript:showMultiSportExerciseInfo('MAXHEARTRATE','MIN')"><img src="../static/image/info.png"></img></a>
						</td>
						<td>
							<input class="easyui-numberbox"
								   name="avgMaxHeartRate" style="width:100px;color:red">
						</td>
					</tr>
					<tr>
						<td align="center">
							平均心率(次/分钟)
						</td>
						<td>
							<input class="easyui-numberbox"
								   name="maxAverageHeartRate" style="width:100px;color:red">
							<a href="javascript:showMultiSportExerciseInfo('AVERAGEHEART','MAX')"><img src="../static/image/info.png"></img></a>
						</td>
						<td>
							<input class="easyui-numberbox"
								   name="minAverageHeartRate" style="width:100px;color:red">
							<a href="javascript:showMultiSportExerciseInfo('AVERAGEHEART','MIN')"><img src="../static/image/info.png"></img></a>
						</td>
						<td>
							<input class="easyui-numberbox"
								   name="avgAverageHeartRate" style="width:100px;color:red">
						</td>
					</tr>
				</table>
				<input type="hidden" id="sportTypeId" name='sportTypeId' />
				<div class="submitForm">
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-cancel" onclick="closeWindow('multiStat-window')">关闭</a>
				</div>
			</form>
		</div>
	</div>

	<div id="milestones-window" title="里程碑信息" class="easyui-window"
		 style="width:650px;height:300px;"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<input type="hidden" id="milestoneSportTypeId" name='milestoneSportTypeId' />
		<div id="milestonesGrid" fit="true"></div>
	</div>

	<div id="next-achieve-milestones-window" title="运动里程碑信息" class="easyui-window"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div class="yhdiv">
			<form id="next-achieve-milestones-form" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>运动类型:</td>
						<td><input id="sportTypeList3" class="easyui-combotree"
								   name="sportType.id" style="width:160px;"
								   data-options="required:true" missingMessage="运动类型"></td>
					</tr>
					<tr>
						<td>名称:</td>
						<td><input class="easyui-validatebox" type="text"
								   name="name" style="width:158px;" />
						</td>
					</tr>
					<tr>
						<td>别名:</td>
						<td><input class="easyui-validatebox" type="text"
								   name="alais" style="width:158px;" />
						</td>
					</tr>
					<tr>
						<td>公里数:</td>
						<td><input class="easyui-numberbox"
								   name="kilometres" style="width:158px;"
								   data-options="min:0,precision:2,required:true">公里</td>
					</tr>
					<tr>
						<td>锻炼时长:</td>
						<td><input class="easyui-numberspinner"
								   name="minutes" style="width:158px;"
								   data-options="min:0,precision:0,required:true">分钟</td>
					</tr>
					<tr>
						<td>排序号:</td>
						<td><input class="easyui-numberspinner" type="text"
								   name="orderIndex" style="width:158px;" data-options="required:true,min:0,precision:0"/>
						</td>
					</tr>
					<tr>
						<td>备注:</td>
						<td><input class="easyui-textbox" type="text" data-options="multiline:true"
								   type="textarea" name="remark" style="width:158px;height:60px"></input></td>
					</tr>
				</table>
				<input type="hidden" name='id' />
				<div class="submitForm">
					<a href="javascript:void(0)" class="easyui-linkbutton"
						icon="icon-cancel" onclick="closeWindow('next-achieve-milestones-window')">关闭</a>
				</div>
			</form>
		</div>
	</div>
	<%@ include file="../plan/userPlanInclude.jsp"%>
	<%@ include file="../health/bodyAbnormalRecordAnalyse.jsp"%>
	<%@ include file="../log/operationLogCompareInclude.jsp"%>

</body>
</html>
