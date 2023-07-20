<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
   <meta charset="UTF-8">
  <title>채팅방 목록창</title>
  <style>
    /* 전체 목록창 스타일 */
    
    @font-face {
    font-family: 'GmarketSansMedium';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2001@1.1/GmarketSansMedium.woff') format('woff');
    font-weight: normal;
    font-style: normal;
}
* {
	font-family : 'GmarketSansMedium';
	box-sizing: border-box;
	}
    
        html, body {
      margin: 0;
      display: flex;
      justify-content: center;
    }
    
    .chat-container {
      width: 600px;
      height: 500px;
      border: 1px solid #ccc;
      overflow: hidden;
    }
    
    /* 헤더 스타일 */
    .header {
      background-color: #182434;
      color : white;
      padding: 10px;
      font-weight: bold;
    }
    
    /* 전체 목록창 내부 스타일 */
    .chat-list {
      height: 100%;
      overflow-y: scroll;
      padding: 10px;
      scrollbar-width: none; /* Firefox 용 */
	-ms-overflow-style: none; /* IE, Edge 용 */
    }
    
    .chat-list::-webkit-scrollbar {
	width: 0;
	height: 0;
	}
    
    /* 채팅방 아이템 스타일 */
    .chat-item {
      display: flex;
      align-items: center;
      padding: 10px;
      margin-bottom: 10px;
      border-radius: 5px;
      background-color: #dcd9d8;
    }
    
    /* 채팅방 아이템 썸네일 이미지 스타일 */
    .chat-item .thumbnail {
      width: 40px;
      height: 40px;
      border-radius: 50%;
      object-fit: cover;
      margin-right: 10px;
    }
    
    /* 채팅방 아이템 정보 스타일 */
    .chat-item .info {
      flex: 1;
    }
    
    /* 채팅방 아이템 정보 제목 스타일 */
    .chat-item .info .title {
      font-weight: bold;
      font-size: 16px;
      line-height: 1.4;
    }
    
    /* 채팅방 아이템 정보 메시지 스타일 */
    .chat-item .info .message {
      font-size: 14px;
      line-height: 1.4;
      color: #666;
    }
  </style>
  <script>
    window.addEventListener('DOMContentLoaded', function() {
      var chatList = document.querySelector('.chat-list');
      chatList.addEventListener('mouseover', function() {
        chatList.style.overflowY = 'hidden';
      });
      chatList.addEventListener('mouseout', function() {
        chatList.style.overflowY = 'scroll';
      });
    });
  </script>
</head>
<body>
  <div class="chat-container">
    <div class="header">채팅방 목록</div>
    <div class="chat-list">
      <div class="chat-item">
        <img class="thumbnail" src="avatar1.jpg" alt="Avatar">
        <div class="info">
          <div class="title">채팅방 1</div>
          <div class="message">안녕하세요!</div>
        </div>
      </div>
      <div class="chat-item">
        <img class="thumbnail" src="avatar2.jpg" alt="Avatar">
        <div class="info">
          <div class="title">채팅방 2</div>
          <div class="message">방문해 주셔서 감사합니다.</div>
        </div>
      </div>
      <div class="chat-item">
        <img class="thumbnail" src="avatar3.jpg" alt="Avatar">
        <div class="info">
          <div class="title">채팅방 3</div>
          <div class="message">잘 지내시나요?</div>
        </div>
      </div>
    </div>
  </div>
</body>
</html>