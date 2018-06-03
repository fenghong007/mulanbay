$(function() {
	// 加载公司树
    initForm();
	initGrid();
});

var dataUrl='../userCalendar/getData';

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
            field : 'title',
            title : '标题'
        }, {
            field : 'delayCounts',
            title : '延迟次数',
            align : 'center'
        },{
            field : 'bussDay',
            title : '日期',
            align : 'center'
        },{
			field : 'expireTime',
			title : '失效时间',
            align : 'center'
        },{
            field : 'bussIdentityKey',
            title : '唯一标识',
            align : 'center'
        }, {
            field : 'sourceTypeName',
            title : '类型',
            align : 'center'
        }, {
            field : 'finishTypeName',
            title : '完成类型',
            align : 'center'
        },{
            field : 'finishedTime',
            title : '完成时间',
            align : 'center'
        }, {
            field : 'messageId',
            title : '消息内容',
            formatter : function(value, row, index) {
                if(value!=null){
                    return '<a href="javascript:showMessageDetail('+row.messageId+');"><img src="../static/image/info.png"></img></a>';
                }else{
                    return '--';
                }
            },
            align : 'center'
        } ,{
            field : 'createdTime',
            title : '创建时间',
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
}

function initForm(){
}

function edit() {
	var rows = getSelectedSingleRow();
	var url='../userCalendar/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data){
		$('#eidt-window').window('open');
        $('#ff').form('clear');
        initForm();
		$('#ff').form('load', data);
		$('#ff').form.url='../userCalendar/edit?id=' + data.id;
		//设置字符
		$('#grid').datagrid('clearSelections');
    });
}

function showAll() {
	refreshDatagrid(dataUrl,1,true);
}

function saveData() {
    var url='../userCalendar/edit';
    if($("#id").val()==null||$("#id").val()==''){
        url='../userCalendar/create';
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
    var delUrlPrefix = '../userCalendar/delete';
    (delUrlPrefix);
}

function stat(){
	var para =form2JsonEnhanced("search-window");
	var url='../userCalendar/stat';

	doAjax(para,url,'GET',false,function(data){
		$('#stat-window').window('open');
        //生成饼图
        createPieData(data);
	});
}

function planStat(){
    getUserPlan('userCalendar');
}

function getPlanConfigTree(){
    $('#planConfigList').combotree({
        url : '../planConfig/getPlanConfigTree?needRoot=true',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        }
    });
}

function refreshRate() {
    var url='../userCalendar/refreshRate';
    doAjax(null,url,'GET',false,function(data){
        $.messager.alert('成功', data, 'info');
        reloadDatagrid();
    });
}


function showMessageDetail(id) {
    $('#grid').datagrid('clearSelections');
    var url='../userMessage/getByUser?id='+ id;
    doAjax(null,url,'GET',false,function(data){
        $.messager.alert(data.title, data.content, 'info');
    });
}
