<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<h1>[로그인]</h1>
<style>
.loginBox {
	width: 300px;
	margin: 0px auto;
	padding: 10px;
}

input[type=text], input[type=password] {
	width: 100%;
	margin-bottom: 10px;
	font-size: 15px;
}

input[type=submit] {
	padding: 10px 20px 10px 20px;
	background: rgb(57, 109, 192);
	color: white;
}

input[type=checkbox] {
	margin: 10px 0px 10px 0px;
}
</style>
<div class="loginBox">
	<form action="post" name="frm">
		<input type="text" name="memId" placeholder="ID" /> 
		<input	type="password" name="memPw" placeholder="PASS" />
		<input type="submit" value="LOGIN" />
	</form>
</div>
<script>
	$(frm).on('submit', function(e) {
		e.preventDefault();
		var id = $(frm.memId).val();
		var pw = $(frm.memPw).val();
		if (id == "" || pw == "") {
			alert("회원ID와 비밀번호를 입력하세요.");
			return;
		}
		if (!confirm("로그인 하시겠습니까?"))
			return;
		$.ajax({
			type : 'post',
			url : '/module/login.do',
			data : {
				memId : id,
				memPw : pw
			},
			success : function(data) {
				if (data == 0) {
					alert("해당 유저가 존재하지 않습니다.")
				} else if (data == 2) {
					alert("비밀번호를 확인하세요.")
				} else {
					location.href = "/module/home"

				}
			}
		})
	})
</script>