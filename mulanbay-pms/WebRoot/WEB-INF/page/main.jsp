<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>木兰湾管理系统</title>
<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/easyui/jquery.portal.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/main.js"></script>
	<style type="text/css">
		.wrap{
			text-align: center;
			line-height: 180px;
			}
		.mulanbay-header-left {
			position: absolute;
			z-index: 1;
			left: 30px;
			top: 5px;
		}
		.mulanbay-header-right {
			position: absolute;
			z-index: 1;
			right: 10px;
			top: 25px;
			color: #fff;
			text-align: right;
		}
		a:link,a:visited{
			text-decoration:none;  /*超链接无下划线*/
		}
		a:hover{
			text-decoration:underline;  /*鼠标放上去有下划线*/
		}
	</style>
</head>
<body class="easyui-layout" style="overflow-y: hidden" scroll="no">
	<noscript>
		<div
			style="position: absolute; z-index: 100000; height: 2046px; top: 0px; left: 0px;
            width: 100%; background: white; text-align: center;">
			<img src="images/noscript.gif" alt='抱歉，请开启脚本支持！' />
		</div>
	</noscript>
	<div region="north" split="true"
		style="overflow: hidden; height: 70px; background: #D2E0F2 repeat-x center 50%;
        line-height: 20px; color: #ffff;">
		<div class="mulanbay-header-left">
			<h1>木兰湾管理系统</h1>
		</div>
		<div class="mulanbay-header-right">
			<p><strong class="easyui-tooltip" ><label id="username"></label></strong>，欢迎您！
				<a href="#" onclick="todayCalendar()">今日任务<label style="color: red" id="todayCalendars"></label></a>
				<a href="#" onclick="editMyInfo()">个人设置</a>
				<a href="#" onclick="logout()">安全退出</a>
			</p>
		</div>

	</div>
	<div region="south" style="height: 20px; background: #D2E0F2;">
		<div style="text-align: center; font-weight: bold">
			Copyright © 2017-2018 木兰湾. All rights reserved.
			V<label id="version"></label>

		</div>
	</div>
	<div region="west" split="true" title="导航菜单"
		style="width: 200px;overflow:hidden;">
		<div id="menu" class="easyui-accordion" fit="true" border="false">
			<div title="功能菜单" style="overflow:auto; padding: 10px;">
				<ul id="menu-tree" class="easyui-tree">
					<li><span>消费管理</span>
						<ul>
							<li><span><a onclick=addTab('购买来源管理','../buyType/list')>购买来源管理</a></span></li>
							<li><span><a onclick=addTab('商品类型管理','../goodsType/list')>商品类型管理</a></span></li>
							<li><span><a onclick=addTab('价格区间管理','../priceRegion/list')>价格区间管理</a></span></li>
							<li><span><a onclick=addTab('消费记录管理','../buyRecord/list')>消费记录管理</a></span></li>
							<li><span><a onclick=addTab('消费实时分析','../buyRecord/analyseStatList')>消费实时分析</a></span></li>
							<li><span><a onclick=addTab('消费统计','../buyRecord/dateStatList')>消费统计</a></span></li>
							<li><span><a onclick=addTab('消费同期比对','../buyRecord/yoyStatList')>消费同期比对</a></span></li>
							<li><span><a onclick=addTab('关键字统计','../buyRecord/keywordsStatList')>关键字统计</a></span></li>
						</ul></li>
					<li><span>音乐练习管理</span>
						<ul>
							<li><span><a onclick=addTab('乐器管理','../musicInstrument/list')>乐器管理</a></span></li>
							<li><span><a onclick=addTab('音乐练习记录管理','../musicPractice/list')>音乐练习记录管理</a></span></li>
							<li><span><a onclick=addTab('音乐练习统计','../musicPractice/dateStatList')>音乐练习统计</a></span></li>
							<li><span><a onclick=addTab('音乐练习分析','../musicPractice/timeStatList')>音乐练习分析</a></span></li>
							<li><span><a onclick=addTab('音乐练习比对','../musicPractice/compareStatList')>音乐练习比对</a></span></li>
							<li><span><a onclick=addTab('音乐练习同期比对','../musicPractice/yoyStatList')>音乐练习同期比对</a></span></li>
							<li><span><a onclick=addTab('音乐练习曲子记录管理','../musicPracticeTune/list')>音乐练习曲子记录管理</a></span></li>
							<li><span><a onclick=addTab('音乐练习曲子记录统计','../musicPracticeTune/tuneStatList')>音乐练习曲子记录统计</a></span></li>
						</ul></li>
					<li data-options="state:'closed'"><span>梦想管理</span>
						<ul>
							<li><span><a onclick=addTab('梦想管理','../dream/list')>梦想管理</a></span></li>
							<li><span><a onclick=addTab('梦想统计','../dream/dreamStatList')>梦想统计</a></span></li>
						</ul></li>
					<li data-options="state:'closed'"><span>锻炼管理</span>
						<ul>
							<li><span><a onclick=addTab('运动类型管理','../sportType/list')>运动类型管理</a></span></li>
							<li><span><a onclick=addTab('运动里程碑管理','../sportMilestone/list')>运动里程碑管理</a></span></li>
							<li><span><a onclick=addTab('锻炼管理','../sportExercise/list')>锻炼管理</a></span></li>
							<li><span><a onclick=addTab('锻炼统计','../sportExercise/dateStatList')>锻炼统计</a></span></li>
							<li><span><a onclick=addTab('锻炼同期比对','../sportExercise/yoyStatList')>锻炼同期比对</a></span></li>
						</ul></li>
					<li data-options="state:'closed'"><span>健康管理</span>
						<ul>
						<li><span>看病管理</span>
							<ul>
								<li><span><a onclick=addTab('看病记录管理','../treatRecord/list')>看病记录管理</a></span></li>
								<li><span><a onclick=addTab('看病分析','../treatRecord/analyseStatList')>看病分析</a></span></li>
								<li><span><a onclick=addTab('看病统计','../treatRecord/dateStatList')>看病统计</a></span></li>
								<li><span><a onclick=addTab('看病同期比对','../treatRecord/yoyStatList')>看病同期比对</a></span></li>
								<li><span><a onclick=addTab('用药管理','../treatDrug/list')>用药管理</a></span></li>
								<li><span><a onclick=addTab('手术管理','../treatOperation/list')>手术管理</a></span></li>
							</ul>
						</li>
						</ul>
						<ul>
							<li><span>身体基本情况管理</span>
								<ul>
									<li><span><a onclick=addTab('身体基本情况管理','../bodyBasicInfo/list')>身体基本情况管理</a></span></li>
									<li><span><a onclick=addTab('身体基本情况统计','../bodyBasicInfo/dateStatList')>身体基本情况统计</a></span></li>
									<li><span><a onclick=addTab('身体基本情况同期比对','../bodyBasicInfo/yoyStatList')>身体基本情况同期比对</a></span></li>
								</ul>
							</li>
						</ul>
						<ul>
							<li><span>身体不适管理</span>
								<ul>
									<li><span><a onclick=addTab('身体不适管理','../bodyAbnormalRecord/list')>身体不适管理</a></span></li>
									<li><span><a onclick=addTab('身体不适实时分析','../bodyAbnormalRecord/statList')>身体不适实时分析</a></span></li>
									<li><span><a onclick=addTab('身体不适统计','../bodyAbnormalRecord/dateStatList')>身体不适统计</a></span></li>
								</ul>
							</li>
						</ul>
						<ul>
							<li><span>睡眠管理</span>
								<ul>
									<li><span><a onclick=addTab('睡眠管理','../sleep/list')>睡眠管理</a></span></li>
									<li><span><a onclick=addTab('睡眠分析','../sleep/analyseSatList')>睡眠分析</a></span></li>

								</ul>
							</li>
						</ul>
					<li data-options="state:'closed'"><span>人生经历管理</span>
						<ul>
							<li><span><a onclick=addTab('城市位置维护','../cityLocation/list')>城市位置维护</a></span></li>
							<li><span><a onclick=addTab('消费类型管理','../consumeType/list')>消费类型管理</a></span></li>

							<li><span><a onclick=addTab('人生经历管理','../lifeExperience/list')>人生经历管理</a></span></li>
							<li><span><a onclick=addTab('人生经历统计','../lifeExperience/dateStatList')>人生经历统计</a></span></li>
							<li><span><a onclick=addTab('人生经历同期比对','../lifeExperience/yoyStatList')>人生经历同期比对</a></span></li>
							<li><span><a onclick=addTab('人生经历统计地图','../lifeExperience/mapStatList')>人生经历统计地图</a></span></li>
							<li><span><a onclick=addTab('人生经历线路地图','../lifeExperience/transferMapStatList')>人生经历线路地图</a></span></li>
							<li><span><a onclick=addTab('回家次数统计','../backHome/dateStatList')>回家次数统计</a></span></li>

						</ul></li>

					<li data-options="state:'closed'"><span>报表管理</span>
						<ul>
							<li><span>提醒管理</span>
								<ul>
									<li><span><a onclick=addTab('提醒配置模板','../notifyConfig/list')>提醒配置模板</a></span></li>
									<li><span><a onclick=addTab('用户提醒管理','../userNotify/list')>用户提醒管理</a></span></li>
									<li><span><a onclick=addTab('提醒统计','../notifyStat/list')>提醒统计</a></span></li>
								</ul>
							</li>
						</ul>
						<ul>
							<li><span>报表管理</span>
								<ul>
									<li><span><a onclick=addTab('报表配置模板','../reportConfig/list')>报表配置模板</a></span></li>
									<li><span><a onclick=addTab('用户报表配置','../userReportConfig/list')>用户报表配置</a></span></li>
									<li><span><a onclick=addTab('报表统计','../reportStat/list')>报表统计</a></span></li>
								</ul>
							</li>
						</ul>
						<ul>
							<li><span>计划管理</span>
								<ul>
									<li><span><a onclick=addTab('计划配置模板','../planConfig/list')>计划配置模板</a></span></li>
									<li><span><a onclick=addTab('计划执行报告','../planReport/list')>计划执行报告</a></span></li>
									<li><span><a onclick=addTab('计划执行报告平均值','../planReport/avgStatList')>计划执行报告平均值</a></span></li>
									<li><span><a onclick=addTab('计划执行统计','../planReport/dateStatList')>计划执行统计</a></span></li>
									<li><span><a onclick=addTab('用户计划管理','../userPlan/list')>用户计划管理</a></span></li>
									<li><span><a onclick=addTab('用户计划进度统计','../planReport/timelineStatList')>用户计划进度统计</a></span></li>

								</ul>
							</li>
						</ul>
					<li data-options="state:'closed'"><span>日记管理</span>
						<ul>
							<li><span><a onclick=addTab('日记管理','../diary/list')>日记管理</a></span></li>
							<li><span><a onclick=addTab('日记统计','../diary/dateStatList')>日记统计</a></span></li>
						</ul></li>
					<li data-options="state:'closed'"><span>阅读管理</span>
						<ul>
							<li><span><a onclick=addTab('图书分类管理','../bookCategory/list')>图书分类管理</a></span></li>
							<li><span><a onclick=addTab('阅读管理','../readingRecord/list')>阅读管理</a></span></li>
							<li><span><a onclick=addTab('阅读统计','../readingRecord/dateStatList')>阅读统计</a></span></li>
							<li><span><a onclick=addTab('阅读分析','../readingRecord/analyseStatList')>阅读分析</a></span></li>
						</ul></li>
					<li data-options="state:'closed'"><span>工作管理</span>
						<ul>
							<li><span><a onclick=addTab('公司管理','../company/list')>公司管理</a></span></li>
							<li><span><a onclick=addTab('加班记录管理','../workOvertime/list')>加班记录管理</a></span></li>
							<li><span><a onclick=addTab('加班统计','../workOvertime/dateStatList')>加班统计</a></span></li>
							<li><span><a onclick=addTab('出差管理','../businessTrip/list')>出差管理</a></span></li>
						</ul></li>
					<li data-options="state:'closed'"><span>饮食习惯管理</span>
						<ul>
							<li><span><a onclick=addTab('饮食管理','../diet/list')>饮食管理</a></span></li>
							<li><span><a onclick=addTab('饮食习惯统计','../diet/statList')>饮食习惯统计</a></span></li>

						</ul></li>
					<li data-options="state:'closed'"><span>通用记录管理</span>
						<ul>
							<li><span><a onclick=addTab('通用记录类型管理','../commonRecordType/list')>通用记录类型管理</a></span></li>
							<li><span><a onclick=addTab('通用记录管理','../commonRecord/list')>通用记录管理</a></span></li>
							<li><span><a onclick=addTab('通用记录统计','../commonRecord/dateStatList')>通用记录统计</a></span></li>

						</ul></li>
					<li data-options="state:'closed'"><span>数据分析</span>
						<ul>
							<li><span><a onclick=addTab('数据录入分析管理','../dataInputAnalyse/list')>数据录入分析管理</a></span></li>
							<li><span><a onclick=addTab('用户行为模板','../userBehaviorConfig/list')>用户行为模板</a></span></li>
							<li><span><a onclick=addTab('用户行为配置','../userBehavior/list')>用户行为配置</a></span></li>
							<li><span><a onclick=addTab('用户行为分析','../userBehavior/statList')>用户行为分析</a></span></li>
							<li><span><a onclick=addTab('用户操作行为统计','../userBehavior/userOperationStatList')>用户操作行为统计</a></span></li>
							<li><span><a onclick=addTab('用户行为比对','../userBehavior/compareList')>用户行为比对</a></span></li>
							<li><span><a onclick=addTab('八小时外分析','../dataAnalyse/afterEightHourAnalyseStatList')>八小时外分析</a></span></li>
						</ul></li>
					<li data-options="state:'closed'"><span>用户日历</span>
						<ul>
							<li><span><a onclick=addTab('用户日历管理','../userCalendar/list')>用户日历管理</a></span></li>
						</ul></li>
				
			</div>
			<div title="系统管理" style="overflow:auto; padding: 10px;">
				<ul id="menu-tree" class="easyui-tree">
					<li><span>权限管理</span>
						<ul>
							<li><span><a onclick=addTab('用户管理','../user/list')>用户管理</a></span></li>
							<li><span><a onclick=addTab('用户积分记录','../userRewardPointRecord/list')>用户积分记录</a></span></li>
							<li><span><a onclick=addTab('用户积分统计','../userRewardPointRecord/pointsTimelineStatList')>用户积分统计</a></span></li>
							<li><span><a onclick=addTab('功能点管理','../systemFunction/list')>功能点管理</a></span></li>
						</ul>
					</li>
					<li><span>日志管理</span>
						<ul>
							<li><span><a onclick=addTab('操作日志管理','../operationLog/list')>操作日志管理</a></span></li>
							<li><span><a onclick=addTab('系统日志管理','../systemLog/list')>系统日志管理</a></span></li>
							<li><span><a onclick=addTab('用户消息管理','../userMessage/list')>用户消息管理</a></span></li>
						</ul>
					</li>
					<li><span>调度管理</span>
						<ul>
							<li><span><a onclick=addTab('调度管理','../taskTrigger/list')>调度管理</a></span></li>
							<li><span><a onclick=addTab('调度日志管理','../taskLog/list')>调度日志管理</a></span></li>
						</ul>
					</li>
					<li><span>数据库管理</span>
						<ul>
							<li><span><a onclick=addTab('数据库清理','../databaseClean/list')>数据库清理</a></span></li>
						</ul>
					</li>
			</div>
		</div>
	</div>
	<div region="center" id="mainPanle"
		style="background: #eee;overflow:hidden;">
		<div id="tabs" class="easyui-tabs" fit="true" border="false">
			<div title="首页" style="padding: 0px;" id="home">
				<div id="pp" style="position:relative">
					<div style="width:25%;"></div>
					<div style="width:25%;"></div>
					<div style="width:25%;"></div>
					<div style="width:25%;"></div>
				</div>
			</div>
		</div>
	</div>

	<div id="tabsMenu" class="easyui-menu" style="width:120px;">
		<div name="close">关闭</div>
		<div name="Other">关闭其他</div>
		<div name="All">关闭所有</div>
	</div>

	<div id="todayCalendar-window" title="今日行程" class="easyui-window"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div class="yhdiv">
			<form id="todayCalendar-form" method="post">
				<table id="todayCalendarTg" style="width:600px;height: 250px">
				</table>
				<div class="submitForm">
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-cancel" onclick="closeWindow('todayCalendar-window')">关闭</a>
				</div>
			</form>
		</div>
	</div>

	<div id="myInfo-window" title="个人设置" class="easyui-window"
		 data-options="modal:true,closed:true,iconCls:'icon-info'">
		<div class="yhdiv">
			<form id="myInfo-form" method="post">
				<table cellpadding="5" class="tableForm">
					<tr>
						<td>用户名:</td>
						<td><input class="easyui-validatebox" type="text"
								   name="username" style="width:158px;" />
						</td>
					</tr>
					<tr>
						<td>昵称:</td>
						<td><input class="easyui-validatebox" type="text"
								   name="nickname" style="width:158px;" />
						</td>
					</tr>
					<tr>
						<td>密码:</td>
						<td><input class="easyui-passwordbox" type="text" id="password"
								   name="password" style="width:158px;" />
						</td>
					</tr>
					<tr>
						<td>手机:</td>
						<td><input class="easyui-validatebox" type="text"
								   name="phone" style="width:158px;" />
						</td>
					</tr>
					<tr>
						<td>邮箱:</td>
						<td><input class="easyui-validatebox" type="text"
								   name="email" style="width:158px;" />
						</td>
					</tr>
					<tr>
						<td>生日:</td>
						<td><input class="easyui-datebox" name="birthday"
								   data-options="showSeconds:false" style="width:100px"></td>
					</tr>
					<tr>
						<td>发送邮件:</td>
						<td>
							<input name="sendEmail" type="radio" class="easyui-validatebox" checked="checked" required="true" value="true" style="width:30px">是
							<input name="sendEmail" type="radio" class="easyui-validatebox" required="true" value="false" style="width:30px">否
						</td>
					</tr>
					<tr>
						<td>发送微信:</td>
						<td>
							<input name="sendWxMessage" type="radio" class="easyui-validatebox" checked="checked" required="true" value="true" style="width:30px">是
							<input name="sendWxMessage" type="radio" class="easyui-validatebox" required="true" value="false" style="width:30px">否
						</td>
					</tr>
				</table>
				<div class="submitForm">
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-ok" onclick="saveMyInfo()">保存</a>
					<a href="javascript:void(0)" class="easyui-linkbutton"
					   icon="icon-cancel" onclick="closeWindow('myInfo-window')">关闭</a>
				</div>
			</form>
		</div>
	</div>

</body>
<script type="text/javascript">
    $(function () {
        // 绑定tabs的右键菜单
        $("#tabs").tabs({
            onContextMenu : function(e, title) {
                e.preventDefault();
                $('#tabsMenu').menu('show', {
                    left : e.pageX,
                    top : e.pageY
                }).data("tabTitle", title);
            }
        });
        initPanel();

    });



</script>
</html>
