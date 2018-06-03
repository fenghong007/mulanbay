<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>梦想管理</title>
<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/mulanbay/diet.js"></script>
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
							<td>食物类型:</td>
							<td>
								<select class="easyui-combobox" name="foodType"
										style="width:160px;height:23px" >
									<option value="">全部</option>
									<option value="RICE">米饭</option>
									<option value="NOODLE">面食</option>
									<option value="OTHER">其他</option>
								</select>
							</td>
							<td>类型:</td>
							<td>
								<select class="easyui-combobox" name="dietType"
										 style="width:120px;height:23px" >
									<option value="">全部</option>
									<option value="BREAKFAST">早餐</option>
									<option value="LUNCH">午餐</option>
									<option value="DINNER">晚餐</option>
									<option value="OTHER">其他</option>
								</select>
							</td>
							<td>来源:</td>
							<td>
								<select class="easyui-combobox" name="dietSource"
										style="width:160px;height:23px" >
									<option value="">全部</option>
									<option value="SELF_MADE">自己做</option>
									<option value="RESTAURANT">餐馆</option>
									<option value="TAKE_OUT">外卖</option>
									<option value="OTHER">其他</option>
								</select>
							</td>
						</tr>
						<tr>
							<td>关键字:</td>
							<td><input class="easyui-validatebox" type="text"
									   name="name" style="width:160px;" /></td>
							<td>起始日期:</td>
							<td>
								<input class="easyui-datebox" name="startDate"
									   data-options="showSeconds:false" style="width:120px">--
								<input class="easyui-datebox" name="endDate"
									   data-options="showSeconds:false" style="width:120px">
							</td>
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
			<div region="center" border="true" title="饮食记录列表" split="true"
				style="background:#E2E377;overflow-y: hidden">
				<div id="grid" fit="true"></div>
			</div>
		</div>
	</div>

	<div id="eidt-window" title="饮食信息" class="easyui-window"
		data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div class="yhdiv">
			<form id="ff" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>类型:</td>
						<td>
							<select class="easyui-combobox" name="dietType"
									style="width:160px;height:23px" >
								<option value="BREAKFAST">早餐</option>
								<option value="LUNCH">午餐</option>
								<option value="DINNER">晚餐</option>
								<option value="OTHER">其他</option>
							</select>
						</td>
						<td>来源:</td>
						<td>
							<select class="easyui-combobox" name="dietSource"
									style="width:160px;height:23px" >
								<option value="SELF_MADE">自己做</option>
								<option value="RESTAURANT">餐馆</option>
								<option value="TAKE_OUT">外卖</option>
								<option value="OTHER">其他</option>
							</select>
						</td>
					</tr>
					<tr>
						<td>食物:</td>
						<td colspan=3>
							<input class="easyui-tagbox" name="foods" style="width:420px;height:30px" id="keywordsList"
								   data-options="multiline:true" />
							<a href="javascript:loadLastDiet();"><img src="../static/image/reload.png" alt="加载昨天的数据"></img></a>
					</tr>
					<tr>
						<td>食物类型:</td>
						<td>
							<select class="easyui-combobox" name="foodType"
									style="width:160px;height:23px" >
								<option value="RICE">米饭</option>
								<option value="NOODLE">面食</option>
								<option value="OTHER">其他</option>
							</select>
						</td>
						<td>发生时间:</td>
						<td><input class="easyui-datetimebox" name="occurTime"
								   data-options="showSeconds:true" style="width:155px"></td>
					</tr>
					<tr>
						<td>标签:</td>
						<td colspan=3>
							<input class="easyui-tagbox" name="tags" style="width:450px;height:30px"
								   data-options="multiline:true" />
					</tr>
					<tr>
						<td>地点:</td>
						<td colspan=3>
							<input class="easyui-textbox" type="text"
								   name="location" style="width:450px;" />
						</td>
					</tr>
					<tr>
						<td>备注:</td>
						<td colspan=3>
							<input class="easyui-textbox" type="text" data-options="multiline:true"
								   name="remark" style="width:450px;height:60px" />
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

	<div id="diet-analyse-stat-window" title="饮食统计" class="easyui-window"
		 style="width:1100px;height:550px;"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div region="north" border="true" title="查询条件" split="true"
			 style="overflow:auto;height:80px;" data-options="selected:false"
			 icon="icon-edit" class="yhbackground">
			<form id="diet-analyse-stat-search-form" method="post">
				<table cellpadding="5" class="tableForm" style="width: 100%;">
					<tr>
						<td>起始日期:</td>
						<td>
							<input class="easyui-datebox" name="startDate"
								   data-options="showSeconds:false" style="width:100px">--
							<input class="easyui-datebox" name="endDate"
								   data-options="showSeconds:false" style="width:100px">
						</td>
						<td>类型:</td>
						<td>
							<select class="easyui-combobox" name="dietType"
									style="width:100px;" >
								<option value="">全部</option>
								<option value="BREAKFAST">早餐</option>
								<option value="LUNCH">午餐</option>
								<option value="DINNER">晚餐</option>
								<option value="OTHER">其他</option>
							</select>
						</td>
						<td>来源:</td>
						<td>
							<select class="easyui-combobox" name="dietSource"
									style="width:80px;" >
								<option value="">全部</option>
								<option value="SELF_MADE">自己做</option>
								<option value="RESTAURANT">餐馆</option>
								<option value="TAKE_OUT">外卖</option>
								<option value="OTHER">其他</option>
							</select>
						</td>
					</tr>
					<tr>
						<td>食物类型:</td>
						<td>
							<select class="easyui-combobox" name="foodType"
									style="width:100px;" >
								<option value="">全部</option>
								<option value="RICE">米饭</option>
								<option value="NOODLE">面食</option>
								<option value="OTHER">其他</option>
							</select>
						</td>
						<td>地点:</td>
						<td>
							<input class="easyui-textbox" type="text"
								   name="location" style="width:100px;" />
						</td>
						<td align="center" colspan="2">
							<select class="easyui-combobox" name="chartType"
									style="width:70px;">
								<option value="BAR">柱状图</option>
								<option value="PIE">饼图</option>
							</select>
							<a href="javascript:void(0)" class="easyui-linkbutton"
							   icon="icon-search" onclick="statDietAnalyse()">统计</a>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div region="center" border="true" title="统计" split="true"
			 style="overflow-y: hidden;width:auto;height:auto;">
			<div region="center" border="true" title="结果" split="true">
				<div id="dietAnalyseContainer"
					 style="min-width: 500px; height: 450px; margin: 0 auto"></div>
			</div>
		</div>
	</div>

	<%@ include file="../plan/userPlanInclude.jsp"%>
	<%@ include file="../log/operationLogCompareInclude.jsp"%>

</body>
</html>
