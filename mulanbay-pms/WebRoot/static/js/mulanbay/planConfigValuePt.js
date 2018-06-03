
var dataUrlPcv='../planConfigValue/getData';

function initGridPlanConfigValue(planConfigId){
	$('#gridPlanConfigValue').datagrid({
		iconCls : 'icon-save',
		url : dataUrlPcv+'?planConfigId='+planConfigId,
		method:'GET',
		loadFilter: function(data){
			return loadDataFilter(data);
		},
		idField : 'id',
		loadMsg : '正在加载数据...',
		pageSize : 30,
		queryParams: form2Json("search-window-pcv"),
		remoteSort : false,
		frozenColumns : [ [ {
			field : 'ID',
			checkbox : true
		} ] ],
		onDblClickRow: function (rowIndex, rowData) {
            $('#gridPlanConfigValue').datagrid('uncheckAll');
            $('#gridPlanConfigValue').datagrid('checkRow', rowIndex);
            editPlanConfigValue();
        },
        onLoadError: loadDataError,
		columns : [ [ {
			field : 'id',
			title : '记录号',
			sortable : true,
			align : 'center'
		}, {
            field : 'year',
            title : '年份'
        }, {
            field : 'planCountValue',
            title : '计划次数',
            align : 'center'
        }, {
            field : 'planValue',
            title : '计划值',
            align : 'center'
        }] ],
		pagination : true,
		rownumbers : true,
		singleSelect : false,
		toolbar : [ {
			id : 'createBtnPlanConfigValue',
			text : '新增',
			iconCls : 'icon-add',
			handler : addPlanConfigValue
		}, '-', {
			id : 'editBtnPlanConfigValue',
			text : '修改',
			iconCls : 'icon-edit',
			handler : editPlanConfigValue
		}, '-', {
			id : 'deleteBtnPlanConfigValue',
			text : '删除',
			iconCls : 'icon-remove',
			handler : delPlanConfigValue
		}, '-', {
			id : 'searchBtnPlanConfigValue',
			text : '刷新',
			iconCls : 'icon-refresh',
			handler : showAllPlanConfigValue
		} ]
	});
}

function addPlanConfigValue() {
	$('#eidt-window-pcv').window('open');
	$('#ff-plan-config-value').form('clear');
	$('#ff-plan-config-value').form.url='../planConfigValue/create';
	//值为外面看病记录的表单的id
    $("#planConfigId").val($("#id").val());
}

function editPlanConfigValue() {
    var rows = $('#gridPlanConfigValue').datagrid('getSelections');
    var num = rows.length;
    if (num == 0) {
        $.messager.alert('提示', '请选择一条记录进行操作!', 'info');
        return;
    }
	var url='../planConfigValue/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data){
		$('#eidt-window-pcv').window('open');
        $('#ff-plan-config-value').form('clear');
        $('#ff-plan-config-value').form('load', data);
		$('#ff-plan-config-value').form.url='../planConfigValue/edit?id=' + data.id;
		//设置字符
		$('#gridPlanConfigValue').datagrid('clearSelections');
        $("#planConfigId").val($("#id").val());
    });
}

function checkCurrentPlanConfigId(){
    var vPlanConfigId = $("#planConfigId").val();
    if(vPlanConfigId==null||vPlanConfigId==''||vPlanConfigId==undefined){
        return false;
    }else {
    	return true;
	}
}

function showAllPlanConfigValue() {
    if(!checkCurrentPlanConfigId()){
        $.messager.alert('错误', '无当前计划配置信息,请先保存当前计划配置信息！', 'error');
        return;
    }
	var vPlanConfigId = $("#planConfigId").val();
	if(vPlanConfigId==null||vPlanConfigId==''||vPlanConfigId==undefined){
        vPlanConfigId==0;
	}
    var vurl =dataUrlPcv+'?planConfigId='+vPlanConfigId;
    $('#gridPlanConfigValue').datagrid({
        url : vurl,
        type : 'GET',
        pageNumber : 1,
        onLoadError : function() {
            $.messager.alert('错误', '加载数据异常！', 'error');
        }
    });
}

function saveDataPlanConfigValue() {
    if(!checkCurrentPlanConfigId()){
        $.messager.alert('错误', '无当前计划配置信息,请先保存当前计划配置信息！', 'error');
        return;
    }
    var url='../planConfigValue/edit';
    if($("#planConfigValueId").val()==null||$("#planConfigValueId").val()==''){
        url='../planConfigValue/create';
    }
    doFormSubmit('ff-plan-config-value',url,function(){
		closeWindow('eidt-window-pcv');
		$('#gridPlanConfigValue').datagrid('clearSelections');
		showAllPlanConfigValue();
	});
}

function getSelectedIdsPlanConfigValue() {
	var ids = [];
    var rows = $('#gridPlanConfigValue').datagrid('getSelections');
    var num = rows.length;
    if (num == 0) {
        $.messager.alert('提示', '请选择一条记录进行操作!', 'info');
        return;
    }
	for (var i = 0; i < rows.length; i++) {
		ids.push(rows[i].id);
	}
	return ids;
}

function delPlanConfigValue() {
	var arr = getSelectedIdsPlanConfigValue();
	if (arr.length > 0) {
		$.messager.confirm('提示信息', '您确认要删除吗?', function(data) {
			if (data) {
				var vurl = '../planConfigValue/delete?ids=' + arr.join(',');
				doAjax(null, vurl, 'GET', true, function(data) {
					$('#gridPlanConfigValue').datagrid('clearSelections');
                    showAllPlanConfigValue();
				});
			}
		});
	} else {
		$.messager.show({
			title : '警告',
			msg : '请先选择要删除的记录。'
		});
	}
}

function closeWindowPlanConfigValue(){
    closeWindow('eidt-window-pcv');
}
