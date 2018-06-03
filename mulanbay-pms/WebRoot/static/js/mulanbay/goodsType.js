$(function() {
	// 加载公司树
    //initForm();
	initGrid();
});

var dataUrl='../goodsType/getData';

function initGrid(){
	$('#grid').treegrid({
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
			var rowIndex =$('#grid').treegrid('getRowIndex',rowData);
            $('#grid').treegrid('uncheckAll');
            $('#grid').treegrid('checkRow', rowIndex);
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
            field : 'parentName',
            title : '上级分类'
        }, {
            field : 'behaviorName',
            title : '行为名称'
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
    loadParentSystemFunctionList();
	$('#ff').form.url='../goodsType/create';
    var formData = {
        'parent.id': 0,
        status: 'ENABLE',
        orderIndex:1,
        statable : true
    };
    $('#ff').form('load', formData);
}


function loadParentSystemFunctionList(){
    $('#parentGoodsTypeList').combotree({
        url : '../goodsType/getGoodsTypeTree?needRoot=true&rootType=SELF&pid=0',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        }
    });
}

function edit() {
    var rows = getSelectedSingleRow();
    var url='../goodsType/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data){
		$('#eidt-window').window('open');
        $('#ff').form('clear');
        loadParentSystemFunctionList();
		$('#ff').form('load', data);
		$('#ff').form.url='../goodsType/edit?id=' + data.id;
        $('#parentGoodsTypeList').combotree('setValue', data.parentId);
        //设置字符
		$('#grid').treegrid('clearSelections');
	});
}

function showAll() {
    refreshTreegrid(dataUrl,1,true);
}

function saveData() {
    var url='../goodsType/edit';
    if($("#id").val()==null||$("#id").val()==''){
        url='../goodsType/create';
    }
	doFormSubmit('ff',url,function(){
		closeWindow('eidt-window');
		$('#grid').treegrid('clearSelections');
        reloadTreegrid();
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
    var delUrlPrefix = '../goodsType/delete';
    commonDeleteByIds(delUrlPrefix);
}

//清除选择
function clearGridSelection() {
    $('#grid').treegrid('clearSelections');
    $.messager.alert('成功', '清除成功', 'info');
}