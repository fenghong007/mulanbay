$(function() {
	// 加载公司树
    initForm();
	initGrid();
});

var dataUrl='/lifeExperience/getData';

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
            field : 'name',
            title : '名称',
            formatter : function(value, row, index) {
				return value+'<a href="javascript:statMap('+'\''+row.id+'\''+');"> [地图]</a>';
            },
            sortable : true
        }, {
            field : 'typeName',
            title : '类型',
            align : 'center'
        }, {
            field : 'days',
            title : '天数',
            align : 'center'
        }, {
            field : 'cost',
            title : '花费',
            formatter : function(value, row, index) {
                return formatMoneyWithSymbal(value);
            },
            align : 'center'
        }, {
            field : 'startDate',
            title : '开始日期',
            sortable : true
        }, {
            field : 'endDate',
            title : '结束日期',
            sortable : true
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

function showLocationDetail(msg){
    $('#grid').datagrid('uncheckAll');
    $.messager.alert('详情', msg, 'info');
}

function add() {
	$('#eidt-window').window('open');
	$('#ff').form('clear');
	initForm();
	$('#ff').form.url='../lifeExperience/create';
	$('#companyList1').combotree('setValue', 1);
	$('#companyList2').combotree('setValue', 1);
}

function initForm(){
	var a=1;
}

function edit() {
	var rows = getSelectedSingleRow();
	var url='../lifeExperience/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data){
		$('#eidt-window').window('open');
        $('#ff').form('clear');
        initForm();
		$('#ff').form('load', data);
		$('#ff').form.url='../lifeExperience/edit?id=' + data.id;
		//设置字符
		$('#grid').datagrid('clearSelections');
	});
}

function showAll() {
	refreshDatagrid(dataUrl,1,true);
}

function saveData() {
	var url='../lifeExperience/edit';
	if($("#id").val()==null||$("#id").val()==''){
		url='../lifeExperience/create';
    }
	doFormSubmit('ff',url,function(data){
		//closeWindow('eidt-window');
		$('#grid').datagrid('clearSelections');
        $("#id").val(data.id);
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
    var delUrlPrefix = '../lifeExperience/delete';
    commonDeleteByIds(delUrlPrefix);
}

function stat(){
	var para =form2Json("search-window");
	var url='../lifeExperience/stat';

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

function statMap(id){
    var url='../lifeExperience/transferMapByLifeExpStat?id='+id;
    doAjax(null,url,'GET',false,function(data){
        $('#grid').datagrid('clearSelections');
        $('#stat-window').window('open');
        createDoubleTransferMap(data);
    });
}

function planStat(){
    getUserPlan('LifeExperience');
}

//修正数据
function revise(reviseCost,reviseDays){
    $.messager.confirm('提示信息', '是否要自动修正数据', function(data) {
        if (data) {
            var id=$("#id").val();
            var para = {
                id: id,
                reviseCost:reviseCost,
                reviseDays:reviseDays
            };
            var url='../lifeExperience/revise';
            doAjax(para,url,'GET',true,function(data){
            });
        }else{
        }
    });

}