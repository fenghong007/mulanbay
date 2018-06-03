<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>音乐练习曲子统计</title>
<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/mulanbay/musicPracticeTune.js"></script>
	<script type="text/javascript" src="/static/js/echarts/echarts-all-3.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/mulanbaychart.js"></script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="center" border="true" title="音乐练习曲子统计"
		style="border-left:0px;border-right:0px; height: 80px">
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
							<td><input id="musicInstrumentList" class="easyui-combotree"
									   data-options="editable:true" style="width:120px" name="musicInstrumentId"></td>
							<td>曲子名称:</td>
							<td><input class="easyui-validatebox"
									   type="textarea" name="tuneName" style="width:99%;"></input>
							</td>
							</td>
							<td>起始日期:</td>
							<td><input class="easyui-datebox" name="startDate"
									   data-options="showSeconds:false" style="width:110px">--
								<input class="easyui-datebox" name="endDate"
									   data-options="showSeconds:false" style="width:110px"></td>
							</td>
							<td>练习类型:</td>
							<td>
								<select class="easyui-combobox" name="tuneType"
										style="width:70px;">
									<option value="">全部</option>
									<option value="TUNE">曲子</option>
									<option value="TECH">技术</option>
								</select>
							</td>
							<td align="center">
								<a href="javascript:void(0)" class="easyui-linkbutton"
								   icon="icon-search" onclick="getJsonData()">统计</a> <a
									href="javascript:void(0)" class="easyui-linkbutton"
									icon="icon-cancel" onclick="resetAndInitSearchWindow()">重置</a>
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
        initSearchForm();
        initMusicInstrument();
        getJsonData();
    });
    function initMusicInstrument(){
        $('#musicInstrumentList').combotree({
            url : '../musicInstrument/getMusicInstrumentTree?needRoot=true',
            valueField : 'id',
            textField : 'text',
            loadFilter: function(data){
                return loadDataFilter(data);
            }
        });
        //默认为口琴
        $('#musicInstrumentList').combotree('setValue', 1);
    }
    function getJsonData() {
        var vurl = '/musicPracticeTune/stat';
        var para = form2Json("search-window");
        doAjax(para, vurl, 'GET',false, function(data) {
			createBarData(data);
        });
    }

</script>
</html>
