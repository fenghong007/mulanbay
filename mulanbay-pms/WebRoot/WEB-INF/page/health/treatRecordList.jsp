<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>看病记录管理</title>
<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/mulanbay/treatRecord.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/treatDrugPt.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/treatDrugDetailPt.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/treatOperationPt.js"></script>
	<script type="text/javascript" src="/static/js/echarts/echarts-all-3.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/mulanbaychart.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/planCommon.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/userPlanForBuss.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/bodyAbnormalRecordAnalyse.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/operationLogCompare.js"></script>


</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="center" border="true" title="看病记录信息"
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
							<td>关键字:</td>
							<td><input class="easyui-validatebox"
									   type="textarea" name="name" style="width: 150px;"></input></td>
							</td>
							<td>起始日期:</td>
							<td><input class="easyui-datebox" name="startDate"
									   data-options="showSeconds:false" style="width:110px">--
								<input class="easyui-datebox" name="endDate"
									   data-options="showSeconds:false" style="width:110px"></td>
							</td>
							<td>是否有病:</td>
							<td>
								<input name="sick" type="radio" class="easyui-validatebox" required="false" value="" checked="checked" style="width:30px">全部
								<input name="sick" type="radio" class="easyui-validatebox" required="false" value="true" style="width:30px">是
								<input name="sick" type="radio" class="easyui-validatebox" required="false" value="false" style="width:30px">否
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
			<div region="center" border="true" title="看病记录列表" split="true"
				style="background:#E2E377;overflow-y: hidden">
				<div id="grid" fit="true"></div>
			</div>
		</div>
	</div>

	<div id="eidt-window" title="看病信息" class="easyui-window"
		data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div style="width:100%;height:400px;">
				<form id="ff" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>医院:</td>
						<td><input class="easyui-validatebox" type="text"
								   name="hospital" style="width:155px;" /></td>
						<td>科室:</td>
						<td><input class="easyui-validatebox" type="text"
								   name="department" style="width:155px;" /></td>
					</tr>
					<tr>
						<td>疾病:</td>
						<td>
							<input id="diseaseCategoryList" class="easyui-combobox" style="width: 155px;"
								   data-options="editable:true" name="disease" />
							<a href="javascript:showBodyAbnomarlAnalyse('diseaseCategoryList','DISEASE');"><img src="../static/image/info.png" alt="查看身体情况分析"></img></a>
						</td>
						<td>器官:</td>
						<td>
							<input id="organCategoryList" class="easyui-combobox" style="width: 155px;"
								   data-options="editable:true" name="organ" />
							<a href="javascript:showBodyAbnomarlAnalyse('organCategoryList','ORGAN');"><img src="../static/image/info.png" alt="查看身体情况分析"></img></a>
						</td>
					</tr>
					<tr>
						<td>确诊疾病:</td>
						<td><input class="easyui-validatebox" type="text"
								   name="diagnosedDisease" style="width:155px;" /></td>
						<td>是否有病:</td>
						<td>
							<input name="isSick" type="radio" class="easyui-validatebox" checked="checked" required="true" value="true" style="width:30px">是
							<input name="isSick" type="radio" class="easyui-validatebox" required="true" value="false" style="width:30px">否
						</td>
					</tr>
					<tr>
						<td>疼痛等级:</td>
						<td><input class="easyui-numberbox"
								   name="painLevel" style="width:155px;"
								   data-options="required:true,min:0,max:10,precision:0">(0-10)</td>
						<td>重要程度:</td>
						<td><input class="easyui-numberbox"
								   name="importantLevel" style="width:155px;"
								   data-options="required:true,min:0,max:5,precision:1">(0.0-5.0)</td>
					</tr>
					<tr>
						<td>看病日期:</td>
						<td><input class="easyui-datebox" name="treatDate"
								   data-options="required:true,showSeconds:false" style="width:155px"></td>
						<td></td>
					</tr>
					<tr>
						<td>药费:</td>
						<td><input class="easyui-numberbox"
								   name="drugFee" style="width:155px;"
								   data-options="required:true,min:0,precision:2">&nbsp;&nbsp;元</td>
						<td>手术/治疗费用:</td>
						<td><input class="easyui-numberbox"
								   name="operationFee" style="width:155px;"
								   data-options="required:true,min:0,precision:2">&nbsp;&nbsp;元</td>
					</tr>
					<tr>
						<td>医保花费:</td>
						<td><input class="easyui-numberbox"
								   name="medicalInsurancePaidFee" style="width:155px;"
								   data-options="required:true,min:0,precision:2">&nbsp;&nbsp;元</td>
						<td>个人支付费用:</td>
						<td><input class="easyui-numberbox"
								   name="personalPaidFee" style="width:155px;"
								   data-options="required:true,min:0,precision:2">&nbsp;&nbsp;元</td>
					</tr>
					<tr>
						<td>挂号费:</td>
						<td><input class="easyui-numberbox"
								   name="registeredFee" style="width:155px;"
								   data-options="required:true,min:0,precision:2">&nbsp;&nbsp;元</td>
						<td>总共花费:</td>
						<td><input class="easyui-numberbox"
								   name="totalFee" style="width:155px;"
								   data-options="required:true,min:0,precision:2">&nbsp;&nbsp;元</td>
					</tr>
					<tr>
						<td>备注:</td>
						<td colspan="3">
							<input class="easyui-textbox" type="text" data-options="multiline:true"
											   name="remark" style="width:485px;height:60px" /></td>
					</tr>
					<tr>
						<td colspan="4" align="center">
							<a href="javascript:void(0)" class="easyui-linkbutton"
							   icon="icon-ok" onclick="saveData()">保存</a>
							<a href="javascript:void(0)" class="easyui-linkbutton"
								icon="icon-cancel" onclick="closeWindow()">关闭</a>
							<a href="javascript:void(0)" class="easyui-linkbutton"
							   icon="icon-info" onclick="showEditLogCompare('TreatRecord','id')">修改记录</a>
						</td>
					</tr>
				</table>
				<input type="hidden" name='id' id="id" />
				<input type="hidden" name='createdTime' />
				</form>
			<div region="center" border="true" title="药品列表" split="true"
					 style="background:#E2E377;overflow-y: hidden;height: 200px">
					<div id="gridDrug" fit="true"></div>
				</div>
				<div region="center" border="true" title="手术列表" split="true"
					 style="background:#E2E377;overflow-y: hidden;height: 200px">
					<div id="gridOperation" fit="true"></div>
				</div>
		</div>
	</div>

	<div id="eidt-window-drug" title="药品信息" class="easyui-window"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div class="yhdiv">
			<form id="ff-drug" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>药品名称:</td>
						<td><input class="easyui-textbox"
								   name="name" style="width:155px;"
								   data-options="required:true"></td>
						<td>单位:</td>
						<td><input class="easyui-textbox"
								   name="unit" style="width:155px;"
								   data-options="required:true"></td>
					</tr>
					<tr>
						<td>数量:</td>
						<td><input class="easyui-numberspinner"
								   name="amount" style="width:155px;"
								   data-options="required:true,min:0,precision:0"></td>
						<td>针对疾病:</td>
						<td><input id="diseaseCategoryList2" class="easyui-combobox" style="width: 155px;"
								   data-options="editable:true" name="disease" />
							<a href="javascript:showBodyAbnomarlAnalyse('diseaseCategoryList2','DISEASE');"><img src="../static/image/info.png" alt="查看身体情况分析"></img></a>
						</td>
					</tr>
					<tr>
						<td>单价:</td>
						<td><input class="easyui-numberspinner"
								   name="unitPrice" style="width:155px;"
								   data-options="required:true,min:0,precision:2"></td>
						<td>总价:</td>
						<td><input class="easyui-numberspinner"
								   name="totalPrice" style="width:155px;"
								   data-options="required:true,min:0,precision:2"></td>
					</tr>
					<tr>
						<td>用药开始日期:</td>
						<td><input class="easyui-datebox" name="beginDate"
								   data-options="showSeconds:false" style="width:155px"></td>
						<td>用药结束日期:</td>
						<td><input class="easyui-datebox" name="endDate"
								   data-options="showSeconds:false" style="width:155px"></td>
					</tr>
					<tr>
						<td>看病日期:</td>
						<td><input class="easyui-datebox" name="treatDate"
								   data-options="showSeconds:false" style="width:155px"></td>
						<td>用药频率:</td>
						<td>每<input class="easyui-numberspinner"
									name="perDay" style="width:60px;"
									data-options="required:true,min:0,precision:0">天
							<input class="easyui-numberspinner"
								   name="perTimes" style="width:60px;"
								   data-options="required:true,min:0,precision:0">次
						</td>
					</tr>
					<tr>
						<td>是否有效:</td>
						<td>
							<input name="available" type="radio" class="easyui-validatebox" checked="checked" required="true" value="true" style="width:30px">是
							<input name="available" type="radio" class="easyui-validatebox" required="true" value="false" style="width:30px">否
						</td>
						<td>是否提醒:</td>
						<td>
							<input name="remind" type="radio" class="easyui-validatebox" checked="checked" required="true" value="true" style="width:30px">是
							<input name="remind" type="radio" class="easyui-validatebox" required="true" value="false" style="width:30px">否
						</td>
					</tr>
					<tr>
						<td>备注:</td>
						<td colspan="3"><input class="easyui-textbox" type="text" data-options="multiline:true"
											   name="remark" style="width:450px;height:40px" /></td>
					</tr>
				</table>
				<input type="hidden" id='drugTreatRecordId' name="treatRecordId"/>
				<input type="hidden" name='id' id="treatDrugId"/>
				<input type="hidden" name='createdTime' />
				<div region="center" border="true" title="用药记录列表" split="true"
					 style="background:#E2E377;overflow-y: hidden;height: 180px">
					<div id="gridDrugDetail" fit="true"></div>
				</div>
				<div class="submitForm">
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-ok" onclick="saveDataDrug()">保存</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
						icon="icon-cancel" onclick="closeWindowDrug()">关闭</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-info" onclick="showEditLogCompare('TreatDrug','treatDrugId')">修改记录</a>
				</div>
			</form>
		</div>
	</div>
	<form id="search-window-drug" method="post">
	</form>

	<div id="eidt-window-operation" title="手术信息" class="easyui-window"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div class="yhdiv">
			<form id="ff-operation" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>手术名称:</td>
						<td><input class="easyui-textbox"
								   name="name" style="width:155px;"
								   data-options="required:true"></td>
					</tr>
					<tr>
						<td>费用:</td>
						<td><input class="easyui-numberspinner"
								   name="fee" style="width:155px;"
								   data-options="required:true,min:0,precision:2"></td>
					</tr>
					<tr>
						<td>看病日期:</td>
						<td><input class="easyui-datebox" name="treatDate"
								   data-options="showSeconds:false" style="width:155px"></td>
					</tr>
					<tr>
						<td>是否有效:</td>
						<td>
							<input name="available" type="radio" class="easyui-validatebox" checked="checked" required="true" value="true" style="width:30px">是
							<input name="available" type="radio" class="easyui-validatebox" required="true" value="false" style="width:30px">否
						</td>
					</tr>
					<tr>
						<td>是否有病:</td>
						<td>
							<input name="isSick" type="radio" class="easyui-validatebox" checked="checked" required="true" value="true" style="width:30px">是
							<input name="isSick" type="radio" class="easyui-validatebox" required="true" value="false" style="width:30px">否
						</td>
					</tr>
					<tr>
						<td>备注:</td>
						<td><input class="easyui-textbox" type="text" data-options="multiline:true"
								   name="remark" style="width:155px;height:60px" /></td>
					</tr>
				</table>
				<input type="hidden" id='operationTreatRecordId' name="treatRecordId"/>
				<input type="hidden" name='createdTime' />
				<input type="hidden" name='id' id="treatOperationId"/>
				<div class="submitForm">
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-ok" onclick="saveDataOperation()">保存</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
						icon="icon-cancel" onclick="closeWindowOperation()">关闭</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-info" onclick="showEditLogCompare('TreatOperation','treatOperationId')">修改记录</a>
				</div>
			</form>
		</div>
	</div>
	<form id="search-window-operation" method="post">
	</form>

	<div id="stat-window" title="统计信息（基于当前查询条件）" class="easyui-window"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
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
									<td>总次数</td>
									<td><input id="totalCount" class="easyui-numberspinner"
											   name="totalCount" style="width:120px;color:red"></td>
								</tr>
								<tr>
									<td>总挂号费</td>
									<td><input name="totalRegisteredFee" id="totalRegisteredFee" class="easyui-textbox"
											   style="width:120px;color:red"></td>
								</tr>
								<tr>
									<td>总药费</td>
									<td><input name="totalDrugFee" id="totalDrugFee"
											   class="easyui-textbox" style="width:120px;color:red"></td>
								</tr>
								<tr>
									<td>总手术费</td>
									<td><input name="totalOperationFee" id="totalOperationFee"
											   class="easyui-textbox" style="width:120px;color:red"></td>
								</tr>
								<tr>
									<td>总医保报销费用</td>
									<td><input name="totalMedicalInsurancePaidFee" id="totalMedicalInsurancePaidFee"
											   class="easyui-textbox" style="width:120px;color:red"></td>
								</tr>
								<tr>
									<td>总个人支付费用</td>
									<td><input name="totalPersonalPaidFee" id="totalPersonalPaidFee"
											   class="easyui-textbox" style="width:120px;color:red"></td>
								</tr>
								<tr>
									<td>总费用</td>
									<td><input name="totalTotalFee" id="totalTotalFee"
											   class="easyui-textbox" style="width:120px;color:red"></td>
								</tr>
							</table>
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

	<div id="eidt-window-drugDetail" title="用药记录详情" class="easyui-window"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div class="yhdiv">
			<form id="search-drugDetail-form" method="post">
				<input type="hidden" name='treatDrugId' id="treatDrugSearchId" value="0" />
			</form>
			<form id="ff-drugDetail" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>用药时间:</td>
						<td><input class="easyui-datetimebox" name="occurTime"
								   data-options="showSeconds:true" style="width:155px"></td>
					</tr>
					<tr>
						<td>备注:</td>
						<td colspan="3"><input class="easyui-validatebox"
											   type="textarea" name="remark" style="width:99%;"></input></td>
					</tr>
				</table>
				<input type="hidden" name='treatDrugId' id="fkTreatDrugId"/>
				<input type="hidden" name="id" id="treatDrugDetailId"/>
				<input type="hidden" name='createdTime' />
				<div class="submitForm">
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-ok" onclick="saveDataDrugDetail()">保存</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-cancel" onclick="closeWindow('eidt-window-drugDetail')">取消</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-info" onclick="showEditLogCompare('TreatDrugDetail','id')">修改记录</a>
				</div>
			</form>
		</div>
	</div>

	<%@ include file="../health/treatDrugDetailChartInclude.jsp"%>
	<%@ include file="../plan/userPlanInclude.jsp"%>
	<%@ include file="../health/bodyAbnormalRecordAnalyse.jsp"%>
	<%@ include file="../log/operationLogCompareInclude.jsp"%>

</body>
<script type="text/javascript">
    $(function() {
        var showTreatRecordId = ${showTreatRecordId};
        if(showTreatRecordId!=null&&showTreatRecordId>0){
            //显示某个具体的记录
			showEdit(showTreatRecordId);
        }
    });
</script>
</html>
