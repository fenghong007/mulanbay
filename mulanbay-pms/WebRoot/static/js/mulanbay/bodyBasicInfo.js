$(function() {
	// 加载公司树
    initForm();
	initGrid();
    $('#weight').numberbox({
        onChange: calculateBmi
    });
});

var dataUrl='../bodyBasicInfo/getData';

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
            field : 'recordDate',
            title : '记录日期'
        },{
			field : 'weight',
			title : '体重(公斤)',
            align : 'center'
        } ,{
            field : 'height',
            title : '身高(厘米)',
            sortable : true,
            align : 'center'
        },{
            field : 'bmi',
            title : 'BMI指数',
            sortable : true,
            formatter : function(value, row, index) {
                if (value <=18.4) {
                    return '<font color="#bc8f8f">'+value+'(偏瘦)</font>';
                }else if(value >=18.5&&value<=23.9){
                    return '<font color="green">'+value+'(正常)</font>';
                }else if(value >=24&&value<=27.9){
                    return '<font color="yellow">'+value+'(过重)</font>';
                } else {
                    return '<font color="red">'+value+'(肥胖)</font>';
                }
            },
            align : 'center'
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
    var nowDate =getNowDateString();
    var formData = {
        recordDate:nowDate,
        height:173
    };
    $('#ff').form('load', formData);
}

function initForm(){
}

function edit() {
	var rows = getSelectedSingleRow();
	var url='../bodyBasicInfo/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data){
		$('#eidt-window').window('open');
        $('#ff').form('clear');
        initForm();
		$('#ff').form('load', data);
		//设置字符
		$('#grid').datagrid('clearSelections');
	});
}

function showAll() {
	refreshDatagrid(dataUrl,1,true);
}

function saveData() {
    var url='../bodyBasicInfo/edit';
    if($("#id").val()==null||$("#id").val()==''){
        url='../bodyBasicInfo/create';
    }
	doFormSubmit('ff',url,function(){
		closeWindow('eidt-window');
		$('#grid').datagrid('clearSelections');
        reloadDatagrid();
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
    var delUrlPrefix = '../bodyBasicInfo/delete';
    commonDeleteByIds(delUrlPrefix);
}

function calculateBmi(){
    var height=$('#height').val()/100.0;
    var weight =$('#weight').val();
    var bmi= weight/(height*height);
    $('#bmi').numberbox('setValue', bmi);
}