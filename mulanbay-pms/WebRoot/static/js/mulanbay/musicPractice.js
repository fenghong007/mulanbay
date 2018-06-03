$(function() {
    initForm();
	initGrid();
    $('#practiceEndTime').datebox({
        onChange: calculateMinutes
    });
});

var dataUrl='../musicPractice/getData';

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
        view: detailview,
        detailFormatter:function(index,row){
            return '<div style="padding:2px"><table class="ddv"></table></div>';
        },
        onExpandRow: function(index,row){
            var ddv = $(this).datagrid('getRowDetail',index).find('table.ddv');
            ddv.datagrid({
                url:'../musicPracticeTune/getData?musicPracticeId='+row.id,
                fitColumns:true,
                singleSelect:true,
                rownumbers:true,
                loadMsg:'',
                height:'auto',
                method:'GET',
                loadFilter: function(data){
                    return loadDataFilter(data);
                },
                columns:[[
                    {field:'tune',title:'曲子名称',width:200,align:'center'},
                    {field:'times',title:'次数',width:100,align:'center'}
                ]],
                onResize:function(){
                    $('#dg').datagrid('fixDetailRowHeight',index);
                },
                onLoadSuccess:function(){
                    setTimeout(function(){
                        $('#dg').datagrid('fixDetailRowHeight',index);
                    },0);
                }
            });
            $('#dg').datagrid('fixDetailRowHeight',index);
        },
		columns : [ [ {
			field : 'id',
			title : '记录号',
			sortable : true,
			align : 'center'
		}, {
            field : 'musicInstrument.id',
            title : '乐器',
            sortable : true,
            formatter : function(value, row, index) {
                return row.musicInstrument.name;
            },
        }, {
            field : 'practiceDate',
            title : '练习日期',
            sortable : true
        }, {
            field : 'practiceStartTime',
            title : '练习开始时间',
            formatter : function(value, row, index) {
                return value.substring(11);
            },
            sortable : true,
            align : 'center'
        }, {
            field : 'practiceEndTime',
            title : '练习结束时间',
            formatter : function(value, row, index) {
                return value.substring(11);
            },
            sortable : true,
            align : 'center'
        },{
			field : 'minutes',
			title : '练习时长(分钟)',
			sortable : true,
            formatter : function(value, row, index) {
                if (value <=50) {
                    return '<font color="red">'+value+'</font>';
                }else {
                    return value;
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
			id : 'statBtn',
			text : '统计',
			iconCls : 'icon-stat',
			handler : stat
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

function initForm(){
    $('#musicInstrumentList').combotree({
        url : '../musicInstrument/getMusicInstrumentTree?needRoot=true',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        }
    });
    $('#musicInstrumentList2').combotree({
        url : '../musicInstrument/getMusicInstrumentTree',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        }
    });
}


function add() {
	$('#eidt-window').window('open');
	$('#ff').form('clear');
	$('#ff').form.url='../musicPractice/create';
    initForm();
    initGridTune(0);
    var nowTime =getNowDateTimeString();
    var nowDate = getNowDateString();
    var formData = {
        practiceStartTime: nowTime,
        practiceEndTime: nowTime,
        practiceDate: nowDate,
        minutes:60,
        'musicInstrument.id':1
    };
    $('#ff').form('load', formData);
}

function edit() {
	var rows = getSelectedSingleRow();
	var url='../musicPractice/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data) {
        $('#eidt-window').window('open');
        $('#ff').form('clear');
        initForm();
        $('#ff').form('load', data);
        $('#ff').form.url = '../musicPractice/edit?id=' + data.id;
        //设置字符
        $('#grid').datagrid('clearSelections');
        initGridTune(data.id);
        $("#musicPracticeId").val(data.id);
        $('#musicInstrumentList2').combotree('setValue', data.musicInstrument.id);
    });
}

function showAll() {
	refreshDatagrid(dataUrl,1,true);
}

function saveData() {
    //自动设置，原来在增加与修改时设置经常出问题
    var url='../musicPractice/edit';
    if($("#id").val()==null||$("#id").val()==''){
        url='../musicPractice/create';
    }
	doFormSubmit('ff',url,function(data){
		//closeWindow('eidt-window');
        $('#ff').form('clear');
        $('#ff').form('load', data);
        $('#ff').form.url = '../musicPractice/edit?id=' + data.id;
		$('#grid').datagrid('clearSelections');
        reloadDatagrid();
        $('#musicInstrumentList2').combotree('setValue', data.musicInstrument.id);
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
    var delUrlPrefix = '../musicPractice/delete';
    commonDeleteByIds(delUrlPrefix);
}

function stat(){
	var para =form2Json("search-window");
	var url='../musicPractice/stat';

	doAjax(para,url,'GET',false,function(data){
		$('#stat-window').window('open');
		var formData = {
			totalcount: data.totalCount,
            totalHours: minuteToHour(data.totalMinutes,1),
            averageValue: minuteToHour(data.averageMinutes,1)+"(小时),"+data.averageMinutes+"(分钟)"
        };
		$('#stat-form').form('load', formData);
        //生成饼图
        createMyPieData(data.pieData);
	});
}

function planStat(){
    getUserPlan('MusicPractice');
}

// 自动计算天数
function calculateMinutes(){
    var practiceStartTime=$('#practiceStartTime').val();
    var practiceEndTime =$('#practiceEndTime').val();
    var minutes = minuteDiff(practiceStartTime,practiceEndTime);
    $('#minutes').numberspinner('setValue', minutes);
}
