$(function() {
	// 加载公司树
    initForm();
	initGrid();
});

var dataUrl='../sleep/getData';

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
            field : 'sleepDate',
            title : '睡眠日'
        },{
			field : 'sleepTime',
			title : '睡觉时间',
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
            field : 'getUpTime',
            title : '起床时间',
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
        }, {
            field : 'totalMinutes',
            title : '睡眠总时长',
            formatter : function(value, row, index) {
                var ss=tillNowString(value*60);
                if(value<360){
                	//小于6个小时
                    return '<font color="red">'+ss+'</font>';

                }else if(value<=450){
                    //小于7个半小时
                    return '<font color="#9acd32">'+ss+'</font>';

                }else {
                    return ss;
                }

            },
            align : 'center'
        }, {
            field : 'lightSleep',
            title : '浅睡时长',
            align : 'center'
        }, {
            field : 'deepSleep',
            title : '深睡时长',
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
    var formData = {
        lightSleep: 0,
        deepSleep: 0
    };
    $('#ff').form('load', formData);
}

function initForm(){
}

function edit() {
	var rows = getSelectedSingleRow();
    var url='../sleep/get?id='+ rows[0].id;
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
    var url='../sleep/edit';
    if($("#id").val()==null||$("#id").val()==''){
        url='../sleep/create';
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
    var delUrlPrefix = '../sleep/delete';
    commonDeleteByIds(delUrlPrefix);
}

function stat(){
    var para =form2Json("search-window");
    var url='../sleep/stat';

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
    getUserPlan('Sleep');
}