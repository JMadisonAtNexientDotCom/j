<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>TEST PAGE!</title>
    <%= I.INCLUDE_CSS %>
    <%= I.INCLUDE_JS %>
  </head>
<!-- AUTHOR: JMadison.  ON:20##.##.##_####AMPM                               -->
<!-- TABSIZE: 2 Spaces.                                                      -->
<!-- This header belongs BELOW the <head></head> declaration.                -->
<!--   10|       20|       30|       40|       50|       60|       70|       -->
<!--5678901234567890123456789012345678901234567890123456789012345678901234567-->

  <body>
    <p> varname: <%=I.V.ADMIN_ID%> </p>
    <p> varname: <%=I.V.NINJA_ID%> </p>
    <p> varname: <%=I.VARNAME.TOKEN_ID%> </p>
    <p> TOKEN      == <%=I.REST_SERVICE_URL.TOKEN%></p>
    <p> ADMIN      == <%=I.REST_SERVICE_URL.ADMIN%></p>
    <p> NINJA      == <%=I.REST_SERVICE_URL.NINJA%></p>
    <p> OWNER      == <%=I.REST_SERVICE_URL.OWNER%></p>
    <p> FILE       == <%=I.REST_SERVICE_URL.FILE  %></p>
    <p> RIDDLERHYME== <%=I.REST_SERVICE_URL.RIDDLERHYME%></p>
    <p> TRANSDEBUG == <%=I.REST_SERVICE_URL.TRANSDEBUG%></p>
  </body>
</html>
<!--   10|       20|       30|       40|       50|       60|       70|       -->
<!--5678901234567890123456789012345678901234567890123456789012345678901234567-->

<%-- This .JSP page should basically be a normal .JSP page except for this  --%>
<%-- ONE AND ONLY IMPORT and the references to it.                          --%>
<%@ page import="frontEndBackEndIntegration.I" %>