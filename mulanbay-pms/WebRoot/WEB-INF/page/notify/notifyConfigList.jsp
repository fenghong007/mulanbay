<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>提醒设置</title>
	<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/mulanbay/notifyConfig.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/statValueConfigPt.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/operationLogCompare.js"></script>

</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="center" border="true" title="提醒设置统计"
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
			<div region="center" border="true" title="提醒配置列表" split="true"
				 style="background:#E2E377;overflow-y: hidden">
				<div id="grid" fit="true"></div>
			</div>
		</div>
	</div>

	<div id="eidt-window" title="提醒配置信息" class="easyui-window"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div style="width:100%;height:480px;">
			<form id="ff" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>选取模板:</td>
						<td colspan="3">
							<input id="notifyConfigList" class="easyui-combotree" style="width:460px;"
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
						<td>查询类型:</td>
						<td><select class="easyui-combobox" name="sqlType"
									style="width:158px;">
							<option value="SQL">SQL</option>
							<option value="HQL">HQL</option>
						</select></td>
						<td>关联的业务:</td>
						<td>
							<input id="relatedBeansList" class="easyui-combotree" style="width:150px;"
								   data-options="editable:true" name="relatedBeans">
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
						<td>返回类型:</td>
						<td><select class="easyui-combobox" name="resultType"
									style="width:158px;">
							<option value="DATE">日期</option>
							<option value="NUMBER">数字</option>
							<option value="NAME_DATE">名称-日期</option>
							<option value="NAME_NUMBER">名称-数字</option>
						</select></td>
						<td>返回值类型:</td>
						<td><select class="easyui-combobox" name="valueType"
									style="width:155px;">
							<option value="DAY">天</option>
							<option value="HOUR">小时</option>
							<option value="MINUTE">分钟</option>
							<option value="NUMBER">个</option>
							<option value="TIMES">次</option>
							<option value="PERCENT">百分比</option>
							<option value="KILOMETRES">公里</option>
							<option value="MONEY">元</option>
							<option value="BOOK">本</option>
							<option value="KIND">种</option>
						</select></td>
					</tr>
					<tr>
						<td>用户字段:</td>
						<td><input class="easyui-textbox" type="text"
								   name="userField" style="width:155px;" />
						</td>
						<td>状态:</td>
						<td>
							<input name="status" type="radio" class="easyui-validatebox" required="true" value="ENABLE" style="width:30px">可用
							<input name="status" type="radio" class="easyui-validatebox" checked="checked" required="true" value="DISABLE" style="width:30px">不可用
						</td>
					</tr>
					<tr>
						<td>比较类型:</td>
						<td><select class="easyui-combobox" name="notifyType"
									style="width:155px;">
							<option value="LESS">小于</option>
							<option value="MORE">大于</option>
						</select></td>
						<td>排序号:</td>
						<td><input class="easyui-numberspinner" type="text"
								   name="orderIndex" style="width:155px;" data-options="required:true,min:0,precision:0"/>
						</td>
					</tr>
					<tr>
						<td>等级:</td>
						<td><input class="easyui-numberspinner" type="text"
								   name="level" style="width:155px;" data-options="required:true,min:0,precision:0"/>
						</td>
						<td>积分奖励:</td>
						<td><input class="easyui-numberspinner"
								   name="rewardPoint" style="width:155px;"
								   data-options="precision:0">
						</td>
					</tr>
					<tr>
						<td>链接地址:</td>
						<td colspan="3"><input class="easyui-validatebox" type="text"
											   name="url" style="width:450px;" />
						</td>
					</tr>
					<tr>
						<td>TAB名称:</td>
						<td><input class="easyui-textbox" type="text"
								   name="tabName" style="width:155px;" />
						</td>
						<td>业务KEY:</td>
						<td><input class="easyui-textbox" type="text"
								   name="bussKey" style="width:155px;" />
						</td>
					</tr>
					<tr>
						<td>用户日历标题:</td>
						<td colspan="3"><input class="easyui-validatebox" type="text"
											   name="defaultCalendarTitle" style="width:450px;" />
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
					   icon="icon-info" onclick="showEditLogCompare('NotifyConfig','id')">修改记录</a>
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
				<input type="hidden" name='type' id="type" value="NOTIFY" />
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
