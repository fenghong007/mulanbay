function bodyAnalyse() {
    $('#body-analyse-window').window('open');
    var formData = {
        startDate: getYear(0)+'-01-01',
        endDate: getYear(0)+'-12-31'
    };
    $('#body-analyse-form').form('load', formData);
    initBodyAnalyseGrid();
}

//其他页面进入
function bodyAnalyseWithCondition(name,groupField) {
    $('#body-analyse-window').window('open');
    // 查询条件今年的
    var formData = {
        startDate: getYear(0)+'-01-01',
        endDate: getYear(0)+'-12-31',
        name:name,
        groupField:groupField
    };
    $('#body-analyse-form').form('load', formData);
    initBodyAnalyseGrid();
}

// 自定义化
function resetAndInitBodyAnalyseSearchWindowYear() {
    $('#body-analyse-form').form('reset');
}

function getSearchBodyAnalysePara() {
    var para = form2JsonEnhanced("body-analyse-form");
    para.chartType='PIE';
    return para;
}

var bodyAnalysegDataUrl='../bodyAbnormalRecord/analyse';

function initBodyAnalyseGrid(){
    $('#bodyAnalyseGrid').datagrid({
        iconCls : 'icon-save',
        url : bodyAnalysegDataUrl,
        method:'GET',
        loadFilter: function(data){
            return loadDataFilter(data);
        },
        idField : 'id',
        loadMsg : '正在加载数据...',
        pageSize : 30,
        queryParams: getSearchBodyAnalysePara(),
        remoteSort : false,
        frozenColumns : [ [ {
            field : 'ID',
            checkbox : true
        } ] ],
        onDblClickRow: function (rowIndex, rowData) {
            $('#bodyAnalyseGrid').datagrid('uncheckAll');
            $('#bodyAnalyseGrid').datagrid('checkRow', rowIndex);
        },
        onLoadError: loadDataError,
        columns : [ [ {
            field : 'id',
            title : '记录号',
            sortable : true,
            align : 'center'
        }, {
            field : 'name',
            title : '名称',
            align : 'center'
        }, {
            field : 'totalCount',
            title : '发生总次数',
            align : 'center'
        }, {
            field : 'totalLastDays',
            title : '持续总天数',
            align : 'center'
        }, {
            field : 'minOccurDate',
            title : '最早一次发生时间',
            align : 'center'
        }, {
            field : 'maxOccurDate',
            title : '最近一次发生时间',
            align : 'center'
        }, {
            field : 'treatRecordStat.maxTreatDate',
            title : '最近一次看病时间',
            formatter : function(value, row, index) {
                if(row.treatRecordStat.maxTreatDate){
                    var ss = '<a href="javascript:showTreatRecord('+'\''+row.treatRecordStat.maxTreatRecord.id+'\''+');"><img src="../static/image/info.png"></img></a>';;
                    return row.treatRecordStat.maxTreatDate+ss;
                }else {
                    return '--';
                }
            },
            align : 'center'
        }, {
            field : 'aa',
            title : '距离最近一次看病',
            formatter : function(value, row, index) {
                if(row.treatRecordStat.maxTreatDate){
                	var days = dateDiff(row.treatRecordStat.maxTreatDate,row.maxOccurDate);
                	var msg = formatDays(days);
                	if(days>=180){
                        return '<font color="red">'+msg+'</font>';
                    }else if(days>=90){
                        return '<font color="#c71585">'+msg+'</font>';
                    }else if(days>=30){
                        return '<font color="purple">'+msg+'</font>';
                    }else if(days>=10){
                        return '<font color="green">'+msg+'</font>';
                    }else {
                    	return msg;
					}
				}else {
                    return '<font color="red">从未</font>';
                }
            },
            align : 'center'
        }, {
            field : 'treatRecordStat.maxTreatRecord.diagnosedDisease',
            title : '最近一次看病确诊疾病',
            formatter : function(value, row, index) {
                if(row.treatRecordStat.maxTreatRecord){
                    return row.treatRecordStat.maxTreatRecord.diagnosedDisease;
                }else {
                    return '--';
                }
            },
            align : 'center'
        }, {
            field : 'treatRecordStat.maxTreatRecord.isSick',
            title : '最近一次看病是否有病',
            formatter : function(value, row, index) {
                if(row.treatRecordStat.maxTreatRecord){
                    if (row.treatRecordStat.maxTreatRecord.isSick == true) {
                        return '<img src="../static/image/warn.png"></img>';
                    } else {
                        return '<img src="../static/image/okay.png"></img>';
                    }
                }else {
                    return '--';
                }
            },
            align : 'center'
        }, {
            field : 'treatRecordStat.minTreatDate',
            title : '最早一次看病时间',
            formatter : function(value, row, index) {
                if(row.treatRecordStat.minTreatDate){
                    var ss = '<a href="javascript:showTreatRecord('+'\''+row.treatRecordStat.minTreatRecord.id+'\''+');"><img src="../static/image/info.png"></img></a>';;
                    return row.treatRecordStat.minTreatDate+ss;
                }else {
                    return '--';
                }
            },
            align : 'center'
        }, {
            field : 'bb',
            title : '距离最早一次看病',
            formatter : function(value, row, index) {
                if(row.treatRecordStat.minTreatDate){
                    var days = dateDiff(row.treatRecordStat.minTreatDate,row.maxOccurDate);
                    var msg = formatDays(days);
                    if(days>=180){
                        return '<font color="red">'+msg+'</font>';
                    }else if(days>=90){
                        return '<font color="#c71585">'+msg+'</font>';
                    }else if(days>=30){
                        return '<font color="purple">'+msg+'</font>';
                    }else if(days>=10){
                        return '<font color="green">'+msg+'</font>';
                    }else {
                        return msg;
                    }
                }else {
                    return '<font color="red">从未</font>';
                }
            },
            align : 'center'
        }, {
            field : 'treatRecordStat.minTreatRecord.diagnosedDisease',
            title : '最早一次看病确诊疾病',
            formatter : function(value, row, index) {
                if(row.treatRecordStat.minTreatRecord){
                    return row.treatRecordStat.minTreatRecord.diagnosedDisease;
                }else {
                    return '--';
                }
            },
            align : 'center'
        }, {
            field : 'treatRecordStat.minTreatRecord.isSick',
            title : '最早一次看病是否有病',
            formatter : function(value, row, index) {
                if(row.treatRecordStat.minTreatRecord){
                    if (row.treatRecordStat.minTreatRecord.isSick == true) {
                        return '<img src="../static/image/warn.png"></img>';
                    } else {
                        return '<img src="../static/image/okay.png"></img>';
                    }
                }else {
                    return '--';
                }
            },
            align : 'center'
        },{
            field : 'avgWeight',
            title : '平均体重(KG)',
            formatter : function(value, row, index) {
            	if(value){
            		return value.toFixed(1);
				}else{
                    return '--';
                }
            },
            align : 'center'
        },{
            field : 'avgHeight',
            title : '平均身高(cm)',
            formatter : function(value, row, index) {
                return value;
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
            handler : initBodyAnalyseGrid
        } ]
    });
}

function showTreatRecord(id) {
    $('#bodyAnalyseGrid').datagrid('uncheckAll');
    parent.addTab('看病记录管理','../treatRecord/list?id='+id);
}

function showBodyAbnomarlAnalyse(comoboxId,groupField) {
    var name=$('#'+comoboxId).combobox('getValue');;
    if(name!=null&&name!=''){
        bodyAnalyseWithCondition(name,groupField);
    }else{
        $.messager.alert('提示', '请先输入相应的疾病或器官信息！', 'info');

    }
}