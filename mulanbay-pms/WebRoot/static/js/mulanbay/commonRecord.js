$(function() {
    initForm();
	initGrid();
});

var dataUrl='../commonRecord/getData';

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
            field : 'commonRecordType.id',
            title : '类型',
            sortable : true,
            formatter : function(value, row, index) {
                if(row.commonRecordType){
                    return row.commonRecordType.name;
                }else {
                    return '--';
                }
            },
        },{
            field : 'name',
            title : '名称',
            align : 'center'
        }, {
            field : 'occurTime',
            title : '发生时间',
            sortable : true
        },{
			field : 'value',
			title : '值',
            align : 'center'
        }, {
            field : 'commonRecordType.unit',
            title : '单位',
            sortable : true,
            formatter : function(value, row, index) {
                if(row.commonRecordType){
                    return row.commonRecordType.unit;
                }else {
                    return '--';
                }
            },
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

function initForm(){
    $('#commonRecordTypeList').combotree({
        url : '../commonRecordType/getCommonRecordTypeTree?needRoot=true',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        }
    });
    $('#commonRecordTypeList2').combotree({
        url : '../commonRecordType/getCommonRecordTypeTree',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        },
        onChange : function (newValue, oldValue) {
            loadCommonRecordType(newValue);
        }
    });
}

function loadCommonRecordType(id) {
    if(id==null||id==''){
        return;
    }
    var url='../commonRecordType/get?id='+ id;
    doAjax(null,url,'GET',false,function(data) {
        document.getElementById("unitName").innerText=data.unit;
        $('#name').val(data.name);
    });
}

function add() {
	$('#eidt-window').window('open');
	$('#ff').form('clear');
	$('#ff').form.url='../commonRecord/create';
    initForm();
}

function edit() {
	var rows = getSelectedSingleRow();
	var url='../commonRecord/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data) {
        $('#eidt-window').window('open');
        $('#ff').form('clear');
        initForm();
        $('#ff').form('load', data);
        $('#ff').form.url = '../commonRecord/edit?id=' + data.id;
        $('#commonRecordTypeList2').combotree('setValue', data.commonRecordType.id);
        //设置字符
        $('#grid').datagrid('clearSelections');
    });
}

function showAll() {
	refreshDatagrid(dataUrl,1,true);
}

function saveData() {
    //自动设置，原来在增加与修改时设置经常出问题
    var url='../commonRecord/edit';
    if($("#id").val()==null||$("#id").val()==''){
        url='../commonRecord/create';
    }
	doFormSubmit('ff',url,function(data){
		closeWindow('eidt-window');
        $('#ff').form('clear');
        $('#ff').form('load', data);
        //$('#ff').form.url = '../commonRecord/edit?id=' + data.id;
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
    var delUrlPrefix = '../commonRecord/delete';
    commonDeleteByIds(delUrlPrefix);
}

function stat(){
	var para =form2Json("search-window");
	var url='../commonRecord/stat';

	doAjax(para,url,'GET',false,function(data){
		$('#stat-window').window('open');
		var formData = {
			totalcount: data.totalCount,
            totalHours: minuteToHour(data.totalMinutes,1),
            averageValue: minuteToHour(data.averageMinutes,1)+"(小时),"+data.averageMinutes+"(分钟)"
        };
		$('#stat-form').form('load', formData);
        //生成饼图
        createMyPieData(data.pieData);
	});
}

function planStat(){
    getUserPlan('CommonRecord');
}
