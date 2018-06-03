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
				style="overflow:auto;height:110px;" data-options="selected:false"
				icon="icon-edit" class="yhbackground">
				<form id="search-window" method="post">
					<table cellpadding="5" class="tableForm" style="width: 100%;">
						<tr>
							<td>乐器:</td>
							<td><input id="musicInstrumentList" class="easyui-combotree"
									   data-options="editable:true" style="width:120px" name="musicInstrumentId"></td>
							<td>起始日期:</td>
							<td><input class="easyui-datebox" name="startDate"
									   data-options="showSeconds:false" style="width:110px">--
								<input class="easyui-datebox" name="endDate"
									   data-options="showSeconds:false" style="width:110px"></td>
							</td>
							<td>补全日期:</td>
							<td>
								<input name="compliteDate" type="radio" class="easyui-validatebox" required="true" value="true" style="width:30px">是
								<input name="compliteDate" type="radio" class="easyui-validatebox" checked="checked"  required="true" value="false" style="width:30px">否
							</td>
							<td>图表类型:</td>
							<td><select class="easyui-combobox" name="chartType"
										style="width:80px;">
								<option value="BAR">柱状图</option>
								<option value="LINE">折线图</option>
							</select>
							</td>
						</tr>
						<tr>
							<td>统计分类:</td>
							<td><select class="easyui-combobox" name="dateGroupType" id="dateGroupTypeList"
										style="width:120px;">
								<option value="MONTH">月</option>
								<option value="DAY">天</option>
								<option value="DAYCALENDAR">天(日历)</option>
								<option value="WEEK">周</option>
								<option value="YEAR">年</option>
								<option value="YEARMONTH">年月</option>
							</select>
							</td>
							<td>跟踪曲子/技术:</td>
							<td>
								<select class="easyui-combobox" name="tuneType" id="tuneTypeList"
										style="width:60px;">
									<option value="TUNE">曲子</option>
									<option value="TECH">技术</option>
								</select>
								<input id="tuneList" class="easyui-combobox" style="width: 175px;"
									   data-options="editable:true" name="tune" />
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
        $('#dateGroupTypeList').combobox({
            onChange : function (newValue, oldValue) {
                if(newValue=='DAYCALENDAR'){
                    $('#tuneTypeList').combobox('setValue','TUNE');
                    loadTuneList('TUNE');
                }
            }
        });
        $('#tuneTypeList').combobox({
            onChange : function (newValue, oldValue) {
                loadTuneList(newValue);
            }
        });
        setTimeout(getJsonData,1000);
    });

    function loadTuneList(tuneType) {
        var para=form2Json("search-window");
        var listPara = {
            tuneType:tuneType,
            startDate:para.startDate,
            endDate : para.endDate,
            musicInstrumentId:para.musicInstrumentId
        };
        $('#tuneList').combobox({
            url : '../musicPracticeTune/getMusicPracticeTuneTree',
            valueField : 'id',
            textField : 'text',
            queryParams: listPara,
            loadFilter: function(data){
                return loadDataFilter(data);
            }
        });
    }
    function initMusicInstrument(){
        $('#musicInstrumentList').combotree({
            url : '../musicInstrument/getMusicInstrumentTree',
            valueField : 'id',
            textField : 'text',
            loadFilter: function(data){
                return loadDataFilter(data);
            },
            onLoadSuccess:function(node,data){
                $('#musicInstrumentList').combotree('setValue', { id: data[0].id, text: data[0].text });
            }
        });
    }
	function getJsonData() {
		var vurl = '../musicPractice/dateStat';
		var para = form2Json("search-window");
		doAjax(para, vurl, 'GET',false, function(data) {
            if(para.dateGroupType=='DAYCALENDAR'){
                createCalanderData(data);
            }else if(para.chartType=='BAR'){
                createBarData(data);
            }else{
                createLineData(data);
            }
		});
	}
</script>
</html>
