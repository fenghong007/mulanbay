<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>购买记录统计</title>
	<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/echarts/echarts-all-3.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/mulanbaychart.js"></script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="center" border="true" title="梦想统计"
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
							<td>
								<select class="easyui-combobox" name="dateQueryType"
										style="width:100px;">
									<option value="proposed_date">期望实现日期</option>
									<option value="finished_date">完成日期</option>
									<option value="deadline">最晚截止日期</option>
									<option value="created_time">创建日期</option>
								</select>
								<input class="easyui-datebox" name="startDate"
									   data-options="showSeconds:false" style="width:100px">--
								<input class="easyui-datebox" name="endDate"
									   data-options="showSeconds:false" style="width:100px">
							</td>
							<td>分组类型:</td>
							<td><select class="easyui-combobox" name="groupType"
										style="width:90px;">
								<option value="STATUS">状态</option>
								<option value="DIFFICULTY">困难程度</option>
								<option value="IMPORTANTLEVEL">重要程度</option>
							</select>
							</td>
							<td>图表类型:</td>
							<td><select class="easyui-combobox" name="chartType"
										style="width:90px;">
								<option value="PIE">饼图</option>
								<option value="BAR">柱状图</option>
							</select>
							</td>
							<td>
								<a href="javascript:void(0)" class="easyui-linkbutton"
								   icon="icon-search" onclick="getJsonData()">统计</a> <a
									href="javascript:void(0)" class="easyui-linkbutton"
									icon="icon-cancel" onclick="resetMySearchWindow()">重置</a>
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
        initSearchForm();
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
            startDate: getYear(0)+'-01-01',
			endDate: getYear(0)+'-12-31'
        };
        $('#search-window').form('load', formData);
    }
	function getJsonData() {
		var vurl = '/dream/statList';
		var para = form2Json("search-window");
		doAjax(para, vurl, 'GET',false, function(data) {
            if(para.chartType=='PIE'){
                createPieData(data);
            }else{
                createBarData(data);
            }
		});
	}
</script>
</html>
