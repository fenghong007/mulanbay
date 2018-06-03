<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>用户饮食习惯分析</title>
	<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/echarts/echarts-all-3.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/mulanbaychart.js"></script>

</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="center" border="true" title="用户饮食习惯分析"
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
							<td>类型:</td>
							<td>
								<input name="dateGroupType" type="radio" class="easyui-validatebox" required="true" value="YEAR" style="width:30px">年
								<input name="dateGroupType" type="radio" class="easyui-validatebox" required="true" checked="checked" value="MONTH" style="width:30px">月
							</td>
							<td>选择日期:</td>
							<td><input id="yearList" class="easyui-combotree" style="width:110px;"
									   data-options="required:true,editable:true" name="year">
								<select class="easyui-combobox" name="month" id="monthList"
										style="width:85px;">
									<option value="01">1月份</option>
									<option value="02">2月份</option>
									<option value="03">3月份</option>
									<option value="04">4月份</option>
									<option value="05">5月份</option>
									<option value="06">6月份</option>
									<option value="07">7月份</option>
									<option value="08">8月份</option>
									<option value="09">9月份</option>
									<option value="10">10月份</option>
									<option value="11">11月份</option>
									<option value="12">12月份</option>
								</select>
							</td>
							<td>饮食类型:</td>
							<td>
								<select class="easyui-combobox" name="dietType"
										style="width:120px;height:23px" >
									<option value="">全部</option>
									<option value="BREAKFAST">早餐</option>
									<option value="LUNCH">午餐</option>
									<option value="DINNER">晚餐</option>
									<option value="OTHER">其他</option>
								</select>
							</td>
							<td align="center">
								<a href="javascript:void(0)" class="easyui-linkbutton"
								   icon="icon-search" onclick="getJsonData()">统计</a> <a
									href="javascript:void(0)" class="easyui-linkbutton"
									icon="icon-cancel" onclick="resetMySearchWindow()">重置</a>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div region="center" border="true" title="结果" split="true" style="background:gainsboro;overflow-y: hidden">
				<div id="container"
					style="min-width: 400px;width: 100%; height: 100%; margin: 0 auto"></div>
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
            startDate: getDay()
        };
        $('#search-window').form('load', formData);
        $('#yearList').combotree({
            url : '../common/getYearTree',
            valueField : 'id',
            textField : 'text',
            loadFilter: function(data){
                return loadDataFilter(data);
            }
        });
        var year =getYear(0);
        var dd = new Date();
        var month=dd.getMonth()+1;
        if(month<10){
            month="0"+month;
		}
        $('#yearList').combotree('setValue', year);
        $('#monthList').combobox('setValue', month);
    }
    function getJsonData() {
		var vurl = '../diet/stat';
		var para = form2Json("search-window");
		doAjax(para, vurl, 'GET',false, function(data) {
		    if(para.dateGroupType=='YEAR'){
                createCompareCalanderData(data);
            }else{
                createCalanderPieDataEnhanced(data,"container");
            }
		});
	}

</script>
</html>
