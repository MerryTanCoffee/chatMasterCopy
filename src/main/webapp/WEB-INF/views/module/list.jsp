<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<body>

		<c:choose>
			<c:when test="${empty chatList }">
				<tr>
					<td>채팅이 존재하지 않습니다.</td>
				</tr>
			</c:when>
			<c:otherwise>
				<c:forEach items="${chatList }" var="chat">
					<tr>
						<td>아이디 : </td>
						<td>내용 : </td>
						<td>일자 : </td>
					</tr>
					<tr>
						<td>${chat.memId }</td>
						<td>${chat.chatCon }</td>
						<td>${chat.chatDate }</td>
					</tr>
				</c:forEach><br/>
			</c:otherwise>
		</c:choose>
</body>
</html>