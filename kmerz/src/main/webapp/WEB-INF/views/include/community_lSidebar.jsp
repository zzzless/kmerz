<%@page import="com.kmerz.app.vo.CategoryVo"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="lSidebar">
	<div class="communityList border sticky">
	<div class="commHeader hr">
		<span class="font-18">카테고리 리스트</span>
	</div>
		<ul>	
		<%List<CategoryVo> list = (List<CategoryVo>)request.getAttribute("categoryList"); %>
		<%for(int i = 0; i < list.size(); i++){ %>
			<li><a href="/c/<%=list.get(i).getCommunity_id()%>/<%=list.get(i).getCategory_no()%>"><img src="/resources/images/starcraft_small.jpg"><span><%=list.get(i).getCategory_name() %></span></a></li>
			<%}; %>
		</ul>
	</div>
</div>