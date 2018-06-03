<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>人生经历线路统计</title>
	<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/echarts/echarts-all-3.js"></script>
	<script type="text/javascript" src="/static/js/echarts/china.js"></script>
	<script type="text/javascript" src="/static/js/echarts/world.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/transferMapStat.js"></script>

</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="center" border="true" title="人生经历线路统计"
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
						<tr>
							<td>出发城市:</td>
							<td><input id="startCityList" class="easyui-combotree"
								   name="startCity" style="width:110px;"
								   data-options="required:true" missingMessage="出发城市">
							</td>
							</td>
							<td>起始日期:</td>
							<td><input class="easyui-datebox" name="startDate"
									   data-options="showSeconds:false" style="width:110px">--
								<input class="easyui-datebox" name="endDate"
									   data-options="showSeconds:false" style="width:110px"></td>
							</td>
							<td>类型:</td>
							<td>
								<select class="easyui-combobox" name="types"
										multiple="true" multiline="true" multivalue="true" separator="," style="width:80px;height:23px">
									<option value="LIVE">生活</option>
									<option value="WORK">工作</option>
									<option value="TRAVEL">旅行</option>
									<option value="STUDY">读书</option>
								</select>
							</td>
							<td>图表类型:</td>
							<td><select class="easyui-combobox" name="mapType"
										style="width:80px;">
								<option value="TRANSFER_DOUBLE">双向</option>
								<option value="TRANSFER_SINGLE">单向</option>
							</select>
							</td>
							<td align="center" colspan="2">
								<a href="javascript:void(0)" class="easyui-linkbutton"
								   icon="icon-search" onclick="getJsonData()">查询</a> <a
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
        getStartCityTree();
		getJsonData();
    });
    function getStartCityTree(){
        $('#startCityList').combotree({
            url : '../lifeExperience/getStartCityTree?needRoot=true',
            valueField : 'id',
            textField : 'text',
            loadFilter: function(data){
                return loadDataFilter(data);
            }
        });
        $('#startCityList').combotree('setValue', '杭州');
    }
	function createMapChart(data) {
        var myChart = echarts.init(document.getElementById('container'));

        // 指定图表的配置项和数据
        var option = data;

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
	}
	function getJsonData() {
		var vurl = '../lifeExperience/transferMapStat';
		var para = form2JsonEnhanced("search-window");
		doAjax(para, vurl, 'POST',false, function(data) {
            if(para.mapType=='TRANSFER_DOUBLE'){
                createDoubleTransferMap(data)
            }else{
                createSingleTransferMap(data)
            }
		});
	}

</script>
</html>
