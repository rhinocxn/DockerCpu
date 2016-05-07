<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="CPUPerformance.*" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%
String rate = request.getParameter("value");
 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>调整CPU消耗</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
    CPU使用率设置为<%=rate %>%. <br>
  </body>
  <%
  /* if(busyTime <= 100 && busyTime >= 0){
		while (true) 
		{ 
		    startTime = System.currentTimeMillis(); 
		    while (System.currentTimeMillis() - startTime <= busyTime) 
		        try 
		    { 
		        Thread.sleep(idleTime); 
		    } 
		    catch (InterruptedException e) 
		    { 
		        e.printStackTrace(); 
		      }
		} 
	}  */
	/* IMonitorService service = new MonitorServiceImpl();
    MonitorInfoBean monitorInfo = service.getMonitorInfoBean();
	while(true){
		if(monitorInfo.getCpuRatio() > 50){
			Thread.sleep(10);
		}
	} */
	MonitorServiceImpl service = new MonitorServiceImpl();
	service.setCPUrate(Integer.valueOf(rate).intValue());
 	%>
</html>
