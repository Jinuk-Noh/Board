<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<% request.setCharacterEncoding("UTF-8"); %>

<%
String content = (String) request.getAttribute("content");
String subject = (String) request.getAttribute("subject");
String passwd = (String) request.getAttribute("passwd");

if(content.equals("")||subject.equals("")||passwd.equals("")){
%>
<script>
alert("제목 또는 글, 비밀번호를 작성해주세요");
history.go(-1);
</script>
<%
}else{
response.sendRedirect("list.jsp");
}
%>






















