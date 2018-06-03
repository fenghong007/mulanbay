$(function() {
	// 加载公司树
    initForm();
	initGrid();
});

var dataUrl='../treatRecord/getData';

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
		queryParams: form2Json("search-window"),
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
            field : 'hospital',
            title : '医院'
		}, {
            field : 'department',
            title : '科室'
        }, {
            field : 'organ',
            title : '器官'
        }, {
            field : 'disease',
            title : '疾病'
        }, {
            field : 'painLevel',
            title : '疼痛级别',
            formatter : function(value, row, index) {
                if (value >=8) {
                    return '<font color="red">'+value+'</font>';
                }else {
                    return value;
                }
            },
            align : 'center'
        }, {
            field : 'importantLevel',
            title : '重要等级',
            formatter : function(value, row, index) {
                if (value >=4) {
                    return '<font color="red">'+value+'</font>';
                }else {
                    return value;
                }
            },
            align : 'center'
        }, {
            field : 'diagnosedDisease',
            title : '确诊疾病'
        }, {
            field : 'isSick',
            title : '是否有病',
            align : 'center',
            formatter : function(value, row, index) {
                if (value == true) {
                    return '<img src="../static/image/warn.png"></img>';
                } else {
                    return '--';
                }
            }
        }, {
            field : 'treatDate',
            title : '看病日期'
        }, {
            field : 'registeredFee',
            title : '挂号费',
            align : 'center',
            formatter : function(value, row, index) {
                return formatMoneyWithSymbal(value);
            }
        }, {
            field : 'drugFee',
            title : '药费',
            align : 'center',
            formatter : function(value, row, index) {
                return formatMoneyWithSymbal(value);
            }
        }, {
            field : 'operationFee',
            title : '手术/治疗费用',
            align : 'center',
            formatter : function(value, row, index) {
                return formatMoneyWithSymbal(value);
            }
        }, {
            field : 'totalFee',
            title : '总共花费',
            align : 'center',
            formatter : function(value, row, index) {
                return '<font color="red">'+formatMoneyWithSymbal(value)+'</font>';
            },
        } , {
            field : 'medicalInsurancePaidFee',
            title : '医保花费',
            align : 'center',
            formatter : function(value, row, index) {
                return formatMoneyWithSymbal(value);
            }
        }, {
            field : 'personalPaidFee',
            title : '个人支付费用',
            align : 'center',
            formatter : function(value, row, index) {
                return formatMoneyWithSymbal(value);
            }
        }] ],
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
		} ]
	});
}

function add() {
	$('#eidt-window').window('open');
	$('#ff').form('clear');
	initForm();
	$('#ff').form.url='../treatRecord/create';
    initGridDrug(0);
    initGridOperation(0);

}

function initForm(){
    $('#organCategoryList').combobox({
        url : '../treatRecord/getTreatCategoryTree?groupField=organ',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        }
    });
    $('#diseaseCategoryList').combobox({
        url : '../treatRecord/getTreatCategoryTree?groupField=disease',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        }
    });
}

function edit() {
	var rows = getSelectedSingleRow();
	showEdit(rows[0].id);
}

function showEdit(id) {
    var url='../treatRecord/get?id='+ id;
    doAjax(null,url,'GET',false,function(data){
        $('#eidt-window').window('open');
        $('#ff').form('clear');
        initForm();
        $('#ff').form('load', data);
        $('#ff').form.url='../treatRecord/edit?id=' + data.id;
        //设置字符
        $('#grid').datagrid('clearSelections');
        initGridDrug(data.id);
        initGridOperation(data.id);
        $("#drugTreatRecordId").val(data.id);
        $("#operationTreatRecordId").val(data.id);
    });
}
function showAll() {
	refreshDatagrid(dataUrl,1,true);
}

function saveData() {
    var url='../treatRecord/edit';
    if($("#id").val()==null||$("#id").val()==''){
        url='../treatRecord/create';
    }
	doFormSubmit('ff',url,function(data){
		//closeWindow('eidt-window');
		$('#grid').datagrid('clearSelections');
        $('#ff').form('clear');
        reloadDatagrid();
        $('#ff').form('load', data);
        $('#ff').form.url='../treatRecord/edit?id=' + data.id;
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
    var delUrlPrefix = '../treatRecord/delete';
    commonDeleteByIds(delUrlPrefix);
}

function stat(){
    var para =form2Json("search-window");
    var url='../treatRecord/stat';

    doAjax(para,url,'GET',false,function(data){
        $('#stat-window').window('open');
        var formData = {
            totalCount: data.totalCount,
            totalRegisteredFee: formatMoneyWithSymbal(data.totalRegisteredFee),
            totalDrugFee: formatMoneyWithSymbal(data.totalDrugFee),
            totalOperationFee: formatMoneyWithSymbal(data.totalOperationFee),
            totalTotalFee: formatMoneyWithSymbal(data.totalTotalFee),
            totalMedicalInsurancePaidFee: formatMoneyWithSymbal(data.totalMedicalInsurancePaidFee),
            totalPersonalPaidFee: formatMoneyWithSymbal(data.totalPersonalPaidFee)
        };
        $('#stat-form').form('load', formData);
        //生成饼图
        createPieData(data.pieData);
    });
}

function planStat(){
    getUserPlan('TreatRecord');
}
