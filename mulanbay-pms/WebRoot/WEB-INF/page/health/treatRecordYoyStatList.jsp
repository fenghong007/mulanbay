<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>看病同期对比</title>
	<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/echarts/echarts-all-3.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/mulanbaychart.js"></script>

</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="center" border="true" title="看病同期对比"
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
							<td>年份:</td>
							<td><input id="yearList" class="easyui-combotree" style="width:230px;" multiple="true"
									   data-options="required:true,editable:true" name="years">
							</td>
							<td>统计分类:</td>
							<td><select class="easyui-combobox" name="dateGroupType"
										style="width:100px;">
								<option value="MONTH">月</option>
								<option value="DAY">天</option>
							</select>
							</td>
							<td>统计分组:</td>
							<td>
								<select class="easyui-combobox" name="groupType"
										style="width:70px;">
									<option value="TOTALPRICE">费用</option>
									<option value="COUNT">次数</option>
								</select>
							</td>
						</tr>
						<tr>
							<td>关键字:</td>
							<td><input class="easyui-validatebox"
									   type="textarea" name="name" style="width: 230px;"></input></td>
							<td>统计字段:</td>
							<td>
								<select class="easyui-combobox" name="feeField"
										style="width:100px;">
									<option value="total_fee">总费用</option>
									<option value="medical_insurance_paid_fee">医保花费</option>
									<option value="personal_paid_fee">个人支付费用</option>
									<option value="operation_fee">手术费用</option>
									<option value="drug_fee">药费</option>
								</select>
							</td>
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
			<div region="center" border="true" title="结果" split="true" >
				<div id="container"
					style="min-width: 400px; height: 100%; margin: 0 auto"></div>
			</div>
		</div>
	</div>

</body>
<script type="text/javascript">
    $(function() {
        getYearTree();
        //getJsonData();
    });
	function getJsonData() {
		var vurl = '../treatRecord/yoyStat';
		var para = form2JsonEnhanced("search-window");
		doAjax(para, vurl, 'GET',false, function(data) {
            if(para.dateGroupType=='DAY'){
                createCalanderHeatMapData(data);
            }else{
                createLineData(data);
            }
        });
	}
    function getYearTree(){
        $('#yearList').combotree({
            url : '../common/getYearTree',
            valueField : 'id',
            textField : 'text',
            loadFilter: function(data){
                return loadDataFilter(data);
            }
        });
    }
</script>
</html>
