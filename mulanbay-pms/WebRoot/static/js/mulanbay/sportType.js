$(function() {
	// 加载公司树
    initForm();
	initGrid();
});

var dataUrl='../sportType/getData';

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
            field : 'idValue',
            title : '统计',
            formatter : function(value, row, index) {
                return '<a href="javascript:multiStat('+row.id+')"><img src="../static/image/sum.png"></img></a>';
            }
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
	$('#ff').form.url='../sportType/create';
}

function initForm(){
}

function edit() {
	var rows = getSelectedSingleRow();
	var url='../sportType/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data){
		$('#eidt-window').window('open');
        $('#ff').form('clear');
        initForm();
		$('#ff').form('load', data);
		$('#ff').form.url='../sportType/edit?id=' + data.id;
		//设置字符
		$('#grid').datagrid('clearSelections');
	});
}

function showAll() {
	refreshDatagrid(dataUrl,1,true);
}

function saveData() {
    var url='../sportType/edit';
    if($("#id").val()==null||$("#id").val()==''){
        url='../sportType/create';
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
    var delUrlPrefix = '../sportType/delete';
    commonDeleteByIds(delUrlPrefix);
}

function multiStat(id) {
    var url='../sportExercise/multiStat?sportTypeId='+id;
    doAjax(null,url,'GET',false,function(data){
        $('#grid').datagrid('clearSelections');
        $('#multiStat-window').window('open');
        $('#multiStat-form').form('clear');
        $('#multiStat-form').form('load', data);
    });
}

function showMultiSportExerciseInfo(groupType,type) {
    var para = {
        sportTypeId: $("#sportTypeId").val(),
        groupType: groupType,
        type:type
    };
    var url='../sportExercise/getByMultiStat';
    doAjax(para,url,'GET',false,function(data){
        $('#sportExercise-window').window('open');
        $('#sportExercise-form').form('clear');
        $('#sportExercise-form').form('load', data);
        loadSportTypeList();
        $('#sportTypeList2').combotree('setValue', data.sportType.id);
    });
}

function loadSportTypeList() {
    $('#sportTypeList2').combotree({
        url : '../sportType/getSportTypeTree',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        }
    });
}
