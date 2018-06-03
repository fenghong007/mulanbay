var dataUrlPcv='../statValueConfig/getData';

function initGridStatValueConfig(fid,type){
	$('#gridStatValueConfig').datagrid({
		iconCls : 'icon-save',
		url : dataUrlPcv+'?fid='+fid+'&type='+type,
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
            $('#gridStatValueConfig').datagrid('uncheckAll');
            $('#gridStatValueConfig').datagrid('checkRow', rowIndex);
            editStatValueConfig();
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
            field : 'typeName',
            title : '类型',
            align : 'center'
        }, {
            field : 'valueClass',
            title : '值类型',
            align : 'center'
        }, {
            field : 'orderIndex',
            title : '排序号',
            align : 'center'
        }, {
            field : 'casCadeTypeName',
            title : '级联类型',
            align : 'center'
        }, {
            field : 'promptMsg',
            title : '提示信息',
            align : 'center'
        }] ],
		pagination : true,
		rownumbers : true,
		singleSelect : false,
		toolbar : [ {
			id : 'createBtnStatValueConfig',
			text : '新增',
			iconCls : 'icon-add',
			handler : addStatValueConfig
		}, '-', {
			id : 'editBtnStatValueConfig',
			text : '修改',
			iconCls : 'icon-edit',
			handler : editStatValueConfig
		}, '-', {
			id : 'deleteBtnStatValueConfig',
			text : '删除',
			iconCls : 'icon-remove',
			handler : delStatValueConfig
		}, '-', {
			id : 'searchBtnStatValueConfig',
			text : '刷新',
			iconCls : 'icon-refresh',
			handler : showAllStatValueConfig
		} ]
	});
}

function addStatValueConfig() {
	$('#eidt-window-pcv').window('open');
	var type = $("#type").val();
	$('#ff-stat-value-config-value').form('clear');
	$('#ff-stat-value-config-value').form.url='../statValueConfig/create';
	//值为外面看病记录的表单的id
    $("#statValueConfigFid").val($("#id").val());
    $("#type").val(type);
}

function editStatValueConfig() {
    var rows = $('#gridStatValueConfig').datagrid('getSelections');
    var num = rows.length;
    if (num == 0) {
        $.messager.alert('提示', '请选择一条记录进行操作!', 'info');
        return;
    }
	var url='../statValueConfig/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data) {
        $('#eidt-window-pcv').window('open');
        $('#ff-stat-value-config-value').form('clear');
        $('#ff-stat-value-config-value').form('load', data);
        $('#ff-stat-value-config-value').form.url = '../statValueConfig/edit?id=' + data.id;
        //设置字符
        $('#gridStatValueConfig').datagrid('clearSelections');
        $("#statValueConfigFid").val($("#id").val());
        //$("#type").val($("#type").val());
    });
}

function checkCurrentFid(){
    var statValueConfigFid = $("#statValueConfigFid").val();
    if(statValueConfigFid==null||statValueConfigFid==''||statValueConfigFid==undefined){
        return false;
    }else {
    	return true;
	}
}

function showAllStatValueConfig() {
    var statValueConfigFid = $("#statValueConfigFid").val();
	if(statValueConfigFid==null||statValueConfigFid==''||statValueConfigFid==undefined){
        statValueConfigFid==0;
	}
	var type =$("#type").val();
    var vurl =dataUrlPcv+'?fid='+statValueConfigFid+'&type='+type;
    $('#gridStatValueConfig').datagrid({
        url : vurl,
        type : 'GET',
        pageNumber : 1,
        onLoadError : function() {
            $.messager.alert('错误', '加载数据异常！', 'error');
        }
    });
}

function saveDataStatValueConfig() {
    if(!checkCurrentFid()){
        $.messager.alert('错误', '无当前计划配置信息,请先保存当前计划配置信息！', 'error');
        return;
    }
    //alert($("#type").val());
    var url='../statValueConfig/edit';
    if($("#statValueConfigId").val()==null||$("#statValueConfigId").val()==''){
        url='../statValueConfig/create';
    }
    doFormSubmit('ff-stat-value-config-value',url,function(){
		closeWindow('eidt-window-pcv');
		$('#gridStatValueConfig').datagrid('clearSelections');
		showAllStatValueConfig();
	});
}

function getSelectedIdsstatValueConfig() {
	var ids = [];
    var rows = $('#gridStatValueConfig').datagrid('getSelections');
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

function delStatValueConfig() {
	var arr = getSelectedIdsstatValueConfig();
	if (arr.length > 0) {
		$.messager.confirm('提示信息', '您确认要删除吗?', function(data) {
			if (data) {
				var vurl = '../statValueConfig/delete?ids=' + arr.join(',');
				doAjax(null, vurl, 'GET', true, function(data) {
					$('#gridStatValueConfig').datagrid('clearSelections');
                    showAllStatValueConfig();
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

function closeWindowStatValueConfig(){
    closeWindow('eidt-window-pcv');
}
