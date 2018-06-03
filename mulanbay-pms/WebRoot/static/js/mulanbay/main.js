function initPanel(){
    $('#pp').portal({
        onStateChange:function(){
        }
    });
    var url='../notifyStat/getData?showInIndex=true&status=ENABLE&page=-1';
    doAjax(null,url,'GET',false,function(data){
        var panels=new Array();
        for(var i=0; i<data.rows.length; i++){
            var pdata =data.rows[i];
            var title ='';
            var color ="black";
            if (pdata.overAlertValue>0) {
                title='<img src="../static/image/alert.gif"></img>';
                color="red";
            } else if (pdata.overWarningValue>0) {
                title='<img src="../static/image/warn.png"></img>';
                color="BlueViolet";
            } else {
                title='<img src="../static/image/tick.png"></img>';
            }
            title+=pdata.userNotify.title+'(W:'+pdata.userNotify.warningValue+'>>A:'+pdata.userNotify.alertValue+')';
            var content ='<div align="center" class="wrap"><font size="6" color="'+color+'">'+pdata.compareValue+'</font>&nbsp;&nbsp;'+pdata.userNotify.notifyConfig.valueTypeName+'</div>';
            if(pdata.userNotify.notifyConfig.resultType=='NAME_DATE'||pdata.userNotify.notifyConfig.resultType=='NAME_NUMBER'){
                content ='<div align="center" class="wrap"><font size="6" color="'+color+'">'+pdata.name+'</font>&nbsp;&nbsp;'+pdata.compareValue+'&nbsp;&nbsp;'+pdata.userNotify.notifyConfig.valueTypeName+'</div>';
            }
            var enhContent = '<a href="#" onclick="addTab(\''+pdata.userNotify.notifyConfig.tabName+'\',\'..'+pdata.userNotify.notifyConfig.url+'\')">'+content+'</a>';
            var panel = {
                id: pdata.id,
                title: title,
                height:200,
                collapsible:true,
                content:enhContent
            };
            panels.push(panel);
        }
        addPanelsToContent(panels);
        $('#pp').portal('resize');
        //加载用户信息
        loadUserInfo();
    });
}

function addPanelsToContent(panels){
    for(var i=0; i<panels.length; i++){
        var panel = panels[i];
        var p = $('<div/>').attr('id',panel.id).appendTo('body');
        p.panel(panel);
        //columnIndex为0，1，2,对于0，2需要调换位置
        var index = panel.id%4;
        if(index==0){
            index=3;
        }else if(index==3){
            index=0;
        }
        $('#pp').portal('add',{
            panel:p,
            columnIndex:index
        });
    }
}

function logout(){
    $.messager.confirm('提示信息', '您确认要退出系统吗?', function(data) {
        if (data) {
            location.href='../main/logout';
        }
    });
}

/**
 * 今日行程
 */
function todayCalendar(){
    var url='../userCalendar/todayCalendarList';
    doAjax(null,url,'GET',false,function(data){
        $('#todayCalendar-window').window('open');
        $('#todayCalendarTg').datagrid({
            //url: '../userCalendar/todayCalendarList',
            showGroup: true,
            scrollbarSize: 0,
            columns:[[
                {field:'title',title:'标题',width:150,align:'center'},
                {field:'expireTime',title:'失效时间',width:140,align:'center'},
                {field:'delayCounts',title:'延迟次数',width:60,align:'center'},
                {field:'sourceTypeName',title:'来源',width:60,align:'center'},
                {field:'createdTime',title:'创建时间',width:140,align:'center'},
                {field:'ops',title:'操作',width:50,align:'center'}
            ]]
        });
        //清空所有数据
        $('#todayCalendarTg').datagrid('loadData', { total: 0, rows: [] });
        for (var i = 0; i < data.length; i++) {
            var title =data[i].title;
            if(data[i].delayCounts>2){
                title='<font color="red">'+data[i].title+'</font>';
            }
            var row = {
                title:title,
                expireTime:data[i].expireTime,
                delayCounts:data[i].delayCounts,
                sourceTypeName:data[i].sourceTypeName,
                createdTime:data[i].createdTime,
                ops:'<a href="#" onclick="finishUserCalendar('+data[i].id+')">完成</a>'
            };
            $('#todayCalendarTg').datagrid('appendRow',row);
        }
    });
}

function finishUserCalendar(id) {
    var url='../userCalendar/finish';
    var para ={
        id:id
    };
    doAjax(para,url,'POST',false,function() {
        todayCalendar();
    });
}

function loadUserInfo() {
    var url='../main/myInfo';
    doAjax(null,url,'GET',false,function(data) {
        document.getElementById("version").innerText=data.version;
        document.getElementById("username").innerText=data.username;
        var todayCalendars = data.todayCalendars;
        if(todayCalendars>0){
            document.getElementById("todayCalendars").innerText='('+data.todayCalendars+')';
        }
    });
}

function editMyInfo() {
    var url='../user/getMyInfo';
    doAjax(null,url,'GET',false,function(data){
        $('#myInfo-window').window('open');
        $('#myInfo-form').form('clear');
        $('#myInfo-form').form('load', data);
        $("#password").textbox('setValue',null);
    });
}

function saveMyInfo() {
    var url='../user/editMyInfo';
    doFormSubmit('myInfo-form',url,function(){
        closeWindow('myInfo-window');
    });
}