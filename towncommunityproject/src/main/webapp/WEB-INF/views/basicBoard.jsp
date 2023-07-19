<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>글 게시판 화면</title>
<script src="/js/jquery-3.6.4.min.js"></script>
<link rel="stylesheet" type="text/css" href="/css/basicBoard.css"> 
</head>
<body>
<jsp:include page="Header.jsp" />
<div id="board_layout">
	<jsp:include page="boardMenu.jsp" />
	<div id="board_main">
		<div id="board_name">
			<span>${empty param.ctgy ? "공지사항" : param.ctgy}</span>
		</div>
		<div id="top_btnBox">
			<input type="button" id="writeBtn" value="글쓰기">
		</div>
		
		<div id="board_page">
			<table id="board-table">
				<thead>
					<tr>
						<th>글번호</th>
						<th>제목</th>
						<th>작성자</th>
						<th>작성일</th>
						<th>조회수</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="boardlist" items="${boardlist}" varStatus="vs">
						<tr>
							<td>
								${empty param.page || param.page == 1 
								? totalPostCnt - vs.index
								: totalPostCnt - param.page * 10 - vs.index}
							</td>
							<td><a href="/boarddetail?bi=${boardlist.board_id}" class="writing-title">${boardlist.board_title}</a></td>
							<td>${boardlist.writer}</td>
							<td>${fn:split(boardlist.writing_time, " ")[0]}</td>
							<td>${boardlist.view_cnt}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		
		<div id="board_pagingNum">
			<div id="pagination">
				<c:if test="${prev}">
					<input type="button" class="pageNumBtn" value="◁◁">
					<input type="button" class="pageNumBtn" value="◁">
				</c:if>
				
				<c:forEach begin="${startPageNum}" end="${endPageNum}" var="i">
					<c:if test="${selectedPageNum != i}">
						<input type="button" class="pageNumBtn" value="${i}" style="font-weight: 300; color: #555">
					</c:if>
					<c:if test="${selectedPageNum == i}">
						<input type="button" class="pageNumBtn" value="${i}" style="font-weight: 900">
					</c:if>
				</c:forEach>
				<c:if test="${next}">
					<input type="button" class="pageNumBtn" value="▷">
					<input type="button" class="pageNumBtn" value="▷▷">
				</c:if>
			</div>
		</div>
		
		<!-- 검색창 -->
		<div id="board_search_box">
			<div id="board_search_inputs">
			  <select id="selectBox" name="board_search_from">
					<option value="board_all"> 전체 </option>
					<option value="board_title"> 제목 </option>
					<option value="board_contents"> 내용 </option>
					<option value="writer"> 작성자 </option>
	      </select>
				<input type=text id="board_searchword" >
				<input type=hidden id="search_ctgy" value="${boardName}">
				<input type=hidden id="search_ti" value="${town_id}">
				<input type=button id="board_search_btn" value="검색">
			</div> 
		</div>
		
		<script>
		$(document).ready(function() {
			// page 이동 버튼 클릭시 동작
			$("#pagination input:button").on("click", function(e) {
				let url = document.location.href;
				let pageIndex = url.indexOf("&page=");
				if (pageIndex > -1) {
					url = url.substr(0, pageIndex);
				}
				let pageval = $(this).val();
				let page = "$page=1";
				if (pageval === "◁◁") {
					page = "&page=1";
				} else if (pageval === "◁") {
					page = "&page=${startPageNum - 10}";
				} else if (pageval === "▷") {	
					page = "&page=${endPageNum + 1}";
				} else if (pageval === "▷▷") {
					page = "&page=${totalPageCnt}";
				} else if (pageval <= parseInt("${totalPageCnt}") && pageval >= 1) {
					page = "&page=" + pageval;
				}
				window.location.href = url + page;
			}); //onclick
			
			// 게시글 검색 버튼 클릭
			$("#board_search_btn").on("click", function() {
				let ctgy = "${param.ctgy}";
				let sort = $("select[name=board_search_form]").val();
				let keyword = $("#board_searchword").val();
				location.href = "/basicBoard?ctgy=" + ctgy + "&ti=${param.ti}" + "&sort=" + sort + "&keyword=" + keyword;
			}); //onclick
		}); //ready
		</script>
		
	</div>
</div>
</body>
</html>
