<jsp:include page="../dummys/htmlFrame/head.html" />
<p> hello world! </p>
<jsp:include page="../dummys/htmlFrame/foot.html" />

<!-- Putting imports at BOTTOM so that the injected html template will start -->
<!-- on the first line of the document. -->
<%@ page import="test.config.debug.DebugConfig" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
