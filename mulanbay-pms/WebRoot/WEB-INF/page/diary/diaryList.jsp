<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>日记管理</title>
	<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/mulanbay/diary.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/planCommon.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/userPlanForBuss.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/operationLogCompare.js"></script>

</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="center" border="true" title="日记管理"
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
							<td>关键字:</td>
							<td><input class="easyui-validatebox" type="text"
									   name="remark" style="width:235px;" /></td>
							<td>字数:</td>
							<td><input class="easyui-numberspinner" type="text" id="minWords"
									   name="minWords" style="width:158px;" data-options="min:0,precision:0"/>--
								<input class="easyui-numberspinner" type="text" id="maxWords"
									   name="maxWords" style="width:158px;" data-options="min:0,precision:0"/></td>
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
			<div region="center" border="true" title="日记列表" split="true"
				 style="background:#E2E377;overflow-y: hidden">
				<div id="grid" fit="true"></div>
			</div>
		</div>
	</div>

	<div id="eidt-window" title="日记信息" class="easyui-window"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div class="yhdiv">
			<form id="ff" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>年份:</td>
						<td><input class="easyui-numberbox" type="text"
								   name="year" style="width:150px;" />
						</td>
					</tr>
					<tr>
						<td>日记第一天:</td>
						<td><input class="easyui-datebox" name="firstDay"
								   data-options="showSeconds:false" style="width:155px">
						</td>
					</tr>
					<tr>
						<td>字数:</td>
						<td><input class="easyui-numberbox" type="text"
								   name="words" style="width:150px;" data-options="required:true,min:0,precision:0"/>
						</td>
					</tr>
					<tr>
						<td>篇数:</td>
						<td><input class="easyui-numberbox" type="text"
								   name="pieces" style="width:150px;" data-options="required:true,min:0,precision:0"/>
						</td>
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
					   icon="icon-info" onclick="showEditLogCompare('Diary','id')">修改记录</a>
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
						<td>总字数</td>
						<td><input name="totalWords" id="totalWords" class="easyui-textbox"
								   style="width:155px;color:red"></td>
					</tr>
					<tr>
						<td>总篇数</td>
						<td><input name="totalPieces" id="totalPieces"
								   class="easyui-textbox" style="width:155px;color:red"></td>
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
