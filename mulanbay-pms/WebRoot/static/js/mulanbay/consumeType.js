$(function() {
	// 加载公司树
    initForm();
	initGrid();
});

var dataUrl='../consumeType/getData';

function initGrid(){
	$('#grid').datagrid({
		iconCls : 'icon-save',
		url : dataUrl,
		method:'GET',
		loadFilter: function(data){
			return loadDataFilter(data);
		},
		idField : 'id',
        treeField: 'name',
		loadMsg : '正在加载数据...',
		pageSize : 30,
		queryParams: form2JsonEnhanced("search-window"),
		remoteSort : false,
		frozenColumns : [ [ {
			field : 'ID',
			checkbox : true
		} ] ],
		onDblClickRow: function (rowData) {
			var rowIndex =$('#grid').datagrid('getRowIndex',rowData);
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
            field : 'statable',
            title : '是否加入统计',
            sortable : true,
            formatter : function(value, row, index) {
                if (value == true) {
                    return '<img src="../static/image/tick.png"></img>';
                }else {
                    return '<img src="../static/image/cross.png"></img>';
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
			id : 'searchBtn',
			text : '刷新',
			iconCls : 'icon-refresh',
			handler : showAll
		}, '-', {
            id : 'clearGridSelectionBtn',
            text : '清除选择',
            iconCls : 'icon-refresh',
            handler : clearGridSelection
        } ]
	});
}

function add() {
	$('#eidt-window').window('open');
	$('#ff').form('clear');
	initForm();
    var formData = {
        status:'ENABLE',
        orderIndex:1,
        statable : true
    };
    $('#ff').form('load', formData);
}

function initForm(){

}

function edit() {
    var rows = getSelectedSingleRow();
    var url='../consumeType/get?id='+ rows[0].id;
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
    //自动设置，原来在增加与修改时设置经常出问题
    var url='../consumeType/edit';
    if($("#id").val()==null||$("#id").val()==''){
        url='../consumeType/create';
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
    var delUrlPrefix = '../consumeType/delete';
    commonDeleteByIds(delUrlPrefix);
}

//清除选择
function clearGridSelection() {
    $('#grid').datagrid('clearSelections');
    $.messager.alert('成功', '清除成功', 'info');
}