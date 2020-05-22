<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String id = (String)session.getAttribute("loginId");

%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MEMBER SIGN UP</title>
</head>
<body> 
<jsp:include page="index.jsp"/>  
	<form name="login" action="loginPro.do" method="post">
		<table border=1 style="margin-left:auto; margin-right:auto;">
			<tr>
				<td colspan="2" align="center"><b><font size=5>LOGIN</font></b></td>
			</tr>
			<tr align="center">
				<td>ID :</td>
				<td><input type="text" name="id"></td>
			</tr>
			<tr align="center">
				<td>PASSWORD :</td>
				<td><input type="password" name="passwd"></td>
			</tr>
			<tr>
				<td colspan="2"  align="center">
					<input type="submit" value="login">
					<input type="button" onclick="location.href='joinForm.jsp'" value="sign up">
				</td>
			</tr>
		</table>
	</form>
	

</body>
</html>