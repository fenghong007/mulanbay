<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>用户行为月度统计</title>
	<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/echarts/echarts-all-3.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/mulanbaychart.js"></script>

</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="center" border="true" title="用户行为月度统计"
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
							<td>类型:</td>
							<td>
								<input name="dateGroupType" type="radio" class="easyui-validatebox" required="true" value="YEAR" style="width:30px">年
								<input name="dateGroupType" type="radio" class="easyui-validatebox" required="true" checked="checked" value="MONTH" style="width:30px">月
							</td>
							<td>选取配置:</td>
							<td>
								<input id="userBehaviorList" class="easyui-combotree" style="width:200px;"
									   data-options="editable:true" name="userBehaviorId">
							</td>
							<td>类型:</td>
							<td>
								<input id="behaviorTypeList" class="easyui-combotree" style="width:120px;"
									   data-options="editable:true" name="behaviorType">
							</td>

						</tr>
						<tr>
							<td>选择日期:</td>
							<td><input class="easyui-datebox" name="yearmonth"
									   data-options="formatter:myYearMonthformatter,parser:myYearMonthparser" style="width:120px">
							</td>
							<td>关键字:</td>
							<td><input class="easyui-combobox"  type="text" data-options="editable:true" id="keywordsList"
									   name="name" style="width:200px;" />
							</td>
							<td align="center">
								<a href="javascript:void(0)" class="easyui-linkbutton"
								   icon="icon-search" onclick="getJsonData()">统计</a> <a
									href="javascript:void(0)" class="easyui-linkbutton"
									icon="icon-cancel" onclick="resetMySearchWindow()">重置</a>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div region="center" border="true" title="结果" split="true" style="background:gainsboro;overflow-y: hidden">
				<div id="container"
					style="min-width: 400px;width: 100%; height: 100%; margin: 0 auto"></div>
			</div>
		</div>
	</div>

</body>
<script type="text/javascript">
    $(function() {
        initSearchForm();
        loadUserBehaviorList('');
        getJsonData();
    });
    // 自定义化
    function resetMySearchWindow() {
        $('#search-window').form('reset');
        initSearchForm();
    }
    //初始化查询界面的开始、结束日期
    function initSearchForm(){
        // 查询条件今年的
        var formData = {
            yearmonth: getDay()
        };
        $('#search-window').form('load', formData);
        $('#behaviorTypeList').combotree({
            url : '../userBehaviorConfig/getUserBehaviorTypeTree?needRoot=true',
            valueField : 'id',
            textField : 'text',
            loadFilter: function(data){
                return loadDataFilter(data);
            },
            onChange: function (n,o) {
                loadUserBehaviorList(n);
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

    }
    function loadUserBehaviorList(behaviorType) {
        $('#userBehaviorList').combotree({
            url : '../userBehavior/getUserBehaviorTree?behaviorType='+behaviorType,
            valueField : 'id',
            textField : 'text',
            loadFilter: function(data){
                return loadDataFilter(data);
            },
            onChange: function (n,o) {
                loadUserBehaviorKeywordsList(n);
            }
        });
    }
    function loadUserBehaviorKeywordsList(id) {
        $('#keywordsList').combobox({
            url : '../userBehavior/getUserBehaviorKeywordsTree?id='+id,
            valueField : 'id',
            textField : 'text',
            loadFilter: function(data){
                return loadDataFilter(data);
            }
        });
    }
    function getJsonData() {
		var vurl = '../userBehavior/stat';
		var para = form2Json("search-window");
        var ss = (para.yearmonth.split('-'));
        para.year=parseInt(ss[0],10);
        para.month=parseInt(ss[1],10);
		doAjax(para, vurl, 'GET',false, function(data) {
		    if(para.dateGroupType=='YEAR'){
                createCalanderData(data);
            }else{
                createCalanderPieDataEnhanced(data,"container");
            }
		});
	}

</script>
</html>
