$(function() {
    loadRoundDaysList();
});

var dataUrlLifeExperienceConsume='../lifeExperienceConsume/getData';

function initLifeExperienceConsumeGrid(){
	$('#lifeExperienceConsumeGrid').datagrid({
		iconCls : 'icon-save',
		url : dataUrlLifeExperienceConsume,
		method:'GET',
		loadFilter: function(data){
			return loadDataFilter(data);
		},
		idField : 'id',
		loadMsg : '正在加载数据...',
        queryParams: form2Json("search-lifeExperienceConsume-form"),
        pageSize : 30,
		remoteSort : false,
		frozenColumns : [ [ {
			field : 'ID',
			checkbox : true
		} ] ],
		onDblClickRow: function (rowIndex, rowData) {
            $('#lifeExperienceConsumeGrid').datagrid('uncheckAll');
            $('#lifeExperienceConsumeGrid').datagrid('checkRow', rowIndex);
            editLifeExperienceConsume();
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
            sortable : true
        }, {
            field : 'consumeType.id',
            title : '消费类型',
            sortable : true,
            formatter : function(value, row, index) {
                return row.consumeType.name;
            },
            align : 'center'
        }, {
            field : 'cost',
            title : '花费',
            sortable : true,
            formatter : function(value, row, index) {
                return formatMoneyWithSymbal(value);
            },
            align : 'center'
        }, {
            field : 'statable',
            title : '加入统计',
            sortable : true,
            formatter : function(value, row, index) {
                return getStatusImage(value);
            },
            align : 'center'
        }, {
            field : 'buyRecordId',
            title : '关联总的消费记录',
            sortable : true,
            formatter : function(value, row, index) {
                var vv = false;
                if(value){
                    vv =true;
                }
                return getStatusImage(vv);
            },
            align : 'center'
        }] ],
		pagination : true,
		rownumbers : true,
		singleSelect : false,
		toolbar : [ {
			id : 'createBtnLifeExperienceConsume',
			text : '新增',
			iconCls : 'icon-add',
			handler : addLifeExperienceConsume
		}, '-', {
			id : 'editBtnLifeExperienceConsume',
			text : '修改',
			iconCls : 'icon-edit',
			handler : editLifeExperienceConsume
		}, '-', {
			id : 'deleteBtnLifeExperienceConsume',
			text : '删除',
			iconCls : 'icon-remove',
			handler : delLifeExperienceConsume
		}, '-', {
			id : 'searchBtnLifeExperienceConsume',
			text : '刷新',
			iconCls : 'icon-refresh',
			handler : showAllLifeExperienceConsume
		} ]
	});
}

function addLifeExperienceConsume() {
    if(!checkLifeExperienceDetailId()){
        $.messager.alert('提示', '请先保存人生经历详情!', 'error');
        return;
    }
	$('#eidt-lifeExperienceConsume-window').window('open');
	$('#edit-LifeExperienceConsume-form').form('clear');
	$('#edit-LifeExperienceConsume-form').form.url='../lifeExperienceConsume/create';
    loadConsumeTypeList();
    $("#consumeOfLifeExperienceDetailId").val($("#lifeExperienceDetailId").val());
    var formData = {
        'consumeType.id': 1,
        statable : true
    };
    $('#edit-LifeExperienceConsume-form').form('load', formData);
    loadBuyRecordList(0);
    $('#roundDaysList').combobox('setValue', 0);
}

function editLifeExperienceConsume() {
    if(!checkLifeExperienceDetailId()){
        $.messager.alert('提示', '请先保存人生经历详情!', 'error');
        return;
    }
    var rows = $('#lifeExperienceConsumeGrid').datagrid('getSelections');
    var num = rows.length;
    if (num == 0) {
        $.messager.alert('提示', '请选择一条记录进行操作!', 'info');
        return;
    }
	var url='../lifeExperienceConsume/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data){
		$('#eidt-lifeExperienceConsume-window').window('open');
        $('#edit-LifeExperienceConsume-form').form('clear');
        loadConsumeTypeList();
        $('#edit-LifeExperienceConsume-form').form('load', data);
		$('#edit-LifeExperienceConsume-form').form.url='../lifeExperienceConsume/edit?id=' + data.id;
        $('#consumeTypeList').combotree('setValue', data.consumeType.id);
        //设置字符
		$('#lifeExperienceConsumeGrid').datagrid('clearSelections');
		//设置外键值
        $("#consumeOfLifeExperienceDetailId").val($("#lifeExperienceDetailId").val());
        loadBuyRecordList(0);
        $('#roundDaysList').combobox('setValue', 0);
    });
}

