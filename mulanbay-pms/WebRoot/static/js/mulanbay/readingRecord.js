$(function() {
	// 加载公司树
    initForm();
	initGrid();
});

var dataUrl='../readingRecord/getData';

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
            field : 'bookCategory.id',
            title : '图书分类',
            sortable : true,
            formatter : function(value, row, index) {
            	if(row.bookCategory){
                    return row.bookCategory.name;
                }else {
            		return '--';
				}
            },
            align : 'center'
        }, {
            field : 'bookName',
            title : '书名',
			formatter : function(value, row, index) {
                if(value.length>25){
                    return value.substring(0,25)+'<a href="javascript:showFullDetail('+'\''+value+'\''+');"> ......</a>';
                }
                return value;
            }
        }, {
            field : 'author',
            title : '作者',
            formatter : function(value, row, index) {
                if(value==null){
                    return '--';
                }else if(value.length>6){
                    return value.substring(0,6)+'<a href="javascript:showFullDetail('+'\''+value+'\''+');"> ......</a>';
                }else{
                    return value;
                }
            }
        }, {
            field : 'nation',
            title : '国家'
        }, {
            field : 'statusName',
            title : '状态',
			formatter : function(value, row, index) {
                if (row.status =='READED') {
                    return '<font color="green">'+value+'</font>';
                }else if (row.status =='UNREAD') {
                    return '<font color="red">'+value+'</font>';
                }else if (row.status =='READING') {
                    return '<font color="purple">'+value+'</font>';
                }else {
                    return '<font color="gray">'+value+'</font>';
                }
            },
            align : 'center'
        }, {
            field : 'importantLevel',
            title : '重要等级',
            formatter : function(value, row, index) {
                var s = getStar(value);
                if (value >=4) {
                    return '<font color="red">'+s+'</font>';
                }else {
                    return s;
                }
            },
            align : 'center'
        }, {
            field : 'languageName',
            title : '语言',
            align : 'center'
        }, {
            field : 'bookTypeName',
            title : '书籍类型',
            align : 'center'
        }, {
            field : 'storeDate',
            formatter : function(value, row, index) {
                if(row.storeDate==null){
                    return '--';
                }
                var days = dateDiff(getNowDateString(),value);
                if (days <=30) {
                    return '<font color="red">'+value+'</font>';
                }else {
                    return value;
                }
            },
            title : '借入/购买日期',
            align : 'center'
        }, {
            field : 'proposedDate',
            formatter : function(value, row, index) {
            	if(row.finishedDate!=null){
            		return value;
				}
            	var days = dateDiff(value,getNowDateString());
                if (days <=30) {
                    return '<font color="red">'+value+'</font>';
                }else {
                    return value;
                }
            },
            title : '期望完成日期',
            align : 'center'
        }, {
            field : 'beginDate',
            title : '开始日期',
            align : 'center'
        }, {
            field : 'finishedDate',
            title : '完成日期',
            align : 'center'
        }, {
            field : 'costDays',
            formatter : function(value, row, index) {
                if(value){
                    if (value >30) {
                        return '<font color="red">'+formatDays(value)+'</font>';
                    }else {
                        return formatDays(value);
                    }
                }else {
                    return '--';
                }

            },
            title : '完成时长',
            align : 'center'
        }, {
            field : 'isbn',
            title : 'ISBN'
        }, {
            field : 'publishedYear',
            title : '出版年份'
        }, {
            field : 'press',
            title : '出版社'
        } ] ],
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
			text : '已完成统计',
			iconCls : 'icon-stat',
			handler : showStat
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
	$('#ff').form.url='../readingRecord/create';
    var formData = {
        status: 'UNREAD',
        language:'CHINESE',
        bookType:'PAPER'
    };
    $('#ff').form('load', formData);
    initGridReadingRecordDetail(0);
}

function initForm(){
    $('#bookCategoryList').combotree({
        url : '../bookCategory/getBookCategoryTree',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        }
    });
    $('#bookCategoryList2').combotree({
        url : '../bookCategory/getBookCategoryTree?needRoot=true',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        }
    });
}

function edit() {
	var rows = getSelectedSingleRow();
	var url='../readingRecord/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data){
		$('#eidt-window').window('open');
        $('#ff').form('clear');
        initForm();
		$('#ff').form('load', data);
		$('#ff').form.url='../readingRecord/edit?id=' + data.id;
        $('#bookCategoryList').combotree('setValue', data.bookCategory.id);
        initGridReadingRecordDetail(data.id);
        $("#detailReadingRecordId").val(data.id);
        //设置字符
		$('#grid').datagrid('clearSelections');
	});
}

function showAll() {
	refreshDatagrid(dataUrl,1,true);
}

function saveData() {
    var url='../readingRecord/edit';
    if($("#id").val()==null||$("#id").val()==''){
        url='../readingRecord/create';
    }
	doFormSubmit('ff',url,function(data){
		//closeWindow('eidt-window');
        $('#ff').form.url='../readingRecord/edit?id=' + data.id;
        $('#grid').datagrid('clearSelections');
        reloadDatagrid();
        $('#ff').form('load', data);
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
    var delUrlPrefix = '../readingRecord/delete';
    commonDeleteByIds(delUrlPrefix);
}

function stat(){
	var para =form2Json("read-stat-search-form");
	var url='../readingRecord/stat';

	doAjax(para,url,'GET',false,function(data){
        $('#stat-form').form('clear');
        var formData = {
            totalCount: data.totalCount,
            totalCostDays: data.totalCostDays,
            avgCostDays: data.avgCostDays
        };
        $('#stat-form').form('load', formData);
        //生成饼图
        createPieData(data.pieData);
	});
}

function planStat(){
    getUserPlan('ReadingRecord');
}

function showStat() {
    $('#read-stat-window').window('open');
    stat();
}