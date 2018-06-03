$(function() {
	// 加载公司树
    initForm();
	initGrid();
});

var dataUrl='../planReport/getData';

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
            //edit();
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
                if(row.userPlan){
                    return row.userPlan.title;
                }else {
                    return '--';
                }
            }
        }, {
            field : 'userPlan.planConfig.planTypeName',
            title : '计划类型',
            formatter : function(value, row, index) {
                if(row.userPlan.planConfig){
                    return row.userPlan.planConfig.planTypeName;
                }else {
                    return '--';
                }
            },
            align : 'center'
        }, {
            field : 'bussDay',
            title : '业务日期索引',
            align : 'center'
        }, {
            field : 'bussStatDate',
            title : '业务日期',
            align : 'center'
        }, {
            field : 'reportCountValue',
            title : '次数值',
            width : 120,
            formatter : function(value, row, index) {
                return getValueCompareResult(value,row.planCountValue,row.userPlan.planConfig.compareType);
            },
            align : 'center'
        }, {
            field : 'reportValue',
            title : '值',
            width : 120,
            formatter : function(value, row, index) {
                if(value==0 && row.planValue==0){
                    //说明不需要
                    return '--';
                }else {
                    return getValueCompareResult(value,row.planValue,row.userPlan.planConfig.compareType);
                }
            },
            align : 'center'
        }, {
            field : 'userPlan.planConfig.compareTypeName',
            formatter : function(value, row, index) {
                if(row.userPlan.planConfig){
                    return row.userPlan.planConfig.compareTypeName;
                }else {
                    return '--';
                }
            },
            title : '比较类型',
            align : 'center'
        }, {
            field : 'planCountValue',
            title : '计划次数',
            align : 'center'
        }, {
            field : 'planValue',
            title : '计划值',
            formatter : function(value, row, index) {
                if(value==0){
                    //说明不需要
                    return '--';
                }else {
                    return value;
                }
            },
            align : 'center'
        }, {
            field : 'userPlan.planConfig.unit',
            title : '值单位',
            formatter : function(value, row, index) {
                if(row.userPlan.planConfig){
                    return row.userPlan.planConfig.unit;
                }else {
                    return '--';
                }
            },
            align : 'center'
        }, {
            field : 'planConfigYear',
            title : '数据参考年份',
            align : 'center'
        },{
            field : 'createdTime',
            title : '统计时间'
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
            id : 'deleteBtn',
            text : '删除',
            iconCls : 'icon-remove',
            handler : del
        },'-', {
            id : 'reStatBtn',
            text : '重新统计',
            iconCls : 'icon-stat',
            handler : showReStat
        },'-', {
            id : 'statBtn',
            text : '图表统计',
            iconCls : 'icon-stat',
            handler : showStat
        },'-', {
            id : 'manualStatBtn',
            text : '手动统计数据',
            iconCls : 'icon-stat',
            handler : showManualStat
        },'-', {
            id : 'cleanDataBtn',
            text : '清理数据',
            iconCls : 'icon-info',
            handler : showCleanData
        } ]
	});
}

function getPlanConfigTree(){
    $('#userPlanList').combotree({
        url : '../userPlan/getPlanConfigTree?needRoot=true',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        },
        onLoadSuccess:function(node,data){
            $('#userPlanList').combotree('setValue', { id: data[1].id, text: data[1].text });
        }
    });
}

function initForm(){
	getPlanConfigTree();
}

function showAll() {
	refreshDatagrid(dataUrl,1,true);
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
    var delUrlPrefix = '../planReport/delete';
    commonDeleteByIds(delUrlPrefix);
}

function showManualStat() {
    $('#manual-stat-window').window('open');
    $('#manual-stat-form').form('clear');
    $('#userPlanList2').combotree({
        url : '../userPlan/getPlanConfigTree?needRoot=true',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        }
    });
    var nowDate = new Date();
    var formData = {
        statType:'RE_STAT',
        startDate: getFirstDayOfMonth(nowDate),
        endDate: getLastDayOfMonth(nowDate)
    };
    $('#manual-stat-form').form('load', formData);
}

