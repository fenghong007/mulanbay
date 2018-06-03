
function showLifeExperienceDetailGrid() {
    if(!checkLifeExperienceId()){
        $.messager.alert('提示', '请先保存人生经历!', 'error');
        return;
    }
    $("#searchLifeExperienceId").val($("#id").val());
    $('#lifeExperienceDetail-window').window('open');
    initLifeExperienceDetailGrid();
}

var dataUrlLifeExperienceDetail='../lifeExperienceDetail/getData';

function initLifeExperienceDetailGrid(){
	$('#lifeExperienceDetailGrid').datagrid({
		iconCls : 'icon-save',
		url : dataUrlLifeExperienceDetail,
		method:'GET',
		loadFilter: function(data){
			return loadDataFilter(data);
		},
		idField : 'id',
		loadMsg : '正在加载数据...',
        queryParams: form2Json("lifeExperienceDetail-form"),
        pageSize : 30,
		remoteSort : false,
		frozenColumns : [ [ {
			field : 'ID',
			checkbox : true
		} ] ],
		onDblClickRow: function (rowIndex, rowData) {
            $('#lifeExperienceDetailGrid').datagrid('uncheckAll');
            $('#lifeExperienceDetailGrid').datagrid('checkRow', rowIndex);
            editLifeExperienceDetail();
        },
        onLoadError: loadDataError,
		columns : [ [ {
			field : 'id',
			title : '记录号',
			sortable : true,
			align : 'center'
		}, {
            field : 'country',
            title : '国家',
            sortable : true
        }, {
            field : 'provinceId',
            title : '省份',
            sortable : true
        }, {
            field : 'cityId',
            title : '城市',
            sortable : true
        }, {
            field : 'districtId',
            title : '县(地区)',
            sortable : true
        }, {
            field : 'startCity',
            title : '出发城市',
            sortable : true
        }, {
            field : 'arriveCity',
            title : '抵达城市',
            sortable : true
        }, {
            field : 'cost',
            title : '花费',
            sortable : true,
            formatter : function(value, row, index) {
                return formatMoneyWithSymbal(value);
            },
            align : 'center'
        }, {
            field : 'mapStat',
            title : '加入地图绘制',
            sortable : true,
            formatter : function(value, row, index) {
                return getStatusImage(value);
            },
            align : 'center'
        }, {
            field : 'occurDate',
            title : '日期',
            sortable : true
        }] ],
		pagination : true,
		rownumbers : true,
		singleSelect : false,
		toolbar : [ {
			id : 'createBtnLifeExperienceDetail',
			text : '新增',
			iconCls : 'icon-add',
			handler : addLifeExperienceDetail
		}, '-', {
			id : 'editBtnLifeExperienceDetail',
			text : '修改',
			iconCls : 'icon-edit',
			handler : editLifeExperienceDetail
		}, '-', {
			id : 'deleteBtnLifeExperienceDetail',
			text : '删除',
			iconCls : 'icon-remove',
			handler : delLifeExperienceDetail
		}, '-', {
			id : 'searchBtnLifeExperienceDetail',
			text : '刷新',
			iconCls : 'icon-refresh',
			handler : showAllLifeExperienceDetail
		} ]
	});
}

function addLifeExperienceDetail() {
    if(!checkLifeExperienceId()){
        $.messager.alert('提示', '请先保存人生经历!', 'error');
        return;
    }
	$('#eidt-lifeExperienceDetail-window').window('open');
	$('#edit-lifeExperienceDetail-form').form('clear');
	$('#edit-lifeExperienceDetail-form').form.url='../lifeExperienceDetail/create';
    initGeoTreeList();
	//把父类的值复制过来，主要是城市等信息
    var para =form2Json("ff");
    //然后把子类中的人生经历外键设置为父类的ID
    var formData = {
        country: para.country,
        province:para.province,
        city:para.city,
        occurDate:para.startDate,
        location:para.location,
        startCity:para.startCity,
        arriveCity:para.arriveCity,
        mapStat : true
    };
    $('#edit-lifeExperienceDetail-form').form('load', formData);
    $("#detailOfLifeExperienceId").val($("#id").val());
    $("#searchLifeExperienceDetailId").val(0);
    initLifeExperienceConsumeGrid();
}

