<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지사항 작성 폼</title>
<script src="/js/jquery-3.6.4.min.js"></script>
<link href="//cdn.quilljs.com/1.3.6/quill.snow.css" rel="stylesheet">
<script src="//cdn.quilljs.com/1.3.6/quill.js"></script>
<link rel="stylesheet" type="text/css" href="/css/noticeWritingForm.css"/>
</head>
<body>
<div id="board_main">
	<select id="board-name" multiple>
		<option value="0">전체 동네</option>
		<c:forEach items="${townNameList}" var="townName" varStatus="vs">
			<option value="${vs.count}">${townName}</option>
		</c:forEach>
	</select>
	<div id="notice_wrap">
		<div id="board_name">
			<input id="write-title" type="text" placeholder="공지사항 제목"/>
		</div>
		<div id="board_page">			
			
			<div id="editor"></div>
			<input type="hidden" id="quill_html" name="content">
			
			<div id="content-footer">
				<div id="place-and-write">
					<button id="write-btn">작성완료</button>
				</div>
			</div>
		</div>
	</div>
</div>
<div id='addplace-result' style="display: none"></div>
</body>
<script src="/js/writingFormQuill.js"></script>
<script>
//작성완료 버튼 클릭 이벤트
$("#write-btn").on("click", function() {
	$(".ql-editor").append("<p></p>")
	let board_title = $("#write-title").val(); // 게시글 제목
	let board_contents = $("#quill_html").val(); // 게시글 내용
	let board_imgurl; // 업로드한 이미지가 저장된 경로
	let board_fileurl; // 업로드한 파일이 저장된 경로
	let board_preview = $(".ql-editor").text(); // 게시글 미리보기 텍스트
	let writer = "${sessionScope.member_id}"; // 관리자 아이디
	let town_ids = $("#board-name").val(); // ['0', '1', '2'] 이런식으로 문자열 배열로 저장됨
	if (town_ids.includes("0")) {
		town_ids = [];
		let option = document.querySelectorAll("#board-name option");
		for (let i = 1; i < option.length; i++) {
			town_ids.push(option[i].value);
		}
	}
	
	// 만약 이미지가 하나 이상이면 첫번째 이미지 src 속성 board_imgurl에 저장 
	if ($(".ql-editor img").length > 0) {
		board_imgurl = $($(".ql-editor img")[0]).attr("src");
	}	
	
	// board_contents에서 resize 속성 삭제
	let removeStr = "resize: both; ";
	let removeIdx = board_contents.indexOf(removeStr);
	while(removeIdx > -1) {
		board_contents = board_contents.replace(removeStr, "");
		removeIdx = board_contents.indexOf(removeStr);
	}
	
	// .ql-editor img 태그 부모 p 태그에 style 속성이 없으면 #quill_html img에 style="width: 50%; height: auto; display: block;" 추가
	let imgStrIdx = 0;
	while(true) {
		imgStrIdx = board_contents.indexOf("<p><img ", imgStrIdx);
		if (imgStrIdx === -1) break;
		board_contents = board_contents.slice(0, imgStrIdx + 2) + ' style="width: 50%; height: auto; display: block;"' + board_contents.slice(imgStrIdx + 2);
		imgStrIdx++;
	} //while
	
	$.ajax({
		url: "/noticeWritingForm",
		data: {
			"board_title": board_title,
			"board_contents": board_contents,
			"board_imgurl": board_imgurl,
			"board_fileurl": board_fileurl,
			"board_preview": board_preview,
			"writer": writer,
			"town_ids": town_ids,
		},
		dataType: "json",
		method: "post",
		success: function(response) {
			if (response.insertResult === -1) {
				alert("공지사항 제목을 입력해주세요.");
			} else if (response.insertResult === -2) {
				alert("공지사항을 올릴 동네를 선택해주세요.");
			} else if (response.insertResult >= 1) {
				alert("공지사항 작성이 완료되었습니다.");
				// # 공지사항 목록 페이지로 이동하기 구현
			} else {
				alert("알 수 없는 오류 발생");
			}
		},
		error: function(request,status,error) {
    	alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
    	console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
    }
	}); //ajax
}) //onclick
</script>
</html>