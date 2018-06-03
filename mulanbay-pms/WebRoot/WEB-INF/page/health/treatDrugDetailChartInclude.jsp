<%@ page language="java" pageEncoding="UTF-8"%>
<div id="drugDetail-stat-window" title="用药统计" class="easyui-window"
     style="width:870px;height:550px;"
     data-options="modal:true,closed:true,iconCls:'icon-info'">
    <div region="north" border="true" title="查询条件" split="true"
         style="overflow:auto;height:50px;" data-options="selected:false"
         icon="icon-edit" class="yhbackground">
        <form id="drugDetail-stat-search-form" method="post">
            <table cellpadding="5" class="tableForm" style="width: 100%;">
                <tr>
                    <td>类型:</td>
                    <td>
                        <input name="dateGroupType" type="radio" class="easyui-validatebox" required="true" value="YEAR" style="width:30px">年
                        <input name="dateGroupType" type="radio" class="easyui-validatebox" required="true"  checked="checked" value="MONTH" style="width:30px">月
                    </td>
                    <td>选择日期:</td>
                    <td><input class="easyui-datebox" name="yearmonth"
                               data-options="formatter:myYearMonthformatter,parser:myYearMonthparser" style="width:120px">
                    </td>
                    <td align="center" colspan="2">
                        <a href="javascript:void(0)" class="easyui-linkbutton"
                           icon="icon-search" onclick="calandarStatDrugDetail()">查询</a>
                    </td>
                </tr>
            </table>
            <input type="hidden" name='treatDrugId' id="statTreatDrugId"/>
        </form>
    </div>
    <div region="center" border="true" title="统计" split="true"
         style="overflow-y: hidden;width:auto;height:auto;">
        <div region="center" border="true" title="结果" split="true" style="background:gainsboro;overflow-y: hidden">
            <div id="drugDetailContainer"
                 style="min-width: 870px; height: 450px; margin: 0 auto"></div>
        </div>
    </div>
</div>
