
function getUserPlan(beanName){
    initUserPlanForBussForm(beanName);
    $('#user-plan-window').window('open');
    resetSearchWindowById('user-plan-search-window');
    initUserPlanGrid();
}
var userPlanDataUrl='../userPlan/getData';

function initUserPlanGrid(){
	$('#userPlanGrid').datagrid({
		iconCls : 'icon-save',
		url : userPlanDataUrl,
		method:'GET',
		loadFilter: function(data){
			return loadDataFilter(data);
		},
		idField : 'id',
		loadMsg : '正在加载数据...',
		pageSize : 30,
		queryParams: getSearchPara(),
		remoteSort : false,
		frozenColumns : [ [ {
			field : 'ID',
			checkbox : true
		} ] ],
		onDblClickRow: function (rowIndex, rowData) {
            $('#userPlanGrid').datagrid('uncheckAll');
            //$('#userPlanGrid').datagrid('checkRow', rowIndex);
            statUserPlanTimeline(rowData.id,null,rowData.planConfig.planType);
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
            field : 'createdTime',
            title : '创建时间'
        }, {
            field : 'pointStat',
            title : '积分统计',
            formatter : function(value, row, index) {
                return '<a href="javascript:statValuePoint('+row.id+',\'PLAN\''+');"><img src="../static/image/sum.png"></img></a>';;
            },
            align : 'center'
        }, {
            field : 'rateStat',
            title : '进度图表',
            formatter : function(value, row, index) {
                return '<a href="javascript:userPlanRateStat('+row.id+');"><img src="../static/image/sum.png"></img></a>';;
            },
            align : 'center'
        } ] ],
		pagination : true,
		rownumbers : true,
		singleSelect : false,
		toolbar : [ {
			id : 'searchBtn',
			text : '刷新',
			iconCls : 'icon-refresh',
			handler : showAllPlanConfig
		},{
            id : 'statPlanBtn',
            text : '计划进度统计',
            iconCls : 'icon-stat',
            handler : userPlanRateStat
        },{
            id : 'statNotifyBtn',
            text : '提醒进度统计',
            iconCls : 'icon-stat',
            handler : userNotifyStat
        } ]
	});
}

var planReportDataUrl='../planReport/getData';

function initPlanReportGrid(){
    $('#userPlanGrid').datagrid({
        iconCls : 'icon-save',
        url : planReportDataUrl,
        method:'GET',
        loadFilter: function(data){
            return loadDataFilter(data);
        },
        idField : 'id',
        loadMsg : '正在加载数据...',
        pageSize : 30,
        queryParams: getSearchPara(),
        remoteSort : false,
        frozenColumns : [ [ {
            field : 'ID',
            checkbox : true
        } ] ],
        onDblClickRow: function (rowIndex, rowData) {
            $('#userPlanGrid').datagrid('uncheckAll');
            //$('#grid').datagrid('checkRow', rowIndex);
            statUserPlanTimeline(rowData.userPlan.id,rowData.bussStatDate,rowData.userPlan.planConfig.planType);
        },
        onLoadError: loadDataError,
        columns : [ [ {
            field : 'id',
            title : '记录号',
            sortable : true,
            align : 'center'
        }, {
            field : 'title',
            title : '名称',
            formatter : function(value, row, index) {
                if(row.userPlan){
                    return row.userPlan.title;
                }else {
                    return '--';
                }
            }
        }, {
            field : 'userPlan.planConfig.planTypeName',
            title : '计划类型',
            formatter : function(value, row, index) {
                if(row.userPlan.planConfig){
                    return row.userPlan.planConfig.planTypeName;
                }else {
                    return '--';
                }
            },
            align : 'center'
        }, {
            field : 'bussDay',
            title : '业务日期索引',
            align : 'center'
        }, {
            field : 'bussStatDate',
            title : '业务日期',
            align : 'center'
        }, {
            field : 'reportCountValue',
            title : '次数值',
            width : 120,
            formatter : function(value, row, index) {
                return getValueCompareResult(value,row.planCountValue,row.userPlan.planConfig.compareType);
            },
            align : 'center'
        }, {
            field : 'reportValue',
            title : '值',
            width : 120,
            formatter : function(value, row, index) {
                if(value==0 && row.planValue==0){
                    //说明不需要
                    return '--';
                }else {
                    return getValueCompareResult(value,row.planValue,row.userPlan.planConfig.compareType);
                }
            },
            align : 'center'
        }, {
            field : 'userPlan.planConfig.compareTypeName',
            formatter : function(value, row, index) {
                if(row.userPlan.planConfig){
                    return row.userPlan.planConfig.compareTypeName;
                }else {
                    return '--';
                }
            },
            title : '比较类型',
            align : 'center'
        }, {
            field : 'planCountValue',
            title : '计划次数',
            align : 'center'
        }, {
            field : 'planValue',
            title : '计划值',
            formatter : function(value, row, index) {
                if(value==0){
                    //说明不需要
                    return '--';
                }else {
                    return value;
                }
            },
            align : 'center'
        }, {
            field : 'planConfig.unit',
            title : '值单位',
            formatter : function(value, row, index) {
                if(row.userPlan.planConfig){
                    return row.userPlan.planConfig.unit;
                }else {
                    return '--';
                }
            },
            align : 'center'
        }, {
            field : 'planConfigYear',
            title : '数据参考年份',
            align : 'center'
        },{
            field : 'createdTime',
            title : '统计时间'
        }, {
            field : 'pointStat',
            title : '积分统计',
            formatter : function(value, row, index) {
                return '<a href="javascript:statValuePoint('+row.userPlan.id+',\'PLAN\''+');"><img src="../static/image/sum.png"></img></a>';;
            },
            align : 'center'
        }, {
            field : 'rateStat',
            title : '进度图表',
            formatter : function(value, row, index) {
                return '<a href="javascript:userPlanRateStat('+row.userPlan.id+');"><img src="../static/image/sum.png"></img></a>';;
            },
            align : 'center'
        }] ],
        pagination : true,
        rownumbers : true,
        singleSelect : false,
        toolbar : [ {
            id : 'searchBtn',
            text : '刷新',
            iconCls : 'icon-refresh',
            handler : showAllPlanReport
        },{
            id : 'statPlanBtn',
            text : '计划进度统计',
            iconCls : 'icon-stat',
            handler : userPlanRateStat
        },{
            id : 'statNotifyBtn',
            text : '提醒进度统计',
            iconCls : 'icon-stat',
            handler : userNotifyStat
        } ]
    });
}

