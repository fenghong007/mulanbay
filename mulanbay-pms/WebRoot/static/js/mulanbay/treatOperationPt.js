
var dataUrlOperation='/treatOperation/getData';

function initGridOperation(treatRecordId){
	$('#gridOperation').datagrid({
		iconCls : 'icon-save',
		url : dataUrlOperation+'?treatRecordId='+treatRecordId,
		method:'GET',
		loadFilter: function(data){
			return loadDataFilter(data);
		},
		idField : 'id',
		loadMsg : '正在加载数据...',
		pageSize : 30,
		remoteSort : false,
		frozenColumns : [ [ {
			field : 'ID',
			checkbox : true
		} ] ],
		onDblClickRow: function (rowIndex, rowData) {
            $('#gridOperation').datagrid('uncheckAll');
            $('#gridOperation').datagrid('checkRow', rowIndex);
            editOperation();
        },
        onLoadError: loadDataError,
		columns : [ [ {
			field : 'id',
			title : '记录号',
			sortable : true,
			align : 'center'
		}, {
            field : 'name',
            title : '手术名称'
        }, {
            field : 'fee',
            title : '费用',
            align : 'center'
        }, {
            field : 'treatDate',
            title : '看病日期',
            align : 'center'
        }, {
            field : 'available',
            title : '是否有效',
            formatter : function(value, row, index) {
                return getStatusImage(value);
            },
            align : 'center'
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
        }] ],
		pagination : true,
		rownumbers : true,
		singleSelect : false,
		toolbar : [ {
			id : 'createBtnOperation',
			text : '新增',
			iconCls : 'icon-add',
			handler : addOperation
		}, '-', {
			id : 'editBtnOperation',
			text : '修改',
			iconCls : 'icon-edit',
			handler : editOperation
		}, '-', {
			id : 'deleteBtnOperation',
			text : '删除',
			iconCls : 'icon-remove',
			handler : delOperation
		}, '-', {
			id : 'searchBtnOperation',
			text : '刷新',
			iconCls : 'icon-refresh',
			handler : showAllOperation
		} ]
	});
}

function addOperation() {
	$('#eidt-window-operation').window('open');
	$('#ff-operation').form('clear');
	$('#ff-operation').form.url='../treatOperation/create';
	//值为外面看病记录的表单的id
    $("#operationTreatRecordId").val($("#id").val());
}

function editOperation() {
    var rows = $('#gridOperation').datagrid('getSelections');
    var num = rows.length;
    if (num == 0) {
        $.messager.alert('提示', '请选择一条记录进行操作!', 'info');
        return;
    }
	var url='../treatOperation/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data){
		$('#eidt-window-operation').window('open');
        $('#ff-operation').form('clear');
        $('#ff-operation').form('load', data);
		$('#ff-operation').form.url='../treatOperation/edit?id=' + data.id;
		//设置字符
		$('#gridOperation').datagrid('clearSelections');
        $("#operationTreatRecordId").val($("#id").val());
    });
}

function showAllOperation() {
	var vtreatRecordId = $("#operationTreatRecordId").val();
	if(vtreatRecordId==null||vtreatRecordId==''||vtreatRecordId==undefined){
        vtreatRecordId==0;
	}
    var vurl =dataUrlOperation+'?treatRecordId='+vtreatRecordId;
    $('#gridOperation').datagrid({
        url : vurl,
        type : 'GET',
        pageNumber : 1,
        onLoadError : function() {
            $.messager.alert('错误', '加载数据异常！', 'error');
        }
    });
}

function checkOperationTreatRecordId() {
    var vtreatRecordId = $("#operationTreatRecordId").val();
    if(vtreatRecordId==null||vtreatRecordId==0){
    	return false;
    }else {
    	return true;
	}
}

function saveDataOperation() {
    if(!checkOperationTreatRecordId()){
        $.messager.alert('提示', '请先保存看病记录!', 'error');
        return;
    }
    var url='../treatOperation/edit';
    if($("#treatOperationId").val()==null||$("#treatOperationId").val()==''){
        url='../treatOperation/create';
    }
    doFormSubmit('ff-operation',url,function(){
		closeWindow('eidt-window-operation');
		$('#gridOperation').datagrid('clearSelections');
		showAllOperation();
	});
}

function getSelectedIdsOperation() {
	var ids = [];
    var rows = $('#gridOperation').datagrid('getSelections');
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

function delOperation() {
	var arr = getSelectedIdsOperation();
	if (arr.length > 0) {
		$.messager.confirm('提示信息', '您确认要删除吗?', function(data) {
			if (data) {
				var vurl = '../treatOperation/delete?ids=' + arr.join(',');
				doAjax(null, vurl, 'GET', true, function(data) {
					$('#gridOperation').datagrid('clearSelections');
                    showAllOperation();
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

function closeWindowOperation(){
    closeWindow('eidt-window-operation');
}
