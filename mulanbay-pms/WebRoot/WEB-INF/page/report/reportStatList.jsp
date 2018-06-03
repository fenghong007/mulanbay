<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>报表统计</title>
	<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/easyui/jquery.portal.js"></script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
<div region="center" border="true" title="报表统计"
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
						<td>年份:</td>
						<td><input id="yearList" class="easyui-combotree" style="width:150px;"
								   data-options="required:true,editable:true" name="year">
						</td>
						<td align="center">
							<a href="javascript:void(0)" class="easyui-linkbutton"
							   icon="icon-search" onclick="initPanel()">查询</a> <a
								href="javascript:void(0)" class="easyui-linkbutton"
								icon="icon-cancel" onclick="resetSearchWindow()">重置</a>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div region="center" id="mainPanle"
			 style="background: #eee;overflow:hidden;">
			<div id="tabs" class="easyui-tabs" fit="true" border="false">
					<div id="pp" style="position:relative">
						<div style="width:25%;"></div>
						<div style="width:25%;"></div>
						<div style="width:25%;"></div>
						<div style="width:25%;"></div>
					</div>
			</div>
		</div>
	</div>
</div>

</body>
<script type="text/javascript">
    $(function() {
        getYearTree();
        var currentYear = getYear(0);
        $('#yearList').combotree('setValue', currentYear);
        initPanel();
    });
    function initPanel(){
        //$('#pp').empty();
        $('#pp').portal({
            onStateChange:function(){
            }
        });
        var url='../reportStat/getData';
        var para = form2Json("search-window");
        var year = para.year;
        doAjax(para,url,'GET',false,function(data){
            remove();
            var currentPanels=new Array();
            for(var i=0; i<data.rows.length; i++){
                var pdata =data.rows[i];
                var title ='<img src="../static/image/sum.png"></img>'+year+pdata.title;
                var brs = '<br/><br/><br/>';
                var size =4;
                if(pdata.reportConfig.resultColumns ==1){
                    brs = '<br/><br/><br/><br/>';
                }else if(pdata.reportConfig.resultColumns > 2){
                    brs = '<br/><br/>';
                    size=3;
				}
                var content ='<div align="center" class="wrap">'+brs+'<font size="'+size+'" color="black">'+pdata.result+'</font></div>';
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
    //清空以前的历史
    function remove(){
        var panels = $('#pp').portal('getPanels');
        for(var i=0; i<panels.length; i++){
            $('#pp').portal('remove',panels[i]);
        }
        $('#pp').portal('resize');
    }
    function getYearTree(){
        $('#yearList').combotree({
            url : '../common/getYearTree',
            valueField : 'id',
            textField : 'text',
            loadFilter: function(data){
                return loadDataFilter(data);
            }
        });
    }
</script>
</html>
