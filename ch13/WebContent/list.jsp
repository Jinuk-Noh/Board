<%@page import="java.util.List"%>
<%@page import="ch13.model.BoardDBBean"%>
<%@page import="ch13.model.BoardDataBean"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="color.jspf"%>
<link href="style.css" rel="stylesheet" type="text/css">
<%!int pageSize = 5;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");%>

<jsp:include page="index.jsp" />
<%
	request.setCharacterEncoding("utf-8");
	String search = request.getParameter("search");
	String searchType = request.getParameter("searchType");
	if (searchType == null) {
		searchType = "option";
	}
	if (search == null) {
		search = "";
	}
%>

<%
	String pageNum = request.getParameter("pageNum");
	if (pageNum == null) {
		pageNum = "1";
	}
	int currentPage = Integer.parseInt(pageNum); // 1  // 2
	int startRow = (currentPage - 1) * pageSize + 1; // 1  // 11
	int endRow = currentPage * pageSize; //10  //20
	int count = 0;
	int number = 0;
	List<BoardDataBean> articleList = null;

	BoardDBBean dbPro = BoardDBBean.getInstance();

	if (searchType.equals("option") || search.equals("")) {
		count = dbPro.getArticleCount(); //16
		if (count > 0) {
			//  mysql 의   limit (시작하는index, 몇개)
			// articleList = dbPro.getArticles(startRow, pageSize);
			// ejkim.m for oracle
			// oracle 은  runum (시작하는index, 끝나는 index)

			articleList = dbPro.getArticles(startRow, endRow);
		}
	} else {
		count = dbPro.getArticleCount(searchType, search);
		if (count > 0) {
			articleList = dbPro.getArticles(startRow, endRow, searchType, search);
		}
	}

	number = count - (currentPage - 1) * pageSize;
%>
<html>
<head>
<link href="style.css?after" rel="stylesheet" type="text/css">
<title>게시판</title>
</head>
<body>

	<!-- <div class="container"> -->
	<div>
		<table>
			<%
				if (session.getAttribute("loginId") != null) {
			%>
			<tr>
				<td align="right" bgcolor="<%=value_c%>"><a href="write.do">글쓰기</a>
				</td>
			</tr>
			<%
				}
			%>
		</table>

		<%
			if (count == 0) {
		%>

		<table>
			<tr>
				<td align="center">게시판에 저장된 글이 없습니다.</td>
		</table>

		<%
			} else {
		%>
		<table class="table table-hover">
			<thead>
				<tr height="30" bgcolor="<%=value_c%>">
					<th align="center" width="50">번 호</th>
					<th align="center" width="250">제 목</th>
					<th align="center" width="100">작성자</th>
					<th align="center" width="150">작성일</th>
					<th align="center" width="50">조 회</th>
					<th align="center" width="100">IP</th>
				</tr>
			</thead>
			<%
				for (int i = 0; i < articleList.size(); i++) {
						BoardDataBean article = articleList.get(i);
			%>
			<tr height="30">
				<td width="50"><%=number--%></td>
				<td width="250" align="left">
					<%
						int wid = 0;
								if (article.getRe_level() > 0) {
									wid = 5 * (article.getRe_level());
					%> <img src="reply.png.png?a" width="<%=wid%>" height="16">
					<img src="reply.png?a" height="10px"> <%
 	} else {
 %> <img src="reply.png" width="<%=wid%>" height="16"> <%
 	}
 %> <a
					href="content.do?num=<%=article.getNum()%>&pageNum=<%=currentPage%>">
						<%=article.getSubject()%></a> <%
 	if (article.getReadcount() >= 20) {
 %> <img src="images/hot.gif?a" border="0" height="16"> <%
 	}
 %>
				</td>
				<td width="100" align="left"><a
					href="mailto:<%=article.getEmail()%>"> <%=article.getWriter()%></a></td>
				<td width="150"><%=sdf.format(article.getReg_date())%></td>
				<td width="50"><%=article.getReadcount()%></td>
				<td width="100"><%=article.getIp()%></td>
			</tr>
			<%
				}
			%>
		</table>

		<%
			}
		%>

		<%
			if (count > 0) {
				int pageCount = count / pageSize + (count % pageSize == 0 ? 0 : 1);
				int startPage = 1;

				if (currentPage % 10 != 0)
					startPage = (int) (currentPage / 10) * 10 + 1;
				else
					startPage = ((int) (currentPage / 10) - 1) * 10 + 1;

				int pageBlock = 10;
				int endPage = startPage + pageBlock - 1;
				if (endPage > pageCount)
					endPage = pageCount;

				if (startPage > 10) {
		%>
		<a href="list.do?searchType=<%=searchType%>&search=<%=search%>&pageNum=<%=startPage - 10%>">[이전]</a>
		<%
			}

				for (int i = startPage; i <= endPage; i++) {
		%>
		<a href="list.do?searchType=<%=searchType%>&search=<%=search%>&pageNum=<%=i%>">[<%=i%>]
		</a>
		<%
			}

				if (endPage < pageCount) {
		%>
		<a href="list.do?searchType=<%=searchType%>&search=<%=search%>&pageNum=<%=startPage + 10%>">[다음]</a>
		<%
			}
			}
		%>
	</div>
	<br>
	<br>
	<p>
		글목록(전체 글:<%=count%>)
	</p>
</body>
</html>