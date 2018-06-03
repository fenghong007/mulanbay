<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>用户积分统计</title>
	<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/echarts/echarts-all-3.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/mulanbaychart.js"></script>

</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="center" border="true" title="用户积分统计"
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
							<td>统计分类:</td>
							<td><select class="easyui-combobox" name="dateGroupType" id="dateGroupTypeList"
										style="width:120px;">
								<option value="">时间线</option>
								<option value="MONTH">月</option>
								<option value="DAY">天</option>
								<option value="DAYCALENDAR">天(日历)</option>
								<option value="WEEK">周</option>
								<option value="YEAR">年</option>
								<option value="YEARMONTH">年月</option>
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
		var vurl = '../userRewardPointRecord/pointsTimelineStat';
		var para = form2Json("search-window");
		doAjax(para, vurl, 'GET',false, function(data) {
            if(para.dateGroupType=='DAYCALENDAR'){
                createCompareCalanderData(data);
            }else{
                createLineData(data);
            }
		});
	}


</script>
</html>
