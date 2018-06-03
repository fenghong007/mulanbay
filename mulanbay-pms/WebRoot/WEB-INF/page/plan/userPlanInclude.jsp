<%@ page language="java" pageEncoding="UTF-8"%>
<div id="user-plan-window" title="计划信息" class="easyui-window"
     style="width:880px;height:450px;"
     data-options="modal:true,closed:true,iconCls:'icon-info'">
    <div region="north" border="true" title="查询条件" split="true"
         style="overflow:auto;height:80px;" data-options="selected:false"
         icon="icon-edit" class="yhbackground">
        <form id="user-plan-search-window" method="post">
            <table cellpadding="5" class="tableForm" style="width: 100%;">
                <tr>
                    <td>起始日期:</td>
                    <td><input class="easyui-datebox" name="startDate"
                               data-options="showSeconds:false" style="width:100px">--
                        <input class="easyui-datebox" name="endDate"
                               data-options="showSeconds:false" style="width:100px"></td>
                    </td>
                    <td>计划类型:</td>
                    <td><select class="easyui-combobox" name="planType"
                                style="width:100px;">
                        <option value="">全部</option>
                        <option value="MONTH">月</option>
                        <option value="YEAR">年</option>
                        <option value="DAY">天</option>
                        <option value="WEEK">周</option>
                        <option value="SEASON">季度</option>
                    </select></td>
                    <td>数据来源:</td>
                    <td>
                        <input name="dataType" type="radio" class="easyui-validatebox" checked="checked" required="true" value="LATEST" style="width:15px">最新
                        <input name="dataType" type="radio" class="easyui-validatebox" required="true" value="HISTORY" style="width:15px">历史
                    </td>
                </tr>
                <tr>
                    <td>选择计划:</td>
                    <td><input id="userPlanForBussList" class="easyui-combotree" style="width:220px"
                               data-options="editable:true" name="userPlanId"></td>
                    </td>
                    <td>数据类型:</td>
                    <td>
                        <select class="easyui-combobox" name="filterType"
                                style="width:100px;">
                            <option value="ORIGINAL">默认</option>
                            <option value="NO_USER">不过滤用户</option>
                            <option value="NO_DATE">不过滤时间</option>
                            <option value="NONE">完全不过滤</option>
                        </select>
                    </td>
                    <td align="center" colspan="2">
                        <a href="javascript:void(0)" class="easyui-linkbutton"
                           icon="icon-search" onclick="showData()">查询</a>
                        <a href="javascript:void(0)" class="easyui-linkbutton"
                           icon="icon-cancel" onclick="resetSearchWindowById('user-plan-search-window')">重置</a>
                    </td>
                </tr>
            </table>
            <input type="hidden" name='relatedBeans' id="relatedBeans" />
            <input type="hidden" name='statNow' value="true"/>
            <input type="hidden" name='status' value="ENABLE"/>
        </form>
    </div>
    <div region="center" border="true" title="计划列表" split="true"
         style="background:#E2E377;overflow-y: hidden;width:auto;height:335px;">
        <div id="userPlanGrid" fit="true"></div>
    </div>
</div>
<div id="user-plan-timeline-window" title="计划进度统计" class="easyui-window"
     style="width:880px;height:450px;"
     data-options="modal:true,closed:true,iconCls:'icon-info'">
    <div region="center" border="true" title="结果" split="true">
        <div id="uptContainer"
             style="min-width: 400px; height: 430px; margin: 0 auto"></div>
    </div>
</div>
<div id="userRewardPoint-value-stat-window" title="积分统计" class="easyui-window"
     style="width:950px;height:550px;"
     data-options="modal:true,closed:true,iconCls:'icon-info'">
    <div region="north" border="true" title="查询条件" split="true"
         style="overflow:auto;height:80px;" data-options="selected:false"
         icon="icon-edit" class="yhbackground">
        <form id="userRewardPoint-value-stat-search-form" method="post">
            <table cellpadding="5" class="tableForm" style="width: 100%;">
                <tr>
                    <td>起始日期:</td>
                    <td>
                        <input class="easyui-datebox" name="startDate"
                               data-options="showSeconds:false" style="width:100px">--
                        <input class="easyui-datebox" name="endDate"
                               data-options="showSeconds:false" style="width:100px">
                    </td>
                    <td>统计分类:</td>
                    <td><select class="easyui-combobox" name="dateGroupType" id="dateGroupTypeList"
                                style="width:120px;">
                        <option value="">默认</option>
                        <option value="MONTH">月</option>
                        <option value="DAY">天</option>
                        <option value="DAYCALENDAR">天(日历)</option>
                        <option value="WEEK">周</option>
                        <option value="YEAR">年</option>
                        <option value="YEARMONTH">年月</option>
                    </select>
                    </td>
                </tr>
                <tr>
                    <td>来源:</td>
                    <td>
                        <select class="easyui-combobox" name="rewardSource"
                                style="width:80px;height:23px" >
                            <option value="OPERATION">操作</option>
                            <option value="NOTIFY">提醒</option>
                            <option value="PLAN">计划</option>
                        </select>
                        主键值:
                        <input class="easyui-validatebox" type="text"
                               name="sourceId" style="width:80px;" />
                    <td align="center" colspan="2">
                        <a href="javascript:void(0)" class="easyui-linkbutton"
                           icon="icon-search" onclick="statUserRewardPointValue()">统计</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div region="center" border="true" title="统计" split="true"
         style="overflow-y: hidden;width:auto;height:auto;">
        <div region="center" border="true" title="结果" split="true">
            <div id="userRewardPoint-value-stat-container"
                 style="min-width: 500px; height: 450px; margin: 0 auto"></div>
        </div>
    </div>
