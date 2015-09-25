<jsp:include page="../dummys/htmlFrame/head_beforeTitle.html" />
<title>TITLE OF PAGE</title>
<jsp:include page="../dummys/htmlFrame/head_afterTitle.html" />
<p> hello world! </p>
<jsp:include page="../dummys/htmlFrame/app_controller_divider.html" />




serviceURL = <%= RestServiceUtil.getServiceURL(RSIDS.ADMIN) %>

<jsp:include page="../dummys/htmlFrame/foot.html" />

<%= VARNAME.TOKEN_ID %>
<%= VARNAME.NINJA_ID %>
<%= VARNAME.ADMIN_ID %>
<%= VARNAME.ADMIN_ID %>


<!-- as much as I would like, you cannot put these imports into ONE .jsp     -->
<!-- and then "jsp:include page" them. Wont work. Tried.                     -->
<%@ page import="test.config.debug.DebugConfig" %>
<%@ page import="test.servlets.rest.util.RestServiceUtil" %>
<%@ page import="test.servlets.rest.util.RSIDS" %>
<%@ page import="test.servlets.rest.util.VARNAME" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>


   
   

