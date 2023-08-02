<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>동네일보 관리자 페이지</title>
<script src="/js/jquery-3.6.4.min.js"></script>
<link rel="stylesheet" href="/css/BoardCommon.css" />
<link rel="stylesheet" href="/css/AdminListPage.css" />

<style>
.admin_group {
	font-size : 21px;
	border : none;
	background-color : transparent;	
	cursor: pointer;
}
.moreDetailBtn{
	cursor: pointer;	
}
#board-table td{
	font-size : 15px;
}
.detail_wrap_box {
	width : 1100px;
	margin-left : 20px;
}
.grid_container{
    display: grid;
    max-width: 1100px;
    /* margin: 0 auto; */
    
    grid-template-columns: 0.13fr 1fr;
    grid-gap: 10px;
    grid-auto-rows: minmax(20px, auto);
}

.grid_container>div{
	/* border : 1px solid black; */
    font-size : 16px;
}
.one, .three {
	border-right: 1px solid  gray;
}
.two, .four  {
	padding-left : 5px;
}
.five{
    grid-column: 1/3;
    grid-row:3;
}
.six{
    grid-column: 1/3;
    grid-row:4;
    padding-top : 10px;
    padding-bottom : 10px;
    border-top : 1px solid  gray;
    border-bottom : 1px solid  gray;
    display:flex;
}
.seven{
    grid-column: 1/3;
    grid-row:5;
    display : flex;
    justify-content: flex-end;
    padding-bottom : 10px;
}
.commentContents {
	margin-left : 10px;
	padding : 5px;
}
.report_detail_view{
	display:none;
}

