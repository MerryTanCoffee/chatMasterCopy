<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>    
<body>
	<h2>채팅 정보 설정하기</h2>

	<form action = "/module/signup.do" method = "post" id="signupForm">
	
	<input type="text" id="memId" name="memId" value="${member.memId }" placeholder="아이디를 입력해주세요."/><font color="red" class="mt-4 mb-2" id="id">${errors.memId }</font>
	<span class="input-group-append">
		<button type="button" class="btn btn-secondary btn-flat" id="idCheckBtn">중복확인</button>
	</span><br/>
	<input type="text" id="memName" name="memName"   value="${member.memName }" placeholder="대화명을 입력해주세요."/>${errors.memName }<br/>
	<input type="text" id="memPw" name="memPw"  value="${member.memPw }" placeholder="비밀번호을 입력해주세요."/>${errors.memPw }<br/>
	<input type="text" id="memEmail" name="memEmail" value="${member.memEmail }"  placeholder="이메일을 입력해주세요."/><br/>
	<button type="button" id="signupBtn">채팅 시작하기</button>
	<br/><hr/>
	</form>
	
</body>	
<script type="text/javascript">
$(function(){
	// 채팅 대화명 설정->입장
var idCheckBtn = $("#idCheckBtn");
var signupBtn = $("#signupBtn");
var signupForm = $("#signupForm");
var idCheckFlag = false;
	
	idCheckBtn.on("click",function(){
		
		$.ajax({
			type : "post",
			url : "/module/idCheck.do",
			data : {memId : $("#memId").val()},
			// 키 : 밸류
			success : function(res){
				console.log(res);
				// 결과로 넘어오는 데이터가 ServiceResult인데
				// EXIST, NOTEXIST 에 따라서
				// "사용 가능한 아이디입니다 또는 사용 중인 아이디입니다." 출력
				// 중복확인 시 idCheckFlag 라는 스위쳐가 발동(true로 변환)
				var text = "사용 가능한 아이디입니다.";	
				if(res == "NOTEXIST") { // 없는 아이디어서 사용 가능
					$("#id").html(text).css("color","green");
					idCheckFlag = true; // 중복확인 했다는 Flag 설정
					console.log(idCheckFlag)
				} else if (res == "EXIST"){
					text = "이미 사용중인 아이디입니다.";
					$("#id").html(text).css("color","red");
				} else {
					text = "일시적 오류.";
					$("#id").html(text);
				}
			}
		});
	});
	
	
	

	signupBtn.on("click", function(){
		var memId = $("#memId").val();
		var memPw = $("#memPw").val();
		var memName = $("#memName").val();
		var memEmail = $("#memEmail").val();
		
		if(idCheckFlag == true) { // 아이디 중복체크 완
			signupForm.submit();
		} else { // 중복 체크 미완
			alert("중복 체크 필");
		}	
	});
	

});
</script>


