<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>消费记录同期对比</title>
	<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/echarts/echarts-all-3.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/mulanbaychart.js"></script>

</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="center" border="true" title="消费记录同期对比"
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
							<td><input id="yearList" class="easyui-combotree" style="width:260px;" multiple="true"
									   data-options="required:true,editable:true" name="years">
							</td>
							<td>统计分类:</td>
							<td><select class="easyui-combobox" name="dateGroupType"
										style="width:110px;">
								<option value="MONTH">月</option>
								<option value="WEEK">周</option>
								<option value="DAY">天</option>
							</select>
							</td>
							<td>统计分组:</td>
							<td>
								<select class="easyui-combobox" name="groupType"
										style="width:110px;">
									<option value="TOTALPRICE">花费</option>
									<option value="COUNT">次数</option>
								</select>
							</td>
							<td>价格区间:</td>
							<td><input id="startTotalPrice" class="easyui-numberbox"
									   name="startTotalPrice" style="width:70px;"
									   data-options="min:0,precision:2">
								--
								<input id="endTotalPrice" class="easyui-numberbox"
									   name="endTotalPrice" style="width:70px;"
									   data-options="min:0,precision:2">
							</td>
						</tr>
						<tr>
							<td>商品类型:</td>
							<td><input id="goodsTypeList" class="easyui-combotree" style="width:130px;"
									   data-options="editable:true" name="goodsType">
								<input id="subGoodsTypeList" class="easyui-combotree" style="width:128px;"
									   data-options="editable:true" name="subGoodsType">
							</td>
							<td>购买来源:</td>
							<td><input id="buyTypeList" class="easyui-combotree" style="width:110px;"
									   data-options="editable:true" name="buyType">
							</td>
							<td>是否二手:</td>
							<td><select class="easyui-combobox" name="secondhand" id="secondhandList"
										style="width:110px;">
								<option value="">全部</option>
								<option value="false">否</option>
								<option value="true">是</option>
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
        getYearTree();
        getGoodsTypeTree();
        getBuyTypeTree();
        //getJsonData();
    });
	function getJsonData() {
		var vurl = '../buyRecord/yoyStat';
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
    function getGoodsTypeTree(){
        $('#goodsTypeList').combotree({
            url : '/goodsType/getGoodsTypeTree?needRoot=true&pid=0',
            valueField : 'id',
            textField : 'text',
            loadFilter: function(data){
                return loadDataFilter(data);
            },
            onChange : function (newValue, oldValue) {
                loadSubGoodsTypeList(newValue);
            }
        });
    }
    function getBuyTypeTree(){
        $('#buyTypeList').combotree({
            url : '/buyType/getBuyTypeTree?needRoot=true',
            valueField : 'id',
            textField : 'text',
            loadFilter: function(data){
                return loadDataFilter(data);
            }
        });
    }
    function loadSubGoodsTypeList(pid){
        $('#subGoodsTypeList').combotree({
            url : '../goodsType/getGoodsTypeTree?pid='+pid,
            valueField : 'id',
            textField : 'text',
            loadFilter: function(data){
                return loadDataFilter(data);
            }
        });
    }
</script>
</html>
