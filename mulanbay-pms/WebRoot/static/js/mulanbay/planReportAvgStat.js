$(function() {
	// 加载公司树
    initForm();
	initGrid();
});

var dataUrl='../planReport/avgStat';

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
            field : 'userPlan.title',
            title : '名称',
            formatter : function(value, row, index) {
                if(row.userPlan){
                    return row.userPlan.title;
                }else {
                    return '--';
                }
            },
        }, {
            field : 'reportCountValue',
            title : '次数的平均值',
            width : 120,
            formatter : function(value, row, index) {
                return getValueCompareResult(value.toFixed(1),row.userPlanConfigValue.planCountValue,row.userPlan.planConfig.compareType);
            },
            align : 'center'
        }, {
            field : 'reportValue',
            title : '值的平均值',
            width : 120,
            formatter : function(value, row, index) {
                return getValueCompareResult(value.toFixed(1),row.userPlanConfigValue.planValue,row.userPlan.planConfig.compareType);
            },
            align : 'center'
        }, {
            field : 'planConfig.compareTypeName',
            title : '比较类型',
            formatter : function(value, row, index) {
                if(row.userPlan){
                    return row.userPlan.planConfig.compareTypeName;
                }else {
                    return '--';
                }
            },
            align : 'center'
        }, {
            field : 'planCountValue',
            title : '计划次数',
            formatter : function(value, row, index) {
                if(row.userPlanConfigValue){
                    if(row.userPlanConfigValue.year==row.year){
                        return row.userPlanConfigValue.planCountValue;
                    }else{
                        return row.userPlanConfigValue.planCountValue+'&nbsp;<font color="red">('+row.userPlanConfigValue.year+')</font>';
                    }
                }else {
                    return '--';
                }
            },
            align : 'center'
        }, {
            field : 'planValue',
            title : '计划值',
            formatter : function(value, row, index) {
                if(row.userPlanConfigValue){
                    if(row.userPlanConfigValue.year==row.year){
                        return row.userPlanConfigValue.planValue;
                    }else{
                        return row.userPlanConfigValue.planValue+'&nbsp;<font color="red">('+row.userPlanConfigValue.year+')</font>';
                    }
                }else {
                    return '--';
                }
            },
            align : 'center'
        }, {
            field : 'planConfig.unit',
            title : '计划值单位',
            formatter : function(value, row, index) {
                if(row.userPlan){
                    return row.userPlan.planConfig.unit;
                }else {
                    return '--';
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

function getPlanConfigTree(){
    $('#userPlanList').combotree({
        url : '../userPlan/getPlanConfigTree',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        }
    });
}

function initForm(){
	getPlanConfigTree();
    getYearTree();
}

function showAll() {
	refreshDatagrid(dataUrl,1,true);
}

function getYearTree(){
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