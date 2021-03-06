<%@page import="com.kmerz.app.vo.MemberVo"%>
<%@page import="com.kmerz.app.vo.CommentVo"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	List<CommentVo> mentList = (List<CommentVo>) request.getAttribute("mentList");
%>
<%
	List<MemberVo> memList = (List<MemberVo>) request.getAttribute("memList");
%>
<%
	for (int i = 0; i < mentList.size(); i++) {
%>
<div id="<%=mentList.get(i).getComment_no()%>">
	<input type="checkbox" id="reply-toggleBtn" style="display: none">
	<div class="replyContent">
		<div class="reply-user">
			<div class="reply-user-profile">
				<img src="/resources/images/starcraft_small.jpg">
				<div class="reply-user-status">
					<span> <%=mentList.get(i).getUser_no()%>
					</span>
				</div>
			</div>
			<div class="reply-user-content reply-margin-top"
				onclick="replytoggle(<%=mentList.get(i).getComment_no()%>)">
				<span><%=mentList.get(i).getComment_content()%> </span>
			</div>
			<div class="currentTime reply-margin-top">
				<span>3분전</span>
			</div>
		</div>
		<div class="reply-section">
			<div class="reply-content">
				<div class="reply-form-input">
					<textarea id="comment_content" cols="120" rows="10"></textarea>
				</div>
				<div class="reply-submit">
					<button class="btn">CON</button>
					<button class="btn"
						onclick="addReply()">REPLY</button>
				</div>
			</div>
		</div>
	</div>
</div>
<%
	}
%>