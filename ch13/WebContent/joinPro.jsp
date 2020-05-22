<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="java.sql.*" %>
    <%request.setCharacterEncoding("utf-8"); %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>등록</title>
</head>
<body>
<%
int check = (int)request.getAttribute("check");
if(check == 1){			
%>
<script>
alert("이미 사용중인 아이디입니다.");
location.href="joinForm.jsp";
</script>
<%										
}
if(check == -2){
%>
	<script>
	alert("이름,비밀번호, ID가 입력되지 않았습니다.");
	location.href="joinForm.jsp";
	</script>
<%				
}
if(check == -1){
%>
	<script>
	alert("입력한 비밀번호가 일치하지 않습니다.");
	location.href="joinForm.jsp";
	</script>
<%		
}
if(check == 0){
%>
	<script>
	alert("가입에 성공하였습니다.");
	location.href = "login.jsp";
	</script>																									
<%		
}		
%>


</body>
</html>