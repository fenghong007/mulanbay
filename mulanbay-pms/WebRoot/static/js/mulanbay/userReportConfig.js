$(function() {
	// 加载公司树
    initForm();
	initGrid();
});

var dataUrl='../userReportConfig/getData';

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

function getReportConfigTree(){
    $('#reportConfigList').combotree({
        url : '../reportConfig/getReportConfigForUserTree?needRoot=true',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        },
        onClick: function(node){
            loadStatValueConfig(node.id,'REPORT',null);
        }
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
    getReportConfigTree();
    $('#reportConfigList').combotree('enable');
    var formData = {
        status:'ENABLE',
        remind:false,
        orderIndex :1
    };
    $('#ff').form('load', formData);
    //initGriduserReportConfigConfigValue(0);
}

function initForm(){

}

function edit() {
	var rows = getSelectedSingleRow();
	var url='../userReportConfig/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data){
		$('#eidt-window').window('open');
        $('#ff').form('clear');
        initForm();
        getReportConfigTree();
        $('#ff').form('load', data);
        $('#reportConfigList').combotree('setValue', data.reportConfig.id);
        loadStatValueConfig(data.reportConfig.id,'REPORT',data.bindValues);
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
        $('#reportConfigList').combotree('setValue', data.reportConfig.id);
		//设置字符
		$('#grid').datagrid('clearSelections');
        //initGriduserReportConfigConfigValue(data.id);
    });
}

function showAll() {
	refreshDatagrid(dataUrl,1,true);
}

function saveData() {
    var url='../userReportConfig/edit';
    if($("#id").val()==null||$("#id").val()==''){
        url='../userReportConfig/create';
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
        //initGriduserReportConfigConfigValue(data.id);
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
    var delUrlPrefix = '../userReportConfig/delete';
    commonDeleteByIds(delUrlPrefix);
}

function openRemindWindow() {
    var vuserReportConfigId = $("#id").val();
    if(vuserReportConfigId==null){
        $.messager.alert('提示', '请先保存计划配置!', 'error');
        return;
    }
    $('#eidt-user-plan-remind-window').window('open');
    var url='../userReportConfig/getRemind?id='+vuserReportConfigId;
    doAjax(null,url,'GET',false,function(data){
        $('#ff-user-plan-remind').form('clear');
        if(data==null){
            data ={};
            //设置默认值
            data.remindTime='08:30';

        }
        $('#ff-user-plan-remind').form('load', data);
    });

}

function saveRemindData() {
    $("#userReportConfigIdInRemind").val($("#id").val());
    var url='../userReportConfig/addOrEditRemind';
    doFormSubmit('ff-user-plan-remind',url,function(data){
        $('#eidt-user-plan-remind-window').window('close');
    });
}

function closeRemindWindow() {
    $('#eidt-user-plan-remind-window').window('close');
}