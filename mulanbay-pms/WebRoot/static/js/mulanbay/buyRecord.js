$(function() {
	// 加载部门树
	getGoodsTypeTree();
	getBuyTypeTree();
    initSearchFormYear();
	initGrid();
    $('#totalPrice').numberbox({
        onSelect: calculatePrice
    });
});

var dataUrl='../buyRecord/getData';

function initGrid(){
	$('#grid').datagrid({
		iconCls : 'icon-save',
		url : dataUrl,
		method:'GET',
		loadFilter: function(data){
			return loadDataFilter(data);
		},
		idField : 'id',
		loadMsg : '正在加载数据...',
		pageSize : 30,
		queryParams: form2Json("search-window"),
		remoteSort : false,
		frozenColumns : [ [ {
			field : 'ID',
			checkbox : true
		} ] ],
		onDblClickRow: function (rowIndex, rowData) {
            $('#grid').datagrid('uncheckAll');
            $('#grid').datagrid('checkRow', rowIndex);
            edit();
        },
        onLoadError: loadDataError,
		columns : [ [ {
			field : 'id',
			title : '记录号',
			sortable : true,
			align : 'center'
		}, {
			field : 'goodsName',
			title : '商品名称',
            width :  400,
            formatter : function(value, row, index) {
				var vv=value;
				if(row.keywords!=null){
					vv="★"+vv;
				}
                if(vv.length>35){
                    return vv.substring(0,35)+'<a href="javascript:showDetail('+'\''+value+'\''+');"> ......</a>';
                }
                return vv;
            },
			sortable : true
		}, {
			field : 'buyType.id',
			title : '购买来源',
			sortable : true,
			formatter : function(value, row, index) {
				return row.buyType.name;
			},
            align : 'center'
        }, {
			field : 'goodsType.id',
			title : '商品类型',
			sortable : true,
			formatter : function(value, row, index) {
				return row.goodsType.name;
			},
            align : 'center'
        }, {
            field : 'subGoodsType.id',
            title : '商品子类型',
            sortable : true,
            formatter : function(value, row, index) {
            	if(row.subGoodsType){
                    return row.subGoodsType.name;
                }else {
            		return '--';
				}
            },
            align : 'center'
        }, {
            field : 'crossGoodsType.id',
            title : '交叉商品类型',
            sortable : true,
            formatter : function(value, row, index) {
                if(row.crossGoodsType){
                    return row.crossGoodsType.name;
                }else {
                    return '--';
                }
            },
            align : 'center'
        }, {
			field : 'price',
			title : '商品价格',
			sortable : true,
			formatter : function(value, row, index) {
				return formatMoneyWithSymbal(value);
			},
            align : 'center'
        }, {
			field : 'amount',
			title : '数量',
			sortable : true,
            align : 'center'
        }, {
			field : 'shipment',
			title : '运费',
			sortable : true,
			formatter : function(value, row, index) {
				return formatMoneyWithSymbal(value);
			},
            align : 'center'
        }, {
			field : 'totalPrice',
			title : '总价',
			sortable : true,
			formatter : function(value, row, index) {
				return formatMoneyWithSymbal(value);
			},
            align : 'center'
        }, {
			field : 'buyDate',
			title : '购买日期',
			sortable : true
		}, {
            field : 'consumeDate',
            title : '消费日期',
            sortable : true
        }, {
			field : 'status',
			title : '商品状态',
			sortable : true,
			formatter : function(value, row, index) {
				if (value == 'UNBUY') {
					return '<a href="javascript:change()"><img src="../static/image/cross.png"></img></a>';
				} else if (value == 'BUY'){
					return '<a href="javascript:change()"><img src="../static/image/tick.png"></img></a>';
				} else {
					return '<img src="../static/image/unknown.png"></img>';
				}
			},
			align : 'center'
		}, {
            field : 'shopName',
            title : '店铺名称'
		}, {
            field : 'paymentName',
            title : '支付方式'
        }, {
			field : 'secondhand',
			title : '是否二手',
			sortable : true,
			formatter : function(value, row, index) {
				if (value == true) {
					return '<img src="../static/image/tick.png"></img>';
				}else {
					return '--';
				}
			},
			align : 'center'
		}, {
            field : 'statable',
            title : '是否加入统计',
            sortable : true,
            formatter : function(value, row, index) {
                return getStatusImage(value);
            },
            align : 'center'
        } ] ],
		pagination : true,
		rownumbers : true,
		singleSelect : false,
		toolbar : [ {
			id : 'createBtn',
			text : '新增',
			iconCls : 'icon-add',
			handler : add
		}, '-', {
            id : 'createAsTemplateBtn',
            text : '以此为模板新增',
            iconCls : 'icon-add',
            handler : addFromTemplate
        }, '-', {
			id : 'editBtn',
			text : '修改',
			iconCls : 'icon-edit',
			handler : edit
		}, '-', {
			id : 'deleteBtn',
			text : '删除',
			iconCls : 'icon-remove',
			handler : del
		}, '-', {
			id : 'statBtn',
			text : '统计',
			iconCls : 'icon-stat',
			handler : stat
		}, '-', {
            id : 'planStatBtn',
            text : '计划执行统计',
            iconCls : 'icon-stat',
            handler : planStat
        }, '-', {
			id : 'searchBtn',
			text : '刷新',
			iconCls : 'icon-refresh',
			handler : showAll
		} ]
	});
}

