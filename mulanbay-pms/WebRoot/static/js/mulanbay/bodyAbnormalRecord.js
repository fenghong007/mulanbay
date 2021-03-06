$(function() {
	// 加载公司树
    initForm();
	initGrid();
});

var dataUrl='../bodyAbnormalRecord/getData';

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
		queryParams: form2JsonEnhanced("search-window"),
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
		},{
            field : 'organ',
            title : '器官'
        }, {
            field : 'disease',
            title : '疾病'
        }, {
            field : 'painLevel',
            title : '疼痛级别',
            formatter : function(value, row, index) {
                if (value >=8) {
                    return '<font color="red">'+value+'</font>';
                }else {
                    return value;
                }
            },
            align : 'center'
        }, {
            field : 'importantLevel',
            title : '重要等级',
            formatter : function(value, row, index) {
                if (value >=4) {
                    return '<font color="red">'+value+'</font>';
                }else {
                    return value;
                }
            },
            align : 'center'
        }, {
            field : 'occurDate',
            title : '发生日期',
            align : 'center'
        }, {
            field : 'finishDate',
            title : '结束日期',
            align : 'center'
        }, {
            field : 'lastDays',
            title : '持续天数',
            formatter : function(value, row, index) {
                if (value >7) {
                    return '<font color="red">'+value+'</font>';
                }else {
                    return value;
                }
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
            id : 'bodyAnalyseBtn',
            text : '身体分析',
            iconCls : 'icon-stat',
            handler : bodyAnalyse
        }, '-', {
			id : 'searchBtn',
			text : '刷新',
			iconCls : 'icon-refresh',
			handler : showAll
		} ]
	});
}

function add() {
	$('#eidt-window').window('open');
	$('#ff').form('clear');
	initForm();
}

function initForm(){
}

function edit() {
	var rows = getSelectedSingleRow();
	var url='../bodyAbnormalRecord/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data){
		$('#eidt-window').window('open');
        $('#ff').form('clear');
        initForm();
		$('#ff').form('load', data);
		//设置字符
		$('#grid').datagrid('clearSelections');
	});
}

function showAll() {
	refreshDatagrid(dataUrl,1,true);
}

function saveData() {
    var url='../bodyAbnormalRecord/edit';
    if($("#id").val()==null||$("#id").val()==''){
        url='../bodyAbnormalRecord/create';
    }
	doFormSubmit('ff',url,function(){
		closeWindow('eidt-window');
		$('#grid').datagrid('clearSelections');
        reloadDatagrid();
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
	var delUrlPrefix = '../bodyAbnormalRecord/delete';
    commonDeleteByIds(delUrlPrefix);
}