$(function() {
    initSearchFormYear();
    loadSportTypeList('sportTypeList',0);
	initGrid();
});

var dataUrl='../sportExercise/getData';

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
			field : 'sportType.id',
			title : '运动类型',
			formatter : function(value, row, index) {
				return row.sportType.name;
			}
		}, {
            field : 'exerciseDate',
            title : '锻炼日期'
        }, {
            field : 'kilometres',
            title : '公里数',
            formatter : function(value, row, index) {
                if ('CURRENT' ==row.mileageBest) {
                    return '<a href="javascript:edit()" title="当前最佳"><font color="red" alt="aaaa">★</font></a>'+value;
                }else if ('ONCE' ==row.mileageBest) {
                    return '<a href="javascript:edit()" title="历史最佳"><font color="purple">☆</font></a>'+value;
                }else {
                    return value;
                }
            },
            align : 'center'
        }, {
            field : 'minutes',
            title : '锻炼时长(分钟)',
            formatter : function(value, row, index) {
                if (value <60) {
                    return '<font color="red">'+value+'</font>';
                }else {
                    return value;
                }
            },
            align : 'center'
        },{
			field : 'speed',
			title : '平均速度(公里/小时)',
            formatter : function(value, row, index) {
                if ('CURRENT' ==row.fastBest) {
                    return '<a href="javascript:edit()" title="当前最佳"><font color="red" alt="aaaa">★</font></a>'+value;
                }else if ('ONCE' ==row.fastBest) {
                    return '<a href="javascript:edit()" title="历史最佳"><font color="purple">☆</font></a>'+value;
                }else {
                    return value;
                }
            },
            align : 'center'
        },{
            field : 'maxSpeed',
            title : '最佳速度(公里/小时)',
            align : 'center'
        },{
            field : 'pace',
            title : '平均配速(分钟/公里)',
            align : 'center'
        },{
            field : 'maxPace',
            title : '最佳配速(分钟/公里)',
            align : 'center'
        },{
            field : 'maxHeartRate',
            title : '最大心率',
            formatter : function(value, row, index) {
                if (value >=row.safeMaxHeartRate) {
                    return '<font color="red">'+value+'</font>';
                }else {
                    return value;
                }
            },
            align : 'center'
        },{
            field : 'averageHeartRate',
            title : '平均心率',
            formatter : function(value, row, index) {
                if (value >row.safeMaxHeartRate*0.8) {
                    return '<font color="red">'+value+'</font>';
                }else {
                    return value;
                }
            },
            align : 'center'
        },{
            field : 'sportMilestones',
            title : '里程碑',
            formatter : function(value, row, index) {
                var ss = '<a href="javascript:showAchieveMilestones('+'\''+row.id+'\''+');"><img src="../static/image/info.png"></img></a>';;
                if (value >0) {
                	var s = getStar(value);
                    ss += '<a href="javascript:showMilestones('+'\''+row.id+'\''+');">'+s+'</a>';
                }else {
                    ss += '';
                }
                return ss;
            },
            align : 'center'
        }, {
            field : 'aa',
            title : '身体近况',
            formatter : function(value, row, index) {
                return ss = '<a href="javascript:bodyAnalyseSe('+'\''+row.exerciseDate+'\''+');"><img src="../static/image/info.png"></img></a>';;
            },
            align : 'center'
        }] ],
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
            id : 'multiStatBtn',
            text : '数据统计',
            iconCls : 'icon-stat',
            handler : multiStat
        }, '-', {
            id : 'refreshMaxStatBtn',
            text : '刷新最佳统计',
            iconCls : 'icon-refresh',
            handler : refreshMaxStat
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
    loadSportTypeList('sportTypeList2',0);
    var nowDate =getNowDateTimeString();
    var formData = {
        speed: 0,
        exerciseDate:nowDate,
        maxSpeed:0,
        pace:0,
        maxPace:0,
        maxHeartRate:0,
        averageHeartRate : 0
    };
    $('#ff').form('load', formData);
	//$('#sportTypeList').combotree('setValue', 1);
	//$('#sportTypeList2').combotree('setValue', 1);
}

function loadSportTypeList(id,defaultValue){
    $('#'+id).combotree({
        url : '../sportType/getSportTypeTree?needRoot=true',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        }
    });
    if(defaultValue>0){
        $('#'+id).combotree('setValue', defaultValue);
    }
}

function edit() {
	var rows = getSelectedSingleRow();
	var url='../sportExercise/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data){
		$('#eidt-window').window('open');
        $('#ff').form('clear');
        loadSportTypeList('sportTypeList2',data.sportType.id);
		$('#ff').form('load', data);
		$('#ff').form.url='../sportExercise/edit?id=' + data.id;
		//$('#sportTypeList2').combotree('setValue', data.sportType.id);
		//设置字符
		$('#grid').datagrid('clearSelections');
	});
}

function showAll() {
	refreshDatagrid(dataUrl,1,true);
}

