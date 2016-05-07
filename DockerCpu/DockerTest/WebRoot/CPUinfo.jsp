<%@ page language="java" import="CPUPerformance.*" pageEncoding="utf-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <base href="<%=basePath%>">

        <title>运行状态</title>

        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">
        <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
        <meta http-equiv="description" content="This is my page">
        <meta http-equiv="refresh" content="5">

    </head>

    <body>
        <%
            IMonitorService service = new MonitorServiceImpl();
            MonitorInfoBean monitorInfo = service.getMonitorInfoBean();
            out.println("cpu占有率=" + monitorInfo.getCpuRatio() + "<br>");
        %>

        cpu:
        <table width="150" style="border: 1px solid #00FF00;">
            <tr>
                <td>
                    <table width="150" bgcolor="#FFFFFF" cellpadding="0"
                        cellspacing="0">
                        <tr>
                            <td width="<%=monitorInfo.getCpuRatio()%>%" bgcolor="#00FF00"
                                align="center">
                                <%=(int) (monitorInfo.getCpuRatio())%>%
                            </td>
                            <td bgcolor="#FFFFFF">

                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </body>
</html>