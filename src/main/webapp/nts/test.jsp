<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>TEST PAGE!</title>
    <%= I.INCLUDE_CSS() %>
    <%= I.INCLUDE_JS() %>
  </head>
<!-- AUTHOR: JMadison.  ON:20##.##.##_####AMPM                               -->
<!-- TABSIZE: 2 Spaces.                                                      -->
<!-- This header belongs BELOW the <head></head> declaration.                -->
<!--   10|       20|       30|       40|       50|       60|       70|       -->
<!--5678901234567890123456789012345678901234567890123456789012345678901234567-->

  <body>
    
    <%--   Doing away with direct access to var names like this. 
    <p> varname: <%=I.V().ADMIN_ID%> </p>
    <p> varname: <%=I.V().NINJA_ID%> </p>
    <p> varname: <%=I.GET_VARIABLE_NAMES().TOKEN_ID%> </p>
    --%>
    
    <p> TOKEN      == <%=I.R().TOKEN.BASE%></p>
    <p> ADMIN      == <%=I.GET_REST_SERVICE_URLS().ADMIN.BASE%></p>
    <p> NINJA      == <%=I.R().NINJA.BASE%></p>
    <p> OWNER      == <%=I.R().OWNER.BASE%></p>
    <p> FILE       == <%=I.R().FILE.BASE  %></p>
    <p> RIDDLERHYME== <%=I.R().RIDDLERHYME.BASE%></p>
    <p> TRANSDEBUG == <%=I.R().TRANSDEBUG.BASE%></p>
    
  </body>
</html>
<!--   10|       20|       30|       40|       50|       60|       70|       -->
<!--5678901234567890123456789012345678901234567890123456789012345678901234567-->

<%-- This .JSP page should basically be a normal .JSP page except for this  --%>
<%-- ONE AND ONLY IMPORT and the references to it.                          --%>
<%@ page import="frontEndBackEndIntegration.I" %>