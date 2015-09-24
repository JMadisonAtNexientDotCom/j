<%-- 
    Document   : TestJspPage
    Created on : Sep 24, 2015, 1:54:52 PM
    Author     : jmadison
--%>

<%@ page import="test.config.debug.DebugConfig" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JSP Page</title>
  
  </head>
  <body>
    <h1>Hello World!</h1>
    <p> <%= DebugConfig.isDebugBuild %> </p>
  </body>
</html>
