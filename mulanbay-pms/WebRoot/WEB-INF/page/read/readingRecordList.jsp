<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>读书管理</title>
	<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/mulanbay/readingRecord.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/readingRecordDetailPt.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/planCommon.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/userPlanForBuss.js"></script>
	<script type="text/javascript" src="/static/js/echarts/echarts-all-3.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/mulanbaychart.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/operationLogCompare.js"></script>

</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="center" border="true" title="读书统计"
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
							<td>书名:</td>
							<td><input class="easyui-validatebox" type="text"
									   name="name" style="width:150px;" /></td>
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
							<td>图书分类:</td>
							<td><input id="bookCategoryList2" class="easyui-combotree" style="width:120px"
									   data-options="editable:true" name="bookCategoryId"></td>
							<td>状态:</td>
							<td>
								<select class="easyui-combobox" name="status"
										style="width:75px;">
									<option value="">全部</option>
									<option value="READING">正在读</option>
									<option value="READED">已读</option>
									<option value="UNREAD">未读</option>
									<option value="GIVEUP">放弃</option>
								</select>
							</td>
							<td align="center">
								<a href="javascript:void(0)" class="easyui-linkbutton"
								   icon="icon-search" onclick="showAll()">查询</a> <a
									href="javascript:void(0)" class="easyui-linkbutton"
									icon="icon-cancel" onclick="resetSearchWindow()">重置</a>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div region="center" border="true" title="读书列表" split="true"
				 style="background:#E2E377;overflow-y: hidden">
				<div id="grid" fit="true"></div>
			</div>
		</div>
	</div>

	<div id="eidt-window" title="读书信息" class="easyui-window"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div style="width:100%;height:500px;">
			<form id="ff" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>书名:</td>
						<td colspan="3"><input class="easyui-validatebox" type="text"
								   name="bookName" style="width:380px;" />
						</td>
					</tr>
					<tr>
						<td>出版社:</td>
						<td colspan="3"><input class="easyui-validatebox" type="text"
											   name="press" style="width:380px;" />
						</td>
					</tr>
					<tr>
						<td>作者:</td>
						<td colspan="3"><input class="easyui-validatebox" type="text"
											   name="author" style="width:380px;" />
						</td>
					</tr>
					<tr>
						<td>ISBN:</td>
						<td><input class="easyui-validatebox" type="text"
											   name="isbn" style="width:120px;" />
						</td>
						<td>国家:</td>
						<td><input class="easyui-validatebox" type="text"
								   name="nation" style="width:120px;" />
						</td>
					</tr>
					<tr>
						<td>图书分类:</td>
						<td><input id="bookCategoryList" class="easyui-combotree" style="width:120px"
								   data-options="editable:true" name="bookCategoryId"></td>
						<td>重要程度:</td>
						<td><input class="easyui-numberbox"
								   name="importantLevel" style="width:120px;"
								   data-options="required:true,min:0,max:5,precision:1">(0.0-5.0)</td>
					</tr>
					<tr>
						<td>语言:</td>
						<td><select class="easyui-combobox" name="language"
									style="width:120px;">
							<option value="CHINESE">中文</option>
							<option value="ENGLISH">英文</option>
						</select></td>
						<td>出版年份:</td>
						<td><input class="easyui-numberbox"
								   name="publishedYear" style="width:120px;"
								   data-options="min:0,max:9999,precision:0"></td>
					</tr>
					<tr>
						<td>借入/购买日期:</td>
						<td><input class="easyui-datebox" name="storeDate"
								   data-options="showSeconds:false" style="width:120px"></td>
						<td>期望完成日期:</td>
						<td><input class="easyui-datebox" name="proposedDate"
								   data-options="showSeconds:false" style="width:120px"></td>
					</tr>
					<tr>
						<td>开始日期:</td>
						<td><input class="easyui-datebox" name="beginDate"
								   data-options="showSeconds:false" style="width:120px"></td>
						<td>完成日期:</td>
						<td><input class="easyui-datebox" name="finishedDate"
								   data-options="showSeconds:false" style="width:120px"></td>

					</tr>
					<tr>
						<td>状态:</td>
						<td><select class="easyui-combobox" name="status"
									style="width:120px;">
							<option value="UNREAD">未读</option>
							<option value="READING">正在读</option>
							<option value="READED">已读</option>
							<option value="GIVEUP">放弃</option>
						</select></td>
						<td>书籍类型:</td>
						<td>
							<input name="bookType" type="radio" class="easyui-validatebox" checked="checked" required="true" value="PAPER" style="width:30px">纸质书
							<input name="bookType" type="radio" class="easyui-validatebox" required="true" value="EBOOK" style="width:30px">电子书
						</td>
					</tr>
					<tr>
						<td>备注:</td>
						<td colspan=3>
							<input class="easyui-textbox" type="text" data-options="multiline:true"
								   name="remark" style="width:380px;height:60px" />
						</td>			</tr>
				</table>
				<input type="hidden" name='id' id="id"/>
				<input type="hidden" name='createdTime' />
				<div class="submitForm">
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-ok" onclick="saveData()">保存</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
						icon="icon-cancel" onclick="closeWindow()">取消</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-info" onclick="showEditLogCompare('ReadingRecord','id')">修改记录</a>
				</div>
			</form>
			<div region="center" border="true" title="阅读详情列表" split="true"
				 style="background:#E2E377;overflow-y: hidden;height: 200px">
				<div id="gridReadingRecordDetail" fit="true"></div>
			</div>
		</div>
	</div>

	<div id="read-stat-window" title="已完成的阅读统计" class="easyui-window"
		 style="width:850px;height:550px;"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div region="north" border="true" title="查询条件" split="true"
			 style="overflow:auto;height:50px;" data-options="selected:false"
			 icon="icon-edit" class="yhbackground">
			<form id="read-stat-search-form" method="post">
				<table cellpadding="5" class="tableForm" style="width: 100%;">
					<tr>
						<td>
							完成日期:
						</td>
						<td>
							<input class="easyui-datebox" name="startDate"
								   data-options="showSeconds:false" style="width:100px">--
							<input class="easyui-datebox" name="endDate"
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
						<td align="center" colspan="2">
							<a href="javascript:void(0)" class="easyui-linkbutton"
							   icon="icon-search" onclick="stat()">统计</a>
						</td>
					</tr>
				</table>
				<input type="hidden" name='status' value="2"/>
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
									 style="min-width: 500px; height: 450px; margin: 0 auto"></div>
							</div>
						</td>
						<td>
							<form id="stat-form" method="post">
								<table cellpadding="5" class="tableForm">
									<tr>
										<td align=center>名称:</td>
										<td align=center>值</td>
									</tr>
									<tr>
										<td>总本数</td>
										<td><input  class="easyui-numberspinner"
												   name="totalCount" style="width:100px;color:red"></td>
									</tr>
									<tr>
										<td>总花费天数</td>
										<td><input name="totalCostDays" class="easyui-numberspinner"
												   style="width:100px;color:red"></td>
									</tr>
									<tr>
										<td>平均每本花费天数</td>
										<td><input name="avgCostDays" class="easyui-numberspinner"
												   style="width:100px;color:red"></td>
									</tr>
								</table>
								<br>
								<font color="red">表格数据只包含能计算出花费天数的</font>
								<div class="submitForm">
									<a href="javascript:void(0)" class="easyui-linkbutton"
									   icon="icon-cancel" onclick="closeWindow('read-stat-window')">关闭</a>
								</div>
							</form>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>

	<div id="eidt-window-ReadingRecordDetail" title="具体阅读信息" class="easyui-window"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div class="yhdiv">
			<form id="ff-ReadingRecordDetail" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>阅读时间:</td>
						<td><input class="easyui-datetimebox" name="readTime"
								   data-options="showSeconds:true" style="width:200px"></td>
					</tr>
					<tr>
						<td>花费时间:</td>
						<td><input class="easyui-numberspinner"
								   name="minutes" style="width:200px;"
								   data-options="required:true,min:0,precision:0">分钟</td>
					</tr>
					<tr>
						<td>备注:</td>
						<td><input class="easyui-textbox" type="text" data-options="multiline:true"
								   name="remark" style="width:200px;height:60px" /></td>
					</tr>
				</table>
				<input type="hidden" id='detailReadingRecordId' name="readingRecordId"/>
				<input type="hidden" name='createdTime' />
				<input type="hidden" name='id' id="readingRecordDetailId"/>

				<div class="submitForm">
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-ok" onclick="saveDataReadingRecordDetail()">保存</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
						icon="icon-cancel" onclick="closeWindowReadingRecordDetail()">关闭</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-info" onclick="showEditLogCompare('ReadingRecordDetail','readingRecordDetailId')">修改记录</a>
				</div>
			</form>
		</div>
	</div>
	<form id="search-window-ReadingRecordDetail" method="post">
	</form>

	<%@ include file="../plan/userPlanInclude.jsp"%>
	<%@ include file="../log/operationLogCompareInclude.jsp"%>

</body>
</html>