function editLifeExperienceDetail() {
    if(!checkLifeExperienceId()){
        $.messager.alert('提示', '请先保存人生经历!', 'error');
        return;
    }
    var rows = $('#lifeExperienceDetailGrid').datagrid('getSelections');
    var num = rows.length;
    if (num == 0) {
        $.messager.alert('提示', '请选择一条记录进行操作!', 'info');
        return;
    }
	var url='../lifeExperienceDetail/get?id='+ rows[0].id;
	doAjax(null,url,'GET',false,function(data){
		$('#eidt-lifeExperienceDetail-window').window('open');
        $('#edit-lifeExperienceDetail-form').form('clear');
        $('#edit-lifeExperienceDetail-form').form('load', data);
		$('#edit-lifeExperienceDetail-form').form.url='../lifeExperienceDetail/edit?id=' + data.id;
		//设置字符
		$('#lifeExperienceDetailGrid').datagrid('clearSelections');
		//设置外键值（$("#id").val()为总的人生经历ID）
        $("#detailOfLifeExperienceId").val($("#id").val());
        //消费记录查询的明细id为该值的ID
        $("#searchLifeExperienceDetailId").val(data.id);
        initLifeExperienceConsumeGrid();
        initGeoTreeList();
        $('#provinceList').combotree('setValue', data.provinceId);
        $('#cityList').combotree('setValue', data.cityId);
        $('#districtList').combotree('setValue', data.districtId);


    });
}

function showAllLifeExperienceDetail() {
    $('#lifeExperienceDetailGrid').datagrid({
        url : dataUrlLifeExperienceDetail,
        type : 'GET',
        queryParams: form2Json("lifeExperienceDetail-form"),
        pageNumber : 1,
        onLoadError : function() {
            $.messager.alert('错误', '加载数据异常！', 'error');
        }
    });
}

function checkLifeExperienceId() {
    var lifeExperienceId = $("#id").val();
    if(lifeExperienceId==null||lifeExperienceId==0){
    	return false;
    }else {
    	return true;
	}
}

function saveLifeExperienceDetail() {
    if(!checkLifeExperienceId()){
        $.messager.alert('提示', '请先保存人生经历!', 'error');
        return;
    }
    //自动设置，原来在增加与修改时设置经常出问题
    var url='../lifeExperienceDetail/edit';
    if($("#lifeExperienceDetailId").val()==null||$("#lifeExperienceDetailId").val()==''){
        url='../lifeExperienceDetail/create';
    }
    doFormSubmit('edit-lifeExperienceDetail-form',url,function(data){
		//closeWindow('eidt-lifeExperienceDetail-window');
		$('#lifeExperienceDetailGrid').datagrid('clearSelections');
        showAllLifeExperienceDetail();
        //消费列表的外键值需要设置为明细的id值，用于查询
        $("#searchLifeExperienceDetailId").val(data.id);
        $("#lifeExperienceDetailId").val(data.id);
    });
}

function initGeoTreeList() {
    getProvinceTree();
}

function getProvinceTree(){
    $('#provinceList').combotree({
        url : '../province/getAll',
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        },
        onChange : function (newValue, oldValue) {
            getCityTree(newValue);
            getDistrictTree(0);
        }
    });
}

function getCityTree(provinceId){
    $('#cityList').combotree({
        url : '../city/getCityList?provinceId='+provinceId,
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        },
        onChange : function (newValue, oldValue) {
            getDistrictTree(newValue);
        }
    });
}

function getDistrictTree(cityId){
    $('#districtList').combotree({
        url : '../district/getDistrictList?cityId='+cityId,
        valueField : 'id',
        textField : 'text',
        loadFilter: function(data){
            return loadDataFilter(data);
        }
    });
}

function getSelectedIdsLifeExperienceDetail() {
	var ids = [];
    var rows = $('#lifeExperienceDetailGrid').datagrid('getSelections');
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

function delLifeExperienceDetail() {
	var arr = getSelectedIdsLifeExperienceDetail();
	if (arr.length > 0) {
		$.messager.confirm('提示信息', '您确认要删除吗?', function(data) {
			if (data) {
				var vurl = '../lifeExperienceDetail/delete?ids=' + arr.join(',');
				doAjax(null, vurl, 'GET', true, function(data) {
					$('#lifeExperienceDetailGrid').datagrid('clearSelections');
                    showAllLifeExperienceDetail();
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

function closeWindowLifeExperienceDetail(){
    closeWindow('eidt-lifeExperienceDetail-window');
}
