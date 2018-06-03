$(function() {
	// 加载公司树
    initForm();
	initGrid();
    $('#quitDate').datebox({
        onChange: calculateDays
    });
});

var dataUrl='../company/getData';

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
        },{
			field : 'entryDate',
			title : '入职日期',
            align : 'center'
        } ,{
            field : 'quitDate',
            title : '离职日期',
            align : 'center'
        } ,{
            field : 'days',
            title : '天数',
            sortable : true,
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
		} ]
	});
}

function add() {
	$('#eidt-window').window('open');
	$('#ff').form('clear');
	initForm();
	$('#ff').form.url='../company/create';
}

function initForm(){
}

function edit() {
	var rows = getSelectedSingleRow();
	var url='../company/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data){
		$('#eidt-window').window('open');
        $('#ff').form('clear');
        initForm();
		$('#ff').form('load', data);
		$('#ff').form.url='../company/edit?id=' + data.id;
		//设置字符
		$('#grid').datagrid('clearSelections');
	});
}

function showAll() {
	refreshDatagrid(dataUrl,1,true);
}

function saveData() {
    //自动设置，原来在增加与修改时设置经常出问题
    var url='../company/edit';
    if($("#id").val()==null||$("#id").val()==''){
        url='../company/create';
    }
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
    var delUrlPrefix = '../company/delete';
    commonDeleteByIds(delUrlPrefix);
}


// 自动计算天数
function calculateDays(){
    var entryDate=$('#entryDate').val();
    var quitDate =$('#quitDate').val();
    var days = dateDiff(entryDate,quitDate);
    $('#days').numberspinner('setValue', days);
}