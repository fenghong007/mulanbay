<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>身体不适记录</title>
	<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/mulanbay/bodyAbnormalRecord.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/bodyAbnormalRecordAnalyse.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/operationLogCompare.js"></script>

</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="center" border="true" title="身体不适记录统计"
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
							<td>名称:</td>
							<td><input class="easyui-validatebox" type="text"
									   name="name" style="width:235px;" /></td>
							<td>起始日期:</td>
							<td><input class="easyui-datebox" name="startDate"
									   data-options="showSeconds:false" style="width:110px">--
								<input class="easyui-datebox" name="endDate"
									   data-options="showSeconds:false" style="width:110px"></td>
							</td>
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
			<div region="center" border="true" title="身体不适记录列表" split="true"
				 style="background:#E2E377;overflow-y: hidden">
				<div id="grid" fit="true"></div>
			</div>
		</div>
	</div>

	<div id="eidt-window" title="身体不适记录信息" class="easyui-window"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div class="yhdiv">
			<form id="ff" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>疾病:</td>
						<td><input class="easyui-validatebox" type="text"
								   name="disease" style="width:155px;" /></td>
						<td>器官:</td>
						<td><input class="easyui-validatebox" type="text"
								   name="organ" style="width:155px;" /></td>
					</tr>
					<tr>
						<td>疼痛等级:</td>
						<td><input class="easyui-numberspinner"
								   name="painLevel" style="width:155px;"
								   data-options="required:true,min:0,max:10,precision:0">(0-10)</td>
						<td>重要程度:</td>
						<td><input class="easyui-numberbox"
								   name="importantLevel" style="width:155px;"
								   data-options="required:true,min:0,max:5,precision:1">(0.0-5.0)</td>
					</tr>
					<tr>
						<td>发生日期:</td>
						<td><input class="easyui-datebox" name="occurDate"
								   data-options="required:true,showSeconds:false" style="width:155px"></td>
						<td>结束日期:</td>
						<td><input class="easyui-datebox" name="finishDate"
								   data-options="required:true,showSeconds:false" style="width:155px"></td>
					</tr>
					<tr>
						<td>持续天数:</td>
						<td><input class="easyui-numberspinner"
								   name="lastDays" style="width:155px;"
								   data-options="required:true,min:0,precision:0">&nbsp;&nbsp;天</td>
					</tr>
					<tr>
						<td>备注:</td>
						<td colspan="3">
							<input class="easyui-textbox" type="text" data-options="multiline:true"
								   name="remark" style="width:460px;height:60px" /></td>
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
					   icon="icon-info" onclick="showEditLogCompare('BodyAbnormalRecord','id')">修改记录</a>
				</div>
			</form>
		</div>
	</div>
	<%@ include file="../health/bodyAbnormalRecordAnalyse.jsp"%>
	<%@ include file="../log/operationLogCompareInclude.jsp"%>

</body>
</html>
