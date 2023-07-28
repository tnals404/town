<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>신고하기</title>
<script src="/js/jquery-3.6.4.min.js"></script>
<style>
@font-face {
    font-family: 'GmarketSansMedium';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2001@1.1/GmarketSansMedium.woff') format('woff');
    font-weight: normal;
    font-style: normal;
}

* {
font-family : 'GmarketSansMedium';
font-size : 14px;
}

html, body {
	margin: 0px;
	padding: 0px;
	background-color: #E3E2E0;
	align-content: center;
}

header {
	height: 50px;
	line-height: 50px;
	background-color: #182434;
	color: white;
	text-align: center;
}
p {
font-size : 15px;
font-weight : bold;
}
textarea {
resize: none;
}
#form-wrap{
margin-left : 10px;

}
#btn-wrap{
width : 100%;
margin-left : 0px;
}
#report_submitBtn{
width : 520px; height : 40px;
margin-top : 10px;
}
#report_submitBtn:hover {
	background-color : #182434;
	color : white;			
}
</style>
<script>
$(document).ready(function(){
	//구현
});
</script>
</head>
<body>
<header>신고하기</header>
<div id="form-wrap">
	<br>
	<div id="writer">작성자  | </div>
	<div id="contents"> 제목/내용  | </div>
	<p>신고하시는 사유를 선택해주세요.</p>
	<form action="" id="" method="post">
	<input type="hidden" />
	<div>
		<input type="radio" name="reason" value="">스팸홍보/도배글입니다.<br>
		<input type="radio" name="reason" value="">음란물입니다.<br>
		<input type="radio" name="reason" value="">불법정보를 포함하고 있습니다.<br>
		<input type="radio" name="reason" value="">청소년에게 유해한 내용입니다.<br>
		<input type="radio" name="reason" value="">욕설/생명경시/혐오/차별적 표현입니다.<br>
		<input type="radio" name="reason" value="">개인정보 노출 게시물입니다.<br>
	</div>
	<p>신고하시는 이유를 알려주세요.</p>
	<textarea rows="5" cols="55" placeholder="신고 사유를 구체적으로 작성해주세요."></textarea>
	<br>
	<div id="btn-wrap"><input type="submit" id="report_submitBtn" value="등록하기" /></div>
	</form>
</div>
</body>
</html>