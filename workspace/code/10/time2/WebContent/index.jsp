<%@ page language="java" pageEncoding="GBK"%>
<%@ taglib uri="http://www.abc.com/ns/mytags" prefix="t"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>show time</title>
</head>
<body>
	<p>
		<%-- 此处提供了脚本变量的名称和类型  --%>
		<t:time color="red" format="yyyy-MM-dd HH:mm:ss" var="d"
			type="java.util.Date" />
	<p>
		<%-- 访问该脚本变量，输出其值 --%>
		<%= d %>  
		
		<%-- 这个脚本变量会出错误提示，可以在Preferences->Validation->JSP Syntax Validation中将验证勾选掉，不进行验证则可以忽略掉这个错误检查 --%>
</body>
</html>
</html>