function getSearchPara() {
    var para = form2JsonEnhanced("user-plan-search-window");
    return para;
}

function getCompareYearString(year) {
    if(year==null||year==''||year==undefined){
        return '';
    }
    var current = getYear(0);
    if(year==current){
        return '';
    }else{
        return '&nbsp;<font color="#db7093">('+year+')</font>'
    }
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

function initUserPlanForBussForm(beanName){
    //查询表单中设置关联的bean
    $('#relatedBeans').val(beanName);
    $('#userPlanForBussList').combotree({
        url : '../userPlan/getPlanConfigTree?needRoot=true&relatedBeans='+beanName,
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        }
    });
}

function showData(){
    var vparameters = getSearchPara();
    if(vparameters.dataType=='HISTORY'){
        showAllPlanReport();
    }else{
        showAllPlanConfig();
    }
}

function showAllPlanConfig(){
    initUserPlanGrid();
    //showPlanConfig(1);
}

function showAllPlanReport(){
    initPlanReportGrid();
    //showPlanReport(1);
}

function showPlanReport(vpageNumber) {
    var vparameters = getSearchPara();
    $('#userPlanGrid').datagrid({
        url : planReportDataUrl,
        type : 'GET',
        queryParams : vparameters,
        pageNumber : vpageNumber,
        onLoadError : function() {
            $.messager.alert('错误', '加载数据异常！', 'error');
        }
    });
}

function showPlanConfig(vpageNumber) {
    var vparameters = getSearchPara();
    $('#userPlanGrid').datagrid({
        url : userPlanDataUrl,
        type : 'GET',
        queryParams : vparameters,
        pageNumber : vpageNumber,
        onLoadError : function() {
            $.messager.alert('错误', '加载数据异常！', 'error');
        }
    });
}

function statUserPlanTimeline(userPlanId,date,planType) {
    $('#user-plan-timeline-window').window('open');
    if(date==null){
        date = new Date();
    }
    var year=date.getFullYear();
    var month=date.getMonth()+1;
    var vurl = '../planReport/timelineStat';
    var para = {
        userPlanId: userPlanId,
        dateGroupType:planType,
        year:year,
        month:month
    };
    doAjax(para, vurl, 'GET',false, function(data) {
        createLineDataEnhanced(data,'uptContainer');
    });
}

function userNotifyStat() {
    openAnalyseUserNotify('');
}


