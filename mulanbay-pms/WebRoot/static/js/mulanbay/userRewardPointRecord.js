$(function() {
	// 加载公司树
    initForm();
	initGrid();
});

var dataUrl='../userRewardPointRecord/getData';

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
			field : 'rewards',
			title : '奖励积分',
			formatter : function(value, row, index) {
				if(value<0){
                    return '<font color="red">'+value+'</font>';
                }else if(value>0){
                    return '<font color="green">'+value+'</font>';
                }else{
                    return value;
                }
			},
            align : 'center'
        }, {
            field : 'sourceName',
            title : '来源',
            align : 'center'
        }, {
            field : 'rewardSourceName',
            title : '来源类型',
            align : 'center'
        }, {
            field : 'sourceId',
            title : '来源主键值',
            align : 'center'
        }, {
            field : 'messageId',
            title : '消息内容',
            formatter : function(value, row, index) {
            	if(value!=null){
                    return '<a href="javascript:showMessageDetail('+row.id+',\''+row.rewardSource+'\''+');"><img src="../static/image/info.png"></img></a>';
                }else{
            		return '--';
				}
            },
            align : 'center'
        }, {
            field : 'afterPoints',
            title : '当前累计积分',
            align : 'center'
        }, {
            field : 'createdTime',
            title : '创建时间'
        }, {
            field : 'aa',
            title : '统计',
            formatter : function(value, row, index) {
                return '<a href="javascript:statValuePoint('+row.sourceId+',\''+row.rewardSource+'\''+');"><img src="../static/image/sum.png"></img></a>';;
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
            handler : openStatUserRewardPointSource
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
	$('#ff').form.url='../userRewardPointRecord/create';
	$('#companyList1').combotree('setValue', 1);
	$('#companyList2').combotree('setValue', 1);
}

function initForm(){
	$('#companyList1').combotree({
		url : '../company/getCompanyTree',
		valueField : 'id',
		textField : 'text',
		loadFilter: function(data){
	    	return loadDataFilter(data);
	    }
	});
	$('#companyList2').combotree({
		url : '../company/getCompanyTree',
		valueField : 'id',
		textField : 'text',
		loadFilter: function(data){
	    	return loadDataFilter(data);
	    }
	});
}

function edit() {
	var rows = getSelectedSingleRow();
	var url='../userRewardPointRecord/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data){
		$('#eidt-window').window('open');
        $('#ff').form('clear');
        initForm();
		$('#ff').form('load', data);
		$('#companyList2').combotree('setValue', data.company.id);
		//设置字符
		$('#grid').datagrid('clearSelections');
	});
}

function showAll() {
	refreshDatagrid(dataUrl,1,true);
}

function saveData() {
    var url='../userRewardPointRecord/edit';
    if($("#id").val()==null||$("#id").val()==''){
        url='../userRewardPointRecord/create';
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
    var delUrlPrefix = '../userRewardPointRecord/delete';
    commonDeleteByIds(delUrlPrefix);
}

function statValuePoint(vsourceId,vrewardSource) {
    $('#grid').datagrid('uncheckAll');
    var formData = {
        sourceId:vsourceId,
        rewardSource:vrewardSource
    };
    $('#userRewardPoint-value-stat-search-form').form('load', formData);
    $('#userRewardPoint-value-stat-window').window('open');
    statUserRewardPointValue();
}

function statUserRewardPointValue(){
    var para =form2Json("userRewardPoint-value-stat-search-form");
    var url='../userRewardPointRecord/pointsValueStat';
    doAjax(para,url,'GET',false,function(data){
        if(para.dateGroupType==null||para.dateGroupType==""){
            //生成饼图
            createPieData(data);
		}else if(para.dateGroupType=='DAYCALENDAR'){
            createCompareCalanderData(data);
        }else{
            createLineData(data);
        }
    });
}

function openStatUserRewardPointSource(){
    $('#userRewardPoint-source-stat-window').window('open');
    var formData = {
        startDate: getYear(0)+'-01-01',
        endDate: getYear(0)+'-12-31'
    };
    //$('#userRewardPoint-source-stat-search-form').form('load', formData);
    statUserRewardPointSource();
}

function statUserRewardPointSource(){
    var para =form2Json("userRewardPoint-source-stat-search-form");
    var url='../userRewardPointRecord/pointsSourceStat';
    doAjax(para,url,'GET',false,function(data){
        if(para.chartType=='PIE'){
            createPieDataEnhanced(data,'sourceStatContainer');
        }else{
            createBarDataEnhanced(data,'sourceStatContainer');
        }
    });
}

function showMessageDetail(id) {
    var url='../userRewardPointRecord/getMessageContent?id='+ id;
    doAjax(null,url,'GET',false,function(data){
        $('#grid').datagrid('uncheckAll');
        $.messager.alert('详情', data, 'info');
    });
}
