$(function() {
	// 加载公司树
    initForm();
	initGrid();
});

var dataUrl='../operationLog/getData';

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
            field : 'paras2',
            title : '修改比较',
            formatter : function(value, row, index) {
                var ss='';
                var icon = '<img src="../static/image/info.png"></img></a>';
                if(row.systemFunction){
                    if(row.systemFunction.functionType=='EDIT'){
                        ss+= '<a href="javascript:showOperationLogCompare('+'\''+row.id+'\''+');">'+icon+'</a>';
                    }
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
            field : 'returnData',
            title : '返回数据',
            formatter : function(value, row, index) {
                var ss='';
                var icon = '<img src="../static/image/info.png"></img></a>';
                if (value) {
                    ss+= '<a href="javascript:showReturnData('+'\''+row.id+'\''+');">'+icon+'</a>';
                }else {
                    ss= '--';
                }
                return ss;
            },
            align : 'center'
        },{
            field : 'occurStartTime',
            title : '请求开始时间',
            align : 'center'
        },{
            field : 'occurEndTime',
            title : '请求结束时间',
            align : 'center'
        },{
            field : 'handleDuration',
            title : '请求处理时间(毫秒)',
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
		},'-', {
            id : 'setOperationLogFunctionIdBtn',
            text : '关联功能点',
            iconCls : 'icon-refresh',
            handler : showSetOperationLogFunctionId
        },'-', {
            id : 'setIdValueBtn',
            text : '设置操作对象主键值',
            iconCls : 'icon-refresh',
            handler : setIdValue
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

//id为OperationLog的主键ID
function showBeanDetail(id) {
    var url='../operationLog/showBeanDetail?id='+ id;
    doAjax(null,url,'GET',false,function(data){
        $('#bean-detail-window').window('open');
        $('#grid').datagrid('clearSelections');
        $('#beanDetailContent').treegrid('loadData', {"total":1,"rows":[
            {"id":'根目录',"name":'根目录'}
        ]});
        var currentTitle='操作对象('+data.beanName+'),ID='+data.idValue;
        $('#beanDetailContent').treegrid({
            title:currentTitle
        });
        showFormatTree('beanDetailContent','根目录',data.beanData);
    });
}

function showParas(id) {
    var url='../operationLog/getParas?id='+ id;
    doAjax(null,url,'GET',false,function(data){
        $('#contentDetail-window').window('open');
        $('#grid').datagrid('clearSelections');
        $('#contentDetail').treegrid('loadData', {"total":1,"rows":[
            {"id":'根目录',"name":'根目录'}
        ],"footer":[
            {"name":"Total Persons:","persons":7,"iconCls":"icon-sum"}
        ]});
        showFormatTree('contentDetail','根目录',eval('(' + data + ')'));
    });
}

function showReturnData(id) {
    var url='../operationLog/getReturnData?id='+ id;
    doAjax(null,url,'GET',false,function(data){
        $('#contentDetail-window').window('open');
        $('#grid').datagrid('clearSelections');
        $('#contentDetail').treegrid('loadData', {"total":1,"rows":[
            {"id":'根目录',"name":'根目录'}
        ],"footer":[
            {"name":"Total Persons:","persons":7,"iconCls":"icon-sum"}
        ]});
        showFormatTree('contentDetail','根目录',eval('(' + data + ')'));
    });
}

function showSetOperationLogFunctionId() {
    $('#setOperationLogFunctionId-window').window('open');
}


function setOperationLogFunctionId() {
    var para =form2Json("setOperationLogFunctionId-form");
    var url='../operationLog/setOperationLogFunctionId';
    doAjax(para,url,'GET',true,function(data){
        $('#setOperationLogFunctionId-window').window('close');
    });
}

function setIdValue() {
    var url='../operationLog/setIdValue';
    doAjax(null,url,'GET',true,function(data){
        showAll();
    });
}