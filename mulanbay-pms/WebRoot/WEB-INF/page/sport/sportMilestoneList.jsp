<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>运动类型</title>
	<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/mulanbay/sportMilestone.js"></script>
	<script type="text/javascript" src="/static/js/echarts/echarts-all-3.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/mulanbaychart.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/operationLogCompare.js"></script>

</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="center" border="true" title="运动里程碑统计"
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
							<td>运动类型:</td>
							<td><input id="sportTypeList" class="easyui-combotree"
									   data-options="editable:true" name="sportTypeId"></td>
							</td>
							<td>名称:</td>
							<td><input class="easyui-validatebox" type="text"
									   name="name" style="width:235px;" /></td>
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
			<div region="center" border="true" title="运动里程碑列表" split="true"
				 style="background:#E2E377;overflow-y: hidden">
				<div id="grid" fit="true"></div>
			</div>
		</div>
	</div>

	<div id="eidt-window" title="运动里程碑信息" class="easyui-window"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div class="yhdiv">
			<form id="ff" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>运动类型:</td>
						<td><input id="sportTypeList2" class="easyui-combotree"
								   name="sportTypeId" style="width:160px;"
								   data-options="required:true" missingMessage="运动类型"></td>
					</tr>
					<tr>
						<td>名称:</td>
						<td><input class="easyui-validatebox" type="text"
								   name="name" style="width:158px;" />
						</td>
					</tr>
					<tr>
						<td>别名:</td>
						<td><input class="easyui-validatebox" type="text"
								   name="alais" style="width:158px;" />
						</td>
					</tr>
					<tr>
						<td>公里数:</td>
						<td><input class="easyui-numberbox"
								   name="kilometres" style="width:158px;"
								   data-options="min:0,precision:2,required:true">公里</td>
					</tr>
					<tr>
						<td>锻炼时长:</td>
						<td><input class="easyui-numberspinner"
								   name="minutes" style="width:158px;"
								   data-options="min:0,precision:0,required:true">分钟</td>
					</tr>
					<tr>
						<td>排序号:</td>
						<td><input class="easyui-numberspinner" type="text"
								   name="orderIndex" style="width:158px;" data-options="required:true,min:0,precision:0"/>
						</td>
					</tr>
					<tr>
						<td>备注:</td>
						<td><input class="easyui-textbox" type="text" data-options="multiline:true"
											 type="textarea" name="remark" style="width:158px;height:60px"></input></td>
					</tr>
				</table>
				<input type="hidden" name='id' id="id"/>
				<div class="submitForm">
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-ok" onclick="saveData()">保存</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
						icon="icon-cancel" onclick="closeWindow()">取消</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-info" onclick="showEditLogCompare('SportMilestone','id')">修改记录</a>
				</div>
			</form>
		</div>
	</div>

	<div id="refresh-window" title="刷新里程碑" class="easyui-window"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div class="yhdiv">
			<form id="refresh-form" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>运动类型:</td>
						<td><input id="sportTypeList3" class="easyui-combotree"
								   name="sportTypeId" style="width:120px;"
								   data-options="required:true" missingMessage="运动类型"></td>
					</tr>
					<tr>
						<td>强制刷新:</td>
						<td>
							<input name="reInit" type="radio" class="easyui-validatebox" checked="checked" required="true" value="true" style="width:30px">是
							<input name="reInit" type="radio" class="easyui-validatebox" required="true" value="false" style="width:30px">否
						</td>
					</tr>
				</table>
				<div class="submitForm">
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-ok" onclick="refreshMilestone()">执行</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-cancel" onclick="closeWindow('refresh-window')">关闭</a>
				</div>
			</form>
		</div>
	</div>

	<div id="stat-window" title="统计信息（基于当前查询条件）" class="easyui-window"
		 data-options="modal:true,closed:true,iconCls:'icon-info'" style="width: 800px; height: 400px; margin: 0 auto">
		<div class="yhdiv">
			<form id="stat-form" method="post">
				<div region="center" border="true" title="结果" split="true">
					<div id="container"
						 style="width: 700px; height: 350px; margin: 0 auto"></div>
				</div>
			</form>
		</div>
	</div>
	<%@ include file="../log/operationLogCompareInclude.jsp"%>

</body>
<script type="text/javascript">
    function getJsonData() {
        var vurl = '../sportMilestone/stat';
        var para = form2Json("search-window");
        doAjax(para, vurl, 'GET',false, function(data) {
            $('#stat-window').window('open');
            createLineData(data);
        });
    }
</script>
</html>
