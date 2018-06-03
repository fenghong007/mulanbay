
var dataUrlDrugDetail='/treatDrugDetail/getData';

function initGridDrugDetail(){
	$('#gridDrugDetail').datagrid({
		iconCls : 'icon-save',
		url : dataUrlDrugDetail,
		method:'GET',
		loadFilter: function(data){
			return loadDataFilter(data);
		},
		idField : 'id',
		loadMsg : '正在加载数据...',
		pageSize : 30,
		queryParams: form2Json("search-drugDetail-form"),
		remoteSort : false,
		frozenColumns : [ [ {
			field : 'ID',
			checkbox : true
		} ] ],
		onDblClickRow: function (rowIndex, rowData) {
            $('#gridDrugDetail').datagrid('uncheckAll');
            $('#gridDrugDetail').datagrid('checkRow', rowIndex);
            editDrugDetail();
        },
        onLoadError: loadDataError,
		columns : [ [ {
			field : 'id',
			title : '记录号',
			sortable : true,
			align : 'center'
		}, {
            field : 'occurTime',
            title : '用药时间',
            align : 'center'
        }] ],
		pagination : true,
		rownumbers : true,
		singleSelect : false,
		toolbar : [ {
			id : 'createBtnDrugDetail',
			text : '新增',
			iconCls : 'icon-add',
			handler : addDrugDetail
		}, '-', {
			id : 'editBtnDrugDetail',
			text : '修改',
			iconCls : 'icon-edit',
			handler : editDrugDetail
		}, '-', {
			id : 'deleteBtnDrugDetail',
			text : '删除',
			iconCls : 'icon-remove',
			handler : delDrugDetail
		}, '-', {
			id : 'searchBtnDrugDetail',
			text : '刷新',
			iconCls : 'icon-refresh',
			handler : showAllDrugDetail
		}, '-', {
            id : 'statBtnDrugDetail',
            text : '用药统计',
            iconCls : 'icon-stat',
            handler : showCalandarStatDrugDetail
        } ]
	});
}

function addDrugDetail() {
    if(!checkTreatDrugId()){
        $.messager.alert('提示', '请先保存药品记录!', 'error');
        return;
    }
	$('#eidt-window-drugDetail').window('open');
	$('#ff-drugDetail').form('clear');
	$('#ff-drugDetail').form.url='../treatDrugDetail/create';
	//外键值为外面看病记录的表单的id
    $("#fkTreatDrugId").val($("#treatDrugId").val());
}

function initDrugDetailForm() {

}
function editDrugDetail() {
    if(!checkTreatDrugId()){
        $.messager.alert('提示', '请先保存药品记录!', 'error');
        return;
    }
    var rows = $('#gridDrugDetail').datagrid('getSelections');
    var num = rows.length;
    if (num == 0) {
        $.messager.alert('提示', '请选择一条记录进行操作!', 'info');
        return;
    }
	var url='../treatDrugDetail/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data){
		$('#eidt-window-drugDetail').window('open');
        $('#ff-drugDetail').form('clear');
        $('#ff-drugDetail').form('load', data);
		$('#ff-drugDetail').form.url='../treatDrugDetail/edit?id=' + data.id;
		//设置字符
		$('#gridDrugDetail').datagrid('clearSelections');
        $("#fkTreatDrugId").val($("#treatDrugId").val());
    });
}

function showAllDrugDetail() {
    var vurl =dataUrlDrugDetail;
    $('#gridDrugDetail').datagrid({
        url : vurl,
        type : 'GET',
        queryParams: form2Json("search-drugDetail-form"),
        pageNumber : 1,
        onLoadError : function() {
            $.messager.alert('错误', '加载数据异常！', 'error');
        }
    });
}

function checkTreatDrugId() {
    var id = $("#treatDrugId").val();
    if(id==null||id==0){
        return false;
    }else {
        return true;
    }
}


function saveDataDrugDetail() {
    if(!checkTreatDrugId()){
        $.messager.alert('提示', '请先保存药品记录!', 'error');
        return;
    }
    var url='../treatDrugDetail/edit';
    if($("#treatDrugDetailId").val()==null||$("#treatDrugDetailId").val()==''){
        url='../treatDrugDetail/create';
    }
    doFormSubmit('ff-drugDetail',url,function(data){
		closeWindow('eidt-window-drugDetail');
		$('#gridDrugDetail').datagrid('clearSelections');
        showAllDrugDetail();
        $('#ff-drugDetail').form('clear');
        $('#ff-drugDetail').form('load', data);
        $("#fkTreatDrugId").val(data.id);
	});
}

