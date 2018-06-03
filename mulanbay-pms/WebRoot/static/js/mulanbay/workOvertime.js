$(function() {
	// 加载公司树
    initForm();
	initGrid();
    $('#workEndTime').datebox({
        onChange: calculateHours
    });
});

var dataUrl='../workOvertime/getData';

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
			field : 'company.id',
			title : '公司',
			sortable : true,
			formatter : function(value, row, index) {
				return row.company.name;
			}
		}, {
            field : 'workDate',
            title : '加班日期',
            sortable : true
        }, {
            field : 'workStartTime',
            title : '加班开始时间',
            sortable : true
        }, {
            field : 'workEndTime',
            title : '加班结束日期',
            sortable : true
        },{
			field : 'hours',
			title : '加班时长(小时)',
			sortable : true,
            formatter : function(value, row, index) {
                if (value >=4) {
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
	$('#ff').form.url='../workOvertime/create';
	$('#companyList1').combotree('setValue', 1);
	$('#companyList2').combotree('setValue', 1);
}

function initForm(){
	$('#companyList').combotree({
		url : '../company/getCompanyTree',
		valueField : 'id',
		textField : 'text',
		loadFilter: function(data){
	    	return loadDataFilter(data);
	    }
	});
	$('#companyList2').combotree({
		url : '../company/getCompanyTree',
		valueField : 'id',
		textField : 'text',
		loadFilter: function(data){
	    	return loadDataFilter(data);
	    }
	});
}

function edit() {
	var rows = getSelectedSingleRow();
	var url='../workOvertime/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data){
		$('#eidt-window').window('open');
        $('#ff').form('clear');
        initForm();
		$('#ff').form('load', data);
		$('#ff').form.url='../workOvertime/edit?id=' + data.id;
		$('#companyList2').combotree('setValue', data.company.id);
		//设置字符
		$('#grid').datagrid('clearSelections');
	});
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
    var delUrlPrefix = '../workOvertime/delete';
    commonDeleteByIds(delUrlPrefix);
}

function stat(){
	var para =form2Json("search-window");
	var url='../workOvertime/stat';

	doAjax(para,url,'GET',false,function(data){
		$('#stat-window').window('open');
		var formData = {
			totalcount: data.totalCount,
            totalHours: data.totalHours,
            averageHours: data.averageHours
	    };
		$('#stat-form').form('load', formData);
	});
}

// 自动计算天数
function calculateHours(){
    var workStartTime=$('#workStartTime').val();
    var workEndTime =$('#workEndTime').val();
    var hours = hourDiff(workStartTime,workEndTime);
    $('#hours').numberspinner('setValue', hours);
}

function planStat(){
    getUserPlan('WorkOvertime');
}