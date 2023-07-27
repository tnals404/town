<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글 작성 페이지</title>
<!-- quill.js -->
<link href="//cdn.quilljs.com/1.3.6/quill.snow.css" rel="stylesheet">
<script src="//cdn.quilljs.com/1.3.6/quill.js"></script>
<!-- jquery -->
<script src="/js/jquery-3.6.4.min.js"></script>
<!-- css -->
<link rel="stylesheet" type="text/css" href="/css/writingForm.css"/>
<style>
</style>
</head>
<body>
<jsp:include page="Header.jsp" />
<div id="board_layout">
	<jsp:include page="boardMenu.jsp" />
	<div id="board_main">
		<div id="board_name">
			<select id="board-name">
				<option>나의 일상</option>
				<option>사건, 사고 소식</option>
				<option>오늘의 사진</option>
				<option>같이 줄서요</option>
				<option>같이 해요 소모임</option>
				<option>분실물센터</option>
				<option>심부름센터</option>
				<option>행사 소식</option>
				<option>새로 오픈했어요</option>
			</select>
			<input id="write-title" type="text" placeholder="제목"/>
		</div>
		<div id="board_page">			
			
			<div id="editor"></div>
			<input type="hidden" id="quill_html" name="content">
			
			<div id="content-footer">
				<div id="place_center_align">
					<div id="place_con">
						<ul id="place_info_con">
							<li class="place_info"></li>
							<li class="place_info"></li>
							<li class="place_info"></li>
							<li class="place_info"></li>
							<li class="place_info" style="display: none"></li>
							<li class="place_info" style="display: none"></li>
						</ul>
					</div>
					<span id="place_delbtn">X</span>
				</div>
				<div id="place-and-write">
					<button id="place-btn">장소추가</button>
					<button id="write-btn">작성완료</button>
				</div>
			</div>
		</div>
	</div>
</div>
<div id='addplace-result' style="display: none"></div>
<script>
$(document).ready(function() {
	// 에디터 placeholder 추가
	$(".ql-editor").attr("data-placeholder", "게시글 내용을 입력해주세요! 파일 용량 제한은 5MB입니다.");
	
	// 게시판에서 글 작성 페이지로 이동시 자동으로 게시판 소분류 선택
	$("#board-name option").each(function(index, item) {
		if ($(item).text() === "${param.ctgy}") {
			$(item).prop("selected", true);
		}
	});
	
	// 작성완료 버튼 클릭 이벤트
	$("#write-btn").on("click", function() {
		$(".ql-editor").append("<p></p>")
		let board_name_inner = $("#board-name option:selected").val(); // 게시글 소분류
		let board_title = $("#write-title").val(); // 게시글 제목
		let board_contents = $("#quill_html").val(); // 게시글 내용
		let board_imgurl; // 업로드한 이미지가 저장된 경로
		let board_preview = $(".ql-editor").text(); // 게시글 미리보기 텍스트
		let place_name; // 게시글에 추가한 장소 이름
		let place_road_address; // 장소 도로명 주소
		let place_address; // 장소 지번 주소
		let place_tel; // 장소 전화번호
		let place_lat; // 장소 위도
		let place_long; // 장소 경도
		if ($("#place_center_align").css("display") !== "none") {
			place_name = $($(".place_info")[0]).text();
			place_road_address = $($(".place_info")[1]).text();
			place_address = $($(".place_info")[2]).text();
			place_tel = $($(".place_info")[3]).text();
			place_lat = $($(".place_info")[4]).text();
			place_long = $($(".place_info")[5]).text();
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
		} //for
		
		$.ajax({
			url: "/writingForm",
			data: {
				"board_name_inner": board_name_inner,
				"board_title": board_title,
				"board_contents": board_contents,
				"board_imgurl": board_imgurl,
				"board_preview": board_preview,
				"place_name": place_name,
				"place_road_address": place_road_address,
				"place_address": place_address,
				"place_tel": place_tel,
				"place_lat": place_lat, 
				"place_long": place_long,
				"writer": "${sessionScope.member_id}",
				"town_id": parseInt("${sessionScope.town_id}"),
			},
			dataType: "json",
			method: "post",
			success: function(response) {
				if (response.insertResult === -1) {
					alert("글 제목을 입력해주세요.");
				} else if (response.insertResult === 1) {
					alert("글 작성이 완료되었습니다.");
					if (board_name_inner === "오늘의 사진" || 
							board_name_inner === "역대 당선작" || 
							board_name_inner === "분실물센터") {
						location.href = "/photoBoard?ctgy=" + board_name_inner + "&ti=${sessionScope.town_id}";
					} else {
						location.href = "/basicBoard?ctgy=" + board_name_inner + "&ti=${sessionScope.town_id}";
					}
					
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
	
	// 장소추가 버튼 클릭 이벤트
	$("#place-btn").on("click", function() {
		if ("${param.ti}" === "${sessionScope.town_id}") {
			window.name = "writingForm";
			window.open('/kakaoMap?ti=${param.ti}', 'kakaoMap', 'width=855px, height=602px, top=0px, left=0px');
		}
	}); //onclick
	
	// 추가 되어있는 장소를 삭제하는 동작
	$("#place_delbtn").on("click", function() {
		$("#place_center_align").css("display", "none");
	}); //onclick
	
	/*
	// 추가된 장소를 클릭하면 네이버에 해당 장소 검색한 결과 페이지 새탭에 열기
	$("#place_con").on("click", function() {
		window.open("https://search.naver.com/search.naver?where=nexearch&sm=tab_jum&query=" + $($(".place_info")[0]).text());
	});
	*/
}); //document ready

// kakaoMap 창에서 장소추가했을 때 동작
function addBoardPlace() {
	let place_html = $("#addplace-result");
	let place_data = $("#addplace-result").text().split("/n");
	
	$($(".place_info")[0]).text(place_data[0]); // 이름
	$($(".place_info")[1]).text(place_data[1]); // 도로명 주소
	$($(".place_info")[2]).text(place_data[2]); // 지번 주소
	$($(".place_info")[3]).text(place_data[3]); // 전화번호
	$($(".place_info")[4]).text(place_data[4]); // 위도
	$($(".place_info")[5]).text(place_data[5]); // 경도
	$("#place_center_align").css("display", "flex");
}
</script>
</body>
<script src="/js/writingFormQuill.js"></script>
</html>