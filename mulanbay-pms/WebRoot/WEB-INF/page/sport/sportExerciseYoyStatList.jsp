<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>运动锻炼同期对比</title>
	<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/echarts/echarts-all-3.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/mulanbaychart.js"></script>

</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="center" border="true" title="运动锻炼同期对比"
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
							<td>运动类型:</td>
							<td><input id="sportTypeList" class="easyui-combotree" style="width:70px;"
									   data-options="editable:true" name="sportTypeId"></td>
							</td>
							<td>年份:</td>
							<td><input id="yearList" class="easyui-combotree" style="width:150px;" multiple="true"
									   data-options="required:true,editable:true" name="years">
							</td>
							<td>统计分类:</td>
							<td><select class="easyui-combobox" name="dateGroupType"
										style="width:50px;">
								<option value="MONTH">月</option>
								<option value="WEEK">周</option>
								<option value="DAY">天</option>
							</select>
							</td>
							<td>统计分组:</td>
							<td>
								<select class="easyui-combobox" name="groupType"
										style="width:80px;">
									<option value="COUNT">次数</option>
									<option value="KILOMETRES">公里数</option>
									<option value="MINUTES">锻炼时间</option>
									<option value="SPEED">平均速度</option>
									<option value="PACE">配速</option>
									<option value="MAXHEARTRATE">最大心率</option>
									<option value="AVERAGEHEART">平均心率</option>

								</select>
							</td>
							<td>数据类型:</td>
							<td>
								<input name="sumValue" type="radio" class="easyui-validatebox" required="true" value="true" style="width:30px" checked="checked" >总计
								<input name="sumValue" type="radio" class="easyui-validatebox" required="true" value="false" style="width:30px">平均值
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
			<div region="center" border="true" title="结果" split="true" style="background: #eee;overflow:hidden;">
				<div id="container"
					style="min-width: 400px; height: 100%; margin: 0 auto"></div>
			</div>
		</div>
	</div>

</body>
<script type="text/javascript">
    $(function() {
        getYearTree();
        initForm();
        //getJsonData();
    });
	function getJsonData() {
		var vurl = '../sportExercise/yoyStat';
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
    function initForm(){
        $('#sportTypeList').combotree({
            url : '../sportType/getSportTypeTree',
            valueField : 'id',
            textField : 'text',
            loadFilter: function(data){
                return loadDataFilter(data);
            }
        });
        $('#sportTypeList').combotree('setValue', 1);
    }

</script>
</html>
