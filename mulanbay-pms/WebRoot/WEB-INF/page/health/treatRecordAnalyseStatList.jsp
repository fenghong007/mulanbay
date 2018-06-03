<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>看病记录天统计</title>
	<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/echarts/echarts-all-3.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/mulanbaychart.js"></script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="center" border="true" title="看病记录统计"
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
								<option value="diagnosed_disease">确诊疾病</option>
								<option value="organ">器官</option>
								<option value="hospital">医院</option>
								<option value="department">科室</option>
								<option value="disease">疾病</option>
								</select>
								<select class="easyui-combobox" name="groupType"
										style="width:55px;">
									<option value="COUNT">次数</option>
									<option value="TOTALPRICE">费用</option>
								</select>
								<select class="easyui-combobox" name="feeField"
										style="width:90px;">
									<option value="total_fee">总费用</option>
									<option value="medical_insurance_paid_fee">医保花费</option>
									<option value="personal_paid_fee">个人支付费用</option>
									<option value="operation_fee">手术费用</option>
									<option value="drug_fee">药费</option>
								</select>
							</td>
							<td>图表类型:</td>
							<td><select class="easyui-combobox" name="chartType"
										style="width:90px;">
								<option value="PIE">饼图</option>
								<option value="BAR">柱状图</option>
							</select>
							</td>
						</tr>
						<tr>
							<td>关键字:</td>
							<td><input class="easyui-validatebox"
									   type="textarea" name="name" style="width: 220px;"></input></td>
							<td>是否有病:</td>
							<td>
								<input name="sick" type="radio" class="easyui-validatebox" required="false" value="" checked="checked" style="width:30px">全部
								<input name="sick" type="radio" class="easyui-validatebox" required="false" value="true" style="width:30px">是
								<input name="sick" type="radio" class="easyui-validatebox" required="false" value="false" style="width:30px">否
							</td>
							<td>
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

</body>
<script type="text/javascript">
    $(function() {
        initSearchFormYear();
        getJsonData();
    });
	function getJsonData() {
		var vurl = '../treatRecord/analyseStat';
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
