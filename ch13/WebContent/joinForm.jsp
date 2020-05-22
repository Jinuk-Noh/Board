<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%request.setCharacterEncoding("utf-8"); %>
    <%@ include file="color.jspf" %>
    <% 
  String id = request.getParameter("id");
  String pwd = request.getParameter("pwd");

%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
</head>
<link href="style.css" rel="stylesheet" type="text/css">
<body>
<jsp:include page="index.jsp"/>
<!-- 회원가입창 -->
<h2 align="center">회원가입</h2>
<form method="post" name="form" >
<table style="margin-left:auto; margin-right:auto; ">
	<tr>
		<td width="100" bgcolor="<%=value_c%>" align="center">ID</td>
		<td width="330" align="right"><input type="text" size="50"
		maxlength="10" name="id" style="ime-mode: active;">			
	</tr>
	<tr>
		<td width="100" bgcolor="<%=value_c%>" align="center">비밀번호</td>
		<td width="330" align="right"><input type="text" size="50"
		maxlength="10" name="passwd" style="ime-mode: active;"></td>							
	</tr>
	<tr>
		<td width="100" bgcolor="<%=value_c%>" align="center">비밀번호 확인</td>
		<td width="330" align="right"><input type="text" size="50"
		maxlength="10" name="checkPasswd" style="ime-mode: active;"></td>
							
	</tr>
	<tr>
		<td width="100" bgcolor="<%=value_c%>" align="center">이름</td>
		<td width="330" align="right"><input type="text" size="50"
		maxlength="10" name="name" style="ime-mode: active;"></td>					
	</tr>
	<tr>
		<td width="100" bgcolor="<%=value_c%>" align="center">생년 월일</td>
		<td width="330" align="right">		
			<input type="text" size="20" maxlength="4" name="year", 
			value="4자리" onFocus="this.value='';">년
			<select name="month"id="month" style="ime-mode: active; width:75px;">				
		<%
			int i = 0;
			while(i<12){
				i++;
				%>
				<option value="<%=i %>"> <%=i %> </option>
				<%}	
		%>
		</select>월
			<input type="text" style="width:62px" maxlength="2" name="day" >일	
		</td>
		<tr>
		<td width="100" bgcolor="<%=value_c%>" align="center">이메일</td>
		<td width="330" align="right">
		<input type="text" name="email1" maxlength="21" style="ime-mode: active; width :150px;"> @
		<select name="email2" style ="width : 138px;">
			<option>naver.com</option>
			<option>daum.net</option>
			<option>gmail.com</option>
			<option>nate.com</option>                        
		</select>
		</td>					
	</tr>
	<tr>
		<td width="100" bgcolor="<%=value_c%>" align="center">주소</td>
		<td width="330" align="right"><input type="text" size="50"
		maxlength="10" name="address" style="ime-mode: active;"></td>					
	</tr>
	<tr>
		<td width="100" bgcolor="<%=value_c%>" align="center">휴대폰번호</td>
		<td width="330" align="right"><input type="text" size="50"
		maxlength="10" name="phoneNumber" style="ime-mode: active;" value = "-없이 입력" onFocus="this.value='';"></td>					
	</tr>
	<tr>
	<td colspan="2" bgcolor="<%=value_c%>" align="right">
	<input type="submit" value="등록"onclick="javascript:form.action='joinPro.do';"/>	
	<input type=button value="취소" onclick="location.href='login.jsp'">
	</td>
</table>
</form>
</body>
</html>