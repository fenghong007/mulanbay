$(function() {
	// 加载公司树
    initForm();
	initGrid();
});

var dataUrl='../dream/getData';

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
                if (row.planConfig) {
                    return '<font color="green">★'+value+'</font>';
                } else {
                    return value;
                }
            }
        },{
			field : 'difficulty',
			title : '困难等级',
            sortable : true,
            formatter : function(value, row, index) {
			    var s = getStar(value);
                if (value >=8) {
                    return '<font color="red">'+s+'</font>';
                }else {
                    return s;
                }
            },
            align : 'center'
        } ,{
            field : 'importantLevel',
            title : '重要等级',
            sortable : true,
            formatter : function(value, row, index) {
                var s = getStar(value);
                if (value >=4.5) {
                    return '<font color="red">'+s+'</font>';
                }else {
                    return s;
                }
            },
            align : 'center'
        } ,{
            field : 'expectDays',
            title : '预计花费天数',
            align : 'center'
        } ,{
            field : 'statusName',
            title : '状态',
            align : 'center',
            width : 70,
            formatter : function(value, row, index) {
                if (row.status == 'FINISHED') {
                    return '<img src="../static/image/tick.png"></img>'+value;
                } else if (row.status == 'GIVEDUP') {
                    return '<img src="../static/image/cross.png"></img>'+value;
                } else if (row.status == 'PAUSED') {
                    return '<img src="../static/image/time_delete.png"></img>'+value;
                } else if (row.status == 'STARTED') {
                    return '<font color="green">'+value+'</font>';
                } else {
                    return value;
                }
            }

        } ,{
            field : 'rate',
            title : '完成百分比',
            sortable : true,
            formatter : function(value, row, index) {
                if (value>80) {
                    return '<font color="green">'+value+'</font>';
                } else {
                    return value;
                }
            },
            align : 'center'
        } ,{
            field : 'proposedDate',
            title : '期望实现日期',
            align : 'center'
        },{
            field : 'leftDays',
            title : '剩余时间',
            sortable : true,
            align : 'center',
            formatter : function(value, row, index) {
                if (value==0) {
                    return '--';
                } else if (value>0) {
                	if(value<30){
                        return '<img src="../static/image/warn.png"></img>'+formatterLeftDays(value);
                    }else if(value<180){
                        return '<font color="#a52a2a">'+formatterLeftDays(value)+'</font>';
                    }else{
                        return formatterLeftDays(value);
                    }
                } else {
                    return '<img src="../static/image/warn.png"></img>已过去'+formatterLeftDays(0-value);
                }
            }
        },{
            field : 'emergencyScore',
            title : '紧急分',
            sortable : true,
            align : 'center'
        },{
            field : 'deadline',
            title : '最晚截止日期',
            align : 'center'
        } ,{
            field : 'delays',
            title : '延期次数',
            formatter : function(value, row, index) {
                if (value==1) {
                    return '<font color="purple">'+value+'</font>';
                }if (value>1) {
                    return '<font color="red">'+value+'</font>';
                } else {
                    return value;
                }
            },
            align : 'center'
        },{
            field : 'finishedDate',
            title : '完成时间',
            align : 'center'
        } ,{
            field : 'planConfig',
            title : '关联计划',
            sortable : true,
            formatter : function(value, row, index) {
                if (row.planConfig) {
                    return row.planConfig.name;
                } else {
                    return '--';
                }
            },
            align : 'center'
        },{
            field : 'planValue',
            title : '计划期望值',
            sortable : true,
            align : 'center'
        } , {
            field : 'minMoney',
            title : '最小资金(元)'
        }, {
            field : 'maxMoney',
            title : '最大资金(元)'
        },{
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
		}, '-', {
            id : 'refreshRateBtn',
            text : '刷新进度',
            iconCls : 'icon-refresh',
            handler : refreshRate
        } ]
	});
}

function formatterLeftDays(days){
    var time = '';
    if (days >= 365) {
        time += parseInt(days / 365) + '年';
        days %= 365;
    }
    if (days >= 30) {
        time += parseInt(days / 30) + '月';
        days %= 30;
    }
    if (days > 0) {
        time += parseInt(days) + '天';
    }
    return time;
}

function add() {
	$('#eidt-window').window('open');
	$('#ff').form('clear');
	initForm();
	$('#ff').form.url='../dream/create';
    var formData = {
        difficulty: 5,
        importantLevel: 3,
        rate: 0,
        status:'CREATED',
        expectDays:0
    };
    $('#ff').form('load', formData);
    getUserPlanList();
}

function initForm(){
}

function edit() {
	var rows = getSelectedSingleRow();
	var url='../dream/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data){
		$('#eidt-window').window('open');
        $('#ff').form('clear');
        initForm();
		$('#ff').form('load', data);
		$('#ff').form.url='../dream/edit?id=' + data.id;
		//设置字符
		$('#grid').datagrid('clearSelections');
        getUserPlanList();
        if(data.userPlan){
            $('#userPlanList').combotree('setValue',data.userPlan.id);
        }
    });
}

function showAll() {
	refreshDatagrid(dataUrl,1,true);
}

function saveData() {
    var url='../dream/edit';
    if($("#id").val()==null||$("#id").val()==''){
        url='../dream/create';
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
    var delUrlPrefix = '../dream/delete';
    commonDeleteByIds(delUrlPrefix);
}

function stat(){
	var para =form2JsonEnhanced("search-window");
	var url='../dream/stat';

	doAjax(para,url,'GET',false,function(data){
		$('#stat-window').window('open');
        //生成饼图
        createPieData(data);
	});
}

function planStat(){
    getUserPlan('Dream');
}

function getUserPlanList(){
    $('#userPlanList').combotree({
        url : '../userPlan/getPlanConfigTree?needRoot=true',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        }
    });
}

function refreshRate() {
    var url='../dream/refreshRate';
    doAjax(null,url,'GET',false,function(data){
        $.messager.alert('成功', data, 'info');
        reloadDatagrid();
    });
}

function showPlanValueHelp() {
    var msg='<font color="red">注意</font>：<br/>' +
        '(1)表达的是为了实现这个梦想需要达到的预期值<br/>'+
        '(2)比如学习尤克里里至少要学1000个小时，那么在这里就填1000，系统到时自动会和计划配置里的统计比对<br/>'+
        '(3)单位类型目前不需要配置';
    $.messager.alert('提示', msg, '');
}