function getSelectedIdsDrugDetail() {
	var ids = [];
    var rows = $('#gridDrugDetail').datagrid('getSelections');
    var num = rows.length;
    if (num == 0) {
        $.messager.alert('提示', '请选择一条记录进行操作!', 'info');
        return;
    }
	for (var i = 0; i < rows.length; i++) {
		ids.push(rows[i].id);
	}
	return ids;
}

function delDrugDetail() {
	var arr = getSelectedIdsDrugDetail();
	if (arr.length > 0) {
		$.messager.confirm('提示信息', '您确认要删除吗?', function(data) {
			if (data) {
				var vurl = '../treatDrugDetail/delete?ids=' + arr.join(',');
				doAjax(null, vurl, 'GET', true, function(data) {
					$('#gridDrugDetail').datagrid('clearSelections');
                    showAllDrugDetail();
				});
			}
		});
	} else {
		$.messager.show({
			title : '警告',
			msg : '请先选择要删除的记录。'
		});
	}
}

function closeWindowDrugDetail(){
    closeWindow('eidt-window-drugDetail');
}

function showCalandarStatDrugDetail() {
    $('#drugDetail-stat-window').window('open');
    //todo 年月设置为用药开始的日期
    var formData = {
        yearmonth: getDay()
    };
    $('#drugDetail-stat-search-form').form('load', formData);
    calandarStatDrugDetail();
}

function calandarStatDrugDetail() {
    //获取药品id
    $("#statTreatDrugId").val($("#treatDrugId").val());
    var vurl = '../treatDrugDetail/calandarStat';
    var para = form2Json("drugDetail-stat-search-form");
    var ss = (para.yearmonth.split('-'));
    para.year=parseInt(ss[0],10);
    para.month=parseInt(ss[1],10);
    doAjax(para, vurl, 'GET',false, function(data) {
        if(para.dateGroupType=='YEAR'){
            createMyCalanderData(data);
        }else {
            createCalanderPieDataEnhanced(data,'drugDetailContainer');
        }
    });
}

// 日历图
function createMyCalanderData(data){
    var perTimes = data.customData;
    // 获取每个格子的点的大小，数值为2,4,6,8,10
    function getSymbolSize(value) {
        return 10;
    }

    function getColor(value) {
        if(value==null||value.value==null||value.value.length==0){
            return 'yellow';
        }
        //alert('value:'+JSON.stringify(value)+',perTimes:'+perTimes);
        if(perTimes==null){
            return 'green';
        }else if(value.value[1]<perTimes){
            return 'red';
        }else {
            return 'green';
        }
    }

    var option = {
        backgroundColor: '#404a59',

        title: {
            top: 10,
            text: data.title,
            subtext: data.subTitle,
            left: 'center',
            textStyle: {
                color: '#fff'
            }
        },
        tooltip : {
            trigger: 'item',
            formatter: "{a}<br/>{b}({c}"+data.unit+")"
        },
        legend: {
            top: '30',
            left: '100',
            data: data.legendData,
            textStyle: {
                color: '#fff'
            }
        },
        calendar: [{
            top: 80,
            left: 'center',
            range: data.rangeFirst,
            splitLine: {
                show: true,
                lineStyle: {
                    color: '#000',
                    width: 4,
                    type: 'solid'
                }
            },
            yearLabel: {
                formatter: '{start}  上半年',
                textStyle: {
                    color: '#fff'
                }
            },
            itemStyle: {
                normal: {
                    color: '#323c48',
                    borderWidth: 1,
                    borderColor: '#111'
                }
            }
        }, {
            top: 260,
            left: 'center',
            range: data.rangeSecond,
            splitLine: {
                show: true,
                lineStyle: {
                    color: '#000',
                    width: 4,
                    type: 'solid'
                }
            },
            yearLabel: {
                formatter: '{start}  下半年',
                textStyle: {
                    color: '#fff'
                }
            },
            itemStyle: {
                normal: {
                    color: '#323c48',
                    borderWidth: 1,
                    borderColor: '#111'
                }
            }
        }],
        series : [
            {
                name: data.legendData[0],
                type: 'scatter',
                coordinateSystem: 'calendar',
                data: data.series,
                symbolSize: function (val) {
                    //return val[1] / data.compareSizeValue;
                    return getSymbolSize(val);
                },
                itemStyle: {
                    normal: {
                        color: function (val) {
                            return getColor(val);
                        }
                    }
                }
            },
            {
                name: data.legendData[0],
                type: 'scatter',
                coordinateSystem: 'calendar',
                calendarIndex: 1,
                data: data.series,
                symbolSize: function (val) {
                    //return val[1] / data.compareSizeValue;
                    return getSymbolSize(val);
                },
                itemStyle: {
                    normal: {
                        color: function (val) {
                            return getColor(val);
                        }
                    }
                }
            }
        ]
    };
    var myChart = echarts.init(document.getElementById('drugDetailContainer'));

    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option,true);
}