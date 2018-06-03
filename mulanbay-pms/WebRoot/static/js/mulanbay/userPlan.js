$(function() {
	// 加载公司树
    initForm();
	initGrid();
});

var dataUrl='../userPlan/getData';

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
            field : 'title',
            title : '标题'
        }, {
            field : 'planConfig.planTypeName',
            title : '计划类型',
            formatter : function(value, row, index) {
                return row.planConfig.planTypeName;
            },
            align : 'center'
        }, {
            field : 'planReport.reportCountValue',
            title : '最新次数统计值',
            formatter : function(value, row, index) {
                if(row.planReport){
                    return getValueCompareResult(row.planReport.reportCountValue,row.planReport.planCountValue,row.planConfig.compareType);
                }else {
                    return '--';
                }
            },
            align : 'center'
        }, {
            field : 'planReport.reportValue',
            title : '最新值统计值',
            formatter : function(value, row, index) {
                if(row.planReport){
                    return getValueCompareResult(row.planReport.reportValue,row.planReport.planValue,row.planConfig.compareType);
                }else {
                    return '--';
                }
            },
            align : 'center'
        }, {
            field : 'planConfig.compareTypeName',
            title : '比较类型',
            formatter : function(value, row, index) {
                return row.planConfig.compareTypeName;
            },
            align : 'center'
        }, {
            field : 'planReport.planCountValue',
            title : '次数参考值',
            formatter : function(value, row, index) {
                if(row.planReport){
                    if(row.planReport.planCountValue){
                        return row.planReport.planCountValue+getCompareYearString(row.planReport.userPlanYear);
                    }else {
                        return '--';
                    }
                }else {
                    return '--';
                }
            },
            align : 'center'
        }, {
            field : 'planReport.planValue',
            title : '值参考值',
            formatter : function(value, row, index) {
                if(row.planReport){
                    if(row.planReport.planValue){
                        return row.planReport.planValue+getCompareYearString(row.planReport.userPlanYear);
                    }else {
                        return '--';
                    }
                }else {
                    return '--';
                }
            },
            align : 'center'
        }, {
            field : 'planConfig.unit',
            title : '值单位',
            formatter : function(value, row, index) {
                return row.planConfig.unit;
            },
            align : 'center'
        }, {
            field : 'planConfig.sqlTypeName',
            title : '查询类型',
            formatter : function(value, row, index) {
                return row.planConfig.sqlTypeName;
            },
            align : 'center'
        }, {
            field : 'orderIndex',
            title : '排序号',
            align : 'center'
        },{
            field : 'status',
            title : '状态',
            formatter : function(value, row, index) {
                return getStatusImage(value);
            },
            align : 'center'
        },{
            field : 'remind',
            title : '配置提醒',
            formatter : function(value, row, index) {
                if(value==null||value==false){
                    return '<img src="../static/image/cross.png"></img>';
                }else{
                    return '<img src="../static/image/tick.png"></img>';
                }
            },
            align : 'center'
        },{
            field : 'aa',
            title : '分析',
            formatter : function(value, row, index) {
                return '<a href="javascript:openAnalyseUserPlan('+row.id+');"><img src="../static/image/sum.png"></img></a>';;
            },
            align : 'center'
        },{
            field : 'createdTime',
            title : '创建时间'
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
            handler : openAnalyseUserPlan
        }, '-', {
			id : 'searchBtn',
			text : '刷新',
			iconCls : 'icon-refresh',
			handler : showAll
		} ]
	});
}

function getPlanConfigTree(){
    $('#planConfigList').combotree({
        url : '../planConfig/getPlanConfigForUserTree?needRoot=true',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        },
        onClick: function(node){
            loadStatValueConfig(node.id,'PLAN',null);
            copyPlanConfig(node.id);
        }
    });
}

/**
 * 从模板中拷贝数据
 * @param id
 */
function copyPlanConfig(id) {
    var url='../planConfig/get?id='+id;
    doAjax(null,url,'GET',false,function(data){
        var formData = {
            calendarTitle: data.defaultCalendarTitle,
            title: data.title
        };
        $('#ff').form('load', formData);
    });
}

