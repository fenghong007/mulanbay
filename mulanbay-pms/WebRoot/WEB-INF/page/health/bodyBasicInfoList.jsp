<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>身体基本情况</title>
	<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/mulanbay/bodyBasicInfo.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/operationLogCompare.js"></script>

</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="center" border="true" title="身体基本情况"
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
									   data-options="showSeconds:false" style="width:100px">--
								<input class="easyui-datebox" name="endDate"
									   data-options="showSeconds:false" style="width:100px"></td>
							</td>
							<td>体重区间:</td>
							<td><input  class="easyui-numberbox"
									   name="startWeight" style="width:60px;"
									   data-options="min:0,precision:1">
								--
								<input class="easyui-numberbox"
									   name="endWeight" style="width:60px;"
									   data-options="min:0,precision:1">
							</td>
							<td>身高区间:</td>
							<td><input class="easyui-numberbox"
									   name="startHeight" style="width:60px;"
									   data-options="min:0,precision:0">
								--
								<input class="easyui-numberbox"
									   name="endHeight" style="width:60px;"
									   data-options="min:0,precision:0">
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
			<div region="center" border="true" title="身体基本情况列表" split="true"
				 style="background:#E2E377;overflow-y: hidden">
				<div id="grid" fit="true"></div>
			</div>
		</div>
	</div>

	<div id="eidt-window" title="身体基本情况信息" class="easyui-window"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div class="yhdiv">
			<form id="ff" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>身高:</td>
						<td><input  class="easyui-numberbox" id="height"
									name="height" style="width:155px;"
									data-options="required:true,min:0,precision:0">&nbsp;&nbsp;厘米
						</td>
					</tr>
					<tr>
						<td>体重:</td>
						<td><input  class="easyui-numberbox" id="weight"
									name="weight" style="width:155px;"
									data-options="required:true,min:0,precision:1">&nbsp;&nbsp;公斤
						</td>
					</tr>
					<tr>
						<td>BMI指数:</td>
						<td><input  class="easyui-numberbox" id="bmi"
									name="bmi" style="width:155px;"
									data-options="required:true,min:0,precision:1">
						</td>
					</tr>
					<tr>
						<td>记录日期:</td>
						<td><input class="easyui-datebox" name="recordDate"
								   data-options="showSeconds:false" style="width:155px"></td>
					</tr>
					<tr>
						<td>备注:</td>
						<td><input class="easyui-textbox" type="text" data-options="multiline:true"
								   type="textarea" name="remark" style="width:155px;height:60px"></input></td>
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
					   icon="icon-info" onclick="showEditLogCompare('BodyBasicInfo','id')">修改记录</a>
				</div>
			</form>
		</div>
	</div>
	<%@ include file="../log/operationLogCompareInclude.jsp"%>

</body>
</html>
