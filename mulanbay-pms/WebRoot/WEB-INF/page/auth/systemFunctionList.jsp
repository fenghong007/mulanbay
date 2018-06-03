<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>系统功能管理</title>
	<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="../../static/js/mulanbay/systemFunction.js"></script>
	<script type="text/javascript" src="../../static/js/mulanbay/operationLogCompare.js"></script>

</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="center" border="true" title="系统功能管理"
		style="border-left:0px;border-right:0px; height: 188px">
		<div class="easyui-layout" id="subWrap"
			style="width:100%;height:100%;background:#0A3DA4;"
			data-options="fit:true,border:false">
			<div region="north" border="true" title="查询条件" split="true"
				style="overflow:auto;height:110px;" data-options="selected:false"
				icon="icon-edit" class="yhbackground">
				<form id="search-window" method="post">
					<table cellpadding="5" class="tableForm" style="width: 100%;">
						<tr>
							<td>名称:</td>
							<td><input class="easyui-validatebox" type="text"
									   name="name" style="width:235px;" /></td>
							<td>功能点类型:</td>
							<td><select class="easyui-combobox" name="functionType"
										style="width:155px;">
								<option value="">全部</option>
								<option value="CREATE">新增</option>
								<option value="EDIT">修改</option>
								<option value="DELETE">删除</option>
								<option value="SEARCH">查询</option>
								<option value="STAT">统计</option>
								<option value="OTHER">其他</option>
							</select>
							</td>
							<td>请求地址类型:</td>
							<td><select class="easyui-combobox" name="urlType"
										style="width:155px;">
								<option value="">全部</option>
								<option value="NORMAL">普通</option>
								<option value="REST_FULL">restfull</option>
							</select>
							</td>
						</tr>
						<tr>
							<td>数据类型:</td>
							<td><select class="easyui-combobox" name="functionDataType"
										style="width:155px;">
								<option value="">全部</option>
								<option value="PAGE">页面</option>
								<option value="NORMAL">普通</option>
								<option value="CONDITION">筛选条件</option>
								<option value="MENU">菜单</option>
							</select>
							</td>
							<td>请求类型:</td>
							<td><select class="easyui-combobox" name="method"
										style="width:155px;">
								<option value="">全部</option>
								<option value="POST">POST</option>
								<option value="GET">GET</option>
								<option value="PUT">PUT</option>
								<option value="DELETE">DELETE</option>
							</select>
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
			<div region="center" border="true" title="功能点列表" split="true"
				 style="background:#E2E377;overflow-y: hidden">
				<div id="grid" fit="true"></div>
			</div>
		</div>
	</div>

	<div id="eidt-window" title="功能点信息" class="easyui-window"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div class="yhdiv" style="width:650px;height:450px;">
			<form id="ff" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>上层功能点名称:</td>
						<td colspan=3>
						<input id="parentSystemFunctionList" class="easyui-combotree"
								   name="parent.id" style="width:100%;"></td>
					</tr>
					<tr>
						<td>功能点名称:</td>
						<td colspan=3><input class="easyui-validatebox"
											 type="textarea" name="name" style="width:99%;"></input></td>
					</tr>
					<tr>
						<td>支持的请求方式:</td>
						<td colspan=3><input class="easyui-validatebox"
											 type="textarea" name="supportMethods" style="width:99%;"></input></td>
					</tr>
					<tr>
						<td>请求URL:</td>
						<td colspan=3><input class="easyui-validatebox"
											 type="textarea" name="urlAddress" style="width:99%;"></input></td>
					</tr>
					<tr>
						<td>类名:</td>
						<td colspan=3><input class="easyui-validatebox"
											 type="textarea" name="beanName" style="width:99%;"></input></td>
					</tr>
					<tr>
						<td>主键列名:</td>
						<td><input class="easyui-validatebox" type="text"
								   name="idField" style="width:158px;" />
						</td>
						<td>主键列类型:</td>
						<td><select class="easyui-combobox" name="idFieldType"
									style="width:155px;">
							<option value="LONG">LONG</option>
							<option value="INTEGER">INTEGER</option>
							<option value="SHORT">SHORT</option>
							<option value="STRING">STRING</option>
						</select>
						</td>
					</tr>
					<tr>
						<td>数据类型:</td>
						<td><select class="easyui-combobox" name="functionDataType"
									style="width:155px;">
							<option value="PAGE">页面</option>
							<option value="NORMAL">普通</option>
							<option value="CONDITION">筛选条件</option>
							<option value="MENU">菜单</option>
						</select>
						</td>
						<td>功能点类型:</td>
						<td><select class="easyui-combobox" name="functionType"
									style="width:155px;">
							<option value="CREATE">新增</option>
							<option value="EDIT">修改</option>
							<option value="DELETE">删除</option>
							<option value="SEARCH">查询</option>
							<option value="STAT">统计</option>
							<option value="OTHER">其他</option>
						</select>
						</td>
					</tr>
					<tr>
						<td>需要登陆认证:</td>
						<td>
							<input name="loginAuth" type="radio" class="easyui-validatebox" checked="checked" required="true" value="true" style="width:30px">需要
							<input name="loginAuth" type="radio" class="easyui-validatebox" required="true" value="false" style="width:30px">不需要
						</td>
						<td>需要权限认证:</td>
						<td>
							<input name="permissionAuth" type="radio" class="easyui-validatebox" checked="checked" required="true" value="true" style="width:30px">需要
							<input name="permissionAuth" type="radio" class="easyui-validatebox" required="true" value="false" style="width:30px">不需要
						</td>
					</tr>
					<tr>
						<td>需要IP认证:</td>
						<td>
							<input name="ipAuth" type="radio" class="easyui-validatebox" checked="checked" required="true" value="true" style="width:30px">需要
							<input name="ipAuth" type="radio" class="easyui-validatebox" required="true" value="false" style="width:30px">不需要
						</td>
						<td>自动登陆:</td>
						<td>
							<input name="autoLogin" type="radio" class="easyui-validatebox" checked="checked" required="true" value="true" style="width:30px">需要
							<input name="autoLogin" type="radio" class="easyui-validatebox" required="true" value="false" style="width:30px">不需要
						</td>
					</tr>
					<tr>
						<td>需要请求限制:</td>
						<td>
							<input name="requestLimit" type="radio" class="easyui-validatebox" checked="checked" required="true" value="true" style="width:30px">需要
							<input name="requestLimit" type="radio" class="easyui-validatebox" required="true" value="false" style="width:30px">不需要
						</td>
						<td>请求限制时间(秒):</td>
						<td><input class="easyui-numberspinner"
								   name="requestLimitPeriod" style="width:155px;"
								   data-options="min:0,precision:0">
						</td>
					</tr>
					<tr>
						<td>每天限制次数:</td>
						<td><input class="easyui-numberspinner"
								   name="dayLimit" style="width:155px;"
								   data-options="min:0,precision:0">
						</td>
						<td>积分奖励:</td>
						<td><input class="easyui-numberspinner"
								   name="rewardPoint" style="width:155px;"
								   data-options="precision:0">
						</td>
					</tr>
					<tr>
						<td>用户触发:</td>
						<td>
							<input name="triggerStat" type="radio" class="easyui-validatebox" required="true" value="true" style="width:30px">是
							<input name="triggerStat" type="radio" class="easyui-validatebox" checked="checked" required="true" value="false" style="width:30px">否
						</td>
						<td>记录返回数据:</td>
						<td>
							<input name="recordReturnData" type="radio" class="easyui-validatebox" required="true" value="true" style="width:30px">是
							<input name="recordReturnData" type="radio" class="easyui-validatebox" checked="checked" required="true" value="false" style="width:30px">否
						</td>
					</tr>
					<tr>
						<td>状态:</td>
						<td>
							<input name="status" type="radio" class="easyui-validatebox" required="true" value="ENABLE" style="width:30px">可用
							<input name="status" type="radio" class="easyui-validatebox" checked="checked" required="true" value="DISABLE" style="width:30px">不可用
						</td>
						<td>是否记录日志:</td>
						<td>
							<input name="doLog" type="radio" class="easyui-validatebox" checked="checked" required="true" value="true" style="width:30px">是
							<input name="doLog" type="radio" class="easyui-validatebox" required="true" value="false" style="width:30px">否
						</td>
					</tr>
					<tr>
						<td>URL类型:</td>
						<td>
							<input name="urlType" type="radio" class="easyui-validatebox" checked="checked" required="true" value="NORMAL" style="width:30px">普通
							<input name="urlType" type="radio" class="easyui-validatebox" required="true" value="REST_FULL" style="width:30px">RestFull
						</td>
						<td>数据区分用户:</td>
						<td>
							<input name="diffUser" type="radio" class="easyui-validatebox" required="true" value="true" style="width:30px">是
							<input name="diffUser" type="radio" class="easyui-validatebox" checked="checked" required="true" value="false" style="width:30px">否
						</td>
					</tr>
					<tr>
						<td>分组号:</td>
						<td><input class="easyui-numberspinner"
								   name="groupId" style="width:155px;"
								   data-options="min:0,precision:0">
						</td>
						<td>排序号:</td>
						<td><input class="easyui-numberspinner"
								   name="orderIndex" style="width:155px;"
								   data-options="min:0,precision:0">
						</td>
					</tr>
					<tr>
						<td>错误代码:</td>
						<td><input class="easyui-numberspinner"
								   name="errorCode" style="width:155px;"
								   data-options="min:0,precision:0">
						</td>
					</tr>
					<tr>
						<td>图片名:</td>
						<td colspan="3"><input class="easyui-validatebox" type="text"
								   name="imageName" style="width:99%;" />
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
				<input type="hidden" name='mileageBest' />
				<input type="hidden" name='fastBest' />
				<div class="submitForm">
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-ok" onclick="saveData()">保存</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
						icon="icon-cancel" onclick="closeWindow()">取消</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-info" onclick="showEditLogCompare('SystemFunction','id')">修改记录</a> 
				</div>
			</form>
		</div>
	</div>
	<%@ include file="../log/operationLogCompareInclude.jsp"%>

</body>
</html>