function getSelectedIds() {
    var ids = [];
    var rows = getSelectedRows();
    for (var i = 0; i < rows.length; i++) {
        ids.push(rows[i].id);
    }
    return ids;
}

function showReStat() {
    var arr = getSelectedIds();
    var ll =arr.length;
    if(ll==0){
        $.messager.alert('提示', '请至少选择一条记录', 'info');
        return;
    }
    $('#re-stat-window').window('open');
    $('#yearList').combotree({
        url : '../common/getYearTree',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        }
    });
    $('#yearList').combotree('setValue', getYear(-1));
}


function reStat() {
    var arr = getSelectedIds();
    var ll =arr.length;
    if(ll==0){
        $.messager.alert('提示', '请至少选择一条记录', 'info');
        return;
    }
    var para =form2Json("re-stat-form");
    para.ids = arr.join(',');
    var url='../planReport/reStat';
    doAjax(para,url,'GET',true,function(data){
        $('#grid').datagrid('clearSelections');
        $('#re-stat-window').window('close');
        reloadDatagrid();
    });
}

function manualStat() {
    var para =form2Json("manual-stat-form");
    var url='../planReport/manualStat';
    doAjax(para,url,'GET',true,function(data){
        $('#manual-stat-window').window('close');
        showAll();
    });
}

function showStat() {
    $('#stat-window').window('open');
    $('#userPlanList4').combotree({
        url : '../userPlan/getPlanConfigTree?needRoot=true',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        }
    });
}

function stat(){
    var para =form2Json("stat-search-form");
    var url='../planReport/stat';

    doAjax(para,url,'GET',false,function(data){
        $('#stat-form').form('clear');
        if(data.userPlanConfigValue){
            //说明选取了某个计划配置
            var formData = {
                year: data.userPlanConfigValue.year,
                totalCount: data.totalCount,
                unit: data.userPlanConfigValue.userPlan.planConfig.unit,
                reportCountValueSum: data.reportCountValueSum,
                reportValueSum: data.reportValueSum,
                planCountValueExpectSum : data.userPlanConfigValue.planCountValue*data.totalCount,
                planValueExpectSum : data.userPlanConfigValue.planValue*data.totalCount,
                averageReportCountValue :((data.reportCountValueSum+0.0)/data.totalCount).toFixed(1),
                averageReportValue :((data.reportValueSum+0.0)/data.totalCount).toFixed(1),
                averageValue :((data.reportValueSum+0.0)/data.reportCountValueSum).toFixed(2),
                planCountValue : data.userPlanConfigValue.planCountValue,
                planValue : data.userPlanConfigValue.planValue,
                diffCountValueSum: data.diffCountValueSum,
                diffValueSum: data.diffValueSum,
                avgDiffCountValue :((data.diffCountValueSum+0.0)/data.totalCount).toFixed(1),
                avgDiffValue: ((data.diffValueSum+0.0)/data.totalCount).toFixed(1)
            };
            $('#stat-form').form('load', formData);
        }
        //生成饼图
        createDoublePieData(data.pieData);
    });
}

function cleanAll() {
    var para =form2Json("clean-data-form");
    cleanData(para);
}

function cleanSelect() {
    var para =form2Json("manual-stat-form");
    cleanData(para);
}

function showCleanData() {
    $('#clean-data-window').window('open');
    $('#planConfigList3').combotree({
        url : '../planConfig/getPlanConfigTree?needRoot=true',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        }
    });
}

function cleanData(para) {
    $.messager.confirm('提示信息', '是否要删除数据?', function(data) {
        if (data) {
            var vurl = '../planReport/cleanData';
            doAjax(para, vurl, 'GET', true, function() {
                $('#clean-data-window').window('close');
                showAll();
            });
        }else{
            $('#clean-data-window').window('close');
            $('#grid').datagrid('clearSelections');
        }
    });
}