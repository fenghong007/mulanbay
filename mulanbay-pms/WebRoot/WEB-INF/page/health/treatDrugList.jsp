<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>看病用药记录管理</title>
<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/mulanbay/treatDrug.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/treatDrugDetailPt.js"></script>
	<script type="text/javascript" src="/static/js/echarts/echarts-all-3.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/mulanbaychart.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/bodyAbnormalRecordAnalyse.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/operationLogCompare.js"></script>

</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="center" border="true" title="看病用药记录信息"
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
							<td>药品品称:</td>
							<td><input class="easyui-validatebox"
									   type="textarea" name="name" style="width: 200px;"></input></td>
							</td>
							<td>疾病:</td>
							<td><input id="diseaseCategoryList" class="easyui-combotree" style="width: 150px;"
									   data-options="editable:true" name="disease"></td>
							</td>
							<td>起始日期:</td>
							<td><input class="easyui-datebox" name="startDate"
									   data-options="showSeconds:false" style="width:110px">--
								<input class="easyui-datebox" name="endDate"
									   data-options="showSeconds:false" style="width:110px"></td>
							</td>
						</tr>
						<tr>
							<td>关键字:</td>
							<td><input class="easyui-validatebox"
									   type="textarea" name="keywords" style="width: 200px;"></input></td>
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
			<div region="center" border="true" title="看病用药记录列表" split="true"
				style="background:#E2E377;overflow-y: hidden">
				<div id="grid" fit="true"></div>
			</div>
		</div>
	</div>

	<div id="eidt-window" title="药品信息" class="easyui-window" style="overflow:auto;height:550px;"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div class="yhdiv">
			<form id="ff" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>药品名称:</td>
						<td><input class="easyui-textbox"
								   name="name" style="width:155px;"
								   data-options="required:true"></td>
						<td>单位:</td>
						<td><input class="easyui-textbox"
								   name="unit" style="width:155px;"
								   data-options="required:true"></td>
					</tr>
					<tr>
						<td>数量:</td>
						<td><input class="easyui-numberspinner"
								   name="amount" style="width:155px;"
								   data-options="required:true,min:0,precision:0"></td>
						<td>针对疾病:</td>
						<td><input id="diseaseCategoryList2" class="easyui-combobox" style="width: 155px;"
								   data-options="editable:true" name="disease" />
							<a href="javascript:showBodyAbnomarlAnalyse('diseaseCategoryList2','DISEASE');"><img src="../static/image/info.png" alt="查看身体情况分析"></img></a>
						</td>
					</tr>
					<tr>
						<td>单价:</td>
						<td><input class="easyui-numberspinner"
								   name="unitPrice" style="width:155px;"
								   data-options="required:true,min:0,precision:2"></td>
						<td>总价:</td>
						<td><input class="easyui-numberspinner"
								   name="totalPrice" style="width:155px;"
								   data-options="required:true,min:0,precision:2"></td>
					</tr>
					<tr>
						<td>用药开始日期:</td>
						<td><input class="easyui-datebox" name="beginDate" disabled="true"
								   data-options="showSeconds:false" style="width:155px"></td>
						<td>用药结束日期:</td>
						<td><input class="easyui-datebox" name="endDate" disabled="true"
								   data-options="showSeconds:false" style="width:155px"></td>
					</tr>
					<tr>
						<td>用药频率:</td>
						<td>每<input class="easyui-numberspinner"
									name="perDay" style="width:60px;"
									data-options="required:true,min:0,precision:0">天
							<input class="easyui-numberspinner"
								   name="perTimes" style="width:60px;"
								   data-options="required:true,min:0,precision:0">次
						</td>
						<td>是否有效:</td>
						<td>
							<input name="available" type="radio" class="easyui-validatebox" checked="checked" required="true" value="true" style="width:30px">是
							<input name="available" type="radio" class="easyui-validatebox" required="true" value="false" style="width:30px">否
						</td>
					</tr>
					<tr>
						<td>是否提醒:</td>
						<td>
							<input name="remind" type="radio" class="easyui-validatebox" checked="checked" required="true" value="true" style="width:30px">是
							<input name="remind" type="radio" class="easyui-validatebox" required="true" value="false" style="width:30px">否
						</td>
					</tr>
					<tr>
						<td>备注:</td>
						<td colspan="3"><input class="easyui-textbox" type="text" data-options="multiline:true"
											   name="remark" style="width:450px;height:15px" /></td>
					</tr>
				</table>
				<input type="hidden" name="treatRecordId" id="treatRecordId"/>
				<input type="hidden" name='id' id="treatDrugId"/>
				<input type="hidden" name='createdTime' />
				<div region="center" border="true" title="用药记录列表" split="true"
					 style="background:#E2E377;overflow-y: hidden;height: 180px">
					<div id="gridDrugDetail" fit="true"></div>
				</div>
				<div class="submitForm">
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-ok" onclick="saveData()">保存</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-cancel" onclick="closeWindow()">关闭</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-info" onclick="showEditLogCompare('TreatDrug','id')">修改记录</a>
				</div>
			</form>
		</div>
	</div>

	<div id="eidt-window-drugDetail" title="用药记录详情" class="easyui-window"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div class="yhdiv">
			<form id="search-drugDetail-form" method="post">
				<input type="hidden" name='treatDrugId' id="treatDrugSearchId" value="0" />
			</form>
			<form id="ff-drugDetail" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>用药时间:</td>
						<td><input class="easyui-datetimebox" name="occurTime"
								   data-options="showSeconds:true" style="width:155px"></td>
					</tr>
					<tr>
						<td>备注:</td>
						<td colspan="3"><input class="easyui-validatebox"
											   type="textarea" name="remark" style="width:99%;"></input></td>
					</tr>
				</table>
				<input type="hidden" name='treatDrugId' id="fkTreatDrugId"/>
				<input type="hidden" name="id" id="treatDrugDetailId"/>
				<input type="hidden" name='createdTime' />
				<div class="submitForm">
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-ok" onclick="saveDataDrugDetail()">保存</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-cancel" onclick="closeWindow('eidt-window-drugDetail')">取消</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-info" onclick="showEditLogCompare('TreatDrugDetail','id')">修改记录</a>
				</div>
			</form>
		</div>
	</div>

	<%@ include file="../health/treatDrugDetailChartInclude.jsp"%>
	<%@ include file="../health/bodyAbnormalRecordAnalyse.jsp"%>
	<%@ include file="../log/operationLogCompareInclude.jsp"%>

</body>
</html>
