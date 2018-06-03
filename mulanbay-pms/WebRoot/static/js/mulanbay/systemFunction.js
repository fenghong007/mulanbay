$(function() {
	// 加载公司树
    //initForm();
	initGrid();
});

var dataUrl='../systemFunction/getData';

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
            field : 'parent.name',
            title : '上层功能点名称',
            formatter : function(value, row, index) {
                if (row.parent) {
                    return row.parent.name;
                } else {
                    return '--';
                }
            },
            align : 'center'
        },{
			field : 'urlAddress',
			title : '请求地址',
            align : 'center'
        },{
            field : 'supportMethods',
            title : '支持的请求方式',
            align : 'center'
        },{
            field : 'urlTypeName',
            title : '请求地址类型',
            align : 'center'
        },{
            field : 'functionTypeName',
            title : '功能类型',
            align : 'center'
        },{
            field : 'functionDataTypeName',
            title : '数据类型',
            align : 'center'
        },{
            field : 'diffUser',
            title : '数据区分用户',
            formatter : function(value, row, index) {
                return getStatusImage(value);
            },
            align : 'center'
        },{
            field : 'loginAuth',
            title : '登陆验证',
            formatter : function(value, row, index) {
                return getStatusImage(value);
            },
            align : 'center'
        },{
            field : 'permissionAuth',
            title : '权限验证',
            formatter : function(value, row, index) {
                return getStatusImage(value);
            },
            align : 'center'
        },{
            field : 'ipAuth',
            title : 'IP验证',
            formatter : function(value, row, index) {
                return getStatusImage(value);
            },
            align : 'center'
        },{
            field : 'autoLogin',
            title : '自动登陆',
            formatter : function(value, row, index) {
                return getStatusImage(value);
            },
            align : 'center'
        },{
            field : 'requestLimit',
            title : '请求限制',
            formatter : function(value, row, index) {
                return getStatusImage(value);
            },
            align : 'center'
        },{
            field : 'requestLimitPeriod',
            title : '请求限制秒数',
            align : 'center'
        },{
            field : 'dayLimit',
            title : '每天限制次数',
            align : 'center'
        },{
            field : 'doLog',
            title : '记录日志',
            formatter : function(value, row, index) {
                return getStatusImage(value);
            },
            align : 'center'
        },{
            field : 'triggerStat',
            title : '用户触发',
            formatter : function(value, row, index) {
                return getStatusImage(value);
            },
            align : 'center'
        },{
            field : 'rewardPoint',
            title : '积分奖励',
            align : 'center'
        },{
            field : 'recordReturnData',
            title : '记录返回数据',
            formatter : function(value, row, index) {
                return getStatusImage(value);
            },
            align : 'center'
        },{
            field : 'groupId',
            title : '分组号',
            align : 'center'
        },{
            field : 'errorCode',
            title : '错误代码',
            align : 'center'
        } ,{
            field : 'orderIndex',
            title : '排序号',
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
            id : 'searchBtn',
            text : '刷新系统缓存',
            iconCls : 'icon-refresh',
            handler : refreshSystemConfig
        }, '-', {
            id : 'searchBtn',
            text : '自动配置功能点',
            iconCls : 'icon-refresh',
            handler : initUnConfigedFunctions
        } ]
	});
}

function add() {
	$('#eidt-window').window('open');
	$('#ff').form('clear');
	initForm();
    var formData = {
        urlType: 'NORMAL',
        functionType: 'CREATE',
        functionDataType: 'NORMAL',
        loginAuth: true,
        permissionAuth: false,
        ipAuth: false,
        autoLogin: false,
        requestLimit: false,
        requestLimitPeriod :5,
        doLog: true,
        triggerStat: false,
        diffUser:true,
        idField:'id',
        idFieldType:'LONG',
        status:'ENABLE',
        orderIndex :1,
        rewardPoint:0,
        groupId:1,
        dayLimit:0,
        recordReturnData:false
    };
    $('#ff').form('load', formData);
	$('#ff').form.url='../systemFunction/create';
}


function initForm(){
    $('#parentSystemFunctionList').combotree({
        url : '../systemFunction/getSystemFunctionMenuTree',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        }
    });
}

function edit() {
	var rows = getSelectedSingleRow();
	var url='../systemFunction/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data){
		$('#eidt-window').window('open');
        $('#ff').form('clear');
        initForm();
		$('#ff').form('load', data);
		$('#ff').form.url='../systemFunction/edit?id=' + data.id;
        $('#parentSystemFunctionList').combotree('setValue', data.parentId);
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
    var delUrlPrefix = '../systemFunction/delete';
    commonDeleteByIds(delUrlPrefix);
}

function refreshSystemConfig() {
    var url='../systemFunction/refreshSystemConfig';
    doAjax(null,url,'GET',true,function(data){
    });
}

function initUnConfigedFunctions() {
    var url='../systemFunction/initUnConfigedFunctions';
    doAjax(null,url,'GET',true,function(data){
        reloadDatagrid();
    });
}
