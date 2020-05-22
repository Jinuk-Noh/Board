<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>   
<%@ include file="color.jspf" %>
<%
String id = (String)session.getAttribute("loginId");
%>

<% 
  int num = 0, ref = 1, re_step = 0, re_level = 0;
  String strV = "";
  try{
    if(request.getParameter("num")!=null){
	   num=Integer.parseInt(request.getParameter("num"));
	   ref=Integer.parseInt(request.getParameter("ref"));
	   re_step=Integer.parseInt(request.getParameter("re_step"));
	   re_level=Integer.parseInt(request.getParameter("re_level"));
    }
%>

<!DOCTYPE html>
<html>
<head>
<style>

</style>
<meta charset="UTF-8">
<title>게시판</title>

<link href="style.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="script.js"></script>
</head>
<body bgcolor="<%=bodyback_c%>">
<jsp:include page="index.jsp"/>
<p align ="center">글쓰기</p>

	<form method="post" action="writePro.do" enctype="multipart/form-data">
		<input type="hidden" name="num" value="<%=num%>"> <input
			type="hidden" name="ref" value="<%=ref%>"> <input
			type="hidden" name="re_step" value="<%=re_step%>"> <input
			type="hidden" name="re_level" value="<%=re_level%>">
		<table  style="margin-left:auto; margin-right:auto;">
			<tr>
				<td align="right" colspan="2" bgcolor="<%=value_c%>"><a
					href="list.jsp"> 글목록</a></td>
			</tr>
			<tr height="30" >
				<td width="70" bgcolor="<%=value_c%>" align="center">이 름</td>
				<td align="center"><%=id %>
				<input type="hidden" name="writer" style="ime-mode: active;" value=<%=id %>></td>					
				<!--active:한글-->
			</tr>
			<tr height="30">
				<td width="70" bgcolor="<%=value_c%>" align="center">제 목</td>
				<td align="left"><input type="text" size="60"
					maxlength="50" name="subject" value="<%=strV%>"
					style="ime-mode: active;"></td>
			</tr>
			<tr height="30">
				<td width="70" bgcolor="<%=value_c%>" align="center">Email</td>
				<td align="left"><input type="text" size="60"
					maxlength="30" name="email" style="ime-mode: inactive;"></td>
				<!--inactive:영문-->
			</tr>
			<tr height="400">
				<td width="70" bgcolor="<%=value_c%>" align="center">내 용</td>
				<td width="375" align="left"><textarea name="content" rows="20"
						cols="60" style="ime-mode: active;"></textarea></td>
			</tr>
			<tr height="30">
				<td width="70" bgcolor="<%=value_c%>" align="center">파일선택</td>
				<td width="330" align="left"><input type="file" 
						name="selectfile"></td>
			</tr>
			<tr height="30">
				<td width="70" bgcolor="<%=value_c%>" align="center">비밀번호</td>
				<td width="330" align="left"><input type="password" size="8"
					maxlength="12" name="passwd" style="ime-mode: inactive;"></td>
			</tr>
			<tr height="30">
				<td colspan=2 bgcolor="<%=value_c%>" align="center">
				<input type="submit" value="글쓰기">
				 <input type="reset" value="다시작성"> 
				 <input type="button" value="목록보기"	OnClick="window.location='list.jsp'"></td>
			</tr>
		</table>
	</form>
</body>
</html>
 <%
  } catch(Exception e){
	  e.printStackTrace();
  }
%> 