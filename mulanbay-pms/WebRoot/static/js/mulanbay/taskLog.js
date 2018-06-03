$(function() {
	// 加载公司树
    loadTaskTriggerList();
	initGrid();
});

var dataUrl='../taskLog/getData';

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
        onLoadError: loadDataError,
		columns : [ [ {
			field : 'id',
			title : '记录号',
			sortable : true,
			align : 'center'
		},{
            field : 'taskTrigger.name',
            title : '调度名称',
            sortable : true,
            formatter : function(value, row, index) {
                return row.taskTrigger.name;
            },
            align : 'center'
        },{
            field : 'bussDate',
            title : '业务日期',
            align : 'center'
        },{
            field : 'scheduleIdentityId',
            title : '调度执行ID',
            align : 'center'
        },{
			field : 'startTime',
			title : '运行开始时间',
            align : 'center'
        },{
            field : 'endTime',
            title : '运行结束时间',
            align : 'center'
        },{
            field : 'costTime',
            title : '花费时间',
            formatter : function(value, row, index) {
                if(value<1000){
                    return value+'毫秒';
                }else{
                    var ss=tillNowString(value/1000);
                    if(value>10*60*1000){
                        return '<font color="red">'+ss+'</font>';
                    }else {
                        return ss;

                    }
                }

            },
            align : 'center'
        } ,{
            field : 'executeResult',
            title : '执行结果',
            formatter : function(value, row, index) {
                var name ='<a href="javascript:showFullDetail('+'\''+row.logComment+'\''+');"> '+row.executeResultName+'</a>';
                if(value=='SUCESS'){
                    return name+'<img src="../static/image/tick.png"></img>';
                }else if(value=='FAIL'){
                    return name+'<img src="../static/image/cross.png"></img>';
                }else{
                    return name;
                }

            },
            align : 'center'
        }  ,{
            field : 'deployId',
            title : '运行服务器节点',
            align : 'center'
        } ,{
            field : 'ipAddress',
            title : '服务器IP',
            align : 'center'
        } ,{
            field : 'redoTimes',
            title : '重做次数',
            align : 'center'
        } ,{
            field : 'lastStartTime',
            title : '最后一次重做开始时间',
            align : 'center'
        },{
            field : 'lastEndTime',
            title : '最后一次重做结束时间',
            align : 'center'
        } ] ],
		pagination : true,
		rownumbers : true,
		singleSelect : false,
		toolbar : [ {
			id : 'searchBtn',
			text : '刷新',
			iconCls : 'icon-refresh',
			handler : showAll
		}, '-', {
            id : 'redoBtn',
            text : '重做',
            iconCls : 'icon-loading',
            handler : redo
        } ]
	});
}

function showAll() {
	refreshDatagrid(dataUrl,1,true);
}

function loadTaskTriggerList(){
    $('#taskTriggerList').combotree({
        url : '../taskTrigger/getTaskTriggerTree?needRoot=true',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        }
    });
}

function redo(){
    var row = getSelectedSingleRow('grid');
    if(row==null){
        return;
    }
    var id=row[0].id;
    $.messager.confirm('提示信息', '是否要重做?', function(data) {
        if (data) {
            var url='../taskLog/redo?id='+id;
            doAjax(null,url,'GET',true,function(data){

            });
        }
    });
    $('#grid').datagrid('clearSelections');

}