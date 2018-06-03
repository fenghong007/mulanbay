<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>人生记录管理</title>
<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/echarts/echarts-all-3.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/mulanbaychart.js"></script>
	<script type="text/javascript" src="/static/js/echarts/china.js"></script>
	<script type="text/javascript" src="/static/js/echarts/world.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/lifeExperience.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/lifeExperienceDetailPt.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/lifeExperienceConsumePt.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/transferMapStat.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/planCommon.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/userPlanForBuss.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/operationLogCompare.js"></script>


</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="center" border="true" title="人生经历信息"
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
							<td>起始日期:</td>
							<td><input class="easyui-datebox" name="startDate"
									   data-options="showSeconds:false" style="width:110px">--
								<input class="easyui-datebox" name="endDate"
									   data-options="showSeconds:false" style="width:110px"></td>
							</td>
							<td>类型:</td>
							<td><select class="easyui-combobox" name="types"
										style="width:154px;">
								<option value="">全部</option>
								<option value="0">生活</option>
								<option value="1">工作</option>
								<option value="2">旅行</option>
								<option value="3">读书</option>
							</select></td>
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
			<div region="center" border="true" title="人生经历列表" split="true"
				style="background:#E2E377;overflow-y: hidden">
				<div id="grid" fit="true"></div>
			</div>
		</div>
	</div>

	<div id="eidt-window" title="人生经历信息" class="easyui-window"
		data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div class="yhdiv">
			<form id="ff" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>名称:</td>
						<td colspan=3><input class="easyui-validatebox"
											 type="textarea" name="name" style="width:370px;"></input></td>
					</tr>
					<tr>
						<td>类型:</td>
						<td><select class="easyui-combobox" name="type"
									style="width:125px;">
							<option value="LIVE">生活</option>
							<option value="WORK">工作</option>
							<option value="TRAVEL">旅行</option>
							<option value="STUDY">读书</option>
						</select></td>
					</tr>
					<tr>
						<td>开始日期:</td>
						<td><input class="easyui-datebox" name="startDate"
								   data-options="showSeconds:false" style="width:125px">
						</td>
						<td>结束日期:</td>
						<td><input class="easyui-datebox" name="endDate"
								   data-options="showSeconds:false" style="width:125px">
						</td>
					</tr>
					<tr>
						<td>天数:</td>
						<td><input class="easyui-spinner"
								   name="days" style="width:110px;"
								   data-options="required:true,min:1">
							<a href="javascript:revise(false,true)"><img src="../static/image/reload.png"></img></a>
						</td>
						<td>花费:</td>
						<td><input class="easyui-numberbox"
								   name="cost" style="width:110px;" disabled="true"
								   data-options="required:true,min:0,precision:2">
							<a href="javascript:revise(true,false)"><img src="../static/image/reload.png"></img></a>
						</td>
					</tr>
					<tr>
						<td>备注:</td>
						<td colspan=3><input class="easyui-textbox" data-options="multiline:true"
							type="textarea" name="remark" style="width:370px;height:80px"></input></td>
					</tr>
				</table>
				<input type="hidden" name='id' id="id"/>
				<input type="hidden" name='cost' />
				<input type="hidden" name='createdTime' />
				<div class="submitForm">
					<a href="javascript:void(0)" class="easyui-linkbutton"
						icon="icon-ok" onclick="saveData()">保存</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
						icon="icon-cancel" onclick="closeWindow()">取消</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-info" onclick="showLifeExperienceDetailGrid()">经历详情</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-info" onclick="showEditLogCompare('LifeExperience','id')">修改记录</a>
				</div>
			</form>
		</div>
	</div>

	<div id="stat-window" title="旅行线路" class="easyui-window"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div class="yhdiv">
			<table>
				<tr>
					<td>
						<div region="center" border="true" title="结果" split="true">
							<div id="container"
								 style="min-width: 800px; height: 500px; margin: 0 auto"></div>
						</div>
					</td>
				</tr>
			</table>

		</div>
	</div>

	<div id="lifeExperienceDetail-window" title="经历详情" class="easyui-window"
		 style="width:880px;height:410px;"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div region="north" border="true" title="查询条件" split="true"
			 style="overflow:auto;height:40px;" data-options="selected:false"
			 icon="icon-edit" class="yhbackground">
			<form id="lifeExperienceDetail-form" method="post">
				<table cellpadding="5" class="tableForm" style="width: 100%;">
					<tr>
						<td>起始日期:</td>
						<td><input class="easyui-datebox" name="startDate"
								   data-options="showSeconds:false" style="width:100px">--
							<input class="easyui-datebox" name="endDate"
								   data-options="showSeconds:false" style="width:100px"></td>
						</td>
						<td align="center" colspan="2">
							<a href="javascript:void(0)" class="easyui-linkbutton"
							   icon="icon-search" onclick="showAllLifeExperienceDetail()">查询</a>
							<a href="javascript:void(0)" class="easyui-linkbutton"
							   icon="icon-cancel" onclick="resetSearchWindowById('lifeExperienceDetail-form')">重置</a>
						</td>
					</tr>
				</table>
				<input type="hidden" name='lifeExperienceId' id="searchLifeExperienceId" value="0" />
			</form>
		</div>
		<div region="center" border="true" title="列表" split="true"
			 style="background:#E2E377;overflow-y: hidden;width:auto;height:335px;">
			<div id="lifeExperienceDetailGrid" fit="true"></div>
		</div>
	</div>

	<div id="eidt-lifeExperienceDetail-window" title="人生经历明细详情" class="easyui-window"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div class="yhdiv">
			<form id="edit-lifeExperienceDetail-form" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>所在国家:</td>
						<td><input class="easyui-validatebox" type="text"
								   name="country" style="width:120px;" />
						</td>
						<td>所在省份:</td>
						<td><input id="provinceList" class="easyui-combotree" style="width:120px"
								   data-options="editable:true" name="provinceId">
						</td>
					</tr>
					<tr>
						<td>所在城市:</td>
						<td>
							<input id="cityList" class="easyui-combotree" style="width:120px"
								   data-options="editable:true" name="cityId">
						</td>
						<td>县(地区):</td>
						<td>
							<input id="districtList" class="easyui-combotree" style="width:120px"
								   data-options="editable:true" name="districtId">

					</tr>
					<tr>
						<td>日期:</td>
						<td><input class="easyui-datebox" name="occurDate"
								   data-options="showSeconds:false" style="width:125px">
						</td>
						<td>是否加入地图绘制:</td>
						<td>
							<input name="mapStat" type="radio" class="easyui-validatebox" checked="checked" required="true" value="true" style="width:30px">是
							<input name="mapStat" type="radio" class="easyui-validatebox" required="true" value="false" style="width:30px">否
						</td>
					</tr>
					<tr>
						<td>出发城市:</td>
						<td><input class="easyui-validatebox" type="text"
								   name="startCity" style="width:120px;" />
						</td>
						<td>抵达城市:</td>
						<td><input class="easyui-validatebox" type="text"
								   name="arriveCity" style="width:120px;" />
						</td>
					</tr>
					<tr>
						<td>备注:</td>
						<td colspan=3><input class="easyui-textbox" data-options="multiline:true"
											 type="textarea" name="remark" style="width:370px;height:80px"></input></td>
					</tr>
				</table>
				<input type="hidden" name='id' id="lifeExperienceDetailId"/>
				<input type="hidden" name='lifeExperienceId' id="detailOfLifeExperienceId"/>
				<input type="hidden" name='createdTime' />
				<input type="hidden" name='cost' />
				<div region="center" border="true" title="消费记录列表" split="true"
					 style="background:#E2E377;overflow-y: hidden;height: 200px">
					<div id="lifeExperienceConsumeGrid" fit="true"></div>
				</div>
				<div class="submitForm">
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-ok" onclick="saveLifeExperienceDetail()">保存</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-cancel" onclick="closeWindowLifeExperienceDetail()">取消</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-info" onclick="showEditLogCompare('LifeExperienceDetail','id')">修改记录</a>
				</div>
			</form>
		</div>
	</div>

	<div id="eidt-lifeExperienceConsume-window" title="消费记录" class="easyui-window"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div class="yhdiv">
			<form id="search-lifeExperienceConsume-form" method="post">
				<input type="hidden" name='lifeExperienceDetailId' id="searchLifeExperienceDetailId" value="0" />
			</form>
			<form id="edit-LifeExperienceConsume-form" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>关联总的消费记录:</td>
						<td colspan="3">
							<input id="buyRecordList" class="easyui-combotree" style="width: 220px;"
								   data-options="editable:true" name="buyRecordId" />
							<select id="roundDaysList" class="easyui-combobox" name="roundDays"
									style="width:80px;">
								<option value="0">当天</option>
								<option value="3">三天内</option>
								<option value="7">一周内</option>
								<option value="30">一个月内</option>
								<option value="365">一年内</option>
							</select>
					</tr>
					<tr>
						<td>名称:</td>
						<td colspan="3"><input class="easyui-validatebox"
								   type="textarea" name="name" style="width:99%;"></input></td>
					</tr>
					<tr>
						<td>消费类型:</td>
						<td>
							<input id="consumeTypeList" class="easyui-combotree" style="width: 100px;"
								   data-options="editable:true" name="consumeTypeId" />
						<td>消费:</td>
						<td><input class="easyui-numberspinner"
								   name="cost" style="width:100px;"
								   data-options="required:true,min:0,precision:2"></td>
					</tr>
					<tr>
						<td>是否需要加入统计:</td>
						<td>
							<input name="statable" type="radio" class="easyui-validatebox" checked="checked" required="true" value="true" style="width:30px">是
							<input name="statable" type="radio" class="easyui-validatebox" required="true" value="false" style="width:30px">否
						</td>
					</tr>
					<tr>
						<td>备注:</td>
						<td colspan="3"><input class="easyui-validatebox"
								   type="textarea" name="remark" style="width:99%;"></input></td>
					</tr>
				</table>
				<input type="hidden" name='lifeExperienceDetailId' id="consumeOfLifeExperienceDetailId"/>
				<input type="hidden" name="id" id="lifeExperienceConsumeId"/>
				<input type="hidden" name='createdTime' />
				<div class="submitForm">
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-ok" onclick="saveLifeExperienceConsume()">保存</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-cancel" onclick="closeWindowLifeExperienceConsume()">取消</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-info" onclick="showEditLogCompare('LifeExperienceConsume','id')">修改记录</a>
				</div>
			</form>
		</div>
	</div>

	<%@ include file="../plan/userPlanInclude.jsp"%>
	<%@ include file="../log/operationLogCompareInclude.jsp"%>

</body>
</html>