</style>
<script>
$(document).ready(function() {
	
	topButtonCss();
//----------------------------------------- page 버튼 ----------------------------------------------------------------
 	//페이지 버튼 눌렀을때 해당페이지 보여주기
 	$(".photoPageNumBtn").on('click', function(){
 		$("#page").val($(this).val());
 		$("#adminFormPage").submit();
	});//pageNumBtn클릭
	
	//첫페이지
 	$("#photoBoardPageBtnFirst").on('click', function(){
 		$("#page").val(1);
 		$("#adminFormPage").submit();
	});
 	//이전페이지
 	$("#photoBoardPageBtnPre").on('click', function(){
 		$("#page").val("${response.pagination.startPage-1}");
 		$("#adminFormPage").submit();
	});
	//다음페이지
 	$("#photoBoardPageBtnNext").on('click', function(){
 		$("#page").val("${response.pagination.endPage+1}");
 		$("#adminFormPage").submit();
	});
	
 	//마지막페이지
 	$("#photoBoardPageBtnLast").on('click', function(){
 		$("#page").val("${response.pagination.totalPageCount}");
 		$("#adminFormPage").submit();
	});//pageNumBtn클릭
//----------------------------------------- page 버튼 -----------------------------------------------------------------
	//신고내용 상세조회
	$(".list_tr").on('click', function(){
		let status = $(this).next().css('display');
 		if(status == 'none'){
			$(this).next().show();
		}
		else {
			$(this).next().hide();
		}
	});
		
	//해당 댓글이 있는 글 보러가기
	$(".moreDetailBtn").on('click', function(){
		let cmtBi = $(this).prev("#commentBoardId").val();
		//alert(cmtBi);
		open("/boarddetail?bi="+cmtBi , "해당글상세조회", "width=900px, height=800px, top=200px, left=800px");
	});
	
	
}); //ready
</script>
</head>
<body>
<jsp:include page="Header.jsp" />
<div id="myPage_layout">
<jsp:include page="adminMenu.jsp" />
	
	<div id="myPage_main">
		<div id="board_name">
			${adminMenu}
		</div>
		
		<div id="board_page">
		
			<div id="adminList_top_btnBox">
				<div id="notYetList">
					 <form id="reportedCommentListForm" action="reportedCommentList" method="post">	
						 <input type="hidden" name="search" value="notYet" />		    	  	
						<input type="submit" id="notYetListBtn" value="미 처리 내역" />
					 </form>
				</div>
				<div id="doneList">
					<form id="completedCommentListForm" action="reportedCommentList" method="post">			    	  	
						 <input type="hidden" name="search" value="done" />		    	  	
						<input type="submit" id="doneListBtn" value="처리 완료 내역" />
					 </form>
				</div>	
			</div>
			
			<table id="board-table">
				<thead>
					<tr>
						<th style="width: 4%;">번호</th>
						<th style="width: 10%;">작성자</th>
						<th style="width: 20%;">제목/내용</th>
						<th style="width: 10%;">신고자</th>
						<th style="width: 14%;">신고 사유</th>
						<th style="width: 9%;">신고 날짜</th>
						<c:choose>
							<c:when test="${searchdto.searchType1 == 'notYet'}">
								<th style="width: 17%;">신고 처리</th>
							</c:when>
							<c:otherwise>
								<th style="width: 17%;">신고 처리 결과</th>						
							</c:otherwise>					
						</c:choose>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${response.list }" var="dto">
				<c:choose>
					<c:when test="${searchdto.searchType1 == 'notYet'}">
						<tr class="list_tr">
							<td>${dto.report_id}</td><td>${dto.reported_member_id}</td>
							<td>${dto.reported_contents }</td>
							<td>${dto.reporter }</td><td>${dto.report_reason }</td>
							<td>
								<jsp:useBean id="now" class="java.util.Date" />
								<fmt:formatDate value="${now}" pattern="yyyy-MM-dd" var="nowDate" />
								<fmt:parseDate value="${dto.report_time }" pattern="yyyy-MM-dd HH:mm:ss" var="reg" />
								<fmt:formatDate value="${reg}" pattern="yyyy-MM-dd" var="regDate" />
								${regDate}
							</td>
							 <td>
							 	<input type="hidden" class="reportedMemId" value="${dto.reported_member_id}" />
								 <button class="adminDeleteComment" id="${dto.comment_id}">댓글삭제</button>	
								 <button class="adminDeleteMember" id="${dto.reported_member_id}_delete">회원삭제</button>
								 <button class="adminStopMember" id="${dto.reported_member_id}_stop">회원정지</button>
							 </td>
						</tr>
						<tr class="report_detail_view"><td colspan="7" style="text-align : left; margin-left : 10px;">
						<div class="detail_wrap_box">
							<p style="font-size : 19px; font-weight : bold;">신고 상세 내용 조회</p>
							<div class="grid_container">
								<div class="one">신고 상세 사유</div>
								<div class="two">${dto.report_detail}</div>
								<div class="three">댓글 작성자</div>
								<div class="four">${dto.reported_member_id}</div>
								<div class="five">댓글 내용</div>
								<div class="six">
									<c:if test="${dto.commentdto.comment_imgurl != null}">								
										<img src="${dto.commentdto.comment_imgurl}" style="width:100px; height:100px;">
									</c:if>
									<div class="commentContents">
										${dto.commentdto.comment_contents}
									</div>
								</div>
								<div class="seven">
								 	<input type="hidden" id="commentBoardId" value="${dto.commentdto.board_id}" />
									<button class="moreDetailBtn" >상세페이지로 이동</button>
								</div>
							</div>	
						</div>
						</td></tr>	
					</c:when>
					<c:otherwise>
						<tr class="list_tr">
							<td>${dto.report_id}</td><td>${dto.reported_member_id}</td>
							<td>${dto.reported_contents }</td>
							<td>${dto.reporter }</td><td>${dto.report_reason }</td>
							<td>
								<fmt:formatDate value="${now}" pattern="yyyy-MM-dd" var="nowDate" />
								<fmt:parseDate value="${dto.report_time }" pattern="yyyy-MM-dd HH:mm:ss" var="reg" />
								<fmt:formatDate value="${reg}" pattern="yyyy-MM-dd" var="regDate" />
								${regDate}
							</td>
							 <td>${dto.report_result }</td>
						</tr>
						<tr class="report_detail_view"><td colspan="7" style="text-align : left; margin-left : 10px;">
						<div class="detail_wrap_box">
							<p style="font-size : 19px; font-weight : bold;">신고 상세 내용 조회</p>
							<div class="grid_container">
								<div class="one">신고 상세 사유</div>
								<div class="two">${dto.report_detail}</div>
								<div class="three">댓글 작성자</div>
								<div class="four">${dto.reported_member_id}</div>
								<div class="five">댓글 내용</div>
								<div class="six">
									<c:if test="${dto.commentdto.comment_imgurl != null}">								
										<img src="${dto.commentdto.comment_imgurl}" style="width:100px; height:100px;">
									</c:if>
									<div class="commentContents">
										${dto.commentdto.comment_contents}
									</div>
								</div>
								<div class="seven">
								 	<input type="hidden" id="commentBoardId" value="${dto.commentdto.board_id}" />
									<button class="moreDetailBtn" >상세페이지로 이동</button>
								</div>
							</div>	
						</div>
						</td></tr>					
					</c:otherwise>
					
				</c:choose>
				</c:forEach>   
				</tbody>
			</table>
			
		</div>
		
