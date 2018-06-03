$(function() {
	// 加载公司树
    initForm();
	initGrid();
});

var dataUrl='../reportConfig/getData';

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
            field : 'resultColumns',
            title : '返回结果列数',
            align : 'center'
        }, {
            field : 'warningValue',
            title : '告警值',
            align : 'center'
        }, {
            field : 'alertValue',
            title : '警报值',
            align : 'center'
        }, {
            field : 'orderIndex',
            title : '排序号'
        },{
            field : 'status',
            title : '状态',
            formatter : function(value, row, index) {
                return getStatusImage(value);
            },
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
        }, {
            field : 'level',
            title : '等级',
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

function getReportConfigTree(){
    $('#reportConfigList').combotree({
        url : '../reportConfig/getReportConfigTree?needRoot=true',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        },
        onClick: function(node){
            //加载模板内容
            var url='../reportConfig/get?id='+ node.id;
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
    getReportConfigTree();
    $('#reportConfigList').combotree('enable');
    $('#ff').form.url='../reportConfig/create';
    var formData = {
        userBand: true,
        status:'ENABLE',
        orderIndex:1,
        level:3
    };
    $('#ff').form('load', formData);
    initGridStatValueConfig(0,'REPORT');
}

function initForm(){
}

function edit() {
	var rows = getSelectedSingleRow();
	var url='../reportConfig/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data){
		$('#eidt-window').window('open');
        $('#ff').form('clear');
        initForm();
        //getreportConfigTree();
        $('#reportConfigList').combotree('disable');
        $('#ff').form('load', data);
		$('#ff').form.url='../reportConfig/edit?id=' + data.id;
        initGridStatValueConfig(data.id,'REPORT');
        //设置字符
		$('#grid').datagrid('clearSelections');
	});
}

function showAll() {
	refreshDatagrid(dataUrl,1,true);
}

function saveData() {
    var url='../reportConfig/edit';
    if($("#id").val()==null||$("#id").val()==''){
        url='../reportConfig/create';
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
    var delUrlPrefix = '../reportConfig/delete';
    commonDeleteByIds(delUrlPrefix);
}

