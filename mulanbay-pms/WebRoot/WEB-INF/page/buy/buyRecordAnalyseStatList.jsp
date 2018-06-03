<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>购买记录统计</title>
	<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/echarts/echarts-all-3.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/mulanbaychart.js"></script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="center" border="true" title="购买统计"
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
							<td>统计分组:</td>
							<td><select class="easyui-combobox" name="groupField"
								style="width:80px;">
									<option value="goods_type_id">商品类型</option>
									<option value="buy_type_id">购买来源</option>
									<option value="price_region">价格区间</option>
									<option value="sub_goods_type_id">商品子类</option>
									<option value="payment">支付方式</option>
									<option value="shop_name">店铺名称</option>
							</select>--
								<select class="easyui-combobox" name="type"
										style="width:70px;">
									<option value="TOTALPRICE">花费</option>
									<option value="COUNT">次数</option>
									<option value="SHIPMENT">运费</option>
								</select>
							</td>
							<td>起始日期:</td>
							<td><input class="easyui-datebox" name="startDate"
								data-options="showSeconds:false" style="width:120px">--
								<input class="easyui-datebox" name="endDate"
								data-options="showSeconds:false" style="width:120px"></td>
							</td>
							<td>名称检索:</td>
							<td><input class="easyui-validatebox" type="text"
									   name="name" style="width:150px;" /></td>
							<td>统计分类:</td>
							<td><select class="easyui-combobox" name="dateGroupType"
										style="width:110px;">
								<option value="MONTH">月</option>
								<option value="DAY">天</option>
								<option value="WEEK">周</option>
								<option value="YEAR">年</option>
								<option value="YEARMONTH">年月</option>
								<option value="DAYOFMONTH">天的序号</option>
								<option value="DAYOFWEEK">星期的序号</option>
							</select>
							</td>
						</tr>
						<tr>

							<td>购买来源:</td>
							<td><input id="buyTypeList" class="easyui-combotree" style="width:165px;"
									   data-options="editable:true" name="buyType">
							</td>
							<td>商品类型:</td>
							<td><input id="goodsTypeList" class="easyui-combotree" style="width:130px;"
									   data-options="editable:true" name="goodsType">
								<input id="subGoodsTypeList" class="easyui-combotree" style="width:128px;"
									   data-options="editable:true" name="subGoodsType">
							</td>
							<td>关键字:</td>
							<td>
								<input id="keywordsList" class="easyui-combotree" style="width:150px;"
									   data-options="editable:true" name="keywords">
							</td>
							</td>
							<td align="center" colspan="2">
								<select class="easyui-combobox" name="chartType"
										style="width:70px;">
									<option value="PIE">饼图</option>
									<option value="BAR">柱状图</option>
									<option value="RADAR">雷达图</option>
								</select>
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
        // 加载部门树
        getGoodsTypeTree();
        getBuyTypeTree();
        getKeywordsTree();
        initSearchFormYear();
        getJsonData();
    });
	function getJsonData() {
		var vurl = '/buyRecord/analyseStat';
		var para = form2Json("search-window");
		doAjax(para, vurl, 'GET',false, function(data) {
            if(para.chartType=='PIE'){
                createPieData(data);
            }else if(para.chartType=='BAR'){
                createBarData(data);
            }else{
                createRadarData(data);
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
    function getKeywordsTree(){
        $('#keywordsList').combotree({
            url : '../buyRecord/getKeywordsTree?needRoot=true',
            valueField : 'id',
            textField : 'text',
            loadFilter: function(data){
                return loadDataFilter(data);
            }
        });
    }
    function createRadarData(data) {
        var option = {
            title: {
                text: data.title,
                subtext: '纯属虚构',
                x:'right',
                y:'bottom'
            },
            tooltip: {
                trigger: 'item',
                backgroundColor : 'rgba(0,0,250,0.2)'
            },
            legend: {
                data: data.legendData
            },
            visualMap: {
                color: ['red', 'yellow']
            },
            radar: {
                indicator : data.indicatorData
            },
            series : (function (){
                var series = [];
                for(var i=0;i<data.series.length;i++){
                    series.push({
                        name: data.series[i].name,
                        type: 'radar',
                        symbol: 'none',
                        itemStyle: {
                            normal: {
                                lineStyle: {
                                    width:1
                                }
                            },
                            emphasis : {
                                areaStyle: {color:'rgba(0,250,0,0.3)'}
                            }
                        },
                        data:[
                            {
                                value: data.series[i].data,
                                name: data.series[i].name
                            }
                        ]
                    });
                }
                return series;
            })()
        };
        createChart(option);
    }
	
</script>
</html>
