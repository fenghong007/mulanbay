$(function() {
	// 加载公司树
    initForm();
	initGrid();
});

var dataUrl='../treatDrug/getData';

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
            field : 'treatRecord.hospital',
            title : '医院',
            sortable : true,
            formatter : function(value, row, index) {
                return row.treatRecord.hospital;
            }
        }, {
            field : 'treatDate',
            title : '看病日期',
            sortable : true
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
            align : 'center',
            formatter : function(value, row, index) {
                return formatMoneyWithSymbal(value);
            }
        }, {
            field : 'totalPrice',
            title : '总价',
            align : 'center',
            formatter : function(value, row, index) {
                return formatMoneyWithSymbal(value);
            }
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
			id : 'createBtn',
			text : '新增',
			iconCls : 'icon-add',
			handler : add
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
			id : 'searchBtn',
			text : '刷新',
			iconCls : 'icon-refresh',
			handler : showAll
		} ]
	});
}

function add() {
    $.messager.alert('提示', '目前该功能不支持，请到看病记录管理中操作!', 'error');
}

function initForm(){
    $('#diseaseCategoryList').combobox({
        url : '../treatDrug/getTreatDrugDiseaseCategoryTree',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        }
    });
}

function edit() {
	var rows = getSelectedSingleRow();
	var url='../treatDrug/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data){
		$('#eidt-window').window('open');
        $('#ff').form('clear');
        initForm();
		$('#ff').form('load', data);
		$('#ff').form.url='../treatDrug/edit?id=' + data.id;
		//设置字符
		$('#grid').datagrid('clearSelections');
        $("#treatDrugSearchId").val(data.id);
        $("#treatRecordId").val(data.treatRecord.id);
        initGridDrugDetail();
    });
}

function showAll() {
	refreshDatagrid(dataUrl,1,true);
}

function saveData() {
    var url='../treatDrug/edit';
    if($("#treatDrugId").val()==null||$("#treatDrugId").val()==''){
        url='../treatDrug/create';
    }
	doFormSubmit('ff',url,function(data){
		//closeWindow('eidt-window');
		$('#grid').datagrid('clearSelections');
        reloadDatagrid();
        $("#treatDrugSearchId").val(data.id);
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
    var delUrlPrefix = '../treatDrug/delete';
    commonDeleteByIds(delUrlPrefix);
}