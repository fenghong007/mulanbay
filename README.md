# mulanbay
木兰湾管理系统，提供消费管理、健康管理、锻炼管理等

# 项目介绍

# 使用到的技术
后端：Hibernate,Spring MVC,Spring,Quartz <br> 
前端：EasyUI，Echarts <br> 
前后端没有分离，前端以jsp页面展示

# 部署所需软件
Mysql 5.7+,JDK 1.8+,Redis 4.0+,Tomcat 8+.

# 部署方式
step1：创建mysql数据库，执行子工程mulanbay-pms/src/docs下的两个sql文件进行初始化数据库<br> 
step2：修改mulanbay-pms/src/main/resources/app.properties或者在该目录下新建app-local.properties文件（本地文件），
       修改数据库连接信息：db.url、db.username、db.password<br> 
step3：利用maven打包整个功能，复制mulanbay-pms/target目录下的mulanbay-pms-1.0.war到tomcat/webapps目录,重新命名为ROOT.war（静态资源路径采用        了绝对路径）

# 演示地址
http://47.98.237.160/  账号密码：test / 123456 

# 业务流程
<img src="http://chuantu.biz/t6/323/1528013892x-1404755546.png"/>

# 微信提醒功能
<table>
    <tr>
        <td>制定计划后，通过每天的调度器自动统计计划执行情况，通过微信模板发送给用户</td>
        <td>根据计划执行情况，每天统计出今日任务。</td>
    </tr>
    <tr>
        <td><img src="http://chuantu.biz/t6/323/1528015149x-1404755546.png"/></td>
        <td><img src="http://chuantu.biz/t6/323/1528015269x-1404755546.png"/></td>
    </tr>
</table>

# 项目截图

首页
<img src="http://chuantu.biz/t6/323/1528013585x-1404817581.png"/>

睡眠分析
<img src="http://chuantu.biz/t6/323/1528014018x-1404755546.png"/>

身体情况统计
<img src="http://chuantu.biz/t6/323/1528014047x-1404755546.png"/>

行为分析
<img src="http://chuantu.biz/t6/323/1528014071x-1404755546.png"/>

调度管理
<img src="http://chuantu.biz/t6/323/1528014099x-1404755546.png"/>

