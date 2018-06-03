<%@ page language="java" pageEncoding="UTF-8"%>
<div id="log-compare-window" title="日志比较信息" class="easyui-window"
     style="width:1120px;height:500px;padding:10px;"
     data-options="modal:true,closed:true,iconCls:'icon-info'">
    <table cellpadding="5" >
        <tr>
            <td valign="top">
                <table id="latestContent" title="数据库中最新内容" class="easyui-treegrid"
                       style="width:350px;height:auto"
                       data-options="rownumbers: true,idField: 'id',treeField: 'name',tools:'#latestContenttt'">
                    <thead>
                    <tr>
                        <th data-options="field:'name'" width="150">项</th>
                        <th data-options="field:'value'" width="150">值</th>
                    </tr>
                    </thead>
                </table>
            </td>
            <td valign="top">
                <table id="currentContent" title="当前操作的内容" class="easyui-treegrid"
                       style="width:350px;height:auto"
                       data-options="rownumbers: true,idField: 'id',treeField: 'name',tools:'#currentContenttt'">
                    <thead>
                    <tr>
                        <th data-options="field:'name'" width="150">项</th>
                        <th data-options="field:'value'" width="150">值</th>
                    </tr>
                    </thead>
                </table>
            </td>
            <td valign="top">
                <table id="compareContent" title="参与比较的内容" class="easyui-treegrid"
                       style="width:350px;height:auto"
                       data-options="rownumbers: true,idField: 'id',treeField: 'name',tools:'#compareContenttt'">
                    <thead>
                    <tr>
                        <th data-options="field:'name'" width="150">项</th>
                        <th data-options="field:'value'" width="150">值</th>
                    </tr>
                    </thead>
                </table>
            </td>
        </tr>
    </table>
    <form id="compare-form" method="post">
        <input type="hidden" name='currentTargetId' />
        <input type="hidden" name='currentCompareId' id="currentCompareId"/>
    </form>
</div>

<div id="latestContenttt">
    <a href="javascript:void(0)" class="icon-add" onclick="javascript:$('#latestContent').treegrid('expandAll')"></a>
    <a href="javascript:void(0)" class="icon-edit" onclick="javascript:$('#latestContent').treegrid('collapseAll')"></a>
</div>
<div id="currentContenttt">
    <a href="javascript:void(0)" class="icon-add" onclick="javascript:$('#currentContent').treegrid('expandAll')"></a>
    <a href="javascript:void(0)" class="icon-edit" onclick="javascript:$('#currentContent').treegrid('collapseAll')"></a>
</div>
<div id="compareContenttt">
    <a href="javascript:void(0)" class="icon-left" onclick="javascript:showNextCompare('EARLY')"></a>
    <a href="javascript:void(0)" class="icon-right" onclick="javascript:showNextCompare('LATER')"></a>
</div>
