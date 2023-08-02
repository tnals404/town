<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>1:1 채팅</title>
<script src="/js/jquery-3.6.4.min.js"></script>
<script>
$(document).ready(function() {
    
	var websocket;
    websocket = new WebSocket("ws://localhost:8091/chatws");
    websocket.onopen = function() {
        console.log("웹소캣 연결 성공");
    }

    websocket.onclose = function() {
        console.log("웹소캣 연결 해제 성공");
    }

    websocket.onmessage = function(event) {
        console.log("웹소캣 수신 성공");
        var data = JSON.parse(event.data);
        
        var message = data.message;
        var sender = data.sender;
        var user = '<%= (String)session.getAttribute("member_id") %>';
        
        if (sender == user) {
        	 var messageSent = $('<div>').addClass("message sent");
             messageSent.html('<div class="bubble">' + message + '</div>');

             var chatArea = $("#chatArea");
             chatArea.append(messageSent);	
        }
        
        else {
	        var messageReceived = $('<div>').addClass("message received");
	        messageReceived.html('<div class="bubble">' + message + '</div>');
	        
	        var chatArea = $("#chatArea");
	        chatArea.append(messageReceived);
        }
        
    };

    $("#exitButton").on('click', function() {
        websocket.close();
        window.location.href = "/chatlist";
    });

    $("#exitButton_dropdown").on('click', function() {
        websocket.close();
        window.location.href = "/chatlist";
    });
    
    $("#sendButton").on('click', function() {
        sendMessage();
    });

    $("#messageInput").on("keyup", function(event) {
        if (event.keyCode === 13) {
            sendMessage();
        }
    });

    function sendMessage() {
            var message = $("#messageInput").val();
            var sender = '<%= (String)session.getAttribute("member_id") %>';
            var pathname = $(location).attr('search');
            var touser_id = pathname.substring("?touser_id=".length);
            var data = {
                    message: message,
                    sender: sender
                };
                
            websocket.send(JSON.stringify(data));
          
            $.ajax({
                type: "POST",
                url: "/sendChat",
                data: { message: message,
                		touser_id: touser_id
                	},
                dataType: "text",
                success: function(response) {
                    console.log("Message sent successfully!");
                    location.reload();
                    $("#messageArea").val("");
                },
                error: function(xhr, status, error) {
                    console.error("Error sending message:", error);
                }            
            });

            // Scroll to the bottom
            chatArea.scrollTop(chatArea.prop("scrollHeight"));
            

        }
    
    $("#setting_btn").click(function() {
        $("#setting_dropdown_list").toggle();
  });
});

</script>

<style>
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

body {
	font-family: Arial, sans-serif;
	background-color: #fafafa;
	margin: 0;
	padding: 0;
}

.chat-wrapper {
	max-width: 600px;
	margin: 0 auto;
	background-color: #fff;
	box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
	border-radius: 10px;
	overflow: hidden;
}

.chat-header {
	padding: 15px;
	background-color: #182434;
	color: #fff;
	font-size: 18px;
	font-weight: bold;
	display: flex;
}

.chat-area {
	height: 500px;
	padding: 20px;
	overflow-y: scroll;
	scrollbar-width: none; /* Firefox 용 */
	-ms-overflow-style: none; /* IE, Edge 용 */
}

.chat-area::-webkit-scrollbar {
	width: 0;
	height: 0;
}

.message {
	display: flex;
	align-items: flex-start;
	margin-bottom: 10px;
}

.message .bubble {
	word-break:break-all;
	max-width: 70%;
	padding: 10px;
	border-radius: 20px;
	background-color: #e6e6e6;
	color: #333;
	font-size: 14px;
}

.message.sent {
	justify-content: flex-end;
}

.message.sent .bubble {
	background-color: #dcd9d8;
	color: #333;
}

.message.received .bubble {
	background-color: #8d98aa;
	color: #fff;
}

.input-area {
	
	display: flex;
	align-items: center;
	padding: 25px;
	background-color: #fff;
}

.input-area textarea {
	flex: 1;
	align-items : center;
	height: 45px;
	padding: 4px 10px;
	padding-top : 10px;
	border: none;
	border-radius: 20px;
	resize: none;
	font-size: 18px;
	background-color: #f0f0f0;
	color: #333;
	overflow: hidden; /* 스크롤 제거 */
}

.input-area button {
	height : 40px;
	width : 40px;
	margin-left: 10px;
	padding : 10;
	border: none;
	border-radius: 20px;
	background-color: #182434;
	color: #fff;
	cursor: pointer;
}

#setting_dropdown_list {
    display: none;
    position: absolute;
    left: 60%;
    z-index: 2;
    background-color: #f9f9f9;
    padding: 5px 0;
    border-radius: 5px;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
    list-style: none;
    margin: 0;
  	
}

#setting_dropdown_list li {
    padding: 8px 15px;
    cursor: pointer;
    color: #333;
}

#setting_dropdown_list li:hover {
    background-color: #eaeaea;
}
</style>
</head>
<body>
	<div class="chat-wrapper">
		<div class="chat-header">
	    	<span id="exitButton" style="margin-right: auto; padding: 0px 10px; cursor: pointer;">
        		<img style="height: 15px;" src="img/뒤로가기버튼.png">
    		</span>
    		<span style="margin-center: auto;">CHAT</span>
    		<span style="margin-left: auto; padding: 0px 10px; cursor: pointer;">
        		<img style="height: 15px; position:relative;" src="img/설정버튼.png" id="setting_btn">
   			</span>

		</div>
   			<ul id="setting_dropdown_list">
	        	<li>신고하기</li>
	        	<li id="exitButton_dropdown">채팅창 나가기</li>
	        </ul>
		<div class="chat-area" id="chatArea">
			<!-- 채팅 메시지 추가 -->
			<c:forEach items="${list}" var="list">
				<c:choose>
					<c:when test="${list.member_id eq sessionScope.member_id}">
						<div class = "message sent">
							<div class="bubble">${list.message_content }</div>
						</div>
					</c:when>
					<c:otherwise>
						<div class = "message received">
							<div class="bubble">${list.message_content }</div>
						</div>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</div>
		<div class="input-area">
			<button style=" margin-left:0; margin-right:10px;"><img style="height: 22px; margin-top : 3px;" src="img/이미지버튼.png"></button>
			<textarea id="messageInput" placeholder="메시지를 입력하세요..."></textarea>
			<button id="sendButton"><img style="height: 22px; margin-top : 3px;" src="img/전송화살표버튼.png"></button>
		</div>
	</div>
</body>
</html>
