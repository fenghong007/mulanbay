<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>用户行为操作统计</title>
	<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/easyui/jquery.portal.js"></script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
<div region="center" border="true" title="用户行为操作统计"
	 style="border-left:0px;border-right:0px; height: 188px">
	<div class="easyui-layout" id="subWrap"
		 style="width:100%;height:100%;background:#0A3DA4;"
		 data-options="fit:true,border:false">
		<div region="north" border="true" title="查询条件" split="true"
			 style="overflow:auto;height:70px;" data-options="selected:false"
			 icon="icon-edit" class="yhbackground">
			<form id="search-window" method="post">
				<table cellpadding="5" class="tableForm" style="width: 100%;">
					<tr>
						<td>起始时间:</td>
						<td>
							<input class="easyui-datetimebox" name="startTime"
								   data-options="showSeconds:true" style="width:155px">--
							<input class="easyui-datetimebox" name="endTime"
								   data-options="showSeconds:true" style="width:155px">
						</td>
						<td>分页信息:</td>
						<td>
							当前页:
							<input  class="easyui-numberspinner" onclick="initPanel()"
									name="page" style="width:60px;"
									data-options="required:true,min:0,precision:0">
							每页数:
							<input  class="easyui-numberspinner" onclick="initPanel()"
									name="pageSize" style="width:60px;"
									data-options="required:true,min:0,precision:0">
						</td>
						<td align="center">
							<a href="javascript:void(0)" class="easyui-linkbutton"
							   icon="icon-search" onclick="initPanel()">查询</a> <a
								href="javascript:void(0)" class="easyui-linkbutton"
								icon="icon-cancel" onclick="initSearchForm()">重置</a>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div region="center" id="mainPanle"
			 style="background: #eee;overflow:hidden;">
			<div id="tabs" class="easyui-tabs" fit="true" border="false">
					<div id="pp" style="position:relative">
						<div style="width:33.3%;"></div>
						<div style="width:33.3%;"></div>
						<div style="width:33.3%;"></div>
					</div>
			</div>
		</div>
	</div>
</div>

</body>
<script type="text/javascript">
    $(function() {
        initSearchForm();
        initPanel();
    });
    function initSearchForm() {
        // 查询条件今年的
        var formData = {
            startTime: getYear(0)+'-01-01 00:00:00',
            endTime: getYear(0)+'-12-31 23:59:59',
            page:1,
            pageSize:10
        };
        $('#search-window').form('load', formData);
    }
    function initPanel(){
        //$('#pp').empty();
        $('#pp').portal({
            onStateChange:function(){
            }
        });
        var url='../userBehavior/userOperationStat';
        var para = form2Json("search-window");
        doAjax(para,url,'GET',false,function(data){
            remove();
            var currentPanels=new Array();
            for(var i=0; i<data.length; i++){
                var pdata =data[i];
                var operationsSize = data[i].operations.length;
                var title ='<img src="../static/image/sum.png"></img>'+pdata.title+'('+operationsSize;
                if(operationsSize==para.pageSize){
                    title+='+)';
				}else{
                    title+=')';
                }
                var brs = '';
                var size =2;
                var contentDetail='<table cellpadding="5" class="tableForm" style="width: 100%;">';
                //contentDetail+='<tr><td>操作日期:</td><td>内容:</td></tr>';
                for(var j=0; j<data[i].operations.length; j++){
                    contentDetail+='<tr>';
                    contentDetail+='<td>'+data[i].operations[j].occurTime+'</td>';
                    contentDetail+='<td>'+data[i].operations[j].content+'</td>';
                    contentDetail+='</tr>';
                }
                contentDetail+='</table>';
                var content ='<div align="top" class="wrap">'+brs+'<font size="'+size+'" color="black">'+contentDetail+'</font></div>';
                //var content ='<div align="center" class="wrap">'+pdata.result+'</div>';
				var panel = {
                    id: pdata.id,
                    title: title,
                    height:200,
                    collapsible:true,
                    content:content
                };
                currentPanels.push(panel);
            }
            addPanelsToContent(currentPanels);
            $('#pp').portal('resize');
        });
    }

    function addPanelsToContent(panels){
        for(var i=0; i<panels.length; i++){
            var panel = panels[i];
            var p = $('<div/>').attr('id',panel.id).appendTo('body');
            p.panel(panel);
            //columnIndex为0，1，2,对于0，2需要调换位置
            var index = panel.id%3;
            if(index==0){
                index=2;
            }else if(index==2){
                index=0;
            }
            $('#pp').portal('add',{
                panel:p,
                columnIndex:index
            });
        }
    }
    //清空以前的历史
    function remove(){
        var panels = $('#pp').portal('getPanels');
        for(var i=0; i<panels.length; i++){
            $('#pp').portal('remove',panels[i]);
        }
        $('#pp').portal('resize');
    }
</script>
</html>
