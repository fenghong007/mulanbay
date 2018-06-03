
var dataUrlPcv='../userPlanConfigValue/getData';

function initGridUserPlanConfigValue(userPlanId){
    $("#userPlanId").val(userPlanId);
    $('#gridUserPlanConfigValue').datagrid({
		iconCls : 'icon-save',
		url : dataUrlPcv+'?userPlanId='+userPlanId,
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
            $('#gridUserPlanConfigValue').datagrid('uncheckAll');
            $('#gridUserPlanConfigValue').datagrid('checkRow', rowIndex);
            editUserPlanConfigValue();
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
			id : 'createBtnUserPlanConfigValue',
			text : '新增',
			iconCls : 'icon-add',
			handler : addUserPlanConfigValue
		}, '-', {
			id : 'editBtnUserPlanConfigValue',
			text : '修改',
			iconCls : 'icon-edit',
			handler : editUserPlanConfigValue
		}, '-', {
			id : 'deleteBtnUserPlanConfigValue',
			text : '删除',
			iconCls : 'icon-remove',
			handler : delUserPlanConfigValue
		}, '-', {
			id : 'searchBtnUserPlanConfigValue',
			text : '刷新',
			iconCls : 'icon-refresh',
			handler : showAllUserPlanConfigValue
		} ]
	});
}

function addUserPlanConfigValue() {
	$('#eidt-window-pcv').window('open');
	$('#ff-plan-config-value').form('clear');
	$('#ff-plan-config-value').form.url='../userPlanConfigValue/create';
	//值为外面看病记录的表单的id
    $("#userPlanId").val($("#id").val());
}

function editUserPlanConfigValue() {
    var rows = $('#gridUserPlanConfigValue').datagrid('getSelections');
    var num = rows.length;
    if (num == 0) {
        $.messager.alert('提示', '请选择一条记录进行操作!', 'info');
        return;
    }
	var url='../userPlanConfigValue/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data){
		$('#eidt-window-pcv').window('open');
        $('#ff-plan-config-value').form('clear');
        $('#ff-plan-config-value').form('load', data);
		$('#ff-plan-config-value').form.url='../userPlanConfigValue/edit?id=' + data.id;
		//设置字符
		$('#gridUserPlanConfigValue').datagrid('clearSelections');
        $("#userPlanId").val($("#id").val());
    });
}

function checkCurrentUserPlanId(){
    var vUserPlanId = $("#userPlanId").val();
    if(vUserPlanId==null||vUserPlanId==''||vUserPlanId==undefined){
        return false;
    }else {
    	return true;
	}
}

function showAllUserPlanConfigValue() {
    if(!checkCurrentUserPlanId()){
        $.messager.alert('错误', '无当前计划配置信息,请先保存当前计划配置信息！', 'error');
        return;
    }
	var vUserPlanId = $("#userPlanId").val();
	if(vUserPlanId==null||vUserPlanId==''||vUserPlanId==undefined){
        vUserPlanId==0;
	}
    var vurl =dataUrlPcv+'?userPlanId='+vUserPlanId;
    $('#gridUserPlanConfigValue').datagrid({
        url : vurl,
        type : 'GET',
        pageNumber : 1,
        onLoadError : function() {
            $.messager.alert('错误', '加载数据异常！', 'error');
        }
    });
}

function saveDataUserPlanConfigValue() {
    if(!checkCurrentUserPlanId()){
        $.messager.alert('错误', '无当前用户计划配置信息,请先保存当前用户计划配置信息！', 'error');
        return;
    }
    var url='../userPlanConfigValue/edit';
    if($("#userPlanConfigValueId").val()==null||$("#userPlanConfigValueId").val()==''){
        url='../userPlanConfigValue/create';
    }
    doFormSubmit('ff-plan-config-value',url,function(){
		closeWindow('eidt-window-pcv');
		$('#gridUserPlanConfigValue').datagrid('clearSelections');
		showAllUserPlanConfigValue();
	});
}

function getSelectedIdsuserPlanConfigValue() {
	var ids = [];
    var rows = $('#gridUserPlanConfigValue').datagrid('getSelections');
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

function delUserPlanConfigValue() {
	var arr = getSelectedIdsuserPlanConfigValue();
	if (arr.length > 0) {
		$.messager.confirm('提示信息', '您确认要删除吗?', function(data) {
			if (data) {
				var vurl = '../userPlanConfigValue/delete?ids=' + arr.join(',');
				doAjax(null, vurl, 'GET', true, function(data) {
					$('#griduserPlanConfigValue').datagrid('clearSelections');
                    showAlluserPlanConfigValue();
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

function closeWindowUserPlanConfigValue(){
    closeWindow('eidt-window-pcv');
}
