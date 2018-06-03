$(function() {
	// 加载公司树
    initForm();
	initGrid();
});

var dataUrl='../diet/getData';

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
            field : 'foods',
            title : '食物'
        }, {
            field : 'tags',
            title : '标签'
        },{
            field : 'foodTypeName',
            title : '食物类型',
            align : 'center'
        },{
			field : 'dietTypeName',
			title : '类型',
            align : 'center'
        },{
            field : 'dietSourceName',
            title : '来源',
            align : 'center'
        }, {
            field : 'location',
            title : '地点'
        },{
            field : 'occurTime',
            title : '发生时间',
            align : 'center'
        } ,{
            field : 'createdTime',
            title : '创建时间',
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
			id : 'analyseBtn',
			text : '分析',
			iconCls : 'icon-stat',
			handler : openStatDietAnalyse
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
		}, '-', {
            id : 'refreshRateBtn',
            text : '刷新进度',
            iconCls : 'icon-refresh',
            handler : refreshRate
        } ]
	});
}

function add() {
	$('#eidt-window').window('open');
	$('#ff').form('clear');
	initForm();
    var formData = {
        dietType: 'BREAKFAST',
        dietSource: 'SELF_MADE'
    };
    $('#ff').form('load', formData);
}

function loadLastDiet() {
    var para =form2JsonEnhanced("ff");
    var url='../diet/getLastDiet?dietType='+para.dietType+"&dietSource="+para.dietSource;
    doAjax(null,url,'GET',false,function(data){
        if(data){
            data.id=null;
            data.occurTime=null;
            $('#ff').form('load', data);
        }
    });
}

function initForm(){
}

function edit() {
	var rows = getSelectedSingleRow();
	var url='../diet/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data){
		$('#eidt-window').window('open');
        $('#ff').form('clear');
        initForm();
		$('#ff').form('load', data);
		$('#ff').form.url='../diet/edit?id=' + data.id;
		//设置字符
		$('#grid').datagrid('clearSelections');
    });
}

function showAll() {
	refreshDatagrid(dataUrl,1,true);
}

function saveData() {
    var url='../diet/edit';
    if($("#id").val()==null||$("#id").val()==''){
        url='../diet/create';
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
    var delUrlPrefix = '../diet/delete';
    (delUrlPrefix);
}

function stat(){
	var para =form2JsonEnhanced("search-window");
	var url='../diet/stat';

	doAjax(para,url,'GET',false,function(data){
		$('#stat-window').window('open');
        //生成饼图
        createPieData(data);
	});
}

function planStat(){
    getUserPlan('Diet');
}

function getPlanConfigTree(){
    $('#planConfigList').combotree({
        url : '../planConfig/getPlanConfigTree?needRoot=true',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        }
    });
}

function refreshRate() {
    var url='../diet/refreshRate';
    doAjax(null,url,'GET',false,function(data){
        $.messager.alert('成功', data, 'info');
        reloadDatagrid();
    });
}

function showPlanValueHelp() {
    var msg='<font color="red">注意</font>：<br/>' +
        '(1)表达的是为了实现这个梦想需要达到的预期值<br/>'+
        '(2)比如学习尤克里里至少要学1000个小时，那么在这里就填1000，系统到时自动会和计划配置里的统计比对<br/>'+
        '(3)单位类型目前不需要配置';
    $.messager.alert('提示', msg, '');
}

function openStatDietAnalyse(){
    $('#diet-analyse-stat-window').window('open');
    var formData = {
        startDate: getYear(0)+'-01-01',
        endDate: getYear(0)+'-12-31'
    };
    $('#diet-analyse-stat-search-form').form('load', formData);
    //statDietAnalyse();
}

function statDietAnalyse(){
    var para =form2Json("diet-analyse-stat-search-form");
    var url='../diet/analyse';
    doAjax(para,url,'GET',false,function(data){
        if(para.chartType=='PIE'){
            createPieDataEnhanced(data,'dietAnalyseContainer');
        }else{
            createBarDataEnhanced(data,'dietAnalyseContainer');
        }
    });
}