$(function() {
	// 加载公司树
    initForm();
	initGrid();
});

var dataUrl='../businessTrip/getData';

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
			formatter : function(value, row, index) {
				return row.company.name;
			}
		}, {
            field : 'tripDate',
            title : '出差日期'
        }, {
            field : 'country',
            title : '国家'
        }, {
            field : 'province',
            title : '省份'
        }, {
            field : 'city',
            title : '城市'
        },{
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
			id : 'statBtn',
			text : '统计',
			iconCls : 'icon-stat',
			handler : stat
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
	$('#ff').form.url='../businessTrip/create';
	$('#companyList1').combotree('setValue', 1);
	$('#companyList2').combotree('setValue', 1);
}

function initForm(){
	$('#companyList1').combotree({
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
	var url='../businessTrip/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data){
		$('#eidt-window').window('open');
        $('#ff').form('clear');
        initForm();
		$('#ff').form('load', data);
		$('#companyList2').combotree('setValue', data.company.id);
		//设置字符
		$('#grid').datagrid('clearSelections');
	});
}

function showAll() {
	refreshDatagrid(dataUrl,1,true);
}

function saveData() {
    var url='../businessTrip/edit';
    if($("#id").val()==null||$("#id").val()==''){
        url='../businessTrip/create';
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
    var delUrlPrefix = '../businessTrip/delete';
    commonDeleteByIds(delUrlPrefix);
}

function stat(){
	var para =form2Json("search-window");
	var url='../company/stat';

	doAjax(para,url,'GET',false,function(data){
		$('#stat-window').window('open');
		var formData = {
			totalcount: data.totalCount,
			totalshipment: formatMoneyWithSymbal(data.totalShipment),
			mtotalPrice: formatMoneyWithSymbal(data.totalPrice)
	    };
		$('#stat-form').form('load', formData);
	});
}