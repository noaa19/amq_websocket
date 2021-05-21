<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<meta http-equiv="Content-Type" content="text/html;  charset=utf8";>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>websocket client</title>
</head>
<script type="text/javascript">
	var ws;
	ws = new WebSocket("ws://localhost:8080/amqDataServer/websocketServer");
    ws.onmessage = function(evn){
    	console.log("收到数据");
        console.log(evn.data);
        var dv = document.getElementById("dv");
         dv.innerHTML+=evn.data;
    };
	
</script>
<body>
	<h2>Hello World!</h2>
	<div id="dv" />
</body>
</html>