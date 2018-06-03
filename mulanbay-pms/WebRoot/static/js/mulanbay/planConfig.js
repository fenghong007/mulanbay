$(function() {
	// 加载公司树
    initForm();
	initGrid();
});

var dataUrl='../planConfig/getData';

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
            title : '名称'
        }, {
            field : 'title',
            title : '标题'
        }, {
            field : 'planTypeName',
            title : '计划类型',
            align : 'center'
        }, {
            field : 'planReport.reportCountValue',
            title : '最新次数统计值',
            formatter : function(value, row, index) {
                if(row.planReport){
                    return getValueCompareResult(row.planReport.reportCountValue,row.planReport.planCountValue,row.compareType);
                }else {
                    return '--';
                }
            },
            align : 'center'
        }, {
            field : 'planReport.reportValue',
            title : '最新值统计值',
            formatter : function(value, row, index) {
                if(row.planReport){
                    return getValueCompareResult(row.planReport.reportValue,row.planReport.planValue,row.compareType);
                }else {
                    return '--';
                }
            },
            align : 'center'
        }, {
            field : 'compareTypeName',
            title : '比较类型',
            align : 'center'
        }, {
            field : 'planReport.planCountValue',
            title : '次数参考值',
            formatter : function(value, row, index) {
                if(row.planReport){
                    if(row.planReport.planCountValue){
                        return row.planReport.planCountValue+getCompareYearString(row.planReport.planConfigYear);
                    }else {
                        return '--';
                    }
                }else {
                    return '--';
                }
            },
            align : 'center'
        }, {
            field : 'planReport.planValue',
            title : '值参考值',
            formatter : function(value, row, index) {
                if(row.planReport){
                    if(row.planReport.planValue){
                        return row.planReport.planValue+getCompareYearString(row.planReport.planConfigYear);
                    }else {
                        return '--';
                    }
                }else {
                    return '--';
                }
            },
            align : 'center'
        }, {
            field : 'unit',
            title : '值单位',
            align : 'center'
        }, {
            field : 'sqlTypeName',
            title : '查询类型',
            align : 'center'
        }, {
            field : 'level',
            title : '等级',
            align : 'center'
        },{
            field : 'rewardPoint',
            title : '积分奖励',
            align : 'center'
        }, {
            field : 'orderIndex',
            title : '排序号',
            align : 'center'
        },{
            field : 'status',
            title : '状态',
            formatter : function(value, row, index) {
                return getStatusImage(value);
            },
            align : 'center'
        },{
            field : 'createdTime',
            title : '创建时间'
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
		} ]
	});
}

function getCompareYearString(year) {
    if(year==null||year==''||year==undefined){
        return '';
    }
    var current = getYear(0);
    if(year==current){
        return '';
    }else{
        return '&nbsp;<font color="#db7093">('+year+')</font>'
    }
}

function getRelatedBeansTree(){
    $('#relatedBeansList').combotree({
        url : '../common/getBussTypeTree',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        }
    });
}


function getPlanConfigTree(){
    $('#planConfigList').combotree({
        url : '../planConfig/getPlanConfigTree?needRoot=true',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        },
        onClick: function(node){
            //加载模板内容
            var url='../planConfig/get?id='+ node.id;
            doAjax(null,url,'GET',false,function(data){
                $('#ff').form('clear');
                //需要设置为空，否则保存有问题
                data.id=null;
                $('#ff').form('load', data);
                $('#ff').form.url='../planConfig/create';
            });
        }
    });
}


function add() {
	$('#eidt-window').window('open');
	$('#ff').form('clear');
	initForm();
    getPlanConfigTree();
    $('#planConfigList').combotree('enable');
    $('#ff').form.url='../planConfig/create';
    var formData = {
        planType: 'MONTH',
        sqlType: 'SQL',
        planValue: 0,
        compareType:'MORE',
        planType:'MONTH',
        status:'ENABLE',
        level:3,
        rewardPoint:0,
        orderIndex :1
    };
    $('#ff').form('load', formData);
    initGridStatValueConfig(0,'PLAN');
}


function initForm(){
    //easyui 的hidden值初始化有问题，需要人工设置
    $("#type").val('PLAN');
    getRelatedBeansTree();
}

function edit() {
	var rows = getSelectedSingleRow();
	var url='../planConfig/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data){
		$('#eidt-window').window('open');
        $('#ff').form('clear');
        initForm();
        //getPlanConfigTree();
        $('#ff').form('load', data);
		$('#ff').form.url='../planConfig/edit?id=' + data.id;
        $('#taskTriggerList').combotree('setValue', data.taskTriggerId);
        //设置字符
		$('#grid').datagrid('clearSelections');
        initGridStatValueConfig(data.id,'PLAN');
    });
}

function showAll() {
	refreshDatagrid(dataUrl,1,true);
}

function saveData() {
    var url='../planConfig/edit';
    if($("#id").val()==null||$("#id").val()==''){
        url='../planConfig/create';
    }
	doFormSubmit('ff',url,function(data){
		//closeWindow('eidt-window');
		$('#grid').datagrid('clearSelections');
        $('#ff').form('clear');
        reloadDatagrid();
        $('#ff').form('load', data);
        $('#ff').form.url='../planConfig/edit?id=' + data.id;
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
    var delUrlPrefix = '../planConfig/delete';
    commonDeleteByIds(delUrlPrefix);
}

function showPlanCommend() {
    $('#plan-commend-window').window('open');
    $('#plan-commend-form').form('clear');
    var formData = {
        startDate: getYear(-1)+'-01-01',
        endDate: getYear(-1)+'-12-31'
    };
    $('#plan-commend-form').form('load', formData);
}

function planCommend() {
    var para =form2Json("plan-commend-form");
    var vPlanConfigId = $("#planConfigId").val();
    para.planConfigId=vPlanConfigId;
    var url='../planReport/planCommend';
    doAjax(para,url,'GET',false,function(data){
        var formData = {
            planCountValue: data.reportCountValue,
            planValue: data.reportValue
        };
        $('#ff-plan-config-value').form('load', formData);
        closeWindow('plan-commend-window');
    });
}

function showSqlHelp() {
    var msg='<font color="red">注意</font>：<br/>' +
        '(1)返回结果需要两个，如果业务需要只要一个次数，返回列中手动在添加一个0<br/>'+
        '(2)比如select count(*) as c,0 as v from ......<br/>'+
        '(3)最后返回结果需要做四舍五入';
    $.messager.alert('提示', msg, '');
}

function getFirstStatDay() {
    var url='../planConfig/getFirstStatDay';
    var para = form2JsonEnhanced("ff");
    doAjax(para,url,'POST',false,function(data){
        if(data){
            $("#firstStatDay").datebox('setValue', data);
        }
    });
}