function saveData() {
    var url='../sportExercise/edit';
    if($("#id").val()==null||$("#id").val()==''){
        url='../sportExercise/create';
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
    var delUrlPrefix = '../sportExercise/delete';
    commonDeleteByIds(delUrlPrefix);
}

function stat(){
    var para =form2Json("search-window");
    var url='../sportExercise/stat';

    doAjax(para,url,'GET',false,function(data){
        $('#stat-window').window('open');
        var formData = {
            totalcount: data.totalCount,
            totalMinutes: data.totalMinutes,
            totalKilometres: data.totalKilometres
        };
        $('#stat-form').form('load', formData);
    });
}

function showAchieveMilestones(id) {
    var vurl='../sportExercise/getAchieveMilestones';
    showMilestonesGrid(vurl,id,'已经实现的里程碑');
}

function showMilestones(id) {
    var vurl='../sportMilestone/getData';
    showMilestonesGrid(vurl,id,'新实现的里程碑');
}

function showMilestonesGrid(vurl,id,title) {
    $('#grid').datagrid('uncheckAll');
    //$('#milestones-window').window("title",title);
    $('#milestones-window').window('open');
    var para = {
        sportExerciseId: id
    };
    $('#milestonesGrid').datagrid({
        iconCls : 'icon-info',
        url : vurl,
        method:'GET',
        loadFilter: function(data){
            return loadDataFilter(data);
        },
        idField : 'id',
        loadMsg : '正在加载数据...',
        pageSize : 30,
        queryParams: para,
        remoteSort : false,
        frozenColumns : [ [ {
            field : 'ID',
            checkbox : true
        } ] ],
        onLoadError: loadDataError,
        columns : [ [ {
            field : 'id',
            title : '记录号',
            sortable : true,
            align : 'center'
        }, {
            field : 'name',
            title : '名称',
            formatter : function(value, row, index) {
                if(row.sportExercise!=null){
                    return '<font color="green">'+value+'</font>'+'<img src="../static/image/tick.png"></img>';
                }else {
                    return value;
                }
            },
        }, {
            field : 'alais',
            title : '别名'
        }, {
            field : 'kilometres',
            title : '公里数',
            align : 'center'
        }, {
            field : 'minutes',
            title : '锻炼时长(分钟)',
            align : 'center'
        }, {
            field : 'fromExerciseDate',
            title : '开始日期',
            align : 'center'
        }, {
            field : 'toExerciseDate',
            title : '实现日期',
            align : 'center'
        }, {
            field : 'costDays',
            title : '花费天数',
            align : 'center'
        } ] ],
        pagination : true,
        rownumbers : true,
        singleSelect : false,
        toolbar : [ {
            id : 'nextMilestoneBtn1',
            text : '待实现的下一个里程碑（针对当前）',
            iconCls : 'icon-help',
            handler : getCurrentNextAchieveMilestone
        },{
            id : 'nextMilestoneBtn2',
            text : '待实现的下一个里程碑(针对全部)',
            iconCls : 'icon-help',
            handler : getWholeNextAchieveMilestone
        } ]
    });
    $("#milestonesGrid").datagrid({title:title});
    $("#milestoneSportTypeId").val(id);
}

function getCurrentNextAchieveMilestone() {
    getNextAchieveMilestone('CURRENT');
}

function getWholeNextAchieveMilestone() {
    getNextAchieveMilestone('WHOLE');
}

function getNextAchieveMilestone(type) {
    var milestoneSportTypeId = $("#milestoneSportTypeId").val();
    //alert(milestoneSportTypeId);
    var para = {
        sportExerciseId: milestoneSportTypeId,
        type:type
    };
    var url='../sportExercise/getNextAchieveMilestone';
    doAjax(para,url,'GET',false,function(data){
        $('#next-achieve-milestones-window').window('open');
        $('#next-achieve-milestones-form').form('clear');
        $('#next-achieve-milestones-form').form('load', data);
        loadSportTypeList('sportTypeList3',data.sportType.id);
        //$('#sportTypeList3').combotree('setValue', data.sportType.id);
    });
}

function multiStat() {
    var para =form2Json("search-window");
    var sportTypeId = para.sportTypeId;
    if(checkStrNull(sportTypeId)){
        $.messager.alert('提示', '请选择运动类型!', 'info');
        return;
    }
    var url='../sportExercise/multiStat';
    doAjax(para,url,'GET',false,function(data){
        $('#grid').datagrid('clearSelections');
        $('#multiStat-window').window('open');
        $('#multiStat-form').form('clear');
        $('#multiStat-form').form('load', data);
    });
}

function showMultiSportExerciseInfo(groupType,type) {
    var para =form2Json("search-window");
    para.groupType=groupType;
    para.type=type;
    var url='../sportExercise/getByMultiStat';
    doAjax(para,url,'GET',false,function(data){
        $('#eidt-window').window('open');
        $('#ff').form('clear');
        $('#ff').form('load', data);
        $('#ff').form.url='../sportExercise/edit?id=' + data.id;
        loadSportTypeList('sportTypeList2',data.sportType.id);
        //$('#sportTypeList2').combotree('setValue', data.sportType.id);
        //设置字符
        $('#grid').datagrid('clearSelections');
    });
}

function refreshMaxStat() {
    var para =form2Json("search-window");
    var sportTypeId = para.sportTypeId;
    if(checkStrNull(sportTypeId)){
        $.messager.alert('提示', '请选择运动类型!', 'info');
        return;
    }
    var vurl = '../sportExercise/refreshMaxStat';
    doAjax(para, vurl, 'GET',true, function(data) {
        reloadDatagrid();
    });
}

function planStat(){
    getUserPlan('SportExercise');
}

//其他页面进入
function bodyAnalyseSe(startDate) {
    $('#grid').datagrid('clearSelections');
    $('#body-analyse-window').window('open');
    //查看锻炼的一个星期内是否有心脏问题
    var endDate = getDayByDate(7,startDate);
    // 查询条件今年的
    var formData = {
        name:'心脏',
        groupField:'DISEASE',
        startDate: startDate,
        endDate: endDate
    };
    $('#body-analyse-form').form('load', formData);
    initBodyAnalyseGrid();
}