function loadStatValueConfig(fid,type,bindValues){
    //加载模板内容
    var url='../statValueConfig/getConfigs?fid='+ fid+'&type='+type;
    doAjax(null,url,'GET',false,function(data){
        var html='';
        if(data.length==0){
            $("#selectList").html('');
            return;
        }
        for(var i=0;i<data.length;i++){
            if(data[i].list.length==0){
                $.messager.alert('提示', data[i].promptMsg, 'info');
                $("#selectList").html('');
                return;
            }else{
                html+=data[i].name+':&nbsp;&nbsp;';
                var vid ='bindValuesList'+i;
                html+='<select class="selector" id="'+vid+'" class="'+vid+'" name="bindValues" style="width:90px;height:25px">\n';
                for(var j=0;j<data[i].list.length;j++){
                    if(isSelectValueEquals(bindValues,i,data[i].list[j].id)){
                        html+='<option selected="selected" value="'+data[i].list[j].id+'">'+data[i].list[j].text+'</option>\n';
                    }else{
                        html+='<option value="'+data[i].list[j].id+'">'+data[i].list[j].text+'</option>\n';
                    }
                }
                html+='</select>';
            }
            $("#selectList").html(html);
        }
    });
}

//判断下拉框是否选中
function isSelectValueEquals(bindValues,index,vid) {
    if(bindValues==null||bindValues==''){
        return false;
    }else{
        var strs= new Array();
        strs=bindValues.split(",");
        if(strs[index]==vid){
            return true;
        }
    }
    return false;
}

function add() {
	$('#eidt-window').window('open');
	$('#ff').form('clear');
	initForm();
    getPlanConfigTree();
    $('#planConfigList').combotree('enable');
    $('#ff').form.url='../userPlan/create';
    var formData = {
        status:'ENABLE',
        orderIndex :1
    };
    $('#ff').form('load', formData);
    initGridUserPlanConfigValue(0);
}

function initForm(){

}

function edit() {
	var rows = getSelectedSingleRow();
	var url='../userPlan/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data){
		$('#eidt-window').window('open');
        $('#ff').form('clear');
        initForm();
        getPlanConfigTree();
        $('#ff').form('load', data);
        $('#planConfigList').combotree('setValue', data.planConfig.id);
        loadStatValueConfig(data.planConfig.id,'PLAN',data.bindValues);
        var bindValues = data.bindValues;
        if(bindValues!=null){
            var strs= new Array();
            strs=bindValues.split(",");
            for (i=0;i<strs.length ;i++) {
                var vid ='bindValuesList'+i;
                $("#"+vid).val(strs[i]);
                //$('#'+vid).combotree('setValue', strs[i]);
            }
        }
        //$('#planConfigList').combotree('disable');
        $('#planConfigList').combotree('setValue', data.planConfig.id);
		//设置字符
		$('#grid').datagrid('clearSelections');
        initGridUserPlanConfigValue(data.id);
    });
}

function showAll() {
	refreshDatagrid(dataUrl,1,true);
}

