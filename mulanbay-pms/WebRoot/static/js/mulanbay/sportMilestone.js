$(function() {
	// 加载公司树
    initForm();
	initGrid();
});

var dataUrl='../sportMilestone/getData';

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
            title : '名称',
            formatter : function(value, row, index) {
                if(row.sportExercise!=null){
                    return '<font color="green">'+value+'</font>'+'<img src="../static/image/tick.png"></img>';
                }else {
                    return value;
                }
            },
        }, {
            field : 'sportType.id',
            title : '运动类型',
            formatter : function(value, row, index) {
                return row.sportType.name;
            }
        }, {
            field : 'alais',
            title : '别名'
        }, {
            field : 'kilometres',
            title : '公里数',
            align : 'center'
        }, {
            field : 'minutes',
            title : '锻炼时长(分钟)',
            align : 'center'
        }, {
            field : 'sportExercise.id',
            title : '达成目标的锻炼信息',
            formatter : function(value, row, index) {
            	if(row.sportExercise!=null){
                    return row.sportExercise.exerciseDate+'('+row.sportExercise.kilometres+'公里,'+row.sportExercise.minutes+'分钟)';
                }else {
            		return '--';
				}
            },
            align : 'center'
        }, {
            field : 'fromExerciseDate',
            title : '开始日期',
            align : 'center'
        }, {
            field : 'toExerciseDate',
            title : '实现日期',
            align : 'center'
        }, {
            field : 'costDays',
            title : '花费天数',
            align : 'center'
        },{
            field : 'orderIndex',
            title : '排序号',
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
			id : 'searchBtn',
			text : '刷新',
			iconCls : 'icon-refresh',
			handler : showAll
		}, '-', {
            id : 'refreshMilestoneBtn',
            text : '刷新里程碑',
            iconCls : 'icon-refresh',
            handler : openRefreshMilestone
        },{
            id : 'statBtn',
            text : '统计',
            iconCls : 'icon-stat',
            handler : getJsonData
        } ]
	});
}

function add() {
	$('#eidt-window').window('open');
	$('#ff').form('clear');
	initForm();
	$('#ff').form.url='../sportMilestone/create';
}

function initForm(){
    $('#sportTypeList').combotree({
        url : '../sportType/getSportTypeTree?needRoot=true',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        }
    });
    //$('#sportTypeList').combotree('setValue', 1);
    $('#sportTypeList2').combotree({
        url : '../sportType/getSportTypeTree?needRoot=true',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        }
    });
    //$('#sportTypeList2').combotree('setValue', 1);
}

function edit() {
	var rows = getSelectedSingleRow();
	var url='../sportMilestone/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data){
		$('#eidt-window').window('open');
        $('#ff').form('clear');
        initForm();
		$('#ff').form('load', data);
		$('#ff').form.url='../sportMilestone/edit?id=' + data.id;
		//设置字符
		$('#grid').datagrid('clearSelections');
        $('#sportTypeList2').combotree('setValue', data.sportType.id);
    });
}

function showAll() {
	refreshDatagrid(dataUrl,1,true);
}

function saveData() {
    var url='../sportMilestone/edit';
    if($("#id").val()==null||$("#id").val()==''){
        url='../sportMilestone/create';
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
    var delUrlPrefix = '../sportMilestone/delete';
    commonDeleteByIds(delUrlPrefix);
}

function openRefreshMilestone() {
    $('#refresh-window').window('open');
    $('#sportTypeList3').combotree({
        url : '../sportType/getSportTypeTree',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        }
    });
    $('#sportTypeList3').combotree('setValue', 1);

}

function refreshMilestone() {
    var vurl = '../sportMilestone/refresh';
    var para = form2Json("refresh-form");
    doAjax(para, vurl, 'GET',true, function(data) {
        reloadDatagrid();
    });
}