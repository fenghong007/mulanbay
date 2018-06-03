<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>报表设置</title>
	<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/mulanbay/reportConfig.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/statValueConfigPt.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/operationLogCompare.js"></script>

</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="center" border="true" title="报表设置统计"
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
							<td>状态:</td>
							<td>
								<select class="easyui-combobox" name="status"
										style="width:155px;">
									<option value="">全部</option>
									<option value="DISABLE">不可用</option>
									<option value="ENABLE">可用</option>
								</select>
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
			<div region="center" border="true" title="报表配置列表" split="true"
				 style="background:#E2E377;overflow-y: hidden">
				<div id="grid" fit="true"></div>
			</div>
		</div>
	</div>

	<div id="eidt-window" title="报表配置信息" class="easyui-window"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div style="width:100%;height:500px;">
			<form id="ff" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>选取模板:</td>
						<td colspan="3">
							<input id="reportConfigList" class="easyui-combotree" style="width:460px;"
								   data-options="editable:true" name="tmpNotifyConfigId">
						</td>
					</tr>
					<tr>
						<td>名称:</td>
						<td colspan="3"><input class="easyui-validatebox" type="text"
											   name="name" style="width:450px;" />
						</td>
					</tr>
					<tr>
						<td>标题:</td>
						<td colspan="3"><input class="easyui-validatebox" type="text"
											   name="title" style="width:450px;" />
						</td>
					</tr>
					<tr>
						<td>结果模板:</td>
						<td colspan=3>
							<input class="easyui-textbox" type="text" data-options="multiline:true"
								   name="resultTemplate" style="width:450px;height:50px" />
						</td>
					</tr>
					<tr>
						<td>查询类型:</td>
						<td><select class="easyui-combobox" name="sqlType"
									style="width:158px;">
							<option value="SQL">SQL</option>
							<option value="HQL">HQL</option>
						</select></td>
						<td>排序号:</td>
						<td><input class="easyui-numberspinner" type="text"
								   name="orderIndex" style="width:155px;" data-options="required:true,min:0,precision:0"/>
						</td>
					</tr>
					<tr>
						<td>查询语句:</td>
						<td colspan=3>
							<input class="easyui-textbox" type="text" data-options="multiline:true"
								   name="sqlContent" style="width:450px;height:80px" />
						</td>
					</tr>
					<tr>
						<td>注意事项:</td>
						<td colspan=3>
							注意：默认情况下查询条件年份在第一个，用户ID在第二个<br>
							因此SQL中一般先是 where year(toExerciseDate)=? and userId=?
						</td>
					</tr>
					<tr>
						<td>绑定用户:</td>
						<td>
							<input name="userBand" type="radio" class="easyui-validatebox" required="true" value="true" style="width:30px">是
							<input name="userBand" type="radio" class="easyui-validatebox" checked="checked" required="true" value="false" style="width:30px">否
						</td>
						<td>状态:</td>
						<td>
							<input name="status" type="radio" class="easyui-validatebox" required="true" value="ENABLE" style="width:30px">可用
							<input name="status" type="radio" class="easyui-validatebox" checked="checked" required="true" value="DISABLE" style="width:30px">不可用
						</td>
					</tr>
					<tr>
						<td>等级:</td>
						<td><input class="easyui-numberspinner" type="text"
								   name="level" style="width:155px;" data-options="required:true,min:0,precision:0"/>
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
						icon="icon-cancel" onclick="closeWindow()">取消</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-info" onclick="showEditLogCompare('ReportConfig','id')">修改记录</a>
				</div>
			</form>
			<div region="center" border="true" title="配置选项值列表" split="true"
				 style="background:#E2E377;overflow-y: hidden;height: 200px">
				<div id="gridStatValueConfig" fit="true"></div>
			</div>
		</div>
	</div>


	<div id="eidt-window-pcv" title="配置选项值" class="easyui-window"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div>
			<form id="ff-stat-value-config-value" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>名称:</td>
						<td colspan="3"><input class="easyui-validatebox" type="text"
											   name="name" style="width:450px;" />
						</td>
					</tr>
					<tr>
						<td>值类型:</td>
						<td><select class="easyui-combobox" name="valueClass"
									style="width:158px;" >
							<option value="LONG">LONG</option>
							<option value="INTEGER">INTEGER</option>
							<option value="SHORT">SHORT</option>
							<option value="STRING">STRING</option>
						</select></td>
						<td>排序号:</td>
						<td><input class="easyui-numberspinner"
								   name="orderIndex" style="width:150px;" value="1"
								   data-options="required:true,min:0,precision:0"></td>
					</tr>
					<tr>
						<td>SQL语句:</td>
						<td colspan=3>
							<input class="easyui-textbox" type="text" data-options="multiline:true"
								   name="sqlContent" style="width:450px;height:80px" />
						</td>
					</tr>
					<tr>
						<td>级联类型:</td>
						<td><select class="easyui-combobox" name="casCadeType"
									style="width:158px;" >
							<option value="NOT_CASCADE">不级联</option>
							<option value="CASCADE_NEXT">级联下一层</option>
							<option value="BE_CASCADED">级联上一层</option>
						</select></td>
						<td>用户字段:</td>
						<td><input class="easyui-textbox" type="text"
								   name="userField" style="width:155px;" />
						</td>
					</tr>
					<tr>
						<td>提示信息:</td>
						<td colspan="3"><input class="easyui-validatebox" type="text"
											   name="promptMsg" style="width:450px;" />
						</td>
					</tr>
				</table>
				<input type="hidden" id='statValueConfigFid' name="fid"/>
				<input type="hidden" name='createdTime' />
				<input type="hidden" name='type' id="type" value="REPORT" />
				<input type="hidden" name='id' id="statValueConfigId" />

				<div class="submitForm">
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-ok" onclick="showPlanCommend()">自动推荐</a><br><br>
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-ok" onclick="saveDataStatValueConfig()">保存</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-cancel" onclick="closeWindowStatValueConfig()">关闭</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-info" onclick="showEditLogCompare('StatValueConfig','statValueConfigId')">修改记录</a>
				</div>
			</form>
		</div>
		<form id="search-window-pcv" method="post">
			<input type="hidden" name='aa' />

		</form>
	</div>
	<%@ include file="../log/operationLogCompareInclude.jsp"%>

</body>
</html>
