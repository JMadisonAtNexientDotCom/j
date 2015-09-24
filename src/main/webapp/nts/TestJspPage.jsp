<jsp:include page="../dummys/htmlFrame/head_beforeTitle.html" />
<title>TITLE OF PAGE</title>
<jsp:include page="../dummys/htmlFrame/head_afterTitle.html" />
<p> hello world! </p>
<jsp:include page="../dummys/htmlFrame/app_controller_divider.html" />

<!-- try including these imports above RestServiceUtil and see what happens -->
<jsp:include page="../dummys/jspJavaClassImports/JavaImports.jsp" />
serviceURL = <%= RestServiceUtil.getServiceURL(RSIDS.ADMIN) %>

<jsp:include page="../dummys/htmlFrame/foot.html" />



   
   

