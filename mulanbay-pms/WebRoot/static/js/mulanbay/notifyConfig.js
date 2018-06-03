$(function() {
	// 加载公司树
    initForm();
	initGrid();
});

var dataUrl='../notifyConfig/getData';

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
            field : 'sqlTypeName',
            title : '查询类型'
        }, {
            field : 'resultTypeName',
            title : '返回结果类型'
        }, {
            field : 'valueTypeName',
            title : '值单位类型'
        }, {
            field : 'orderIndex',
            title : '排序号'
        }, {
            field : 'notifyTypeName',
            title : '提醒类型'
        }, {
            field : 'level',
            title : '等级',
            align : 'center'
        },{
            field : 'rewardPoint',
            title : '积分奖励',
            align : 'center'
        },{
            field : 'status',
            title : '状态',
            formatter : function(value, row, index) {
                return getStatusImage(value);
            },
            align : 'center'
        },{
            field : 'relatedBeans',
            title : '绑定的类',
            align : 'center'
        }, {
            field : 'userBand',
            title : '绑定用户',
            formatter : function(value, row, index) {
                if (value == true) {
                    return '<img src="../static/image/tick.png"></img>';
                }else {
                    return '--';
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

function getNotifyConfigTree(){
    $('#notifyConfigList').combotree({
        url : '../notifyConfig/getNotifyConfigTree?needRoot=true',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        },
        onClick: function(node){
            //加载模板内容
            var url='../notifyConfig/get?id='+ node.id;
            doAjax(null,url,'GET',false,function(data){
                $('#ff').form('clear');
                data.id=null;
                $('#ff').form('load', data);
            });
        }
    });
}


function add() {
	$('#eidt-window').window('open');
	$('#ff').form('clear');
	initForm();
    getNotifyConfigTree();
    $('#notifyConfigList').combotree('enable');
    var formData = {
        sqlType: 'SQL',
        resultType: 'DATE',
        valueType: 'DAY',
        status:'ENABLE',
        notifyType: 'WARN',
        level:3,
        rewardPoint:0,
        orderIndex :1
    };
    $('#ff').form('load', formData);
    initGridStatValueConfig(0,'NOTIFY');
}

function initForm(){
    getRelatedBeansTree();
}

function getRelatedBeansTree(){
    $('#relatedBeansList').combotree({
        url : '../common/getBussTypeTree',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        }
    });
}

function edit() {
	var rows = getSelectedSingleRow();
	var url='../notifyConfig/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data){
		$('#eidt-window').window('open');
        $('#ff').form('clear');
        initForm();
        //getNotifyConfigTree();
        $('#notifyConfigList').combotree('disable');
        $('#ff').form('load', data);
		$('#ff').form.url='../notifyConfig/edit?id=' + data.id;
        initGridStatValueConfig(data.id,'NOTIFY');
        //设置字符
		$('#grid').datagrid('clearSelections');
	});
}

function showAll() {
	refreshDatagrid(dataUrl,1,true);
}

function saveData() {
    var url='../notifyConfig/edit';
    if($("#id").val()==null||$("#id").val()==''){
        url='../notifyConfig/create';
    }
	doFormSubmit('ff',url,function(){
		//closeWindow('eidt-window');
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
    var delUrlPrefix = '../notifyConfig/delete';
    commonDeleteByIds(delUrlPrefix);
}
