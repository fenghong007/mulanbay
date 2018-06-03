$(function() {
	initGrid();
});

var dataUrl='../userMessage/getData';

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
            field : 'userId',
            title : '用户ID',
            align : 'center'
        }, {
            field : 'title',
            title : '标题',
            align : 'center'
        }, {
            field : 'sendStatus',
            title : '发送状态',
            formatter : function(value, row, index) {
                if (value == 'SEND_SUCCESS') {
                    return '<img src="../static/image/tick.png"></img>';
                }else if (value == 'SEND_FAIL') {
                    return '<img src="../static/image/cross.png"></img>';
                }else{
                    return row.sendStatusName;
                }
            },
            align : 'center'
        },{
            field : 'aa',
            title : '重发',
            formatter : function(value, row, index) {
                var ss='<a href="javascript:resend('+row.id+');"><img src="../static/image/reload.png"></img></a>';
                return ss;
            },
            align : 'center'
        }, {
            field : 'failCount',
            title : '失败次数',
            align : 'center'
        }, {
            field : 'content',
            title : '内容',
            formatter : function(value, row, index) {
                return '<a href="javascript:showLogDetail('+row.id+');"><img src="../static/image/info.png"></img></a>';
            },
            align : 'center'
        },{
            field : 'expectSendTime',
            title : '期望发送时间',
            align : 'center'
        },{
            field : 'lastSendTime',
            title : '最后一次发送时间',
            align : 'center'
        },{
            field : 'nodeId',
            title : '服务器节点',
            align : 'center'
        } ,{
            field : 'createdTime',
            title : '创建时间',
            align : 'center'
        } ] ],
		pagination : true,
		rownumbers : true,
		singleSelect : false,
		toolbar : [  {
			id : 'searchBtn',
			text : '刷新',
			iconCls : 'icon-refresh',
			handler : showAll
		}]
	});
}

function showAll() {
	refreshDatagrid(dataUrl,1,true);
}

function showLogDetail(id) {
    var url='../userMessage/get?id='+ id;
    doAjax(null,url,'GET',false,function(data){
        $('#grid').datagrid('uncheckAll');
        $.messager.alert('详情', data.content, 'info');
    });
}

function resend(id) {
    $('#grid').datagrid('uncheckAll');
    $.messager.confirm('提示信息', '是否确定要重新发送', function(data) {
        if (data) {
            var url='../userMessage/resend?id='+ id;
            doAjax(null,url,'GET',true,function(){
                reloadDatagrid();
            });
        }
    });

}
