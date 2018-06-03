
var dataUrlDrug='/treatDrug/getData';

function initGridDrug(treatRecordId){
	$('#gridDrug').datagrid({
		iconCls : 'icon-save',
		url : dataUrlDrug+'?treatRecordId='+treatRecordId,
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
            $('#gridDrug').datagrid('uncheckAll');
            $('#gridDrug').datagrid('checkRow', rowIndex);
            editDrug();
        },
        onLoadError: loadDataError,
		columns : [ [ {
			field : 'id',
			title : '记录号',
			sortable : true,
			align : 'center'
		}, {
            field : 'name',
            title : '药品名'
        }, {
            field : 'unit',
            title : '单位',
            align : 'center'
        }, {
            field : 'amount',
            title : '数量',
            align : 'center'
        }, {
            field : 'disease',
            title : '针对疾病'
        }, {
            field : 'unitPrice',
            title : '单价',
            align : 'center'
        }, {
            field : 'totalPrice',
            title : '总价',
            align : 'center'
        }, {
            field : 'perDay',
            title : '用药频率',
            formatter : function(value, row, index) {
                return row.perDay+'天'+row.perTimes+'次';
            },
            align : 'center'
        }, {
            field : 'beginDate',
            title : '用药开始日期',
            align : 'center'
        }, {
            field : 'treatDate',
            title : '看病日期',
            align : 'center'
        }, {
            field : 'endDate',
            title : '用药结束日期',
            align : 'center'
        }, {
            field : 'available',
            title : '是否有效',
            formatter : function(value, row, index) {
                return getStatusImage(value);
            },
            align : 'center'
        }, {
            field : 'remind',
            title : '提醒',
            formatter : function(value, row, index) {
                return getStatusImage(value);
            },
            align : 'center'
        }] ],
		pagination : true,
		rownumbers : true,
		singleSelect : false,
		toolbar : [ {
			id : 'createBtnDrug',
			text : '新增',
			iconCls : 'icon-add',
			handler : addDrug
		}, '-', {
			id : 'editBtnDrug',
			text : '修改',
			iconCls : 'icon-edit',
			handler : editDrug
		}, '-', {
			id : 'deleteBtnDrug',
			text : '删除',
			iconCls : 'icon-remove',
			handler : delDrug
		}, '-', {
			id : 'searchBtnDrug',
			text : '刷新',
			iconCls : 'icon-refresh',
			handler : showAllDrug
		} ]
	});
}

function addDrug() {
	$('#eidt-window-drug').window('open');
	$('#ff-drug').form('clear');
	$('#ff-drug').form.url='../treatDrug/create';
	//值为外面看病记录的表单的id
    $("#drugTreatRecordId").val($("#id").val());
    initDrugForm();
    var formData = {
        unit: '盒',
        amount: 1,
        unitPrice:0,
        shipment:0,
        perDay:1,
        perTimes:1,
        available:false,
        remind:true,
        totalPrice:0
    };
    $('#ff-drug').form('load', formData);
    //设置药品用药详情的查询外键
    $("#treatDrugSearchId").val(0);
    //初始化药品用药详情
    initGridDrugDetail();
}

function initDrugForm() {
    $('#diseaseCategoryList2').combobox({
        url : '../treatRecord/getTreatCategoryTree?groupField=disease',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        }
    });
}

function editDrug() {
    var rows = $('#gridDrug').datagrid('getSelections');
    var num = rows.length;
    if (num == 0) {
        $.messager.alert('提示', '请选择一条记录进行操作!', 'info');
        return;
    }
	var url='../treatDrug/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data){
		$('#eidt-window-drug').window('open');
        $('#ff-drug').form('clear');
        initDrugForm();
        $('#ff-drug').form('load', data);
		$('#ff-drug').form.url='../treatDrug/edit?id=' + data.id;
		//设置字符
		$('#gridDrug').datagrid('clearSelections');
		//设置药品记录的外键
        $("#drugTreatRecordId").val($("#id").val());
        //设置药品用药详情的查询外键
        $("#treatDrugSearchId").val(data.id);
        //初始化药品用药详情
        initGridDrugDetail();
    });
}

function showAllDrug() {
	var vtreatRecordId = $("#drugTreatRecordId").val();
    if(vtreatRecordId==null||vtreatRecordId==''||vtreatRecordId==undefined||vtreatRecordId.length == 0){
        vtreatRecordId==0;
	}
    var vurl =dataUrlDrug+'?treatRecordId='+vtreatRecordId;
    $('#gridDrug').datagrid({
        url : vurl,
        type : 'GET',
        pageNumber : 1,
        onLoadError : function() {
            $.messager.alert('错误', '加载数据异常！', 'error');
        }
    });
}

function checkDrugTreatRecordId() {
    var vtreatRecordId = $("#drugTreatRecordId").val();
    if(vtreatRecordId==null||vtreatRecordId==0){
        return false;
    }else {
        return true;
    }
}


function saveDataDrug() {
    if(!checkDrugTreatRecordId()){
        $.messager.alert('提示', '请先保存看病记录!', 'error');
        return;
    }
    var url='../treatDrug/edit';
    if($("#treatDrugId").val()==null||$("#treatDrugId").val()==''){
        url='../treatDrug/create';
    }
    doFormSubmit('ff-drug',url,function(data){
		closeWindow('eidt-window-drug');
		$('#gridDrug').datagrid('clearSelections');
		showAllDrug();
        //设置药品用药详情的查询外键
        $("#treatDrugSearchId").val(data.id);
        //初始化药品用药详情
        initGridDrugDetail();
	});
}

function getSelectedIdsDrug() {
	var ids = [];
    var rows = $('#gridDrug').datagrid('getSelections');
    var num = rows.length;
    if (num == 0) {
        $.messager.alert('提示', '请选择一条记录进行操作!', 'info');
        return;
    }
	for (var i = 0; i < rows.length; i++) {
		ids.push(rows[i].id);
	}
	return ids;
}

function delDrug() {
	var arr = getSelectedIdsDrug();
	if (arr.length > 0) {
		$.messager.confirm('提示信息', '您确认要删除吗?', function(data) {
			if (data) {
				var vurl = '../treatDrug/delete?ids=' + arr.join(',');
				doAjax(null, vurl, 'GET', true, function(data) {
					$('#gridDrug').datagrid('clearSelections');
					showAllDrug();
				});
			}
		});
	} else {
		$.messager.show({
			title : '警告',
			msg : '请先选择要删除的记录。'
		});
	}
}

function closeWindowDrug(){
    closeWindow('eidt-window-drug');
}