function saveData() {
    var url='../userPlan/edit';
    if($("#id").val()==null||$("#id").val()==''){
        url='../userPlan/create';
    }
	doFormSubmit('ff',url,function(data){
		//closeWindow('eidt-window');
		$('#grid').datagrid('clearSelections');
        //$('#ff').form('clear');
        reloadDatagrid();
        var formData = {
            id:data.id,
            createdTime:data.createdTime
        };
        $('#ff').form('load', formData);
        // 加载配置项
        initGridUserPlanConfigValue(data.id);
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
    var delUrlPrefix = '../userPlan/delete';
    commonDeleteByIds(delUrlPrefix);
}

function showPlanCommend() {
    $('#plan-commend-window').window('open');
    $('#plan-commend-form').form('clear');
    var formData = {
        startDate: getYear(-1)+'-01-01',
        endDate: getYear(-1)+'-12-31'
    };
    $('#plan-commend-form').form('load', formData);
}

function planCommend() {
    var para =form2Json("plan-commend-form");
    var vuserPlanId = $("#userPlanId").val();
    para.userPlanId=vuserPlanId;
    var url='../planReport/planCommend';
    doAjax(para,url,'GET',false,function(data){
        var formData = {
            planCountValue: data.reportCountValue,
            planValue: data.reportValue
        };
        $('#ff-plan-config-value').form('load', formData);
        closeWindow('plan-commend-window');
    });
}

function getFirstStatDay() {
    var url='../userPlan/getFirstStatDay';
    var para = form2JsonEnhanced("ff");
    doAjax(para,url,'POST',false,function(data){
        if(data){
            $("#firstStatDay").datebox('setValue', data);
        }
    });
}

function openRemindWindow() {
    var vuserPlanId = $("#id").val();
    if(vuserPlanId==null){
        $.messager.alert('提示', '请先保存计划配置!', 'error');
        return;
    }
    $('#eidt-user-plan-remind-window').window('open');
    var url='../userPlan/getRemind?id='+vuserPlanId;
    doAjax(null,url,'GET',false,function(data){
        $('#ff-user-plan-remind').form('clear');
        if(data==null){
            data ={};
            //设置默认值
            data.formTimePassedRate=50;
            data.finishedRemind=true;
            data.triggerInterval=1;
            data.triggerType='DAY';
            data.remindTime='08:30';

        }
        $('#ff-user-plan-remind').form('load', data);
    });

}

function saveRemindData() {
    $("#userPlanIdInRemind").val($("#id").val());
    var url='../userPlan/addOrEditRemind';
    doFormSubmit('ff-user-plan-remind',url,function(data){
        $('#eidt-user-plan-remind-window').window('close');
    });
}

function closeRemindWindow() {
    $('#eidt-user-plan-remind-window').window('close');
}

function openAnalyseUserPlan(id) {
    $('#plan-analyse-stat-window').window('open');
    getPlanConfigAnalyseTree();
    $('#userPlanAnalyseList').combotree('setValue', id);
    if(id!=null&&id>0){
        analyseUserPlan();
    }
}

function getPlanConfigAnalyseTree(){
    $('#userPlanAnalyseList').combotree({
        url : '../userPlan/getPlanConfigTree?needRoot=true',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        },
        onChange : function (newValue, oldValue) {
            analyseUserPlan();
        }
    });
}

function analyseUserPlan() {
    $('#grid').datagrid('clearSelections');
    //$('#plan-analyse-stat-window').window('open');
    var url='../userPlan/getStat';
    var para = form2JsonEnhanced("plan-analyse-search-form");
    doAjax(para,url,'GET',false,function(data){
        var rateCountPercent = getPercent(data.reportCountValue,data.planCountValue);
        var countOption = {
            tooltip : {
                formatter: "{a} <br/>{b} : {c}%"
            },
            toolbox: {
                feature: {
                    restore: {},
                    saveAsImage: {}
                }
            },
            title : {
                text: '次数完成比例',
                subtext: '完成次数值:'+data.reportCountValue+",计划次数:"+data.planCountValue,
                x:'center'
            },
            series: [
                {
                    name: '业务指标',
                    type: 'gauge',
                    detail: {formatter:'{value}%'},
                    data: [{value: rateCountPercent, name: '完成率'}]
                }
            ]
        };
        createChartEnhanced(countOption,'planCountAnalyseContainer');
        var rateValuePercent = getPercent(data.reportValue,data.planValue);
        var valueOption = {
            tooltip : {
                formatter: "{a} <br/>{b} : {c}%"
            },
            toolbox: {
                feature: {
                    restore: {},
                    saveAsImage: {}
                }
            },
            title : {
                text: '值完成比例',
                subtext: '完成值:'+data.reportValue+",计划值:"+data.planValue+',单位:'+data.userPlan.planConfig.unit,
                x:'center'
            },
            series: [
                {
                    name: '业务指标',
                    type: 'gauge',
                    detail: {formatter:'{value}%'},
                    data: [{value: rateValuePercent, name: '完成率'}]
                }
            ]
        };
        createChartEnhanced(valueOption,'planValueAnalyseContainer');
    });
}