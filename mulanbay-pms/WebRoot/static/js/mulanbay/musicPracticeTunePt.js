$(function() {
    $('#tuneTypeList').combobox({
        onChange : function (newValue, oldValue) {
            loadTuneList(newValue);
        }
    });
});


var dataUrlTune='/musicPracticeTune/getData';

function initGridTune(musicPracticeId){
	$('#gridTune').datagrid({
		iconCls : 'icon-save',
		url : dataUrlTune+'?musicPracticeId='+musicPracticeId,
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
            $('#gridTune').datagrid('uncheckAll');
            $('#gridTune').datagrid('checkRow', rowIndex);
            editTune();
        },
        onLoadError: loadDataError,
		columns : [ [ {
			field : 'id',
			title : '记录号',
			sortable : true,
			align : 'center'
		}, {
            field : 'tune',
            title : '练习曲子',
            sortable : true
        }, {
            field : 'times',
            title : '练习次数',
            sortable : true
        }, {
            field : 'tuneTypeName',
            title : '练习类型',
            sortable : true,
            align : 'center'
        }, {
            field : 'levelName',
            title : '水平',
            sortable : true,
            align : 'center'
        }] ],
		pagination : true,
		rownumbers : true,
		singleSelect : false,
		toolbar : [ {
			id : 'createBtnTune',
			text : '新增',
			iconCls : 'icon-add',
			handler : addTune
		}, '-', {
			id : 'editBtnTune',
			text : '修改',
			iconCls : 'icon-edit',
			handler : editTune
		}, '-', {
			id : 'deleteBtnTune',
			text : '删除',
			iconCls : 'icon-remove',
			handler : delTune
		}, '-', {
			id : 'searchBtnTune',
			text : '刷新',
			iconCls : 'icon-refresh',
			handler : showAllTune
		} ]
	});
}

function addTune() {
	$('#eidt-window-tune').window('open');
	$('#ff-tune').form('clear');
	$('#ff-tune').form.url='../musicPracticeTune/create';
    $("#musicPracticeId").val($("#id").val());
    loadTuneList('TUNE');
    $('#tuneTypeList').combobox('setValue','TUNE');
}

function editTune() {
    var rows = $('#gridTune').datagrid('getSelections');
    var num = rows.length;
    if (num == 0) {
        $.messager.alert('提示', '请选择一条记录进行操作!', 'info');
        return;
    }
	var url='../musicPracticeTune/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data){
		$('#eidt-window-tune').window('open');
        loadTuneList(data.tuneType);
        $('#ff-tune').form('clear');
        $('#ff-tune').form('load', data);
		$('#ff-tune').form.url='../musicPracticeTune/edit?id=' + data.id;
        $('#tuneList').combobox('setValue', data.tune);
        //设置字符
		$('#gridTune').datagrid('clearSelections');
        $("#musicPracticeId").val($("#id").val());
    });
}


function loadTuneList(tuneType) {
    $('#tuneList').combobox({
        url : '../musicPracticeTune/getMusicPracticeTuneTree?tuneType='+tuneType,
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        }
    });
}

function showAllTune() {
	var vmusicPracticeId = $("#musicPracticeId").val();
	if(vmusicPracticeId==null){
        vmusicPracticeId==0;
	}
    var vurl =dataUrlTune+'?musicPracticeId='+vmusicPracticeId;
    $('#gridTune').datagrid({
        url : vurl,
        type : 'GET',
        pageNumber : 1,
        onLoadError : function() {
            $.messager.alert('错误', '加载数据异常！', 'error');
        }
    });
}

function checkMusicPracticeId() {
    var vmusicPracticeId = $("#musicPracticeId").val();
    if(vmusicPracticeId==null||vmusicPracticeId==0){
        return false;
    }else {
    	return true;
	}
}

function saveDataTune() {
	if(!checkMusicPracticeId()){
        $.messager.alert('提示', '请先保存练习记录!', 'error');
        return;
    }
    //自动设置，原来在增加与修改时设置经常出问题
    var url='../musicPracticeTune/edit';
    if($("#musicPracticeTuneId").val()==null||$("#musicPracticeTuneId").val()==''){
        url='../musicPracticeTune/create';
    }
    doFormSubmit('ff-tune',url,function(){
		closeWindow('eidt-window-tune');
		$('#gridTune').datagrid('clearSelections');
		showAllTune();
	});
}

function getSelectedIdsTune() {
	var ids = [];
    var rows = $('#gridTune').datagrid('getSelections');
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

function delTune() {
	var arr = getSelectedIdsTune();
	if (arr.length > 0) {
		$.messager.confirm('提示信息', '您确认要删除吗?', function(data) {
			if (data) {
				var vurl = '../musicPracticeTune/delete?ids=' + arr.join(',');
				doAjax(null, vurl, 'GET', true, function(data) {
					$('#gridTune').datagrid('clearSelections');
					showAllTune();
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

function closeWindowTune(){
    closeWindow('eidt-window-tune');
}
