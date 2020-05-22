<%@page import="ch13.model.BoardDBBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("utf-8");%>

<%
//   int num = Integer.parseInt(request.getParameter("num"));
//   String pageNum = request.getParameter("pageNum");
//   String passwd = request.getParameter("passwd");

//   BoardDBBean dbPro = BoardDBBean.getInstance(); 
//   int check = dbPro.deleteArticle(num, passwd);

	String pageNum = (String)request.getAttribute("pageNum");
	int check = (int)request.getAttribute("check");
	
  if(check==1){
%>
	<meta http-equiv="Refresh" content="0;url=list.do?pageNum=<%=pageNum%>">
<%}else{%>
    <script type="text/javascript">      
      
         alert("비밀번호가 맞지 않습니다");
         history.go(-1);
     
   </script>
<%} %>