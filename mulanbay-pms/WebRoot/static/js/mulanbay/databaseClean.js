$(function() {
	// 加载公司树
    initForm();
	initGrid();
});

var dataUrl='../databaseClean/getData';

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
		}, {
            field : 'name',
            title : '名称'
        }, {
            field : 'tableName',
            title : '表名'
        }, {
            field : 'dateField',
            title : '时间字段'
        }, {
            field : 'days',
            title : '保留天数',
            formatter : function(value, row, index) {
                return '<a href="javascript:manualExec('+'\''+row.id+'\''+');"> '+value+'</a>';
            },
            align : 'center'
        }, {
            field : 'cleanTypeName',
            title : '删除类型',
            align : 'center'
        }, {
            field : 'extraCondition',
            title : '含附加条件',
            formatter : function(value, row, index) {
                if(value==null){
                    return '<img src="../static/image/cross.png"></img>';
                }else{
                    return '<img src="../static/image/tick.png"></img>';
                }
            },
            align : 'center'
        }, {
            field : 'lastCleanTime',
            title : '最后一次更新时间'
        }, {
            field : 'lastCleanCounts',
            title : '最后一次更新条数',
            align : 'center'
        },{
			field : 'status',
			title : '状态',
            formatter : function(value, row, index) {
                return getStatusImage(value);
            },
            align : 'center'
        } ,{
            field : 'orderIndex',
            title : '排序号',
            sortable : true,
            align : 'center'
        }, {
            field : 'createdTime',
            title : '创建时间'
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
	$('#ff').form.url='../databaseClean/create';
    var formData = {
        days: 7,
        cleanType:'DATE_COMPARE',
        status:'ENABLE'
    };
    $('#ff').form('load', formData);
}

function initForm(){
}

function edit() {
	var rows = getSelectedSingleRow();
	var url='../databaseClean/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data){
		$('#eidt-window').window('open');
        $('#ff').form('clear');
        initForm();
		$('#ff').form('load', data);
		$('#ff').form.url='../databaseClean/edit?id=' + data.id;
		//设置字符
		$('#grid').datagrid('clearSelections');
	});
}

function manualExec(id) {
    $.messager.confirm('提示信息', '是否要手动执行清理', function(data) {
        if (data) {
            var url='../databaseClean/manualExec?id='+id;
            doAjax(null,url,'GET',true,function(data){
                reloadDatagrid();
            });
        }
    });
    $('#grid').datagrid('clearSelections');
}

function showAll() {
	refreshDatagrid(dataUrl,1,true);
}

function saveData() {
	doFormSubmit('ff',$('#ff').form.url,function(){
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
    var delUrlPrefix = '../databaseClean/delete';
    (delUrlPrefix);
}
