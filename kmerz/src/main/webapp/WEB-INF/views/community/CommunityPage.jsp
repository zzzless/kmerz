<%@page import="com.kmerz.app.vo.PostsVo"%>
<%@page import="com.kmerz.app.util.ContentReadAndWrite"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	List<CategoryVo> categoryList = (List<CategoryVo>) request.getAttribute("categoryList");
	String community_id = (String)request.getAttribute("community_id");
%>
<!DOCTYPE html>
<html>
<head lang="ko">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0 user-scale=no">
<meta charset="UTF-8">
<title>K-Merz The Best Game Community</title>
<link rel="stylesheet" href="/resources/css/MainPage.css?ver<%=System.currentTimeMillis()%>">
<link rel="stylesheet" href="/resources/css/lSidebar.css?v13">
<link rel="stylesheet" href="/resources/css/rSidebar.css?v13">
<link rel="stylesheet" href="/resources/css/header.css?ver6">
<link rel="stylesheet" href="/resources/css/font.css?4">
<link rel="stylesheet" href="/resources/css/margin.css?4">
<link rel="stylesheet" href="/resources/css/slider.css?4">
<link rel="stylesheet" href="/resources/css/icon.css?5">
<link rel="stylesheet" href="/resources/css/transform.css?6">
<script src="/resources/script/CommunityPage.js?ver<%=System.currentTimeMillis()%>">
</script>

<!-- JQeury CDN -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script>
	var community_id = "<%=community_id%>";
	$(document).ready(function(){
		console.log("test1");
		$(".modal-section").on("click","#postDeclared",function(e){
			console.log("test2");
			e.preventDefault();
			url = "/postDeclaring";
			sData = {
				"target_id":$(".target_id").text(),
				"target_type":$(".target_type").text(),
				"user_no":$(".user_no").text(),
				"target_user_no":$(".target_user_no").text()
			};
			console.log(sData);
			$.post(url, sData, function(rData){
				console.log(rData);
			});
		});
		getPosts();
		getPosts();
	});
</script>

</head>

<body>
	<div class="modal-section">
	
	</div>
	<!-- 상단 메뉴바 -->
	<div>
		<%@ include file="../include/community_top_menu_bar.jsp"%>
	</div>
	<!-- 배너 -->
	<%-- <div>
		<%@ include file="../include/banner.jsp"%>
	</div> --%>
	
	<section class="postSection">
		<div class="posts">
		<!-- 왼쪽 사이드 바 -->
			<%@ include file="../include/community_lSidebar.jsp" %>
			<div class="postList">
				<div class="sticky border">
					<!-- 게시글 정렬 sticky bar -->
					<div>
						<%@ include file="../include/sort_bar.jsp"%>
					</div>
					<!-- 게시글 작성 sticky bar  -->
					<div>
						<%@ include file="../include/posting_bar.jsp"%>
					</div>
				</div>	
				<!-- 게시글 양식 -->
				<div id="communityPost_container"></div>
			</div>
			<!-- 오른쪽 사이드 바 -->
			<%@ include file="../include/rSidebar.jsp"%>
		</div>
	</section>
</body>
</html>
