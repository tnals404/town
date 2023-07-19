<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title></title>
<script src="/js/jquery-3.6.4.min.js"></script>
<script>
$(document).ready(function() {
	//
});
</script>
</head>
<body>
<div class="form_section">
	<div class="form_section_title">
		<label>상품 이미지</label>
	</div>
	<div class="form_section_content">
		<input type="file" name="uploadFile">
	</div>
</div>
<div id="result"></div>
<script>
document.querySelector("input[type=file]").addEventListener("change", function () {
    let fileInput = document.querySelector("input[name=uploadFile]");
    let fileObj = fileInput.files[0];

    let formData = new FormData();

    formData.append("uploadFile", fileObj);

    $.ajax({
        url: '/uploadTest',
        processData : false,
        contentType : false,
        data : formData,
        type : 'POST',
        dataType : 'json',
        success: function(data) {
        	$("#result").text(data.uploadFolder + " : " + data.uploadFileName)
        },
        error: function(request,status,error) {
      		alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
      		console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
      	}
    });
});
</script>
</body>
</html>