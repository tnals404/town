$(document).ready(function(){	
	
	let board = "/myPage2"; // 글 or 사진 게시판
	let ctgy = "?ctgy="; // 게시판 카테고리
	
	// 게시판 소분류 클릭시 동작
	$("li.innerMenu").on("click", function(e) {
		
		ctgy = "?ctgy=" + $(this).text().trim();
		
		window.location.href = board + ctgy;
		e.stopPropagation();
		//$("#myPage_main").addClass("display-none");
    	//$("#board_main").addClass("display-inline-block"); 
	}); //onclick
	
}); //ready