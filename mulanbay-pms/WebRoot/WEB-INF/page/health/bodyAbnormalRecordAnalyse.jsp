<%@ page language="java" pageEncoding="UTF-8"%>
<div id="body-analyse-window" title="身体情况统计分析" class="easyui-window"
     style="width:880px;height:450px;"
     data-options="modal:true,closed:true,iconCls:'icon-info'">
    <div region="north" border="true" title="查询条件" split="true"
         style="overflow:auto;height:80px;" data-options="selected:false"
         icon="icon-edit" class="yhbackground">
        <form id="body-analyse-form" method="post">
            <table cellpadding="5" class="tableForm" style="width: 100%;">
                <tr>
                    <td>起始日期:</td>
                    <td><input class="easyui-datebox" name="startDate"
                               data-options="showSeconds:false" style="width:100px">--
                        <input class="easyui-datebox" name="endDate"
                               data-options="showSeconds:false" style="width:100px"></td>
                    </td>
                    <td>分组类型:</td>
                    <td><select class="easyui-combobox" name="groupField"
                                style="width:90px;">
                        <option value="ORGAN">器官</option>
                        <option value="DISEASE">疾病</option>
                    </select>
                    </td>
                </tr>
                <tr>
                    <td>关键字:</td>
                    <td><input class="easyui-validatebox"
                               type="textarea" name="name" style="width: 215px;"></input></td>
                    </td>
                    <td align="center" colspan="2">
                        <a href="javascript:void(0)" class="easyui-linkbutton"
                           icon="icon-search" onclick="initBodyAnalyseGrid()">查询</a>
                        <a href="javascript:void(0)" class="easyui-linkbutton"
                           icon="icon-cancel" onclick="resetAndInitBodyAnalyseSearchWindowYear()">重置</a>
                    </td>
                </tr>
            </table>
            <input type="hidden" name='relatedBeans' id="relatedBeans" />
            <input type="hidden" name='statNow' value="true"/>
            <input type="hidden" name='status' value="ENABLE"/>
        </form>
    </div>
    <div region="center" border="true" title="身体情况统计分析" split="true"
         style="background:#E2E377;overflow-y: hidden;width:auto;height:335px;">
        <div id="bodyAnalyseGrid" fit="true"></div>
    </div>
</div>