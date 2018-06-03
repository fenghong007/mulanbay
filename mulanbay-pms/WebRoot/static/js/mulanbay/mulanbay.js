//全局超时设置：5秒
var global_time_out = 5000;
// 行编辑保存类型的超时设置：20秒，比较长
var global_time_out_re = 20000;
// 获取单个选择
function getSelectedSingleRow(gridName) {
	var rows = getSelectedRows(gridName);
	var num = rows.length;
	if (num > 1) {
		$.messager.alert('提示', '您选择了多条记录,只能选择一条记录进行修改!', 'info');
		return;
	} else {
		return rows;
	}
}

function getSelectedRows(gridName) {
	if (checkStrNull(gridName)) {
		gridName = 'grid';
	}
	var rows = $('#' + gridName).datagrid('getSelections');
	var num = rows.length;
	if (num == 0) {
		$.messager.alert('提示', '请选择一条记录进行操作!', 'info');
		return;
	} else {
		return rows;
	}
}

function getCurrentPageNumber(gridName){
    if (checkStrNull(gridName)) {
        gridName = 'grid';
    }
    var options = $('#' + gridName).datagrid('getPager').data("pagination").options;
    var page = options.pageNumber;//当前页数
	return page;
}


function checkStrNull(name) {
	if (name == null || name == '') {
		return true;
	} else {
		return false;
	}
}

function getNowDateTimeString() {
    return new Date().Format("yyyy-MM-dd hh:mm:ss");
}

function getNowDateString() {
    return new Date().Format("yyyy-MM-dd");
}

function formatDateStr(data) {
	if (data == null || data == '') {
		return '--';
	} else {
		return data.substr(0, 10);
	}
}

function formatTimeStr(data) {
	if (data == null || data == '') {
		return '--';
	} else {
		return data.substr(0, 19);
	}
}

//距离现在的时间
function tillNow(now,data) {
	// 支持 火狐、IE
	//data = data.replace(/-/g, "/");
	var nextExecuteTime = Date.parse(data);
	// console.log(nextExecuteTime);
	var nowTime=null;
	if(now==''){
		nowTime = new Date();
	}else{
		nowTime = Date.parse(now);
	}
	
	// 秒
	var r = (parseInt(nextExecuteTime - nowTime)) / 1000;
	return r;
}

//距离现在的时分秒
function tillNowString(second) {
	var time = '';
	if (second < 0) {
		time = '已过去';
		second = Math.abs(second);
	}
	if (second > 3600 * 24) {
		return time + '大于一天';
	} else {
		if (second >= 3600) {
			time += parseInt(second / 3600) + '小时';
			second %= 3600;
		}
		if (second >= 60) {
			time += parseInt(second / 60) + '分钟';
			second %= 60;
		}
		if (second > 0) {
			time += parseInt(second) + '秒';
		}
		return time;
	}

}

//距离现在的时分秒
function formatSeconds(second) {
	var time = '';
	if (second >= 3600 * 24) {
		time += parseInt(second / (3600 * 24)) + '天';
		second %= 3600 * 24;
	}
	if (second >= 3600) {
		time += parseInt(second / 3600) + '小时';
		second %= 3600;
	}
	if (second >= 60) {
		time += parseInt(second / 60) + '分钟';
		second %= 60;
	}
	if (second > 0) {
		time += parseInt(second) + '秒';
	}
	return time;
}

//距离现在的年月日
function formatDays(days) {
    var time = '';
    if (days >= 365) {
        time += parseInt(days / 365) + '年';
        days %= 365;
    }
    if (days >= 30) {
        time += parseInt(days / 30) + '个月';
        days %= 30;
    }
    if (days > 0) {
        time += parseInt(days) + '天';
    }
    return time;
}

// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
// 例子：
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
Date.prototype.Format = function(fmt)
{ //author: meizz
    var o = {
        "M+" : this.getMonth()+1,                 //月份
        "d+" : this.getDate(),                    //日
        "h+" : this.getHours(),                   //小时
        "m+" : this.getMinutes(),                 //分
        "s+" : this.getSeconds(),                 //秒
        "q+" : Math.floor((this.getMonth()+3)/3), //季度
        "S"  : this.getMilliseconds()             //毫秒
    };
    if(/(y+)/.test(fmt))
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    for(var k in o)
        if(new RegExp("("+ k +")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
    return fmt;
}

//获取指定日期的月第一天
function getFirstDayOfMonth(date) {
    var year = date.getFullYear();
    var month = date.getMonth();
    if (month < 10) {
        month = "0" + month;
    }
    var firstDay = year + "-" + month + "-" + "01";//的第一天
	return firstDay;
}

//获取指定日期的月最后一天
function getLastDayOfMonth(date) {
    var year = date.getFullYear();
    var month = date.getMonth();
    if (month < 10) {
        month = "0" + month;
    }
    var myDate = new Date(year, month, 0);
    var lastDay = year + "-" + month + "-" + myDate.getDate();//月的最后一天
	return lastDay;
}


// 获取距离当前日期，n为距离天数，负数表示往前
function getDay(n){
    var dd = new Date();
    dd.setDate(dd.getDate()+n);
    return dd.Format('yyyy-MM-dd');
}

// 距离指定日期天数后的日期
function getDayByDate(n,dateString){
    var dd = new Date(dateString.replace(/-/,"/"))
    dd.setDate(dd.getDate()+n);
    return dd.Format('yyyy-MM-dd');
}


function getYear(n){
    var dd = new Date();
    return dd.getFullYear()+n;
}

// 分钟转换为小时，n为小数位数
function minuteToHour(minute,n) {
	var res= minute/60.0;
    return res.toFixed(n);
}

function addTab(subtitle, url) {
	if (!$('#tabs').tabs('exists', subtitle)) {
		$('#tabs').tabs('add', {
			title : subtitle,
			content : createFrame(url),
			closable : true,
			width : $('#mainPanle').width() - 10,
			height : $('#mainPanle').height() - 26
		});
	} else {
		$('#tabs').tabs('select', subtitle);
	}
}

// 几个关闭事件的实现
function CloseTab(menu, type) {
	var curTabTitle = $(menu).data("tabTitle");
	var tabs = $("#tabs");
	if (type === "close") {
		tabs.tabs("close", curTabTitle);
		return;
	}
	var allTabs = tabs.tabs("tabs");
	var closeTabsTitle = [];
	$.each(allTabs, function() {
		var opt = $(this).panel("options");
		if (opt.closable && opt.title != curTabTitle && type === "Other") {
			closeTabsTitle.push(opt.title);
		} else if (opt.closable && type === "All") {
			closeTabsTitle.push(opt.title);
		}
	});
	for (var i = 0; i < closeTabsTitle.length; i++) {
		tabs.tabs("close", closeTabsTitle[i]);
	}
}

function createFrame(url) {
	var s = '<iframe name="mainFrame" scrolling="no" frameborder="0"  src="'
			+ url + '" style="width:100%;height:100%;"></iframe>';
	return s;
}

// 支持下拉框多选(即name相同的有多个)
function form2JsonEnhanced(id){
    var o = {};
    var a = $("#" + id).serializeArray();
    $.each(a, function () {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
}
// 将表单数据转为json
function form2Json(id) {

	var arr = $("#" + id).serializeArray();
	var jsonStr = "";
	jsonStr += '{';
	for (var i = 0; i < arr.length; i++) {
		jsonStr += '"' + arr[i].name + '":"' + arr[i].value + '",';
	}
	jsonStr = jsonStr.substring(0, (jsonStr.length - 1));
	jsonStr += '}';
	var json = JSON.parse(jsonStr);
	return json;
}

function errorHandler(XMLHttpRequest, textStatus, errorThrown) {
	$.messager.alert('错误', '请求失败!</br>服务器返回状态：' + textStatus + ",错误代码："
			+ XMLHttpRequest.status, 'error');
}

function loadDataError() {
	$.messager.alert('错误', '请求失败,无法加载数据！', 'error');
}

function loadDataFilter(data) {
	if(data.total){
		//说明是easyui自己排序
		return data;
	}
	if (data.code == 0) {
		return data.data;
	} else {
		$.messager.alert('错误', '请求失败！错误代码：' + data.code + ',错误信息：'
				+ data.message, 'error');
		return;
	}
}

$(function() {
	InitLeftMenu();
	$('body').layout();
})

function InitLeftMenu() {
	$('.easyui-accordion li a').click(function() {
		var tabTitle = $(this).text();
		var url = $(this).attr("href");
		addTab(tabTitle, url);
		$('.easyui-accordion li div').removeClass("selected");
		$(this).parent().addClass("selected");
	}).hover(function() {
		$(this).parent().addClass("hover");
	}, function() {
		$(this).parent().removeClass("hover");
	});
}

function collapseAll(treeName) {
	$('#' + treeName).tree('collapseAll');
}
function expandAll(treeName) {
	$('#' + treeName).tree('expandAll');
}

// 全不选
function unCheckTree(treeName) {
	var nodes = $('#' + treeName).tree('getChecked');
	if (nodes) {
		for (var i = 0; i < nodes.length; i++) {
			$('#' + treeName).tree('uncheck', nodes[i].target);
		}
	}
}

// 全选
function checkAllTree(treeName, checked) {
	var children = $('#' + treeName).tree('getChildren');
	for (var i = 0; i < children.length; i++) {
		if (checked) {
			$('#' + treeName).tree('check', children[i].target);
		} else {
			$('#' + treeName).tree('uncheck', children[i].target);
		}
	}
}

// 反选
function invertTree(treeName) {
	var children = $('#' + treeName).tree('getChildren');
	for (var i = 0; i < children.length; i++) {
		if (children[i].checked) {
			$('#' + treeName).tree('uncheck', children[i].target);
		} else {
			$('#' + treeName).tree('check', children[i].target);
		}
	}
}

function closeWindow(winName) {
	if (checkStrNull(winName)) {
		winName = 'eidt-window';
	}
	$('#' + winName).window('close');
}

function resetSearchWindow() {
	$('#search-window').form('reset');
}


function resetSearchWindowById(formId) {
    $('#'+formId).form('reset');
}

function resetAndInitSearchWindow() {
    $('#search-window').form('reset');
    initSearchForm();
}

//初始化查询界面的开始、结束日期
function initSearchForm(){
    // 查询条件默认是最近一个月
    var formData = {
        startDate: getDay(-30),
        endDate: getDay(0)
    };
    $('#search-window').form('load', formData);
}

// 自定义化
function resetAndInitSearchWindowYear() {
    $('#search-window').form('reset');
    initSearchFormYear();
}

//初始化查询界面的开始、结束日期
function initSearchFormYear(){
    // 查询条件今年的
    var formData = {
        startDate: getYear(0)+'-01-01',
        endDate: getYear(0)+'-12-31'
    };
    $('#search-window').form('load', formData);
}

function getIcon() {
	var s = $("input[name='r']:checked").val();
	$("#imageName").val(s);
	$('#icon-window').window('close');
}

function openIconWindow() {
	var iconName = $("#imageName").val();
	$('#icon-window').window({
		title : '图标选择',
		href : '../main/icon.html'
	});
	$('input:radio[name="r"][value="' + iconName + '"]').prop('checked', true);
	$('#icon-window').window('open');

}

$(function() {
	/* 扩展Editors的datetimebox方法 */
	$.extend($.fn.datagrid.defaults.editors, {
		datetimebox : {// 为方法取名
			init : function(container, options) {
				var editor = $('<input />').appendTo(container);
				options.editable = false;// 设置其不能手动输入
				editor.datetimebox(options);
				return editor;
			},
			getValue : function(target) {// 取值
				return $(target).datetimebox('getValue');
			},
			setValue : function(target, value) {// 设置值
				$(target).datetimebox('setValue', value);
			},
			resize : function(target, width) {
				$(target).datetimebox('resize', width);
			},
			destroy : function(target) {
				$(target).datetimebox('destroy');// 销毁生成的panel
			}
		}
	});
});

function mergeCellsByField(tableID, colList) {
	var ColArray = colList.split(",");
	var tTable = $('#' + tableID);
	var TableRowCnts = tTable.datagrid("getRows").length;
	var tmpA;
	var tmpB;
	var PerTxt = "";
	var CurTxt = "";
	var alertStr = "";
	for (j = ColArray.length - 1; j >= 0; j--) {
		PerTxt = "";
		tmpA = 1;
		tmpB = 0;
		for (i = 0; i <= TableRowCnts; i++) {
			if (i == TableRowCnts) {
				CurTxt = "";
			} else {
				CurTxt = tTable.datagrid("getRows")[i][ColArray[j]];
			}
			if (PerTxt == CurTxt) {
				tmpA += 1;
			} else {
				tmpB += tmpA;
				tTable.datagrid('mergeCells', {
					index : i - tmpA,
					field : ColArray[j],
					rowspan : tmpA,
					colspan : null
				});
				tmpA = 1;
			}
			PerTxt = CurTxt;
		}
	}
}

function showProgress(vtitle) {
	if (vtitle == null || vtitle == '') {
		vtitle = "请稍候";
	}
	$.messager.progress({
		title : vtitle,
		msg : '正在发送请求...',
		text : ''
	});
}

function closeProgress() {
	$.messager.progress('close');
}

//从第一页开始
function refreshTreegrid(vurl, vpageNumber, vauth) {
    var vparameters = form2JsonEnhanced("search-window");
    $('#grid').treegrid({
        url : vurl,
        type : 'GET',
        queryParams : vparameters,
        pageNumber : vpageNumber,
        onLoadError : function() {
            $.messager.alert('错误', '加载数据异常！', 'error');
        }
    });
    if(vauth){
        //authBtn();
    }
}

//保留在当前页
function reloadTreegrid() {
    var queryParams = form2JsonEnhanced("search-window");
    $('#grid').treegrid('options').queryParams=queryParams;
    $("#grid").treegrid('reload');
}

//从第一页开始
function refreshDatagrid(vurl, vpageNumber, vauth) {
    var vparameters = form2JsonEnhanced("search-window");
	$('#grid').datagrid({
		url : vurl,
        type : 'GET',
		queryParams : vparameters,
		pageNumber : vpageNumber,
		onLoadError : function() {
			$.messager.alert('错误', '加载数据异常！', 'error');
		}
	});
	if(vauth){
		//authBtn();
	}
}

//保留在当前页
function reloadDatagrid() {
    var queryParams = form2JsonEnhanced("search-window");
    $('#grid').datagrid('options').queryParams=queryParams;
    $("#grid").datagrid('reload');
}

function reloadAndClearSelections(gridName) {
	if (checkStrNull(gridName)) {
		gridName = 'grid';
	}
	$('#' + gridName).datagrid('reload');
	$('#' + gridName).datagrid('clearSelections');
}

function doAjax(postData, vurl, rqType, showSucInfo, callback) {
	//alert(JSON.stringify(postData));
	$.ajax({
		url : vurl,
		type : rqType,
		dataType : 'json',
        //contentType: "application/json",
        data : postData,
		// traditional解决数组自动添加中挂号问题，导致后台无法绑定
        traditional:true,
        timeout : global_time_out,
		beforeSend : function(XMLHttpRequest) {
			showProgress('');
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			closeProgress();
			errorHandler(XMLHttpRequest, textStatus, errorThrown);
		},
		success : function(data) {
			closeProgress();
			if (data.code == 0) {
				if (showSucInfo == true) {
                    $.messager.show({
                        title:'成功',
                        msg:data.message,
                        timeout:1000,
                        showSpeed:0,
                        showType:'show'
                    });
					//$.messager.alert('成功', data.message, 'info');
				}
				if (callback) {
					callback(data.data);
				}

			}else if(data.code==10004){
                var curUrl = document.location.href;
                if(curUrl.indexOf("main/main")>-1){
                	//首页进来的返回到登录页
                    document.location="/main/login";
				}else{
                    $.messager.alert('错误',  data.message+'(code='+data.code+')', 'error');
                }
			} else {
				$.messager.alert('错误',  data.message+'(code='+data.code+')', 'error');
			}
		}
	});
}

function doFormSubmit(formId, vurl, callback) {
	$('#' + formId).form('submit', {
		url : vurl,
		dataType : 'json',
        beforeSend : function(XMLHttpRequest) {
            //XMLHttpRequest.setRequestHeader("content-type",'application/json; charset=utf-8')
            showProgress('');
		},
		success : function(data) {
			closeProgress();
			var data = eval('(' + data + ')');
			if (data.code == 0) {
                $.messager.show({
                    title:'成功',
                    msg:data.message,
                    timeout:1000,
                    showSpeed:0,
                    showType:'show'
                });
				//$.messager.alert('成功', data.message, 'info');
				if (callback) {
					callback(data.data);
				}
			} else {
				$.messager.alert('错误',  data.message+'(code='+data.code+')', 'error');
			}
		},
		error : function() {
			closeProgress();
			$.messager.alert('系统提示', '系统异常，请稍后再试!', 'error');
		}
	});
}

function formatMoneyWithSymbal(pp){
    return '￥'+pp;
}

//两个日期间天数
function dateDiff(sDate1,  sDate2){
	//sDate1和sDate2是2002-12-18格式
    var oDate1,  oDate2,  iDays;
    var oDate1 = Date.parse(sDate1);
    var oDate2 = Date.parse(sDate2);;
    var iDays  =  parseInt(Math.abs(oDate1  -  oDate2)  /  1000  /  60  /  60  /24);   //把相差的毫秒数转换为天数
    return  iDays;
}

//两个日期间小时
function hourDiff(sDate1,  sDate2){
    //sDate1和sDate2是2002-12-18格式
    var oDate1,  oDate2,  iDays;
    var oDate1 = Date.parse(sDate1);
    var oDate2 = Date.parse(sDate2);;
    var iDays  =  parseFloat(Math.abs(oDate1  -  oDate2)  /  1000  /  60  /  60 );   //把相差的毫秒数转换为天数
    return  iDays;
}

//两个时间间的分钟
function minuteDiff(sDate1,  sDate2){
    //sDate1和sDate2是2002-12-18格式
    var oDate1,  oDate2,  iDays;
    var oDate1 = Date.parse(sDate1);
    var oDate2 = Date.parse(sDate2);;
    var iDays  =  parseFloat(Math.abs(oDate1  -  oDate2)  /  1000  /  60  );   //把相差的毫秒数转换为天数
    return  iDays;
}

//两个日期间毫秒
function msecDiff(sDate1,  sDate2){
    //sDate1和sDate2是2002-12-18格式
    var oDate1,  oDate2,  iDays;
    var oDate1 = Date.parse(sDate1);
    var oDate2 = Date.parse(sDate2);;
    var msec  =  parseFloat(Math.abs(oDate1  -  oDate2) );
    return  msec;
}

//获取状态的图片地址
function getStatusImage(va){
	if(va=='DISABLE'||va==false){
		return '<img src="../static/image/cross.png"></img>';
	}else{
		return '<img src="../static/image/tick.png"></img>';
	}
}

function showFullDetail(msg){
    $('#grid').datagrid('uncheckAll');
    $.messager.alert('详情', msg, 'info');
}

//获取星星
function getStar(n) {
	var vv =parseInt(n);
	var s='';
	for(var i=0;i<vv;i++){
		s+='★';
	}
	if(n>vv){
		//说明有小数部分
        s+='☆';
    }

	return s;
}

//计算百分比
function getPercent(num1,num2) {
	if(num2==0){
		return 'n/a';
	}
    return (Math.round(num1 / num2 * 10000) / 100.00);
}

//计算百分比（带百分号）
function getPercentWithSambol(num1,num2) {
    return getPercent(num1,num2) +"%";
}

//获取删除提示信息
function getDeleteConfirmMsg(ll) {
    return '您确认要删除这<'+ll+'>条数据吗?';
}

//根据ID列表删除
function commonDeleteByIds(vurlPrefix) {
    var arr = getSelectedIds();
    var ll =arr.length;
    var msg = getDeleteConfirmMsg(ll);
    if (ll > 0) {
        $.messager.confirm('提示信息', msg, function(data) {
            if (data) {
                var vurl = vurlPrefix+'?ids=' + arr.join(',');
                doAjax(null, vurl, 'GET', true, function(data) {
                    $('#grid').datagrid('clearSelections');
                    showAll();
                });
            }else{
                $('#grid').datagrid('clearSelections');
            }
        });
    } else {
        $.messager.show({
            title : '警告',
            msg : '请先选择要删除的记录。'
        });
    }
}

function showInfoMsg(msg) {
    $.messager.alert('提示', msg, 'info');
}

//排序的函数
function objKeySort(arys) {
    //先用Object内置类的keys方法获取要排序对象的属性名，再利用Array原型上的sort方法对获取的属性名进行排序，newkey是一个数组
    var newkey = Object.keys(arys).sort();
    //console.log('newkey='+newkey);
    var newObj = {}; //创建一个新的对象，用于存放排好序的键值对
    for(var i = 0; i < newkey.length; i++) {
        //遍历newkey数组
        newObj[newkey[i]] = arys[newkey[i]];
        //向新创建的对象中按照排好的顺序依次增加键值对

    }
    return newObj; //返回排好序的新对象
}

// easyui 时间控件只显示年月
/**
 * html 写法
 * 控件：<input class="easyui-datebox" name="yearmonth"
 data-options="formatter:myYearMonthformatter,parser:myYearMonthparser" style="width:120px">
   js获取：
 var para = form2Json("search-window");
 var ss = (para.yearmonth.split('-'));
 para.year=parseInt(ss[0],10);
 para.month=parseInt(ss[1],10);
 * @param date
 * @returns {string}
 */
function myYearMonthformatter(date){
    var y = date.getFullYear();
    var m = date.getMonth()+1;
    return y+'-'+(m<10?('0'+m):m);
}
function myYearMonthparser(s){
    if (!s) return new Date();
    var ss = (s.split('-'));
    var y = parseInt(ss[0],10);
    var m = parseInt(ss[1],10);
    if (!isNaN(y) && !isNaN(m)){
        return new Date(y,m-1);
    } else {
        return new Date();
    }
}