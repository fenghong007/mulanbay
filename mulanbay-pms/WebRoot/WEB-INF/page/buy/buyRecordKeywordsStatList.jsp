<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>关键字统计</title>
	<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/echarts/echarts-all-3.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/mulanbaychart.js"></script>

</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="center" border="true" title="关键字统计"
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
							<td><input class="easyui-datebox" name="startDate"
									   data-options="showSeconds:false" style="width:120px">
								--
								<input class="easyui-datebox" name="endDate"
									   data-options="showSeconds:false" style="width:120px"></td>
							</td>
							<td>商品类型:</td>
							<td><input id="goodsTypeList" class="easyui-combotree" style="width:130px;"
									   data-options="editable:true" name="goodsType">
								<input id="subGoodsTypeList" class="easyui-combotree" style="width:128px;"
									   data-options="editable:true" name="subGoodsType">
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
        //initSearchFormYear();
        getGoodsTypeTree();
        getJsonData();
    });
	function getJsonData() {
		var vurl = '../buyRecord/keywordsStat';
		var para = form2Json("search-window");
		doAjax(para, vurl, 'GET',false, function(data) {
			createBarData(data);
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
