<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>阅读分析</title>
	<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/echarts/echarts-all-3.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/mulanbaychart.js"></script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="center" border="true" title="阅读分析"
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
							<td>时间范围:</td>
							<td>
								<select class="easyui-combobox" name="dateQueryType"
										style="width:100px;">
									<option value="finishedDate">完成日期</option>
									<option value="beginDate">开始日期</option>
									<option value="proposedDate">期望完成日期</option>
								</select>
								<input class="easyui-datebox" name="startDate"
									   data-options="showSeconds:false" style="width:100px">--
								<input class="easyui-datebox" name="endDate" value="${endDate}"
									   data-options="showSeconds:false" style="width:100px">
							</td>
							<td>分组类型:</td>
							<td><select class="easyui-combobox" name="groupType"
										style="width:100px;">
								<option value="BOOKCATEGORY">图书分类</option>
								<option value="BOOKTYPE">书籍类型</option>
								<option value="LANGUAGE">语言</option>
								<option value="IMPORTANTLEVEL">重要程度</option>
								<option value="STATUS">状态</option>
								<option value="PUBLISHEDYEAR">出版年份</option>
								<option value="PRESS">出版社</option>
								<option value="NATION">国家</option>
							</select>
							</td>
							<td>状态:</td>
							<td>
								<select class="easyui-combobox" name="status"
										style="width:75px;">
									<option value="">全部</option>
									<option value="1">已读</option>
									<option value="0">未读</option>
								</select>
							</td>
							<td align="center" colspan="2">
								<a href="javascript:void(0)" class="easyui-linkbutton"
								   icon="icon-search" onclick="getJsonData()">统计</a> <a
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
        //initSearchFormYear();
        getJsonData();
    });
	function getJsonData() {
		var vurl = '../readingRecord/analyseStat';
		var para = form2Json("search-window");
		doAjax(para, vurl, 'GET',false, function(data) {
            createPieData(data)
		});
	}
</script>
</html>
