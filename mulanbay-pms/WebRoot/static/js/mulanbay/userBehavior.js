$(function() {
	// 加载公司树
    initForm();
	initGrid();
});

var dataUrl='../userBehavior/getData';

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
            field : 'userBehaviorConfig.behaviorTypeName',
            title : '计划类型',
            formatter : function(value, row, index) {
                return row.userBehaviorConfig.behaviorTypeName;
            },
            align : 'center'
        }, {
            field : 'userBehaviorConfig.unit',
            title : '值单位',
            formatter : function(value, row, index) {
                return row.userBehaviorConfig.unit;
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
            field : 'monthStat',
            title : '加入月度分析',
            formatter : function(value, row, index) {
                return getStatusImage(value);
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

function getUserBehaviorTree(){
    $('#userBehaviorConfigList').combotree({
        url : '../userBehavior/getUserBehaviorConfigTree?needRoot=true',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        },
        onClick: function(node){
            $('#title').val(node.text);
            loadStatValueConfig(node.id,'BEHAVIOR',null);
        }
    });
}


function add() {
	$('#eidt-window').window('open');
	$('#ff').form('clear');
	initForm();
    getUserBehaviorTree();
    $('#userBehaviorConfigList').combotree('enable');
    $('#ff').form.url='../userBehavior/create';
    var formData = {
        monthStat: true,
        status:'ENABLE',
        orderIndex :1
    };
    $('#ff').form('load', formData);
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

function initForm(){
    $('#behaviorTypeList').combotree({
        url : '../userBehaviorConfig/getUserBehaviorTypeTree?needRoot=true',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        }
    });
    $('#behaviorTypeList2').combotree({
        url : '../userBehaviorConfig/getUserBehaviorTypeTree',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        }
    });
}

function edit() {
	var rows = getSelectedSingleRow();
	var url='../userBehavior/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data){
		$('#eidt-window').window('open');
        $('#ff').form('clear');
        initForm();
        getUserBehaviorTree();
        $('#userBehaviorConfigList').combotree('setValue', data.userBehaviorConfig.id);
        $('#userBehaviorConfigList').combotree('disable');
        $('#ff').form('load', data);
		$('#ff').form.url='../userBehavior/edit?id=' + data.id;
        loadStatValueConfig(data.userBehaviorConfig.id,'BEHAVIOR',data.bindValues);
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
		//设置字符
		$('#grid').datagrid('clearSelections');
    });
}

function showAll() {
	refreshDatagrid(dataUrl,1,true);
}

function saveData() {
	doFormSubmit('ff',$('#ff').form.url,function(data){
		closeWindow('eidt-window');
		$('#grid').datagrid('clearSelections');
        $('#ff').form('clear');
        reloadDatagrid();
        $('#ff').form('load', data);
        $('#ff').form.url='../userBehavior/edit?id=' + data.id;
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
    var delUrlPrefix = '../userBehavior/delete';
    commonDeleteByIds(delUrlPrefix);
}
