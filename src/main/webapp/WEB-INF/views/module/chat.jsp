<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>   
<body>
	<h2>${memName } 의 채팅방</h2>

	<input type="hidden" name="memId" value = "${memId }" id = "memId"/>
	<input type="text" id="msg" placeholder="채팅 내용 입력"/>
	<input type="button" id="sendBtn" value="전송"/><br/>
	<input type="file" id="inputFile"/>
	 <div>
	 <span id="recMs" name = "recMs" style="float:right;cursor:pointer;margin-right:10px;color:pink;"></span>
      </div>

	<div id="messageArea">
		<c:choose>
			<c:when test="${empty chatList }">
				<tr>
					<td>채팅이 존재하지 않습니다.</td>
				</tr>
			</c:when>
			<c:otherwise>
				<c:forEach items="${chatList }" var="chat">
					<tr>
						<td>보낸 이 : </td><td>${chat.memId }</td>
						<td>내용 : </td><td>${chat.chatCont }</td>
						<td>일자 : </td><fmt:formatDate value="${chat.chatDate }" pattern="yyyy-MM-dd hh:mm:ss" type="date"/>
					</tr><br/>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</div>
	<div class="toast align-items-center fade" role="alert"  id='alert-toast' aria-live="assertive" aria-atomic="true" style="color: white;background: #dc5151; position: absolute;
    top: 78px;right: 0px;">
  <div class="d-flex">
    <div class="toast-body">
   </div>
    <button type="button" class="btn-close me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
  </div>
