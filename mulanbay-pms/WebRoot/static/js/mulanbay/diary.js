$(function() {
	// 加载公司树
    initForm();
	initGrid();
});

var dataUrl='../diary/getData';

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
            field : 'year',
            title : '年份'
        }, {
            field : 'firstDay',
            title : '第一天'
        },{
			field : 'words',
			title : '字数',
            formatter : function(value, row, index) {
                if (value >=200000) {
                    return '<font color="red">'+value+'</font>';
                }else if (value >=100000) {
                    return '<font color="#00008b">'+value+'</font>';
                }else {
                    return value;
                }
            },
            align : 'center'
        } ,{
            field : 'pieces',
            title : '篇数',
            formatter : function(value, row, index) {
                if (value >=150) {
                    return '<font color="red">'+value+'</font>';
                }else if (value >=100) {
                    return '<font color="#00008b">'+value+'</font>';
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

function add() {
	$('#eidt-window').window('open');
	$('#ff').form('clear');
	initForm();
	$('#ff').form.url='../diary/create';
}

function initForm(){
}

function edit() {
	var rows = getSelectedSingleRow();
	var url='../diary/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data){
		$('#eidt-window').window('open');
        $('#ff').form('clear');
        initForm();
		$('#ff').form('load', data);
		$('#ff').form.url='../diary/edit?id=' + data.id;
		//设置字符
		$('#grid').datagrid('clearSelections');
	});
}

function showAll() {
	refreshDatagrid(dataUrl,1,true);
}

function saveData() {
    var url='../diary/edit';
    if($("#id").val()==null||$("#id").val()==''){
        url='../diary/create';
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
    var delUrlPrefix = '../diary/delete';
    commonDeleteByIds(delUrlPrefix);
}

function stat(){
    var para =form2Json("search-window");
    var url='../diary/stat';

    doAjax(para,url,'GET',false,function(data){
        $('#stat-window').window('open');
        var formData = {
            totalcount: data.totalCount,
            totalWords: data.totalWords,
            totalPieces: data.totalPieces
        };
        $('#stat-form').form('load', formData);
    });
}

function planStat(){
    getUserPlan('Diary');
}