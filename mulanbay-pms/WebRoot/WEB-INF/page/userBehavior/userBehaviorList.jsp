<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>用户行为设置</title>
	<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/mulanbay/userBehavior.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/operationLogCompare.js"></script>

</head>
<body class="easyui-layout" data-options="fit:true,border:false">
<div region="center" border="true" title="用户行为设置"
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
							<td>名称:</td>
							<td><input class="easyui-validatebox" type="text"
									   name="name" style="width:200px;" /></td>
							<td>类型:</td>
							<td>
								<input id="behaviorTypeList" class="easyui-combotree" style="width:120px;"
									   data-options="editable:true" name="behaviorType">
							</td>
							<td>状态:</td>
							<td>
								<select class="easyui-combobox" name="status"
										style="width:70px;">
									<option value="">全部</option>
									<option value="DISABLE">不可用</option>
									<option value="ENABLE">可用</option>
								</select>
							</td>
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
			<div region="center" border="true" title="计划配置列表" split="true"
				 style="background:#E2E377;overflow-y: hidden">
				<div id="grid" fit="true"></div>
			</div>
		</div>
	</div>

<div id="eidt-window" title="用户行为设置" class="easyui-window"
	 data-options="modal:true,closed:true,iconCls:'icon-info'">
	<div >
		<form id="ff" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>选取模板:</td>
						<td colspan="3">
							<input id="userBehaviorConfigList" class="easyui-combotree" style="width:460px;"
								   data-options="editable:true" name="userBehaviorConfigId">
						</td>
					</tr>
					<tr>
						<td>类型选择:</td>
						<td colspan="3">
							<div id="selectList"></div>
						</td>
					</tr>
					<tr>
						<td>标题:</td>
						<td colspan="3"><input class="easyui-validatebox" type="text" id="title"
											   name="title" style="width:450px;" />
						</td>
					</tr>
					<tr>
						<td>加入月分析图表:</td>
						<td>
							<input name="monthStat" type="radio" class="easyui-validatebox" checked="checked" required="true" value="true" style="width:30px">是
							<input name="monthStat" type="radio" class="easyui-validatebox" required="true" value="false" style="width:30px">否
						</td>
						<td>状态:</td>
						<td>
							<input name="status" type="radio" class="easyui-validatebox" required="true" value="ENABLE" style="width:30px">可用
							<input name="status" type="radio" class="easyui-validatebox" checked="checked" required="true" value="DISABLE" style="width:30px">不可用
						</td>
					</tr>
					<tr>
						<td>排序号:</td>
						<td><input class="easyui-numberspinner" type="text"
								   name="orderIndex" style="width:155px;" data-options="required:true,min:0,precision:0"/>
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
				<div class="submitForm">
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-ok" onclick="saveData()">保存</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
						icon="icon-cancel" onclick="closeWindow()">关闭</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-info" onclick="showEditLogCompare('UserBehaviorConfig','id')">修改记录</a> 
				</div>
			</form>
		</div>
	</div>
<%@ include file="../log/operationLogCompareInclude.jsp"%>

</body>
</html>
