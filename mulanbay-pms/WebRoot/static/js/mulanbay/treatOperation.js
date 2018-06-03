$(function() {
	// 加载公司树
    initForm();
	initGrid();
});

var dataUrl='../treatOperation/getData';

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
            field : 'treatRecord.hospital',
            title : '医院',
            sortable : true,
            formatter : function(value, row, index) {
                return row.treatRecord.hospital;
            }
        }, {
            field : 'treatDate',
            title : '看病日期',
            sortable : true
        }, {
            field : 'treatRecord.disease',
            title : '疾病',
            sortable : true,
            formatter : function(value, row, index) {
                return row.treatRecord.disease;
            }
        }, {
            field : 'name',
            title : '手术名称'
        }, {
            field : 'fee',
            title : '费用',
            align : 'center',
            formatter : function(value, row, index) {
                return formatMoneyWithSymbal(value);
            }
        }, {
            field : 'available',
            title : '是否有效',
            formatter : function(value, row, index) {
                return getStatusImage(value);
            },
            align : 'center'
        }, {
            field : 'isSick',
            title : '是否有病',
            align : 'center',
            formatter : function(value, row, index) {
                if (value == true) {
                    return '<img src="../static/image/warn.png"></img>';
                } else {
                    return '--';
                }
            }
        }] ],
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
	$('#ff').form.url='../workOvertime/create';
}

function initForm(){

}

function edit() {
	var rows = getSelectedSingleRow();
	var url='../treatOperation/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data){
		$('#eidt-window').window('open');
        $('#ff').form('clear');
        initForm();
		$('#ff').form('load', data);
		$('#ff').form.url='../treatOperation/edit?id=' + data.id;
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
		showAll();
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
    var delUrlPrefix = '../treatOperation/delete';
    commonDeleteByIds(delUrlPrefix);
}

function stat(){
    var para =form2Json("search-window");
    var url='../treatOperation/stat';

    doAjax(para,url,'GET',false,function(data){
        $('#stat-window').window('open');
        $('#pg').datagrid({
            url: '../treatOperation/aa',
            showGroup: true,
            scrollbarSize: 0,
            columns:[[
                {field:'name',title:'手术名',width:150,align:'center'},
                {field:'totalCount',title:'次数',width:100,align:'center'},
                {field:'totalFee',title:'花费（元）',width:100,align:'center'}
            ]]
        });
        //清空所有数据
        $('#pg').datagrid('loadData', { total: 0, rows: [] });
        for (var i = 0; i < data.length; i++) {
        	var name =data[i].name;
        	if(i==data.length-1){
        		name='<font color="red">'+data[i].name+'</font>';
			}
            var row = {
                name:name,
                totalCount:data[i].totalCount,
                totalFee:data[i].totalFee
            };
            $('#pg').datagrid('appendRow',row);
        }
    });
}