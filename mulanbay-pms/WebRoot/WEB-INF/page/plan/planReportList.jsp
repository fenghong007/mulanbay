<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>计划执行报告</title>
	<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/mulanbay/planCommon.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/planReport.js"></script>
	<script type="text/javascript" src="/static/js/echarts/echarts-all-3.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/mulanbaychart.js"></script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="center" border="true" title="计划执行报告"
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
							<td>选择计划:</td>
							<td><input id="userPlanList" class="easyui-combotree" style="width:160px"
									   data-options="editable:true" name="userPlanId"></td>
							</td>
							<td>起始日期:</td>
							<td><input class="easyui-datebox" name="startDate"
									   data-options="showSeconds:false" style="width:100px">--
								<input class="easyui-datebox" name="endDate"
									   data-options="showSeconds:false" style="width:100px"></td>
							</td>
							<td>排序方式:</td>
							<td><select class="easyui-combobox" name="sortField"
										style="width:100px;">
								<option value="bussStatDate">业务日期</option>
								<option value="createdTime">统计时间</option>
								<option value="bussDay">业务日期索引</option>
							</select>
								<select class="easyui-combobox" name="sortType"
										style="width:70px;">
									<option value="desc">倒序</option>
									<option value="asc">顺序</option>
								</select></td>
						</tr>
						<tr>
							<td>计划类型:</td>
							<td><select class="easyui-combobox" name="planType"
										style="width:160px;">
								<option value="">全部</option>
								<option value="MONTH">月</option>
								<option value="YEAR">年</option>
								<option value="DAY">天</option>
								<option value="WEEK">周</option>
								<option value="SEASON">季度</option>
							</select></td>
							<td>统计结果:</td>
							<td>
								<select class="easyui-combobox" name="countValueStatResults"
										multiple="true" multiline="true" multivalue="true" separator="," style="width:100px;height:23px">
									<option value="ACHIEVED">已实现</option>
									<option value="UN_ACHIEVED">未实现</option>
									<option value="SKIP">忽略</option>
								</select>(次数)
								<select class="easyui-combobox" name="valueStatResults"
										   multiple="true" multiline="true" multivalue="true" separator="," style="width:100px;height:23px">
								<option value="ACHIEVED">已实现</option>
								<option value="UN_ACHIEVED">未实现</option>
								<option value="SKIP">忽略</option>
							</select>(值)
							</td>
							<td>过滤全零数据:</td>
							<td>
								<input name="minValue" type="radio" class="easyui-validatebox" required="true" value="0" style="width:40px">是
								<input name="minValue" type="radio" class="easyui-validatebox" checked="checked" required="true" value="" style="width:40px">否
							</td>
							<td align="center">
								<a href="javascript:void(0)" class="easyui-linkbutton"
								   icon="icon-search" onclick="showAll()">查询</a>
								<a href="javascript:void(0)" class="easyui-linkbutton"
									icon="icon-cancel" onclick="resetSearchWindow()">重置</a>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div region="center" border="true" title="计划执行报告列表" split="true"
				 style="background:#E2E377;overflow-y: hidden">
				<div id="grid" fit="true"></div>
			</div>
		</div>
	</div>

	<div id="manual-stat-window" title="手动统计计划报告" class="easyui-window"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div class="yhdiv">
			<form id="manual-stat-form" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>计划:</td>
						<td><input id="userPlanList2" class="easyui-combotree" style="width:220px"
								   data-options="editable:true" name="userPlanId"></td>
						</td>
					</tr>
					<tr>
						<td>计划类型:</td>
						<td><select class="easyui-combobox" name="planType"
									style="width:100px;">
							<option value="">全部</option>
							<option value="MONTH">月</option>
							<option value="YEAR">年</option>
							<option value="DAY">天</option>
							<option value="WEEK">周</option>
							<option value="SEASON">季度</option>
						</select></td>
					</tr>
					<tr>
						<td>起始日期:</td>
						<td><input class="easyui-datebox" name="startDate"
								   data-options="showSeconds:false" style="width:100px">--
							<input class="easyui-datebox" name="endDate"
								   data-options="showSeconds:false" style="width:100px"></td>
						</td>
					</tr>
					<tr>
						<td>统计类型:</td>
						<td>
							<input name="statType" type="radio" class="easyui-validatebox" required="true" value="RE_STAT" style="width:30px">重新统计
							<input name="statType" type="radio" class="easyui-validatebox" checked="checked" required="true" value="STAT_MISS" style="width:30px">统计遗漏
						</td>
					</tr>
				</table>
				<div class="submitForm">
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-stat" onclick="manualStat()">统计数据</a> &nbsp;
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-stat" onclick="cleanSelect()">删除数据</a> &nbsp;
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-cancel" onclick="closeWindow('manual-stat-window')">关闭</a>
				</div>
			</form>
		</div>
	</div>

	<div id="stat-window" title="统计" class="easyui-window"
		 style="width:970px;height:550px;"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div region="north" border="true" title="查询条件" split="true"
			 style="overflow:auto;height:50px;" data-options="selected:false"
			 icon="icon-edit" class="yhbackground">
			<form id="stat-search-form" method="post">
				<table cellpadding="5" class="tableForm" style="width: 100%;">
					<tr>
						<td>选择计划:</td>
						<td><input id="userPlanList4" class="easyui-combotree" style="width:160px"
								   data-options="editable:true" name="userPlanId"></td>
						</td>
						<td>
							起始日期:
						</td>
						<td>
							<input class="easyui-datebox" name="startDate"
								   data-options="showSeconds:false" style="width:100px">--
							<input class="easyui-datebox" name="endDate"
								   data-options="showSeconds:false" style="width:100px">
						</td>
						<td>计划类型:</td>
						<td><select class="easyui-combobox" name="planType"
									style="width:60px;">
							<option value="">全部</option>
							<option value="MONTH">月</option>
							<option value="YEAR">年</option>
							<option value="DAY">天</option>
							<option value="WEEK">周</option>
							<option value="SEASON">季度</option>
						</select></td>
						<td align="center" colspan="2">
							<a href="javascript:void(0)" class="easyui-linkbutton"
							   icon="icon-search" onclick="stat()">查询</a>
						</td>
					</tr>
				</table>
				<input type="hidden" name='status' value="1"/>
			</form>
		</div>
		<div region="center" border="true" title="统计" split="true"
			 style="overflow-y: hidden;width:auto;height:auto;">
			<div class="yhdiv">
				<table>
					<tr>
						<td>
							<div region="center" border="true" title="结果" split="true">
								<div id="container"
									 style="min-width: 500px; height: 400px; margin: 0 auto"></div>
							</div>
						</td>
						<td>
							<form id="stat-form" method="post">
								<table cellpadding="5" class="tableForm">
									<tr>
										<td align=center>名称:</td>
										<td align=center>值</td>
										<td align=center>计划期望值</td>
									</tr>
									<tr>
										<td>总次数</td>
										<td><input class="easyui-numberspinner" data-options="min:0,precision:0"
												   name="reportCountValueSum" style="width:100px;color:red"></td>
										<td><input class="easyui-numberspinner" data-options="min:0,precision:0"
												   name="planCountValueExpectSum" style="width:100px;color:red"></td>
									</tr>
									<tr>
										<td>总值</td>
										<td><input name="reportValueSum" class="easyui-numberspinner" data-options="min:0,precision:0"
												   style="width:100px;color:red"></td>
										<td><input class="easyui-numberspinner" data-options="min:0,precision:0"
												   name="planValueExpectSum" style="width:100px;color:red"></td>
									</tr>
									<tr>
										<td>次数的平均值</td>
										<td><input name="averageReportCountValue" class="easyui-numberspinner" data-options="min:0,precision:1"
												   style="width:100px;color:red"></td>
										<td><input name="planCountValue" class="easyui-numberspinner" data-options="min:0,precision:0"
												   style="width:100px;"></td>
									</tr>
									<tr>
										<td>值的平均值</td>
										<td><input name="averageReportValue" class="easyui-numberspinner" data-options="min:0,precision:1"
												   style="width:100px;color:red"></td>
										<td><input name="planValue"  class="easyui-numberspinner" data-options="min:0,precision:0"
												   style="width:100px;"></td>
									</tr>
									<tr>
										<td>值/次的平均值</td>
										<td><input name="averageValue" class="easyui-numberspinner" data-options="min:0,precision:2"
												   style="width:100px;color:red"></td>
										<td><font color="red">平均每次计划相差</font></td>
									</tr>
									<tr>
										<td><font color="red">总次数相差</font></td>
										<td><input class="easyui-numberspinner"
												   name="diffCountValueSum" style="width:100px;color:red"></td>
										<td><input class="easyui-numberspinner" data-options="precision:1"
												   name="avgDiffCountValue" style="width:100px;color:red"></td>
									</tr>
									<tr>
										<td><font color="red">总值相差</font></td>
										<td><input name="diffValueSum" class="easyui-numberspinner"
												   style="width:100px;color:red"></td>
										<td><input class="easyui-numberspinner" data-options="precision:1"
												   name="avgDiffValue" style="width:100px;color:red"></td>
									</tr>
								</table>
								<br>
								当前数据总条数:&nbsp;&nbsp;<input name="totalCount" class="easyui-textbox"
														   style="width:50px;color:red">
								&nbsp;&nbsp;
								计划期望值参考年份:&nbsp;&nbsp;<input name="year" class="easyui-textbox"
															 style="width:50px;color:red">
								<br>
								<br>
								计划期望值单位:&nbsp;&nbsp;<input name="unit" class="easyui-textbox"
														   style="width:50px;color:red">
								<br>
								<br>
								相差值:正数表示正常(高于预期)，负数表示异常(低于预期)
								<div class="submitForm">
									<a href="javascript:void(0)" class="easyui-linkbutton"
									   icon="icon-cancel" onclick="closeWindow('stat-window')">关闭</a>
								</div>
							</form>
						</td>
					</tr>
				</table>

			</div>
		</div>
	</div>

	<div id="clean-data-window" title="清理数据" class="easyui-window"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div class="yhdiv">
			<form id="clean-data-form" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>选择计划:</td>
						<td><input id="planConfigList3" class="easyui-combotree" style="width:260px"
								   data-options="editable:true" name="planConfigId"></td>
						</td>
					</tr>
					<tr>
						<td>起始日期:</td>
						<td><input class="easyui-datebox" name="startDate"
								   data-options="showSeconds:false" style="width:120px">--
							<input class="easyui-datebox" name="endDate"
								   data-options="showSeconds:false" style="width:120px"></td>
						</td>
					</tr>
					<tr>
						<td>清理类型:</td>
						<td>
							<input name="cleanType" type="radio" class="easyui-validatebox" required="true" value="ALL" style="width:30px">无条件
							<input name="cleanType" type="radio" class="easyui-validatebox" checked="checked" required="true" value="BOTH_ZERO" style="width:30px">全为零
							<input name="cleanType" type="radio" class="easyui-validatebox" required="true" value="ONCE_ZERO" style="width:30px">其中一个为零
						</td>
					</tr>
				</table>
				<div class="submitForm">
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-stat" onclick="cleanData()">清理</a> &nbsp;
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-cancel" onclick="closeWindow('clean-data-window')">关闭</a>
				</div>
			</form>
		</div>
	</div>

	<div id="re-stat-window" title="重新统计" class="easyui-window"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div class="yhdiv">
			<form id="re-stat-form" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>数据比对规则:</td>
						<td>
							<input name="reStatCompareType" type="radio" class="easyui-validatebox" checked="checked" required="true" value="ORIGINAL" style="width:30px">原始数据
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<input name="reStatCompareType" type="radio" class="easyui-validatebox" required="true" value="ORIGINAL_LATEST" style="width:30px">当年最新数据
							<br/>
							<input name="reStatCompareType" type="radio" class="easyui-validatebox" required="true" value="LATEST" style="width:30px">目前最新数据
							<input name="reStatCompareType" type="radio" class="easyui-validatebox" required="true" value="SPECIFY" style="width:30px">手动指定年份
						</td>
					</tr>
					<tr>
						<td>数据参考年份:</td>
						<td><input id="yearList" class="easyui-combotree" style="width:100px;"
								   data-options="required:true,editable:true" name="year"> 只对第四种有效
						</td>
					</tr>
				</table>
				<div class="submitForm">
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-stat" onclick="reStat()">重新统计</a> &nbsp;
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-cancel" onclick="closeWindow('re-stat-window')">关闭</a>
				</div>
			</form>
		</div>
	</div>

</body>
</html>
