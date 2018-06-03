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
	<div region="center" border="true" title="音乐练习统计"
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
							<td>乐器:</td>
							<td><input id="musicInstrumentList" class="easyui-combotree" multiple="true"
									   data-options="editable:true" style="width:120px" name="musicInstrumentIds"></td>
							<td>起始日期:</td>
							<td><input class="easyui-datebox" name="startDate"
									   data-options="showSeconds:false" style="width:110px">--
								<input class="easyui-datebox" name="endDate"
									   data-options="showSeconds:false" style="width:110px"></td>
							</td>
							<td>X轴数据:</td>
							<td><select class="easyui-combobox" name="xgroupType" id="xDateGroupTypeList"
										style="width:120px;">
								<option value="DAYOFWEEK">星期的序号</option>
								<option value="HOURMINUTE">时分</option>
								<option value="HOUR">小时点</option>
								<option value="MINUTE">练习时长</option>
								<option value="DAYOFMONTH">天的序号</option>
								<option value="MONTH">月</option>
								<option value="DAY">天</option>
								<option value="DAYCALENDAR">天(日历)</option>
								<option value="WEEK">周</option>
								<option value="YEAR">年</option>
								<option value="YEARMONTH">年月</option>
							</select>
							</td>
							<td>Y轴数据:</td>
							<td><select class="easyui-combobox" name="ygroupType" id="yDateGroupTypeList"
										style="width:120px;">
								<option value="HOUR">小时点</option>
								<option value="HOURMINUTE">时分</option>
								<option value="MINUTE">练习时长</option>
								<option value="DAYOFMONTH">天的序号</option>
								<option value="DAYOFWEEK">星期的序号</option>
								<option value="MONTH">月</option>
								<option value="DAY">天</option>
								<option value="DAYCALENDAR">天(日历)</option>
								<option value="WEEK">周</option>
								<option value="YEAR">年</option>
								<option value="YEARMONTH">年月</option>
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
        initMusicInstrument();
        //getJsonData();
    });
    function initMusicInstrument(){
        $('#musicInstrumentList').combotree({
            url : '../musicInstrument/getMusicInstrumentTree?needRoot=false',
            valueField : 'id',
            textField : 'text',
            loadFilter: function(data){
                return loadDataFilter(data);
            }
        });
    }
	function getJsonData() {
		var vurl = '../musicPractice/compareStat';
		var para = form2JsonEnhanced("search-window");
		doAjax(para, vurl, 'GET',false, function(data) {
            createScatterData(data);
		});
	}
</script>
</html>
