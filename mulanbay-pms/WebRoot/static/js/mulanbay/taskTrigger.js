$(function() {
	// 加载公司树
    initForm();
	initGrid();
});

var dataUrl='../taskTrigger/getData';
var isScheduleRun = false;

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
        onLoadSuccess : function(data) {
            isScheduleRun = data.isSchedule;
            if (isScheduleRun == false) {
                $('#statusBtn').linkbutton({
                    iconCls : 'icon-cancel',
                    text : '总调度已经停止',
                    onClick: function (){
                        changeSchedule(isScheduleRun,data.currentlyExecutingJobsCount);
                    }
                });
            } else {
                $('#statusBtn').linkbutton({
                    iconCls : 'icon-ok',
                    text : '总调度运行正常',
                    onClick: function (){
                        changeSchedule(isScheduleRun,data.currentlyExecutingJobsCount);
                    }
                });
            }
            $('#scheduleInfo').linkbutton({
                iconCls : 'icon-info',
                text : '正在执行的任务数(' + data.currentlyExecutingJobsCount + ')，已经被调度的任务数('+data.scheduleJobsCount+')'
            });
        },
		columns : [ [ {
			field : 'id',
			title : '记录号',
			sortable : true,
			align : 'center'
		}, {
            field : 'name',
            title : '名称'
        },{
            field : 'deployId',
            title : '部署点',
            align : 'center'
        } ,{
            field : 'triggerType',
            title : '调度周期',
            formatter : function(value, row, index) {
            	if(row.triggerIntervel==1){
            		return '每'+row.triggerTypeName+"一次";
				}else{
                    return '每'+row.triggerInterval+row.triggerTypeName+"一次";
                }
            },
            align : 'center'
        } ,{
            field : 'firstExecuteTime',
            title : '首次执行时间',
            align : 'center'
        } ,{
            field : 'nextExecuteTime',
            title : '下一次执行时间',
            formatter : function(value, row, index) {
                if(row.nextExecuteTime==null){
                    return row.firstExecuteTime;
                }else{
                    return value;
                }
            },
            align : 'center'
        } ,{
            field : 'tillNextExecuteTime',
            title : '距离下一次执行还有',
            formatter : function(value, row, index) {
                var ss=tillNowString(value);
                if(value<0||value<=60){
                    return '<font color="red">'+ss+'</font>';

                }else if(value<=3600){
                    return '<font color="#9acd32">'+ss+'</font>';

                }else if(value<=3600*24){
                    return '<font color="purple">'+ss+'</font>';

                }else {
                    return ss;

                }

            },
            align : 'center'
        },{
            field : 'triggerStatus',
            title : '调度状态',
            formatter : function(value, row, index) {
                return getStatusImage(value);
            },
            align : 'center'
        } ,{
            field : 'lastExecuteResult',
            title : '最后一次执行结果',
            formatter : function(value, row, index) {
                var name =row.lastExecuteResultName;
                if(value=='SUCESS'){
                    return name+'<img src="../static/image/tick.png"></img>';
                }else if(value=='FAIL'){
                    return name+'<img src="../static/image/cross.png"></img>';
                }else{
                    return name;
                }

            },
            align : 'center'
        } ,{
            field : 'lastExecuteTime',
            title : '最后一次执行时间',
            align : 'center'
        } ,{
            field : 'offsetDays',
            title : '日期偏移量',
            align : 'center'
        } ,{
            field : 'totalCount',
            title : '总执行次数',
            align : 'center'
        } ,{
            field : 'failCount',
            title : '总失败次数',
            align : 'center'
        },{
            field : 'groupName',
            title : '组名',
            align : 'center'
        },{
            field : 'userId',
            title : '类型',
            formatter : function(value, row, index) {
                if(value==0){
                    return '系统调度';
                }else{
                    return '用户调度';
                }
            },
            align : 'center'
        } ,{
            field : 'distriable',
            title : '支持分布式',
            formatter : function(value, row, index) {
                return getStatusImage(value);
            },
            align : 'center'
        },{
            field : 'redoTypeName',
            title : '重做类型',
            align : 'center'
        },{
            field : 'allowedRedoTimes',
            title : '允许重做次数',
            align : 'center'
        } ,{
            field : 'timeout',
            title : '超时时间(秒)',
            align : 'center'
        } ,{
            field : 'checkUnique',
            title : '需要检查唯一',
            formatter : function(value, row, index) {
                return getStatusImage(value);
            },
            align : 'center'
        } ,{
            field : 'uniqueTypeName',
            title : '唯一性类型',
            formatter : function(value, row, index) {
                if(value==null){
                    return '--';
                }else{

                }
                return value;
            },
            align : 'center'
        } ,{
            field : 'loggable',
            title : '记录日志',
            formatter : function(value, row, index) {
                return getStatusImage(value);
            },
            align : 'center'
        } ,{
            field : 'notifiable',
            title : '需要提醒',
            formatter : function(value, row, index) {
                return getStatusImage(value);
            },
            align : 'center'
        }  ,{
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
            id : 'manualExecBtn',
            text : '手动执行',
            iconCls : 'icon-refresh',
            handler : openManualExecWindow
        }, '-', {
            id : 'statusBtn',
            text : '调度状态加载中',
            iconCls : 'icon-loading'
        }, '-', {
            id : 'scheduleInfo',
            text : '调度信息加载中',
            iconCls : 'icon-loading',
            handler : showScheduleInfo
        } ]
	});
}

function add() {
	$('#eidt-window').window('open');
	$('#ff').form('clear');
	initForm();
	$('#ff').form.url='../taskTrigger/create';
    var formData = {
        timeout: 60,
        distriable: true,
        redoType: 'MUNUAL_REDO',
        allowedRedoTimes: 5,
        triggerInterval: 1,
        triggerType: 'HOUR',
        offsetDays: 0,
        triggerStatus: 'ENABLE',
        checkUnique: true,
        uniqueType:'IDENTITY',
        loggable: true,
        notifiable:false

    };
    $('#ff').form('load', formData);
}

function initForm(){
}

function edit() {
	var rows = getSelectedSingleRow();
	var url='../taskTrigger/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data){
		$('#eidt-window').window('open');
        $('#ff').form('clear');
        initForm();
		$('#ff').form('load', data);
		$('#ff').form.url='../taskTrigger/edit?id=' + data.id;
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
    var delUrlPrefix = '../taskTrigger/delete';
    commonDeleteByIds(delUrlPrefix);
}

function stat(){
	var para =form2JsonEnhanced("search-window");
	var url='../taskTrigger/stat';

	doAjax(para,url,'GET',false,function(data){
		$('#stat-window').window('open');
        //生成饼图
        createPieData(data);
	});
}

function planStat(){
    getUserPlan('taskTrigger');
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

function showScheduleInfo() {
    var vurl = '../taskTrigger/showScheduleInfo';
    doAjax(null, vurl, 'GET', false, function(data) {
        $('#schedule-info-window').window('open');
        $('#schedule-info-form').form('load', data);
    });
}

function openManualExecWindow() {
    $('#manual-exec-window').window('open');
    $('#taskTriggerList').combotree({
        url : '../taskTrigger/getTaskTriggerTree?needRoot=true',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        }
    });
}

function manualExec(){
    var para =form2JsonEnhanced("manual-exec-form");
    var url='../taskTrigger/manualNew';
    doAjax(para,url,'POST',true,function(data){
    });
}