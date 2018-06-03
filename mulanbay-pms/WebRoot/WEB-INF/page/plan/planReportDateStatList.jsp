<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>音乐练习统计</title>
	<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/echarts/echarts-all-3.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/mulanbaychart.js"></script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="center" border="true" title="计划执行统计"
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
							<td>选择计划:</td>
							<td><input id="userPlanList" class="easyui-combotree" style="width:160px"
									   data-options="editable:true" name="userPlanId"></td>
							</td>
							<td>起始日期:</td>
							<td><input class="easyui-datebox" name="startDate"
									   data-options="showSeconds:false" style="width:100px">--
								<input class="easyui-datebox" name="endDate"
									   data-options="showSeconds:false" style="width:100px"></td>
							</td>
							<td>统计结果:</td>
							<td>
								<select class="easyui-combobox" name="countValueStatResults"
										multiple="true" multiline="true" multivalue="true" separator="," style="width:100px;height:23px">
									<option value="ACHIEVED">已实现</option>
									<option value="UN_ACHIEVED">未实现</option>
									<option value="SKIP">忽略</option>
								</select>(次数)
								<select class="easyui-combobox" name="valueStatResults"
										multiple="true" multiline="true" multivalue="true" separator="," style="width:100px;height:23px">
									<option value="ACHIEVED">已实现</option>
									<option value="UN_ACHIEVED">未实现</option>
									<option value="SKIP">忽略</option>
								</select>(值)
							</td>
						</tr>
						<tr>
							<td>过滤全零数据:</td>
							<td>
								<input name="minValue" type="radio" class="easyui-validatebox" required="true" value="0" style="width:40px">是
								<input name="minValue" type="radio" class="easyui-validatebox" checked="checked" required="true" value="" style="width:40px">否
							</td>
							<td>统计数据:</td>
							<td><select class="easyui-combobox" name="dataStatType"
										style="width:100px;">
								<option value="ORIGINAL">原始数据</option>
								<option value="PERCENT">百分比</option>
							</select>
							</td>
							<td align="center">
								<a href="javascript:void(0)" class="easyui-linkbutton"
								   icon="icon-search" onclick="getJsonData()">统计</a> <a
									href="javascript:void(0)" class="easyui-linkbutton"
									icon="icon-cancel" onclick="resetSearchWindow()">重置</a>
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

</body>
<script type="text/javascript">
    $(function() {
        initSearchFormYear();
        getPlanConfigTree();
        getJsonData();
    });
    function getPlanConfigTree(){
        $('#userPlanList').combotree({
            url : '../userPlan/getPlanConfigTree',
            valueField : 'id',
            textField : 'text',
            loadFilter: function(data){
                return loadDataFilter(data);
            },
            onLoadSuccess:function(node,data){
                $('#userPlanList').combotree('setValue', { id: data[1].id, text: data[1].text });
            }
        });
    }
	function getJsonData() {
		var vurl = '../planReport/dateStat';
		var para = form2Json("search-window");
		doAjax(para, vurl, 'GET',false, function(data) {
			createLineData(data);
		});
	}
</script>
</html>
