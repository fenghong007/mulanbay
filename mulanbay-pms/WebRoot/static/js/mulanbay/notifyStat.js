$(function() {
	initGrid();
});

var dataUrl='../notifyStat/getData';

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
		remoteSort : false,
		frozenColumns : [ [ {
			field : 'ID',
			checkbox : true
		} ] ],
        onLoadError: loadDataError,
		columns : [ [ {
			field : 'userNotify.title',
			title : '名称',
            formatter : function(value, row, index) {
                return row.userNotify.title;
            }
		}, {
            field : 'resultValue',
            title : '统计值',
            align : 'center'
        },{
            field : 'userNotify.id',
            title : '提醒信息',
            formatter : function(value, row, index) {
                var s='';
                if (row.overAlertValue>0) {
					s='<img src="../static/image/alert.gif"></img>';
                } else if (row.overWarningValue>0) {
                    s='<img src="../static/image/warn.png"></img>';
                } else {
                    s='<img src="../static/image/tick.png"></img>';
                }
                return s+row.userNotify.title+'['+row.compareValue+']'+row.userNotify.notifyConfig.valueTypeName;
            },
            align : 'center'
        }, {
            field : 'overWarningValue',
            title : '超过警告值',
            formatter : function(value, row, index) {
                if (value<=0) {
                    return '<font color="green">'+value+'</font>';
                } else {
                    return '<font color="red">'+value+'</font>';
                }
            },
            align : 'center'
        }, {
            field : 'overAlertValue',
            title : '超过报警值',
            formatter : function(value, row, index) {
                if (value<=0) {
                    return '<font color="green">'+value+'</font>';
                } else {
                    return '<font color="red">'+value+'</font>';
                }
            },
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
		} ]
	});
}

function showAll() {
	refreshDatagrid(dataUrl,1,true);
}
