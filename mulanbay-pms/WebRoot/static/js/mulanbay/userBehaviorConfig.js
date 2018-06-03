$(function() {
	// 加载公司树
    initForm();
	initGrid();
});

var dataUrl='../userBehaviorConfig/getData';

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
            field : 'title',
            title : '标题'
        }, {
            field : 'behaviorTypeName',
            title : '计划类型',
            align : 'center'
        }, {
            field : 'unit',
            title : '值单位',
            align : 'center'
        }, {
            field : 'sqlTypeName',
            title : '查询类型',
            align : 'center'
        }, {
            field : 'orderIndex',
            title : '排序号',
            align : 'center'
        },{
            field : 'status',
            title : '状态',
            formatter : function(value, row, index) {
                return getStatusImage(value);
            },
            align : 'center'
        },{
            field : 'dateRegion',
            title : '多天数据',
            formatter : function(value, row, index) {
                return getStatusImage(value);
            },
            align : 'center'
        },{
            field : 'keywords',
            title : '支持搜索',
            formatter : function(value, row, index) {
                if(value!=null){
                    return '<img src="../static/image/tick.png"></img>';
                }else {
                    return '<img src="../static/image/cross.png"></img>';
                }
            },
            align : 'center'
        },{
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

function getUserBehaviorConfigTree(){
    $('#userBehaviorConfigList').combotree({
        url : '../userBehaviorConfig/getUserBehaviorConfigTree?needRoot=true',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        },
        onClick: function(node){
            //加载模板内容
            var url='../userBehaviorConfig/get?id='+ node.id;
            doAjax(null,url,'GET',false,function(data){
                $('#ff').form('clear');
                $('#ff').form('load', data);
            });
        }
    });
}


function add() {
	$('#eidt-window').window('open');
	$('#ff').form('clear');
	initForm();
    getUserBehaviorConfigTree();
    $('#userBehaviorConfigList').combotree('enable');
    var formData = {
        planType: 'MONTH',
        sqlType: 'SQL',
        planValue: 0,
        compareType:'MORE',
        planType:'MONTH',
        status:'ENABLE',
        orderIndex :1
    };
    $('#ff').form('load', formData);
    initGridStatValueConfig(0,'BEHAVIOR');
}

function initForm(){
    $('#behaviorTypeList').combotree({
        url : '../userBehaviorConfig/getUserBehaviorTypeTree?needRoot=true',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        }
    });
    $('#behaviorTypeList2').combotree({
        url : '../userBehaviorConfig/getUserBehaviorTypeTree',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        }
    });
}

function edit() {
	var rows = getSelectedSingleRow();
	var url='../userBehaviorConfig/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data){
		$('#eidt-window').window('open');
        $('#ff').form('clear');
        initForm();
        $('#userBehaviorConfigList').combotree('disable');
        $('#ff').form('load', data);
        initGridStatValueConfig(data.id,'BEHAVIOR');
        //设置字符
		$('#grid').datagrid('clearSelections');
    });
}

function showAll() {
	refreshDatagrid(dataUrl,1,true);
}

function saveData() {
    var url='../userBehaviorConfig/edit';
    if($("#id").val()==null||$("#id").val()==''){
        url='../userBehaviorConfig/create';
    }
	doFormSubmit('ff',url,function(data){
		closeWindow('eidt-window');
		$('#grid').datagrid('clearSelections');
        $('#ff').form('clear');
        reloadDatagrid();
        $('#ff').form('load', data);
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
    var delUrlPrefix = '../userBehaviorConfig/delete';
    commonDeleteByIds(delUrlPrefix);
}

function showDateRegionHelp() {
    var msg='<font color="red">注意</font>：<br/>' +
        '(1)表示返回数据的值是否属于时间区段<br/>'+
        '(2)比如旅行类，返回日期是2017-01-01，但是实际上延续了好几天';
    $.messager.alert('提示', msg, '');
}

function showSqlHelp() {
    var msg='<font color="red">注意</font>：<br/>' +
        '(1)必须返回三列<br/>'+
        '(2)三列的列名分别为:date,name,value<br/>'+
        '(3)最后一列的value需要取整，不要小数，代码中直接以Long类型转换';
    $.messager.alert('提示', msg, '');
}