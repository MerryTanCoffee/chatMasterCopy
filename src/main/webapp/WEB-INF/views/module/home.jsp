<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<title>Web Socket</title>
<body>
	<div id="page">
		<div id="header">
			<a href="/" title="홈으로">홈으로</a><br/>
		</div>
		<div id="center">
			<div id="menu">
			<c:if test="${memId==null}">
					<a href="/module/signup.do">회원가입</a><br/>
					<a href="/module/login.do">로그인</a><br/>
					
			</c:if>
			<c:if test="${memId!=null }">
				<a href="/module/chatList.do">${memId }의 채팅내용</a><br/>
				<a href="#" id="chat" title="채팅">${memId }의 채팅 시작하기</a><br/>
				<a href="/module/logout.do">로그아웃</a><br/>
			</c:if>
			</div>
			<div id="content">
			<jsp:include page="${pageName}"></jsp:include>
			</div>
		</div>
		<div id="footer">
			<h4>Copyright Web Socket Practice. All rights Reserved.</h4>
		</div>
	</div>
</body>
<script>
	$("#chat").on('click',function(e){
		e.preventDefault();
		window.open("/module/chat.do","chat","width=500, height=800, top=200, left=200");
        // 경로, 파일, 너비, 높이, 위치 지정
	})
</script>