function showAllLifeExperienceConsume() {
    $('#lifeExperienceConsumeGrid').datagrid({
        url : dataUrlLifeExperienceConsume,
        type : 'GET',
        queryParams: form2Json("search-lifeExperienceConsume-form"),
        pageNumber : 1,
        onLoadError : function() {
            $.messager.alert('错误', '加载数据异常！', 'error');
        }
    });
}

function checkLifeExperienceDetailId() {
    var lifeExperienceDetailId = $("#lifeExperienceDetailId").val();
    if(lifeExperienceDetailId==null||lifeExperienceDetailId==0){
    	return false;
    }else {
    	return true;
	}
}

function saveLifeExperienceConsume() {
    if(!checkLifeExperienceDetailId()){
        $.messager.alert('提示', '请先保存人生经历详情!', 'error');
        return;
    }
    var url='../lifeExperienceConsume/edit';
    if($("#lifeExperienceConsumeId").val()==null||$("#lifeExperienceConsumeId").val()==''){
        url='../lifeExperienceConsume/create';
    }
    doFormSubmit('edit-LifeExperienceConsume-form',url,function(){
		closeWindow('eidt-LifeExperienceConsume-window');
		$('#lifeExperienceConsumeGrid').datagrid('clearSelections');
        showAllLifeExperienceConsume();
        closeWindowLifeExperienceConsume();
    });
}

function getSelectedIdsLifeExperienceConsume() {
	var ids = [];
    var rows = $('#lifeExperienceConsumeGrid').datagrid('getSelections');
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

function delLifeExperienceConsume() {
	var arr = getSelectedIdsLifeExperienceConsume();
	if (arr.length > 0) {
		$.messager.confirm('提示信息', '您确认要删除吗?', function(data) {
			if (data) {
				var vurl = '../lifeExperienceConsume/delete?ids=' + arr.join(',');
				doAjax(null, vurl, 'GET', true, function(data) {
					$('#lifeExperienceConsumeGrid').datagrid('clearSelections');
                    showAllLifeExperienceConsume();
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

function closeWindowLifeExperienceConsume(){
    closeWindow('eidt-lifeExperienceConsume-window');
}


//编辑使用
function loadConsumeTypeList(){
    $('#consumeTypeList').combotree({
        url : '../consumeType/getConsumeTypeTree?',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        }
    });
}

function loadBuyRecordList(roundDays){
    if(roundDays==''||roundDays==null||roundDays=='undefined'){
        roundDays=0;
    }
    var lifeExperienceDetailId = $("#lifeExperienceDetailId").val();
    $('#buyRecordList').combotree({
        url : '../lifeExperienceConsume/getBuyRecordTree?lifeExperienceDetailId='+lifeExperienceDetailId+'&roundDays='+roundDays,
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        },
        onChange : function (newValue, oldValue) {
            loadBuyRecordForConsume(newValue);
        }
        // keyHandler: {
        //     query: function(q,e){
        //         //人工查询模式
        //         loadBuyRecordList(q);
        //         //q 为输入框的文本值
        //         if(e.keyCode==13){
        //             alert(q);
        //         }
        //     }
        // },
    });
}

function loadRoundDaysList() {
    $('#roundDaysList').combobox({
        onChange : function (newValue, oldValue) {
            loadBuyRecordList(newValue);
        }
    });
    $('#roundDaysList').combobox('setValue', 0);
}


function loadBuyRecordForConsume(value) {
    if(value==null||value==0){
        return;
    }
    var url='../buyRecord/get?id='+ value;
    doAjax(null,url,'GET',false,function(data){
        var formData = {
            name: data.goodsName,
            cost : data.totalPrice
        };
        $('#edit-LifeExperienceConsume-form').form('load', formData);
    });

}