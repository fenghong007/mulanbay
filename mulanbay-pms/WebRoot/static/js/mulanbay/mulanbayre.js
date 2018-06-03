//行编辑通用js
function redo(){
	editRow = undefined;
    $("#grid").datagrid('rejectChanges');
    $("#grid").datagrid('unselectAll');
}

function edit() {
	var row = $("#grid").datagrid('getSelected');
    if (row !=null) {
        if (editRow != undefined) {
            $("#grid").datagrid('endEdit', editRow);
        }

        if (editRow == undefined) {
            var index = $("#grid").datagrid('getRowIndex', row);
            $("#grid").datagrid('beginEdit', index);
            editRow = index;
            $("#grid").datagrid('unselectAll');
        }
    } else {
         
    }
}

function saveRowData(vurl){
	$("#grid").datagrid('endEdit', editRow);
	 
    //如果调用acceptChanges(),使用getChanges()则获取不到编辑和新增的数据。

    //使用JSON序列化datarow对象，发送到后台。
    var rows = $("#grid").datagrid('getChanges');

    var rowstr = JSON.stringify(rows);
    doAjax('data='+rowstr,vurl,'POST',true,function(data){
    	showAll();
    });
}

function moveUp() {
    var row = $("#grid").datagrid('getSelected'); 
    var index = $("#grid").datagrid('getRowIndex', row);
    mysort(index, 'up', 'grid');
     
}
//下移
function moveDown() {
    var row = $("#grid").datagrid('getSelected');
    var index = $("#grid").datagrid('getRowIndex', row);
    mysort(index, 'down', 'grid');
     
}
 
 
function mysort(index, type, gridname) {
    if ("up" == type) {
        if (index != 0) {
            var toup = $('#' + gridname).datagrid('getData').rows[index];
            var todown = $('#' + gridname).datagrid('getData').rows[index - 1];
            $('#' + gridname).datagrid('getData').rows[index] = todown;
            $('#' + gridname).datagrid('getData').rows[index - 1] = toup;
            $('#' + gridname).datagrid('refreshRow', index);
            $('#' + gridname).datagrid('refreshRow', index - 1);
            $('#' + gridname).datagrid('selectRow', index - 1);
        }
    } else if ("down" == type) {
        var rows = $('#' + gridname).datagrid('getRows').length;
        if (index != rows - 1) {
            var todown = $('#' + gridname).datagrid('getData').rows[index];
            var toup = $('#' + gridname).datagrid('getData').rows[index + 1];
            $('#' + gridname).datagrid('getData').rows[index + 1] = todown;
            $('#' + gridname).datagrid('getData').rows[index] = toup;
            $('#' + gridname).datagrid('refreshRow', index);
            $('#' + gridname).datagrid('refreshRow', index + 1);
            $('#' + gridname).datagrid('selectRow', index + 1);
        }
    }
 
}

