$(function() {
	// 加载公司树
    initForm();
	initGrid();
});

var dataUrl='../user/getData';

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
            title : '用户编号',
            sortable : true,
            align : 'center'
        }, {
            field : 'username',
            title : '用户名'
        }, {
            field : 'nickname',
            title : '昵称'
        },{
            field : 'phone',
            title : '手机',
            sortable : true,
            align : 'center'
        }  ,{
            field : 'level',
            title : '等级',
            sortable : true,
            align : 'center'
        } ,{
            field : 'points',
            title : '积分',
            sortable : true,
            align : 'center'
        },{
            field : 'email',
            title : '邮箱',
            sortable : true,
            align : 'center'
        },{
            field : 'status',
            title : '状态',
            formatter : function(value, row, index) {
                return getStatusImage(value);
            },
            align : 'center'
        } ,{
            field : 'expireTime',
            title : '过期时间',
            sortable : true,
            align : 'center'
        }  ,{
            field : 'lastLoginTime',
            title : '最后登陆时间',
            sortable : true,
            align : 'center'
        } ,{
            field : 'lastLoginIp',
            title : '最后登陆IP',
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
            id : 'logoutBtn',
            text : '注销',
            iconCls : 'icon-edit',
            handler : logout
        }, '-', {
            id : 'searchBtn',
            text : '刷新',
            iconCls : 'icon-refresh',
            handler : showAll
        } ]
    });
}

function logout(){
    $.messager.confirm('提示信息', '您确认要退出系统吗?', function(data) {
        if (data) {
            parent.location.href='../main/logout';
        }
    });
}

function add() {
	$('#eidt-window').window('open');
	$('#ff').form('clear');
	initForm();
	$('#ff').form.url='../user/create';
}

function initForm(){

}
function edit() {
    var rows = getSelectedSingleRow();
    var url='../user/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data){
		$('#eidt-window').window('open');
        $('#ff').form('clear');
        initForm();
		$('#ff').form('load', data);
		$('#ff').form.url='../user/edit?id=' + data.id;
        $("#password").textbox('setValue',null);
        //设置字符
		$('#grid').datagrid('clearSelections');
	});
}

function showAll() {
    refreshDatagrid(dataUrl,1,true);
}

function saveData() {
    var url='../user/edit';
    if($("#id").val()==null||$("#id").val()==''){
        url='../user/create';
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
    var delUrlPrefix = '../user/delete';
    commonDeleteByIds(delUrlPrefix);
}