function showDetail(msg){
    $('#grid').datagrid('uncheckAll');
    $.messager.alert('详情', msg, 'info');
}

function add() {
	$('#eidt-window').window('open');
	$('#ff').form('clear');
	initForm();
	$('#ff').form.url='../buyRecord/create';
	var nowDate =getNowDateString();
    var formData = {
        'goodsTypeId': 1,
        'buyTypeId': 1,
        amount: 1,
		buyDate:nowDate,
        // consumeDate:nowDate,
        shipment:0,
        payment:'ALIPAY',
        status:'BUY',
        secondhand:false,
        statable : true
    };
    $('#ff').form('load', formData);
}

function addFromTemplate(){
    var rows = getSelectedSingleRow();
    var url='../buyRecord/get?id='+ rows[0].id;
    doAjax(null,url,'GET',false,function(data){
        $('#eidt-window').window('open');
        $('#ff').form('clear');
        initForm();
        //需要删除id，否则变为修改了
		data.id="";
		data.goodsName="";
        data.price=0;
        data.keywords="";
        $('#ff').form('load', data);
        $('#ff').form.url='../buyRecord/create?id=' + data.id;
        $('#goodsTypeList2').combotree('setValue', data.goodsType.id);
        if(data.subGoodsType){
            $('#subGoodsTypeList').combotree('setValue', data.subGoodsType.id);
        }
        if(data.crossGoodsType){
            $('#crossGoodsTypeList').combotree('setValue', data.crossGoodsType.id);
        }
        $('#buyTypeList2').combotree('setValue', data.buyType.id);
        //设置字符
        $('#secondhandList').combobox('setValue', data.secondhand);
        $("#ordercon").val("asc");
        $('#grid').datagrid('clearSelections');
    });
}

//编辑使用
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

//搜索使用
function loadSubGoodsTypeListForSearch(pid){
    $('#subGoodsTypeList2').combotree({
        url : '../goodsType/getGoodsTypeTree?pid='+pid,
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        }
    });
}

function initForm(){
	$('#goodsTypeList2').combotree({
		url : '../goodsType/getGoodsTypeTree?needRoot=true&pid=0',
		valueField : 'id',
		textField : 'text',
		loadFilter: function(data){
	    	return loadDataFilter(data);
	    },
        onChange : function (newValue, oldValue) {
            loadSubGoodsTypeList(newValue);
        }
	});
	$('#buyTypeList2').combotree({
		url : '../buyType/getBuyTypeTree?needRoot=true',
		valueField : 'id',
		textField : 'text',
		loadFilter: function(data){
	    	return loadDataFilter(data);
	    }
	});
    $('#keywordsList').tagbox({
        url : '../buyRecord/getKeywordsTree',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        }
    });
    $('#crossGoodsTypeList').combotree({
        // checkbox:true,
        url: '../goodsType/getGoodsTypeTree?needRoot=true&pid=0',
        loadFilter: function(data){
            return loadDataFilter(data);
        }
    });
    //loadKeywords();
}

