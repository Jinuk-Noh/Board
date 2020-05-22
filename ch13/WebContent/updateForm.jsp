<%@page import="ch13.model.BoardDataBean"%>
<%@page import="ch13.model.BoardDBBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="color.jspf"%>

<!DOCTYPE html>
<html>
<head>
<title>게시판</title>
<link href="style.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="script.js"></script>
</head>

<body>
<jsp:include page="index.jsp"/>
<%
  String id = (String)session.getAttribute("loginId");

  int num =Integer.parseInt(request.getParameter("num"));
  String pageNum =request.getParameter("pageNum");

 try{
  	BoardDBBean dbPro = BoardDBBean.getInstance();
  	BoardDataBean article = dbPro.updateGetArticle(num);
  	
 
 
  	%>	
<p align ="center">글수정</p>
<form method="post" name="writeform" 
action="updatePro.do?pageNum=<%=pageNum%>" onsubmit="return writeSave()"
 enctype="multipart/form-data">
<table style="margin-left:auto; margin-right:auto;">
  <tr>
	<td align="right" colspan="2" bgcolor="<%=value_c%>"><a href="list.jsp"> 글목록</a>
	</td>
  </tr>
  <tr height="30">
    <td  width="70"  bgcolor="<%=value_c%>" align="center">이 름</td>
    <td align="center" width="330"><%=article.getWriter()%>
       <input type="hidden" name="writer" value="<%=article.getWriter()%>" style="ime-mode:active;">
	   <input type="hidden" name="num" value="<%=article.getNum()%>"></td>
  </tr>
  <tr height="30">
    <td  width="70"  bgcolor="<%=value_c%>" align="center" >제 목</td>
    <td align="left" width="330">
       <input type="text" size="60" maxlength="50" name="subject"
        value="<%=article.getSubject()%>" style="ime-mode:active;"></td>
  </tr>
  <tr height="30">
    <td  width="70"  bgcolor="<%=value_c%>" align="center">Email</td>
    <td align="left" width="330">
       <input type="text" size="60" maxlength="30" name="email" 
        value="<%=article.getEmail()%>" style="ime-mode:inactive;"></td>
  </tr>
  <tr>
    <td  height="400" width="70"  bgcolor="<%=value_c%>" align="center" >내 용</td>
    <td align="left" width="330">
     <textarea name="content" rows="20" cols="60" 
       style="ime-mode:active;"><%=article.getContent()%></textarea></td>
  </tr>
  <!-- ejkim.a for file upload -->
  <tr height="30">
    <td  width="70"  bgcolor="<%=value_c%>" align="center">파일선택</td>
    <td align="left" width="330"><input type="file" name="filename">    
   <%
   String filename = article.getFileName();
   if( filename !=null && !filename.equals("")) { %>
	<a href="javascript:download('<%=filename %>')">
	<%=filename %>
	</a>
    <%} else { %>
     파일이 없습니다.
    <%}  %>       
       </td>
  </tr>
  <tr height="30">
    <td  width="70"  bgcolor="<%=value_c%>" align="center" >비밀번호</td>
    <td align="left" width="330" >
     <input type="password" size="8" maxlength="12" 
               name="passwd" style="ime-mode:inactive;">
     
	 </td>
  </tr>
  <tr height="30">      
   <td colspan=2 bgcolor="<%=value_c%>" align="center"> 
     <input type="submit" value="글수정" >  
     <input type="reset" value="다시작성">
     <input type="button" value="목록보기" 
       onclick="document.location.href='list.jsp?pageNum=<%=pageNum%>'">
   </td>
 </tr>
 </table>
 <input type="hidden">
</form>
<%
	
}catch(Exception e){}%>      
      
</body>
</html>