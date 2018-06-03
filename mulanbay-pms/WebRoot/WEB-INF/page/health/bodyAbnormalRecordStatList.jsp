<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>身体不适统计</title>
	<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/echarts/echarts-all-3.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/mulanbaychart.js"></script>

</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="center" border="true" title="身体不适统计"
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
								<input class="easyui-datebox" name="startDate"
									   data-options="showSeconds:false" style="width:100px">--
								<input class="easyui-datebox" name="endDate" value="${endDate}"
									   data-options="showSeconds:false" style="width:100px">
							</td>
							<td>分组类型:</td>
							<td><select class="easyui-combobox" name="groupField"
										style="width:90px;">
								<option value="DISEASE">疾病</option>
								<option value="ORGAN">器官</option>
								<option value="PAINLEVEL">疼痛级别</option>
								<option value="IMPORTANTLEVEL">重要等级</option>
								<option value="LASTDAYS">持续天数</option>
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
	function createChart(data) {
        var myChart = echarts.init(document.getElementById('container'));

        // 指定图表的配置项和数据
        var option = data;

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
	}
	function getJsonData() {
		var vurl = '../bodyAbnormalRecord/stat';
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
