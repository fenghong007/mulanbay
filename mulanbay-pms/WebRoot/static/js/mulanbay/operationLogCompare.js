//修改记录分析

//修改比较(id为业务主键的ID)其他功能页面进入
function showEditLogCompare(beanName,idTag) {
    //业务页面必须把主键值设置有id标签
    var id = $('#'+idTag).val();
    if(id==null){
        $.messager.alert('提示', '需要先保存记录或者修改过的记录才会有数据(页面上无法获取到对象主键值)!', 'info');
        return;
    }
    var url='../operationLog/getEditLogData?id='+ id+'&compareType=EARLY'+'&beanName='+beanName;
    doAjax(null,url,'GET',false,function(data){
        $('#log-compare-window').window('open');
        loadCompareData('最新修改记录',data,id);
    });
}

//显示比较数据
function loadCompareData(compareTitle,data,id) {
    $('#latestContent').treegrid('loadData', {"total":1,"rows":[
        {"id":'根目录',"name":'根目录'}
    ],"footer":[
        {"name":"Total Persons:","persons":7,"iconCls":"icon-sum"}
    ]});
    if(data.latestData){
        showFormatTree('latestContent','根目录',objKeySort(data.latestData));
    }
    $('#currentContent').treegrid('loadData', {"total":1,"rows":[
        {"id":'根目录',"name":'根目录'}
    ],"footer":[
        {"name":"Total Persons:","persons":7,"iconCls":"icon-sum"}
    ]});
    if(data.currentData){
        var currentTitle=compareTitle+'('+data.currentData.userName+'在'+data.currentData.occurTime+'操作)';
        $('#currentContent').treegrid({
            title:currentTitle
        });
        var showData = appendChangeTagToCurrent(data.latestData,eval('(' + data.currentData.paras + ')'));
        showFormatTree('currentContent','根目录',objKeySort(showData));
    }

    $('#compareContent').treegrid('loadData', {"total":1,"rows":[
        {"id":'根目录',"name":'根目录'}
    ]});
    var currentCompareId=null;
    if(data.compareData){
        var compareTitle='比较内容('+data.compareData.userName+'在'+data.compareData.occurTime+'操作,id='+data.compareData.id+')';
        $('#compareContent').treegrid({
            title:compareTitle
        });
        currentCompareId=data.compareData.id;
        var showData = appendChangeTagToCompare(eval('(' + data.currentData.paras + ')'),eval('(' + data.compareData.paras + ')'));
        showFormatTree('compareContent','根目录',objKeySort(showData));
    }else{
        $('#compareContent').treegrid({
            title:'无更多数据'
        });
    }
    var formData = {
        currentTargetId: id,//目前没有使用
        currentCompareId : currentCompareId
    };
    $('#compare-form').form('load', formData);
}

//id 为OperationLog的主键值，即要对哪条记录进行比较(日志管理页面进入)
function showOperationLogCompare(id) {
    var url='../operationLog/getCompareData?id='+ id+'&compareType=EARLY';
    doAjax(null,url,'GET',false,function(data){
        $('#log-compare-window').window('open');
        $('#grid').datagrid('clearSelections');
        loadCompareData('当前修改记录',data,id)
    });
}

function showNextCompare(compareType) {
    var para =form2Json("compare-form");
    para.compareType=compareType;
    var url='../operationLog/getNearstCompareData';
    doAjax(para,url,'GET',false,function(data){
        $('#grid').datagrid('clearSelections');
        $('#compareContent').treegrid('loadData', {"total":1,"rows":[
            {"id":'根目录',"name":'根目录'}
        ]});
        if(data.compareData){
            var compareTitle='比较内容('+data.compareData.userName+'在'+data.compareData.occurTime+'操作,id='+data.compareData.id+')';
            $('#compareContent').treegrid({
                title:compareTitle
            });
            $('#currentCompareId').val(data.compareData.id);
            var showData = appendChangeTagToCompare(eval('(' + data.currentData.paras + ')'),eval('(' + data.compareData.paras + ')'));
            showFormatTree('compareContent','根目录',objKeySort(showData));
        }else{
            $('#compareContent').treegrid({
                title:'无更多数据'
            });
        }
    });
}

//添加修改过的标签
function appendChangeTagToCurrent(latest,current) {
    for(var key in current){
        var changed =false;
        if(current[key]=='true'){
            //boolean类型spring mvc当时也是String类型
            current[key]=true;
        }
        if(current[key]=='false'){
            //boolean类型spring mvc当时也是String类型
            current[key]=false;
        }
        if(current[key]!=latest[key]){
            changed =true;
        }
        if(!latest.hasOwnProperty(key)&&current[key]!=null){
            changed =true;
        }
        if(changed){
            current[key]='<font color="red">'+current[key]+'</font>&nbsp;&nbsp;<img src="../static/image/warn.png"></img>';
        }
    }
    return current;
}


//添加修改过的标签
function appendChangeTagToCompare(original,compare) {
    for(var key in compare){
        if(compare[key]!=original[key]){
            compare[key]='<font color="red">'+compare[key]+'</font>&nbsp;&nbsp;<img src="../static/image/alert.gif"></img>';
        }
    }
    return compare;
}

function showFormatTree(treeId,groupName,data){
    for(var key in data){
        if((data[key] instanceof Array)){
            var l = data[key].length;
            if(l==0){
                // 太长或为空只显示数组长度，太长会导致网页无响应
                addTreeRow(treeId,groupName,key,key,'#数组长度：'+l+'，无数据#');
            }else if(l>10){
                addTreeRow(treeId,groupName,key,'#'+key+'#','#数组长度：'+l+'，解析只显示前10条数据#');
                l=10;
            }
            if(l>0){
                for (var i = 0; i < l; i++){
                    var gn = addTreeRow(treeId,groupName,groupName+'_'+key+'_'+i, key+'_'+i,'');
                    showFormatTree(gn,data[key][i]);
                }
            }
        }else if((data[key] instanceof Object)){
            var gn = addTreeRow(treeId,groupName,key,key,'');
            showFormatTree(gn,data[key]);
        }else{
            var keyValue=data[key];
            addTreeRow(treeId,groupName,key,key,keyValue);
        }
    }

}

function addTreeRow(treeId,groupName,vid,vname,vvalue){
    var enode = $('#'+treeId).treegrid('find', vid);
    if(enode){
        vid=vid+'_'+groupName;
    }
    var node = $('#'+treeId).treegrid('find', groupName);
    $('#'+treeId).treegrid('append', {
        parent: node.id,
        data: [{
            id: vid,
            name: vname,
            value: vvalue
        }]
    });
    return vid;
}
