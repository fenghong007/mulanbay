$(function() {
	initGrid();
});

var dataUrl='/musicPracticeTune/getData';

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
            field : 'musicPractice.id',
            title : '乐器',
            sortable : true,
            formatter : function(value, row, index) {
                return row.musicPractice.musicInstrument.name;
            },
        }, {
            field : 'tune',
            title : '曲子名称',
            sortable : true
        }, {
            field : 'times',
            title : '练习次数',
            sortable : true,
            align : 'center'
        }, {
            field : 'levelName',
            title : '水平',
            sortable : true,
            align : 'center'
        }, {
            field : 'tuneTypeName',
            title : '类型',
            sortable : true,
            align : 'center'
        }, {
            field : 'musicPractice.practiceDate',
            title : '练习日期',
            sortable : true,
            formatter : function(value, row, index) {
                return row.musicPractice.practiceDate;
            }
        }] ],
		pagination : true,
		rownumbers : true,
		singleSelect : false,
		toolbar : [ {
			id : 'statBtn',
			text : '统计',
			iconCls : 'icon-stat',
			handler : getJsonData
		}, '-', {
			id : 'searchBtn',
			text : '刷新',
			iconCls : 'icon-refresh',
			handler : showAll
		} ]
	});
}

function showAll() {
	refreshDatagrid(dataUrl,1,true);
}
