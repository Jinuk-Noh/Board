<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="color.jspf" %>

    
<!DOCTYPE html>
<html>
<head>
<style>
nav{
	margin-bottom: 50px;
}

body{
	background-image:url('bg1-1.jpg');
	background-attachment: fixed;
	background-repeat: no-repeat;
	background-size: 110%;
	background-position: bottom;
	
}

</style>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js" integrity="sha384-OgVRvuATP1z7JjHLkuOU7Xw704+h835Lr+6QL9UvYjZE3Ipu6Tp75j7Bh/kR0JKI" crossorigin="anonymous"></script>
<meta charset="UTF-8">
<title>게시판</title>

</head>

<body>
<nav class="navbar navbar-expand-lg navbar-light bg-light">
  <%if(session.getAttribute("loginId")==null){ %>
  <a class="navbar-brand">환영합니다!</a>
  <%} else{
	  %>
	  <a class="navbar-brand"><%=session.getAttribute("loginId")%>님 환영합니다!</a>
  <%} %>

  <div class="collapse navbar-collapse" id="navbarSupportedContent">
    <ul class="navbar-nav mr-auto">
      <li class="nav-item">
       <%if(session.getAttribute("loginId")==null){ %>
        <a class="nav-link" href="login.jsp">로그인</a>
         <%} else{
	  %>
	    <a class="nav-link" href="logoutPro.jsp">로그아웃</a>
	  <%} %>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="list.jsp">게시판</a>
      </li>            
    </ul>
    <form class="form-inline my-2 my-lg-0" action="list.jsp" method="get">
   
	  <select name ="searchType" class="custom-select" id="inputGroupSelect01">
	    <option value="option" selected>검색</option>
	    <option value="num">글 번호</option>
	    <option value="writer">작성자</option>
	    <option value="subject">글 제목</option>
	  </select>
      <input name="search" class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search">
      <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
    </form>
  </div>
</nav>


</body>
</html>