</div>   
</body>	
<script type="text/javascript">
$(function(){
var sendBtn = $("#sendBtn");
var msg = $("#msg");
var messageArea = $("#messageArea");
var inputFile = $("#inputFile");

	// websocket 생성
    const websocket = new WebSocket("ws://192.168.34.73/module"); // IP or localhost넣기
    websocket.onmessage = onMessage;	// 소켓이 메세지를 받을 때
    websocket.onopen = onOpen;		// 소켓이 생성될때(클라이언트 접속)
    websocket.onclose = onClose;	// 소켓이 닫힐때(클라이언트 접속해제)

    
    
    inputFile.on("change", function(event){
		console.log("change event,,");
		
		var files = event.target.files;
		var file = files[0];
		var str = "";
		var memId = $("#memId").val()
		console.log(file);
		
			
			// ajax로 파일을 컨트롤 시 formData를 이용한다.
			// append() 이용
			var formData = new FormData();
			// key : value 형태로 값이 추가된다.
			formData.append("file", file);
			
			// formData는 key/value 형식으로 데이터가 저장된다.
			// dataType : 응답(response) 데이터를 내보낼 때 보내줄 데이터 타입이다.
			// processData : 데이터 파라미터를 data라는 속성으로 넣는데 제이쿼리 내부적으로 쿼리스트링을 구성한다.
			// 				파일 전송의 경우 쿼리 스트링을 사용하지 않으므로 해당 설정을 false한다.
			// contentType : content-Type 설정 시 사용하는데 해당 설정의 기본값은 "application/x-www-form-urlencoded; charset=utf-8"이다
			// request 요청에서 content-Type을 확인해보면 "multipart/form-data; boundary=---WebkitFormBoundary7Taxt434B
			// 과 같은 값을 전송되는 걸 확인할 수 있다.
			
			$.ajax({
				type : "post",
				url : "/ajax/uploadAjax",
				data : formData,
				dataType : "text",
				processData : false,
				contentType : false,
				success : function(data){
					//alert(data);
					
					if(checkImageType(data)) { // 이미지이면 이미지 태그를 이용한 출력형태
						str += "<div>";
						str += "	<a href = '/ajax/displayFile?fileName="+ data + "'>";
						str += "	<img src = '/ajax/displayFile?fileName=" + getThumbnailName(data)+ "'/>";
						str += "	</a>";
						str += "</div>";
						$.ajax({
		   					type:'post',
		   					url:'/module/chat.do',
		   					data:{
		   						memId:memId,
		   						chatCont:"<a href = '/ajax/displayFile?fileName="+ data + "' target='_blank' ><img src = '/ajax/displayFile?fileName=" + getThumbnailName(data)+ "'/></a>"
		   					},
		   					success:function(){
		   						
		   					}
		   				})
					} else { // 파일이면 파일명에 대한 링크로만 출력
						str += "<div>";
						str += "	<a href ='/ajax/displayFile?fileName="+data+"'>" +getOriginalName(data) + "</a>";
						str += "</div>";
						$.ajax({
		   					type:'post',
		   					url:'/module/chat.do',
		   					data:{
		   						memId:memId,
		   						chatCont: "<a href ='/ajax/displayFile?fileName="+data+"'>" +getOriginalName(data) + "</a>"
		   					},
		   					success:function(){
		   						
		   					}
		   				})
					}
					//$("#messageArea").prepend(str);
					 if(str != "") {			
				   			let msg = {
				    		   	'memId' : memId,
				   		    	'message' : str,
				   		    	'time' : new Date().toTimeString().split(' ')[0]
				   		      	}
				   		    	if(str != null) {
				   		    	websocket.send(JSON.stringify(msg));	// websocket handler로 전송(서버로 전송)
					   		    	
				   		    	}
				   		    	document.getElementById("msg").value = '';
				   		 	  } 
				}
			});
		   
	});
    
    //on exit chat room
    function onClose(evt) {
    $("#messageArea").append("연결 끊김");
    console.log("소켓닫힘 : " + evt);
    }

    //on entering chat room
    function onOpen(evt) {
    console.log("소켓열림 : " + evt);
    }

    // on message controller
    function onMessage(msg) {
    	
	    var splitdata =msg.data.split(":");
        if(splitdata[0].indexOf("recMs") > -1) {
        $("#recMs").append("["+splitdata[1]+"통의 쪽지가 왔습니다.]");
        } else {
        	
        $("#chat").append(msg.data + "<br/>");
    	console.log(msg.data); 	
       
        }
        
	    var data = JSON.parse(msg.data); // msg를 받으면 data 필드 안에 Json String으로 정보가 있음
	 
	    
	    
	    
	    console.log("데이터 : ", data)
	    // 필요한 정보를 Json data에서 추출
	     var memId = data.memId;
	     var message = data.message;
	     var time = data.time;
	     var enterGuest = data.enterGuest;
	     
	     if(enterGuest != null) {
	    	 alert(memId + "님이 입장 하셨습니다.");
	    	 $("#messageArea").prepend(memId + message + "  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[" + time + "]" + "<br/>");
	     } else {
		     $("#messageArea").prepend(memId + " : " + message + "  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[" + time + "]" + "<br/>");
		     console.log("메세지 쏘는중 >> " + data);
	    	 
	     }
	     
    }
    // send a message
    $("#sendBtn").click(function(){
   		    var message = $("#msg").val()
   		    var memId = $("#memId").val()
   		    
   
   		    // don't send when content is empty
   		    // 채팅 입력 칸이 비어있지 않을 경우만 정보를 Json형태로 전송
   		    if(message != "") {			
   			let msg = {
    		   	'memId' : memId,
   		    	'message' : message,
   		    	'time' : new Date().toTimeString().split(' ')[0]
   		      	}

   		    	if(message != null) {
   		    	websocket.send(JSON.stringify(msg));	// websocket handler로 전송(서버로 전송)
   		    	
	   		    	$.ajax({
	   					type:'post',
	   					url:'/module/chat.do',
	   					data:{
	   						memId:memId,
	   						chatCont:message
	   					},
	   					success:function(){
	   						
	   					}
	   				})
   		    	
   		    	}
   		    	document.getElementById("msg").value = '';
   		 	  } 
    });
});
	

	function getOriginalName(fileName){
		if(checkImageType(fileName)){
			return;
		}		
		var idx = fileName.indexOf("_") + 1;
		return fileName.substr(idx);
	}
	function getThumbnailName(fileName){
		var front = fileName.substr(0,12);
		var end = fileName.substr(12);
		
		console.log("front : " + front);
		console.log("end : " + end);
		
		return front + "s_" + end;
	}

	function checkImageType(fileName){
		var pattern = /jpg|gif|png|jpeg/i;
		return fileName.match(pattern); // 패턴과 일치하면 true (너 이미지 맞구나?)
	}



</script>