function openAnalyseUserNotify(id) {
    $('#notify-analyse-stat-window').window('open');
    getUserNotifyTree();
    $('#userNotifyList').combotree('setValue', id);
    if(id!=null&&id>0){
        analyseUserNotify();
    }
}

function getUserNotifyTree(){
    var beanName = $('#relatedBeans').val();
    $('#userNotifyList').combotree({
        url : '../userNotify/getUserNotifyTree?needRoot=true&relatedBeans='+beanName,
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        },
        onChange : function (newValue, oldValue) {
            analyseUserNotify();
        }
    });
}

function analyseUserNotify() {
    $('#grid').datagrid('clearSelections');
    $('#notify-analyse-stat-window').window('open');
    var url='../userNotify/getStat';
    var para = form2JsonEnhanced("notify-analyse-search-form");
    doAjax(para,url,'GET',false,function(data){
        var content =data.compareValue+' '+data.userNotify.notifyConfig.valueTypeName;
        if(data.userNotify.notifyConfig.resultType=='NAME_DATE'||data.userNotify.notifyConfig.resultType=='NAME_NUMBER'){
            content =data.name+'  '+data.compareValue+data.userNotify.notifyConfig.valueTypeName;
        }
        document.getElementById("notifyContent").innerText=content;
        var rateWarningPercent = getPercent(data.compareValue,data.userNotify.warningValue);
        var warningOption = {
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
                text: '达到警告比例',
                subtext: '当前值:'+data.compareValue+",警告配置:"+data.userNotify.warningValue+',单位:'+data.userNotify.notifyConfig.valueTypeName,
                x:'center'
            },
            series: [
                {
                    name: '业务指标',
                    type: 'gauge',
                    detail: {formatter:'{value}%'},
                    data: [{value: rateWarningPercent, name: '达到比例'}]
                }
            ]
        };
        createChartEnhanced(warningOption,'notifyAnalyseWarningContainer');
        var rateAlertPercent = getPercent(data.compareValue,data.userNotify.alertValue);
        var alertOption = {
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
                text: '达到告警比例',
                subtext: '当前值:'+data.compareValue+",告警配置:"+data.userNotify.alertValue+',单位:'+data.userNotify.notifyConfig.valueTypeName,
                x:'center'
            },
            series: [
                {
                    name: '业务指标',
                    type: 'gauge',
                    detail: {formatter:'{value}%'},
                    data: [{value: rateAlertPercent, name: '达到比例'}]
                }
            ]
        };
        createChartEnhanced(alertOption,'notifyAnalyseAlertContainer');

    });
}

function userPlanRateStat(userPlanId) {
    openAnalyseUserPlan(userPlanId);
}

function openAnalyseUserPlan(id) {
    $('#plan-analyse-stat-window').window('open');
    getPlanConfigAnalyseTree();
    $('#userPlanAnalyseList').combotree('setValue', id);
    if(id!=null&&id!=''&&id>0){
        analyseUserPlan();
    }
}

function getPlanConfigAnalyseTree(){
    var beanName = $('#relatedBeans').val();
    $('#userPlanAnalyseList').combotree({
        url : '../userPlan/getPlanConfigTree?needRoot=true&relatedBeans='+beanName,
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
    $('#userPlanGrid').datagrid('uncheckAll');
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

function statValuePoint(vsourceId,vrewardSource) {
    $('#grid').datagrid('uncheckAll');
    var formData = {
        sourceId:vsourceId,
        rewardSource:vrewardSource,
        startDate: getYear(0)+'-01-01',
        endDate: getYear(0)+'-12-31'
    };
    $('#userRewardPoint-value-stat-search-form').form('load', formData);
    $('#userRewardPoint-value-stat-window').window('open');
    statUserRewardPointValue();
}

function statUserRewardPointValue(){
    $('#userPlanGrid').datagrid('uncheckAll');
    var para =form2Json("userRewardPoint-value-stat-search-form");
    var url='../userRewardPointRecord/pointsValueStat';
    doAjax(para,url,'GET',false,function(data){
        $('#stat-form').form('clear');
        if(para.dateGroupType==null||para.dateGroupType==""){
            //生成饼图
            createPieDataEnhanced(data,'userRewardPoint-value-stat-container');
        }else if(para.dateGroupType=='DAYCALENDAR'){
            createCompareCalanderDataEnhanced(data,'userRewardPoint-value-stat-container');
        }else{
            createLineDataEnhanced(data,'userRewardPoint-value-stat-container');
        }
    });
}