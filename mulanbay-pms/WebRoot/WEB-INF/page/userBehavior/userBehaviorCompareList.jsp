<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>消费记录同期对比</title>
	<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/echarts/echarts-all-3.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/mulanbaychart.js"></script>

</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="center" border="true" title="消费记录同期对比"
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
							<td>源类型:</td>
							<td>
								<input id="sourceBehaviorTypeList" class="easyui-combotree" style="width:120px;"
									   data-options="editable:true" name="sourceBehaviorType">
							</td>
							<td>源配置:</td>
							<td>
								<input id="sourceUserBehaviorgList" class="easyui-combotree" style="width:200px;"
									   data-options="required:true,editable:true" name="sourceUserBehaviorId">
							</td>
							<td>源关键字:</td>
							<td><input class="easyui-validatebox" type="text"
									   name="sourceName" style="width:150px;" /></td>
							<td>年份:</td>
							<td><input id="yearList" class="easyui-combotree" style="width:110px;"
									   data-options="required:true,editable:true" name="year">
							</td>
						</tr>
						<tr>
							<td>目标类型:</td>
							<td>
								<input id="targetBehaviorTypeList" class="easyui-combotree" style="width:120px;"
									   data-options="editable:true" name="targetBehaviorType">
							</td>
							<td>目标配置:</td>
							<td>
								<input id="targetUserBehaviorList" class="easyui-combotree" style="width:200px;"
									   data-options="required:true,editable:true" name="targetUserBehaviorId">
							</td>
							<td>目标关键字:</td>
							<td><input class="easyui-validatebox" type="text"
									   name="targetName" style="width:150px;" /></td>
							<td align="center" colspan="2">
								<a href="javascript:void(0)" class="easyui-linkbutton"
								   icon="icon-search" onclick="getJsonData()">统计</a> <a
									href="javascript:void(0)" class="easyui-linkbutton"
									icon="icon-cancel" onclick="resetAndInitSearchWindowYear()">重置</a>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div region="center" border="true" title="结果" split="true">
				<div id="container"
					style="min-width: 400px; height: 100%; margin: 0 auto"></div>
			</div>
		</div>
	</div>

	<div id="stat-window" title="统计信息" class="easyui-window"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div class="yhdiv">
			<form id="stat-form" method="post">
				<table id="pg" style="width:400px;height: 300px">
				</table>
				<div class="submitForm">
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-cancel" onclick="closeWindow('stat-window')">关闭</a>
				</div>
			</form>
		</div>
	</div>

</body>
<script type="text/javascript">
    $(function() {
        initSearchForm();
        //getJsonData();
    });
	function getJsonData() {
		var vurl = '../userBehavior/compare';
		var para = form2JsonEnhanced("search-window");
		doAjax(para, vurl, 'GET',false, function(data) {
            createCompareCalanderData(data);
            loadCompareRowData(data.legendData,data.customData);
		});
	}
	function loadCompareRowData(legendData,data) {
        $('#stat-window').window('open');
        $('#pg').datagrid({
            url: '../treatOperation/aa',
            showGroup: true,
            scrollbarSize: 0,
            columns:[[
                {field:'sourceDate',title:legendData[0],width:150,align:'center'},
                {field:'targetDate',title:legendData[1],width:150,align:'center'},
                {field:'days',title:'相差天数',width:100,align:'center'}
            ]]
        });
        //清空所有数据
        $('#pg').datagrid('loadData', { total: 0, rows: [] });
        for (var i = 0; i < data.length; i++) {
            var days = data[i].days;
            var sourceDate =data[i].sourceDate;
            if(days>=0&&days<=3){
                sourceDate='<font color="red">'+data[i].sourceDate+'</font>';
            }
            if(days==-1){
                days='--';
			}
            var row = {
                sourceDate:sourceDate,
                targetDate:data[i].targetDate,
                days:days
            };
            $('#pg').datagrid('appendRow',row);
        }
    }
    //初始化查询界面的开始、结束日期
    function initSearchForm(){
        $('#sourceBehaviorTypeList').combotree({
            url : '../userBehaviorConfig/getUserBehaviorTypeTree?needRoot=true',
            valueField : 'id',
            textField : 'text',
            loadFilter: function(data){
                return loadDataFilter(data);
            },
            onChange: function (n,o) {
                loadSourceUserBehaviorList(n);
            }
        });
        $('#targetBehaviorTypeList').combotree({
            url : '../userBehaviorConfig/getUserBehaviorTypeTree?needRoot=true',
            valueField : 'id',
            textField : 'text',
            loadFilter: function(data){
                return loadDataFilter(data);
            },
            onChange: function (n,o) {
                loadTargetUserBehaviorList(n);
            }
        });
        $('#yearList').combotree({
            url : '../common/getYearTree',
            valueField : 'id',
            textField : 'text',
            loadFilter: function(data){
                return loadDataFilter(data);
            }
        });
        var year =getYear(0);
        $('#yearList').combotree('setValue', year);
    }
    function loadSourceUserBehaviorList(behaviorType) {
        $('#sourceUserBehaviorgList').combotree({
            url : '../userBehavior/getUserBehaviorTree?behaviorType='+behaviorType,
            valueField : 'id',
            textField : 'text',
            loadFilter: function(data){
                return loadDataFilter(data);
            }
        });
    }
    function loadTargetUserBehaviorList(behaviorType) {
        $('#targetUserBehaviorList').combotree({
            url : '../userBehavior/getUserBehaviorTree?behaviorType='+behaviorType,
            valueField : 'id',
            textField : 'text',
            loadFilter: function(data){
                return loadDataFilter(data);
            }
        });
    }

</script>
</html>
