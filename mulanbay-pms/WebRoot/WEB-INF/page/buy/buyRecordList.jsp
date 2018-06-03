<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>购买记录管理</title>
	<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/mulanbay/buyRecord.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/planCommon.js"></script>
	<script type="text/javascript" src="/static/js/echarts/echarts-all-3.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/userPlanForBuss.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/mulanbaychart.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/operationLogCompare.js"></script>

</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="west" split="true" title="商品类型" style="width: 150px;"
		id="west">
		<div class="easyui-panel" style="padding:3px;">
			<a href="#" class="easyui-linkbutton" onclick="getGoodsTypeTree()"
				data-options="plain:true,iconCls:'icon-refresh'">刷新</a> 
		</div>
		<div id="goodsTypeTree" fit="true"></div>
	</div>

	<div region="center" border="true" title="购买记录信息"
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
							<td>名称检索:</td>
							<td><input class="easyui-validatebox" type="text"
								name="name" style="width:200px;" /></td>
							<td>商品类型:</td>
							<td><input id="goodsTypeList" class="easyui-combotree"
								data-options="editable:true" name="goodsType" style="width:130px;">
								<input id="subGoodsTypeList2" class="easyui-combotree" style="width:128px;"
									   data-options="editable:true" name="subGoodsType">
							</td>
							<td>排序方式:</td>
							<td><select class="easyui-combobox" name="sortField"
										style="width:80px;">
								<option value="buyDate">购买时间</option>
								<option value="totalPrice">总价</option>
								<option value="price">商品价格</option>
								<option value="shipment">运费</option>
							</select>
								<select class="easyui-combobox" name="sortType"
										style="width:75px;">
									<option value="desc">倒序</option>
									<option value="asc">顺序</option>
								</select></td>
						</tr>
						<tr>
							<td>起始日期:</td>
							<td><input class="easyui-datebox" name="startDate"
								data-options="showSeconds:false" style="width:95px">--
								<input class="easyui-datebox" name="endDate"
								data-options="showSeconds:false" style="width:95px"></td>
							</td>
							<td>购买来源:</td>
							<td><input id="buyTypeList" class="easyui-combotree" style="width:130px;"
									   data-options="editable:true" name="buyType">
								<select class="easyui-combobox" name="secondhand" id="secondhandList"
										style="width:128px;">
									<option value="">全部</option>
									<option value="false">非二手</option>
									<option value="true">二手</option>
								</select>
							</td>
							<td align="center" colspan="2">
								<select class="easyui-combobox" name="moneyFlow"
										style="width:70px;">
									<option value="">全部</option>
									<option value="BUY">买入</option>
									<option value="SALE">售出</option>
								</select>
								&nbsp;&nbsp;
								<a href="javascript:void(0)" class="easyui-linkbutton"
								   icon="icon-search" onclick="showAll()">查询</a> <a
									href="javascript:void(0)" class="easyui-linkbutton"
									icon="icon-cancel" onclick="resetAndInitSearchWindowYear()">重置</a>
							</td>
						</tr>
					</table>

				</form>
			</div>
			<div region="center" border="true" title="购买记录列表" split="true"
				style="background:#E2E377;overflow-y: hidden">
				<div id="grid" fit="true"></div>
			</div>
		</div>
	</div>

	<div id="eidt-window" title="购买信息" class="easyui-window"
		data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div class="yhdiv">
			<form id="ff" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>商品类型:</td>
						<td><input id="goodsTypeList2" class="easyui-combotree"
							name="goodsTypeId" style="width:160px;"
							data-options="required:true" missingMessage="商品分类"></td>
						<td>商品子类:</td>
						<td><input id="subGoodsTypeList" class="easyui-combotree"
							name="subGoodsTypeId" style="width:160px;"></td>
					</tr>
					<tr>
						<td>交叉商品类型:</td>
						<td><input id="crossGoodsTypeList" class="easyui-combotree"
								   name="crossGoodsTypeId" style="width:160px;"></td>
						<td>购买来源:</td>
						<td><input id="buyTypeList2" class="easyui-combotree"
								   name="buyTypeId" style="width:160px;"
								   data-options="required:true" missingMessage="购买来源"></td>
					</tr>
					<tr>
						<td>商品名称:</td>
						<td colspan=3><input class="easyui-validatebox" id="goodsName"
							type="textarea" name="goodsName" style="width:450px;"></input></td>
					</tr>
					<tr>
						<td>SKU信息:</td>
						<td colspan=3><input class="easyui-validatebox"
											 type="textarea" name="skuInfo" style="width:450px;"></input></td>
					</tr>
					<tr>
						<td>关键字:</td>
						<td colspan=3>
							<input class="easyui-tagbox" name="keywords" style="width:450px;" id="keywordsList">
						</td>
					</tr>
					<tr>
						<td>店铺名称:</td>
						<td><input class="easyui-textbox" name="shopName"
								   data-options="showSeconds:false" style="width:155px"></td>
						<td>支付方式:</td>
						<td><select class="easyui-combobox" name="payment" id="paymentList"
									style="width:158px;">
							<option value="ALIPAY">支付宝</option>
							<option value="WECHAT">微信支付</option>
							<option value="UNIONPAY">银联</option>
							<option value="CASH">现金</option>
							<option value="OTHER">其他</option>
						</select></td>
					</tr>
					<tr>
						<td>商品价格:</td>
						<td><input id="price" class="easyui-numberbox"
							name="price" style="width:160px;"
							data-options="required:true,precision:2"></td>
						<td>运费:</td>
						<td><input id="shipment" class="easyui-numberbox"
								   name="shipment" style="width:160px;"
								   data-options="required:true,min:0,precision:2">
						</td>
					</tr>
					<tr>
						<td>数量:</td>
						<td><input id="amount" class="easyui-spinner"
								   name="amount" style="width:160px;"
								   data-options="required:true,min:1"></td>
						<td>总价:</td>
						<td><input id="totalPrice" class="easyui-numberbox"
							name="totalPrice" style="width:160px;" disabled="true"
							data-options="required:true,min:0,precision:2"></td>
					</tr>
					<tr>
						<td>购买日期:</td>
						<td><input class="easyui-datebox" name="buyDate"
								data-options="showSeconds:false" style="width:155px"></td>
						<td>消费日期:</td>
						<td><input class="easyui-datebox" name="consumeDate"
								   data-options="showSeconds:false" style="width:155px"></td>
					</tr>
					<tr>
						<td>状态:</td>
						<td><select class="easyui-combobox" name="status" id="statusList"
									style="width:158px;">
							<option value="BUY">已购买</option>
							<option value="UNBUY">未购买</option>
						</select></td>
					</tr>
					<tr>
						<td>是否需要加入统计:</td>
						<td>
							<input name="statable" type="radio" class="easyui-validatebox" checked="checked" required="true" value="true" style="width:30px">是
							<input name="statable" type="radio" class="easyui-validatebox" required="true" value="false" style="width:30px">否
						</td>
						<td>是否二手:</td>
						<td>
							<input name="secondhand" type="radio" class="easyui-validatebox" checked="checked" required="true" value="true" style="width:30px">是
							<input name="secondhand" type="radio" class="easyui-validatebox" required="true" value="false" style="width:30px">否
						</td>
					</tr>
					<tr>
						<td>备注:</td>
						<td colspan=3><input class="easyui-textbox" type="text" data-options="multiline:true"
							type="textarea" name="remark" style="width:460px;height:40px"></input></td>
					</tr>
				</table>
				<input type="hidden" name='id' id="id"/>
				<input type="hidden" name='createdTime' />
				<div class="submitForm">
					<a href="javascript:void(0)" class="easyui-linkbutton"
						icon="icon-ok" onclick="saveData(false)">保存</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-ok" onclick="saveData(true)">保存并继续</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
						icon="icon-cancel" onclick="closeWindow()">取消</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-info" onclick="showEditLogCompare('BuyRecord','id')">修改记录</a>
				</div>
			</form>
		</div>
	</div>

	<div id="stat-window" title="统计信息（基于当前查询条件）" class="easyui-window"
		data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div class="yhdiv">
			<form id="stat-form" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td align=center>名称:</td>
						<td align=center>值</td>
					</tr>
					<tr>
						<td align=center colspan="2">购买统计</td>
					</tr>
					<tr>
						<td>总条数</td>
						<td><input id="totalcount" class="easyui-numberspinner"
							name="totalcount" style="width:155px;color:red"></td>
					</tr>
					<tr>
						<td>总运费</td>
						<td><input name="totalshipment" id="totalshipment" class="easyui-textbox"
							style="width:155px;color:red"></td>
					</tr>
					<tr>
						<td>总费用</td>
						<td><input name="mtotalPrice" id="mtotalPrice"
							class="easyui-textbox" style="width:155px;color:red"></td>
					</tr>
				</table>
				<table cellpadding="5" class="tableForm">
					<tr>
						<td align=center colspan="2">售出统计</td>
					</tr>
					<tr>
						<td>总条数</td>
						<td><input id="totalcountSale" class="easyui-numberspinner"
								   name="totalcountSale" style="width:155px;color:red"></td>
					</tr>
					<tr>
						<td>总运费</td>
						<td><input name="totalshipmentSale" id="totalshipmentSale" class="easyui-textbox"
								   style="width:155px;color:red"></td>
					</tr>
					<tr>
						<td>总费用</td>
						<td><input name="mtotalPriceSale" id="mtotalPriceSale"
								   class="easyui-textbox" style="width:155px;color:red"></td>
					</tr>
				</table>
				<div class="submitForm">
					<a href="javascript:void(0)" class="easyui-linkbutton"
						icon="icon-cancel" onclick="closeWindow('stat-window')">关闭</a>
				</div>
			</form>
		</div>
	</div>
	<%@ include file="../plan/userPlanInclude.jsp"%>
	<%@ include file="../log/operationLogCompareInclude.jsp"%>

</body>
</html>
