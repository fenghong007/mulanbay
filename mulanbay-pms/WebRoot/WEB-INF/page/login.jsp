<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
<head>
<title>用户登陆</title>
</head>
<%@ include file="/static/include/header.jsp"%>
<script type="text/javascript">
	function doLogin(){
		var postData = {
            username: $("#username").val(),
            password: $("#password").val()
        };
        var vurl ='../main/loginAuth';
    	doAjax(postData,vurl,'GET',true,function(data){
    		showProgress('验证成功，正在进入主界面');
			window.location.href='../main/main';
    	});
	}
</script>
<body class="easyui-layout" data-options="fit:true,border:false">

	<div id="win" class="easyui-window" title="木兰湾管理系统"
		 data-options="modal:true,closed:false,iconCls:'icon-info'"
		style="width:300px;height:180px;">
		<form style="padding:10px 20px 10px 40px;" method="post">
			<p>
				用户名称:&nbsp;&nbsp; <input type="text" id="username" name="username"
					data-options="required:true" style="width:120px;"
					class="easyui-validatebox" missingMessage="请输入用户名或手机号"
					onKeyDown="if(event.keyCode==13){$('#password').focus();;}">
			</p>
			<p>
				登录密码: &nbsp;&nbsp;<input type="password" id="password" name="password"
					data-options="required:true" style="width:120px;"
					class="easyui-validatebox" missingMessage="请输入密码"
					onKeyDown="if(event.keyCode==13){doLogin();}">
			</p>
			<div style="padding:5px;text-align:center;">
				<a href="#" class="easyui-linkbutton" icon="icon-ok"
					onclick="doLogin()">登陆</a>
				<a href="../main/register" class="easyui-linkbutton" icon="icon-ok">注册</a>
			</div>
		</form>
	</div>

</body>
</html>