</div>

<div id="plan-analyse-stat-window" title="统计" class="easyui-window"
     style="width:860px;height:480px;"
     data-options="modal:true,closed:true,iconCls:'icon-info'">
    <div region="north" border="true" title="查询条件" split="true"
         style="overflow:auto;height:50px;" data-options="selected:false"
         icon="icon-edit" class="yhbackground">
        <form id="plan-analyse-search-form" method="post">
            <table cellpadding="5" class="tableForm" style="width: 100%;">
                <tr>
                    <td>
                        起始日期:
                    </td>
                    <td>
                        <input class="easyui-datebox" name="startDate"
                               data-options="showSeconds:false" style="width:100px">--
                        <input class="easyui-datebox" name="endDate"
                               data-options="showSeconds:false" style="width:100px">
                    </td>
                    <td>选择计划:</td>
                    <td><input id="userPlanAnalyseList" class="easyui-combotree" style="width:180px"
                               data-options="editable:true" name="id"></td>
                    </td>
                    <td align="center" colspan="2">
                        <a href="javascript:void(0)" class="easyui-linkbutton"
                           icon="icon-search" onclick="analyseUserPlan()">统计</a>
                    </td>
                </tr>
            </table>
            <input type="hidden" name='status' value="2"/>
        </form>
    </div>
    <div region="center" border="true" title="统计" split="true"
         style="overflow-y: hidden;width:auto;height:auto;">
        <div class="yhdiv">
            <table>
                <tr>
                    <td>
                        <div region="center" border="true" title="结果" split="true">
                            <div id="planCountAnalyseContainer"
                                 style="min-width: 400px; height: 450px; margin: 0 auto"></div>
                        </div>
                    </td>
                    <td>
                        <div region="center" border="true" title="结果" split="true">
                            <div id="planValueAnalyseContainer"
                                 style="min-width: 400px; height: 450px; margin: 0 auto"></div>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </div>
</div>

<div id="notify-analyse-stat-window" title="统计" class="easyui-window"
     style="width:860px;height:480px;"
     data-options="modal:true,closed:true,iconCls:'icon-info'">
    <div region="north" border="true" title="查询条件" split="true"
         style="overflow:auto;height:50px;" data-options="selected:false"
         icon="icon-edit" class="yhbackground">
        <form id="notify-analyse-search-form" method="post">
            <table cellpadding="5" class="tableForm" style="width: 100%;">
                <tr>
                    <td>选择提醒:</td>
                    <td><input id="userNotifyList" class="easyui-combotree" style="width:220px"
                               data-options="editable:true" name="id">
                        &nbsp;&nbsp;
                        <label style="color: red" id="notifyContent"></label>
                    </td>
                    </td>
                    <td align="center" colspan="2">
                        <a href="javascript:void(0)" class="easyui-linkbutton"
                           icon="icon-search" onclick="analyseUserNotify()">统计</a>
                    </td>
                </tr>
            </table>
            <input type="hidden" name='status' value="2"/>
        </form>
    </div>
    <div region="center" border="true" title="统计" split="true"
         style="overflow-y: hidden;width:auto;height:auto;">
        <div class="yhdiv">
            <table>
                <tr>
                    <td>
                        <div region="center" border="true" title="结果" split="true">
                            <div id="notifyAnalyseWarningContainer"
                                 style="min-width: 400px; height: 450px; margin: 0 auto"></div>
                        </div>
                    </td>
                    <td>
                        <div region="center" border="true" title="结果" split="true">
                            <div id="notifyAnalyseAlertContainer"
                                 style="min-width: 400px; height: 450px; margin: 0 auto"></div>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </div>
</div>
