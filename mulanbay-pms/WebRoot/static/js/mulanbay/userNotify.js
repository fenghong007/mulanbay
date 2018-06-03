$(function() {
	// 加载公司树
    initForm();
	initGrid();
});

var dataUrl='../userNotify/getData';

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
            field : 'warningValue',
            title : '警告值',
            align : 'center'
        }, {
            field : 'alertValue',
            title : '告警值',
            align : 'center'
        }, {
            field : 'notifyConfig.valueTypeName',
            title : '单位',
            formatter : function(value, row, index) {
                return row.notifyConfig.valueTypeName;
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
            field : 'showInIndex',
            title : '首页显示',
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
                return '<a href="javascript:openAnalyseUserNotify('+row.id+');"><img src="../static/image/sum.png"></img></a>';;
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
            handler : openAnalyseUserNotify
        }, '-', {
			id : 'searchBtn',
			text : '刷新',
			iconCls : 'icon-refresh',
			handler : showAll
		} ]
	});
}

function getNotifyConfigTree(){
    $('#notifyConfigList').combotree({
        url : '../notifyConfig/getNotifyConfigForUserTree?needRoot=true',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        },
        onClick: function(node){
            loadStatValueConfig(node.id,'NOTIFY',null);
            copyNotifyConfig(node.id);
        }
    });
}

/**
 * 从模板中拷贝数据
 * @param id
 */
function copyNotifyConfig(id) {
    var url='../notifyConfig/get?id='+id;
    doAjax(null,url,'GET',false,function(data){
        var formData = {
            calendarTitle: data.defaultCalendarTitle,
            title: data.title
        };
        $('#ff').form('load', formData);
        document.getElementById("warningUnit").innerText=data.valueTypeName;
        document.getElementById("alertUnit").innerText=data.valueTypeName;
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
    getNotifyConfigTree();
    $('#planConfigList').combotree('enable');
    $('#ff').form.url='../userNotify/create';
    var formData = {
        status:'ENABLE',
        remind:false,
        orderIndex :1
    };
    $('#ff').form('load', formData);
    //initGriduserNotifyConfigValue(0);
}

function initForm(){

}

function edit() {
	var rows = getSelectedSingleRow();
	var url='../userNotify/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data){
		$('#eidt-window').window('open');
        $('#ff').form('clear');
        initForm();
        getNotifyConfigTree();
        $('#ff').form('load', data);
        $('#notifyConfigList').combotree('setValue', data.notifyConfig.id);
        loadStatValueConfig(data.notifyConfig.id,'NOTIFY',data.bindValues);
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
        $('#notifyConfigList').combotree('setValue', data.notifyConfig.id);
		//设置字符
		$('#grid').datagrid('clearSelections');
        //initGriduserNotifyConfigValue(data.id);
    });
}

function showAll() {
	refreshDatagrid(dataUrl,1,true);
}

function saveData() {
    var url='../userNotify/edit';
    if($("#id").val()==null||$("#id").val()==''){
        url='../userNotify/create';
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
        //initGriduserNotifyConfigValue(data.id);
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
    var delUrlPrefix = '../userNotify/delete';
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
    var vuserNotifyId = $("#userNotifyId").val();
    para.userNotifyId=vuserNotifyId;
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
    var url='../userNotify/getFirstStatDay';
    var para = form2JsonEnhanced("ff");
    doAjax(para,url,'POST',false,function(data){
        if(data){
            $("#firstStatDay").datebox('setValue', data);
        }
    });
}

function openRemindWindow() {
    var vuserNotifyId = $("#id").val();
    if(vuserNotifyId==null){
        $.messager.alert('提示', '请先保存计划配置!', 'error');
        return;
    }
    $('#eidt-user-plan-remind-window').window('open');
    var url='../userNotify/getRemind?id='+vuserNotifyId;
    doAjax(null,url,'GET',false,function(data){
        $('#ff-user-plan-remind').form('clear');
        if(data==null){
            data ={};
            //设置默认值
            data.overWarningRemind=true;
            data.overAlertRemind=true;
            data.triggerInterval=1;
            data.triggerType='DAY';
            data.remindTime='08:30';

        }
        $('#ff-user-plan-remind').form('load', data);
    });

}

function saveRemindData() {
    $("#userNotifyIdInRemind").val($("#id").val());
    var url='../userNotify/addOrEditRemind';
    doFormSubmit('ff-user-plan-remind',url,function(data){
        $('#eidt-user-plan-remind-window').window('close');
    });
}

function closeRemindWindow() {
    $('#eidt-user-plan-remind-window').window('close');
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
    $('#userNotifyList').combotree({
        url : '../userNotify/getUserNotifyTree?needRoot=true',
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