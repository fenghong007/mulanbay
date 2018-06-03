
var dataUrlReadingRecordDetail='../readingRecordDetail/getData';

function initGridReadingRecordDetail(readingRecordId){
	$('#gridReadingRecordDetail').datagrid({
		iconCls : 'icon-save',
		url : dataUrlReadingRecordDetail+'?readingRecordId='+readingRecordId,
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
            $('#gridReadingRecordDetail').datagrid('uncheckAll');
            $('#gridReadingRecordDetail').datagrid('checkRow', rowIndex);
            editReadingRecordDetail();
        },
        onLoadError: loadDataError,
		columns : [ [ {
			field : 'id',
			title : '记录号',
			sortable : true,
			align : 'center'
		}, {
            field : 'readingRecord.bookName',
            title : '书名',
            formatter : function(value, row, index) {
                if(row.readingRecord.bookName.length>25){
                    return row.readingRecord.bookName.substring(0,25)+'<a href="javascript:showFullDetail('+'\''+row.readingRecord.bookName+'\''+');"> ......</a>';
                }
                return value;
            }
        }, {
            field : 'readTime',
            title : '阅读时间',
            align : 'center'
        }, {
            field : 'minutes',
            title : '阅读时间(分钟)',
            align : 'center'
        }] ],
		pagination : true,
		rownumbers : true,
		singleSelect : false,
		toolbar : [ {
			id : 'createBtnReadingRecordDetail',
			text : '新增',
			iconCls : 'icon-add',
			handler : addReadingRecordDetail
		}, '-', {
			id : 'editBtnReadingRecordDetail',
			text : '修改',
			iconCls : 'icon-edit',
			handler : editReadingRecordDetail
		}, '-', {
			id : 'deleteBtnReadingRecordDetail',
			text : '删除',
			iconCls : 'icon-remove',
			handler : delReadingRecordDetail
		}, '-', {
			id : 'searchBtnReadingRecordDetail',
			text : '刷新',
			iconCls : 'icon-refresh',
			handler : showAllReadingRecordDetail
		} ]
	});
}

function addReadingRecordDetail() {
	$('#eidt-window-ReadingRecordDetail').window('open');
	$('#ff-ReadingRecordDetail').form('clear');
	$('#ff-ReadingRecordDetail').form.url='../readingRecordDetail/create';
	//值为外面看病记录的表单的id
    $("#detailReadingRecordId").val($("#id").val());
}

function editReadingRecordDetail() {
    var rows = $('#gridReadingRecordDetail').datagrid('getSelections');
    var num = rows.length;
    if (num == 0) {
        $.messager.alert('提示', '请选择一条记录进行操作!', 'info');
        return;
    }
	var url='../readingRecordDetail/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data){
		$('#eidt-window-ReadingRecordDetail').window('open');
        $('#ff-ReadingRecordDetail').form('clear');
        $('#ff-ReadingRecordDetail').form('load', data);
		$('#ff-ReadingRecordDetail').form.url='../readingRecordDetail/edit?id=' + data.id;
		//设置字符
		$('#gridReadingRecordDetail').datagrid('clearSelections');
        $("#detailReadingRecordId").val($("#id").val());
    });
}

function showAllReadingRecordDetail() {
	var vdetailReadingRecordId = $("#detailReadingRecordId").val();
	if(vdetailReadingRecordId==null||vdetailReadingRecordId==''||vdetailReadingRecordId==undefined){
        vdetailReadingRecordId==0;
	}
    var vurl =dataUrlReadingRecordDetail+'?readingRecordId='+vdetailReadingRecordId;
    $('#gridReadingRecordDetail').datagrid({
        url : vurl,
        type : 'GET',
        pageNumber : 1,
        onLoadError : function() {
            $.messager.alert('错误', '加载数据异常！', 'error');
        }
    });
}

function checkDetailReadingRecordId() {
    var vdetailReadingRecordId = $("#detailReadingRecordId").val();
    if(vdetailReadingRecordId==null||vdetailReadingRecordId==0){
    	return false;
    }else {
    	return true;
	}
}

function saveDataReadingRecordDetail() {
    if(!checkDetailReadingRecordId()){
        $.messager.alert('提示', '请先保存阅读记录!', 'error');
        return;
    }
    var url='../readingRecordDetail/edit';
    if($("#readingRecordDetailId").val()==null||$("#readingRecordDetailId").val()==''){
        url='../readingRecordDetail/create';
    }
    doFormSubmit('ff-ReadingRecordDetail',url,function(){
		closeWindow('eidt-window-ReadingRecordDetail');
		$('#gridReadingRecordDetail').datagrid('clearSelections');
		showAllReadingRecordDetail();
	});
}

function getSelectedIdsReadingRecordDetail() {
	var ids = [];
    var rows = $('#gridReadingRecordDetail').datagrid('getSelections');
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

function delReadingRecordDetail() {
	var arr = getSelectedIdsReadingRecordDetail();
	if (arr.length > 0) {
		$.messager.confirm('提示信息', '您确认要删除吗?', function(data) {
			if (data) {
				var vurl = '../readingRecordDetail/delete?ids=' + arr.join(',');
				doAjax(null, vurl, 'GET', true, function(data) {
					$('#gridReadingRecordDetail').datagrid('clearSelections');
                    showAllReadingRecordDetail();
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

function closeWindowReadingRecordDetail(){
    closeWindow('eidt-window-ReadingRecordDetail');
}
