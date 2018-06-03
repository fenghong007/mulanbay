$(function() {
	// 加载公司树
    initForm();
	initGrid();
});

var dataUrl='../systemLog/getData';

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
            field : 'userName',
            title : '用户名',
            align : 'center'
        }, {
            field : 'errorCode',
            title : '错误代码',
            align : 'center'
        }, {
            field : 'title',
            title : '标题',
            align : 'center'
        }, {
            field : 'content',
            title : '内容',
            formatter : function(value, row, index) {
                return '<a href="javascript:showLogDetail('+row.id+');"><img src="../static/image/info.png"></img></a>';
            },
            align : 'center'
        }, {
            field : 'exceptionClassName',
            title : '异常类型',
            formatter : function(value, row, index) {
                return '<a href="javascript:showFullDetail('+'\''+value+'\''+');"><img src="../static/image/info.png"></img></a>';
            },
            align : 'center'
        },{
            field : 'logLevel',
            title : '日志级别',
            formatter : function(value, row, index) {
                return getLogLevelImage(value);
            },
            align : 'center'
        },{
            field : 'systemFunction.name',
            title : '功能点',
            formatter : function(value, row, index) {
                if (row.systemFunction) {
                    return row.systemFunction.name;
                } else {
                    return '--';
                }
            },
            align : 'center'
        },{
			field : 'urlAddress',
			title : '请求地址',
            align : 'center'
        },{
            field : 'method',
            title : '请求方式',
            align : 'center'
        },{
            field : 'paras',
            title : '请求参数',
            formatter : function(value, row, index) {
                var ss='';
                var icon = '<img src="../static/image/info.png"></img></a>';
                if (value) {
                    ss+= '<a href="javascript:showParas('+'\''+row.id+'\''+');">'+icon+'</a>';
                }else {
                    ss= '--';
                }
                return ss;
            },
            align : 'center'
        },{
            field : 'idValue',
            title : '主键值',
            formatter : function(value, row, index) {
                if(value){
                    return '<a href="javascript:showBeanDetail('+'\''+row.id+'\''+');">'+value+'</a>';
                }else {
                    return '--';
                }
            },
            align : 'center'
        },{
            field : 'occurTime',
            title : '发生时间',
            align : 'center'
        },{
            field : 'storeTime',
            title : '保存时间',
            align : 'center'
        },{
            field : 'storeDuration',
            title : '保存延迟时间(毫秒)',
            align : 'center'
        } ,{
            field : 'ipAddress',
            title : '请求IP',
            align : 'center'
        } ,{
            field : 'hostIpAddress',
            title : '服务器IP',
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

function initForm(){
    $('#systemFunctionList').combotree({
        url : '../systemFunction/getSystemFunctionTree?needRoot=true',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        }
    });
}

function showAll() {
	refreshDatagrid(dataUrl,1,true);
}

function showParas(id) {
    var url='../systemLog/getParas?id='+ id;
    doAjax(null,url,'GET',false,function(data){
        $('#paras-window').window('open');
        $('#grid').datagrid('clearSelections');
        $('#parasContent').treegrid('loadData', {"total":1,"rows":[
            {"id":'根目录',"name":'根目录'}
        ],"footer":[
            {"name":"Total Persons:","persons":7,"iconCls":"icon-sum"}
        ]});
        showFormatTree('parasContent','根目录',eval('(' + data + ')'));
    });
}


//获取日志级别的图片地址
function getLogLevelImage(va){
    if(va=='NORMAL'){
        return '<img src="../static/image/tick.png"></img>';
    }else if(va=='WARNING'){
        return '<img src="../static/image/warn.png"></img>';
    }else if(va=='ERROR'){
        return '<img src="../static/image/cross.png"></img>';
    }else{
        return '<img src="../static/image/cross.png"></img>';
    }
}

function showLogDetail(id) {
    var url='../systemLog/get?id='+ id;
    doAjax(null,url,'GET',false,function(data){
        $('#grid').datagrid('uncheckAll');
        $.messager.alert('详情', data.content, 'info');
    });
}
