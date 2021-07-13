<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

<title>loginForm.jsp</title>

<script>
$(document).ready(function(){
	$("#test").click(function() {
		url="/member/listAll"
		$.get(url, function(rData){
			console.log(rData);
		});
	});
	$("#user_email").click(function(){
		// 정병준바보바보
	});
});
</script>	

</head>

<body>
	<div id="wallpaper" style="height: 100vh; background-color: #dae0e6">
		<div class="container h-100">
			<div class="row h-100 justify-content-center align-items-center">
				<div class="col-3"></div>
				<div class="col-6">
					<div class="card shadow" style="max-height:100vh;overflow-y:auto">
						<div class="card-header">
							<h4 class="card-title">로그인</h4>
						</div>
						<div class="card-body">
							<form>
								<div class="form-group">
									<label for="user_email">이메일</label> <input type="email"
										class="form-control" id="user_email"
										name="user_email" placeholder="이메일을 입력해주세요" required>
								</div>
								<div class="form-group">
									<label for="user_pw">비밀번호</label> <input type="password"
										class="form-control" id="user_pw"
										name="user_pw" placeholder="비밀번호를 입력해주세요" required>
								</div>
								<div class="checkbox">
									<label>
										<input type="checkbox" value="saveUser_id">
											아이디 저장
										<a href="#">아이디 또는 비밀번호를 잊으셨나요?</a>
									</label>
								</div>
								<div>
									<button class="btn btn-primary" type="submit">로그인</button>
								</div>
								<div>
									<label>
											K-Merz가 처음이신가요?
										<a href="/joinForm">회원가입</a>
									</label>
								</div>
							</form>
						</div>
					</div>
				</div>
				<div class="col-3"></div>
			</div>
		</div>
	</div>
</body>
</html>