function edit() {
	var rows = getSelectedSingleRow();
	var url='../buyRecord/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data){
		$('#eidt-window').window('open');
        $('#ff').form('clear');
        initForm();
		$('#ff').form('load', data);
		$('#ff').form.url='../buyRecord/edit?id=' + data.id;
		$('#goodsTypeList2').combotree('setValue', data.goodsType.id);
		if(data.subGoodsType){
            $('#subGoodsTypeList').combotree('setValue', data.subGoodsType.id);
        }
        if(data.crossGoodsType){
            $('#crossGoodsTypeList').combotree('setValue', data.crossGoodsType.id);
        }
        $('#buyTypeList2').combotree('setValue', data.buyType.id);
		//设置字符
		$('#secondhandList').combobox('setValue', data.secondhand);
        $("#ordercon").val("asc");
		$('#grid').datagrid('clearSelections');
	});
}

function getGoodsTypeTree(){
	$('#goodsTypeTree').tree({
		// checkbox:true,
	    url: '../goodsType/getGoodsTypeTree?needRoot=true&pid=0',
	    loadFilter: function(data){
	    	return loadDataFilter(data);
	    },
	    onClick: function(node){
	    	$('#goodsTypeList').combotree('setValue', node.id);
            loadSubGoodsTypeListForSearch(node.id);
            showAll();
		}
	});
	$('#goodsTypeList').combotree({
		url : '../goodsType/getGoodsTypeTree?needRoot=true&pid=0',
		valueField : 'id',
		textField : 'text',
		loadFilter: function(data){
	    	return loadDataFilter(data);
	    },
        onChange : function (newValue, oldValue) {
            loadSubGoodsTypeListForSearch(newValue);
        }
	});
}

function getBuyTypeTree(){
	$('#buyTypeList').combotree({
		url : '../buyType/getBuyTypeTree?needRoot=true',
		valueField : 'id',
		textField : 'text',
		loadFilter: function(data){
	    	return loadDataFilter(data);
	    }
	});
}

function formatMoney(pp){
	pp =pp+0.0;
	return (pp/100).toFixed(2);
}

function showAll() {
	refreshDatagrid(dataUrl,1,true);
}

function saveData(continueCreate) {
    //自动设置，原来在增加与修改时设置经常出问题
    var url='../buyRecord/edit';
    if($("#id").val()==null||$("#id").val()==''){
        url='../buyRecord/create';
    }
	doFormSubmit('ff',url,function(){
		$('#grid').datagrid('clearSelections');
        reloadDatagrid();
        if(continueCreate){
        	// 继续增加
            $("#id").val(null);
            $("#goodsName").val(null);
        }else{
            closeWindow('eidt-window');
		}
    });
}

function getSelectedIds() {
	var ids = [];
	var rows = getSelectedRows();
	for (var i = 0; i < rows.length; i++) {
		ids.push(rows[i].id);
	}
	return ids;
}

function del() {
    var delUrlPrefix = '../buyRecord/delete';
    commonDeleteByIds(delUrlPrefix);
}

function stat(){
	var para =form2Json("search-window");
	var url='../buyRecord/stat';

	doAjax(para,url,'GET',false,function(data){
		$('#stat-window').window('open');
		var formData = {
			totalcount: data[0].totalCount,
			totalshipment: formatMoneyWithSymbal(data[0].totalShipment),
			mtotalPrice: formatMoneyWithSymbal(data[0].totalPrice),
            totalcountSale: data[1].totalCount,
            totalshipmentSale: formatMoneyWithSymbal(0-data[1].totalShipment),
            mtotalPriceSale: formatMoneyWithSymbal(0-data[1].totalPrice)
	    };
		$('#stat-form').form('load', formData);
	});
}

function loadKeywords(){
    var url='../buyRecord/getLatestKeywords?days=7';
    doAjax(null,url,'GET',false,function(data){
        $("#latestKeywordsList").attr("value",'test');
    });
}

function calculatePrice() {
    var price=$('#price').val();
    var shipment =$('#shipment').val();
    var amount =$('#amount').val();
    var totalPrice = (Number(price)*Number(amount)).toFixed(2);
    $('#totalPrice').numberbox('setValue', totalPrice);
}

function planStat(){
    getUserPlan('BuyRecord');
}