<!-- pagination -->
	<form action="reportedCommentList" id="adminFormPage" method="post">
		<c:choose>
			<c:when test="${searchdto.searchType1 == 'notYet'}">
				<input type="hidden" name="search" value="notYet" />
			</c:when>
			<c:otherwise>
				<input type="hidden" name="search" value="done" />
			</c:otherwise>					
		</c:choose>
		<input type=hidden id="page" name="page" value="">
	</form>
	
	<div id="board_pagingNum">
         <c:if test="${fn:length(response.list) != 0}">
            <div class="pagefirst"
               <c:if test="${!response.pagination.existPrevPage}"> style="visibility: hidden;" </c:if>>
               <input type="button" class="photoPageBtn" id="photoBoardPageBtnFirst" value="◁◁">
            </div>
            <div class="prev" id="${response.pagination.startPage-1}"
               <c:if test="${!response.pagination.existPrevPage}"> style="visibility: hidden;" </c:if>>
               <input type="button" class="photoPageBtn" id="photoBoardPageBtnPre" value="◁">
            </div>

            <c:forEach begin="${response.pagination.startPage}"
               end="${response.pagination.endPage}" varStatus="vs">
               <c:if test="${vs.index == searchdto.page}">
	               <input type="button" class="photoPageNumBtn" value="${vs.index}" style="font-weight: 900">
               </c:if>
               <c:if test="${vs.index != searchdto.page}">
	               <input type="button" class="photoPageNumBtn" value="${vs.index}" style="font-weight: 300; color: #555">
               </c:if>
            </c:forEach>

            <div class="next" id="${response.pagination.startPage+10}"
               <c:if test="${!response.pagination.existNextPage}"> style="visibility: hidden;" </c:if>>
               <input type="button" class="photoPageBtn" id="photoBoardPageBtnNext" value="▷">
            </div>
            <div class="pagelast" id="${response.pagination.totalPageCount}"
               <c:if test="${!response.pagination.existNextPage}"> style="visibility: hidden;" </c:if>>
               <input type="button" class="photoPageBtn" id="photoBoardPageBtnLast" value="▷▷">
            </div>
         </c:if>
      </div>
      <!-- pagination -->
	</div>	
</div>
</body>
<script>
function topButtonCss(){
	let search = "${searchdto.searchType1}";
	if(search == 'notYet') {
		$("#notYetListBtn").css('font-weight', 'bold');
		$("#doneListBtn").css('font-weight', 'normal');		
	}
	else {
		$("#notYetListBtn").css('font-weight', 'normal');
		$("#doneListBtn").css('font-weight', 'bold');		
	}
};
</script>
</html>