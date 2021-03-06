package com.kmerz.app;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.kmerz.app.service.BannerService;
import com.kmerz.app.service.CategoryService;
import com.kmerz.app.service.CommentService;
import com.kmerz.app.service.CommunityService;
import com.kmerz.app.service.DeclaredService;
import com.kmerz.app.service.MemberService;
import com.kmerz.app.service.MemberServiceImpl;
import com.kmerz.app.service.PostService;
import com.kmerz.app.util.AttachmentProcessing;
import com.kmerz.app.util.ContentReadAndWrite;
import com.kmerz.app.vo.CommunityVo;
import com.kmerz.app.vo.DeclaredVo;
import com.kmerz.app.vo.MemberVo;
import com.kmerz.app.vo.PostsVo;
import com.kmerz.app.vo.SteamAppVo;



@Controller
@RequestMapping(value = "/")
public class HomeController {
	
	@Inject
	CommunityService commService;
	
	@Inject
	PostService postService;
	
	@Inject
	MemberService memService;
	
	@Inject
	CategoryService cateService;
	
	@Inject
	CommentService commentService;
	
	@Inject

	DeclaredService declaredService;
	@Inject
	BannerService bannerService;

	
	@RequestMapping
	public String home(Model model, HttpSession session) {
		// 커뮤니티 리스트
		List<CommunityVo> commList = commService.getCommunityList();
		// 승인된 모든 게시글
		List<PostsVo> postList = postService.selectAllowPosts();
		// 로그인
		MemberVo memberVo = (MemberVo)session.getAttribute("loginVo");
		int userPostCount = 0;
		int userCommentCount = 0;
		int user_point = 0;
		// 로그인이 되어 있을때 유저정보
		if(memberVo != null) {
			int user_no = memberVo.getUser_no();
			userPostCount = postService.getUserPostCount(user_no);
			userCommentCount = commentService.getUserCommentCount(user_no);
			user_point = memberVo.getUser_point();
		}
		List<SteamAppVo> bannerList = bannerService.getBannerList();
		model.addAttribute("bannerList", bannerList);
		model.addAttribute("commList", commList);
		model.addAttribute("postList", postList);
		model.addAttribute("userPostCount", userPostCount);
		model.addAttribute("userCommentCount", userCommentCount);
		model.addAttribute("user_point", user_point);
		return "MainPage";
	}
	@RequestMapping(value="/count")
	public void count(HttpServletResponse res) throws Exception {
		int post_count = postService.countPosts();
		res.setContentType("text/html; charset=utf-8");
		PrintWriter out = res.getWriter();
		System.out.println("카운트 "+post_count);
		out.print("" + post_count);
		out.close();
	}
	@RequestMapping(value="posting")
	public String posting(Model model, HttpSession session) {
		MemberVo memberVo = (MemberVo)session.getAttribute("loginVo");
		System.out.println(memberVo);
		if(memberVo.getUser_status() != MemberServiceImpl.STATUS_WRITE_LOCK) {
			List<CommunityVo> commList = commService.getCommunityList();
			model.addAttribute("commList", commList);
			return "PostingPage";
		} else {
			return "redirect:/";
		}
		
	}
	
	// 게시글 신고하기
	@ResponseBody
	@RequestMapping(value="postDeclaring", method=RequestMethod.POST)
	public String postDeclaring(DeclaredVo declaredVo) throws Exception{
		System.out.println("게시글 신고하기: "+declaredVo);
		declaredService.addPostDeclared(declaredVo);
		return "success";
	}
	
	@RequestMapping(value="/deletePost", method=RequestMethod.POST)
	public String deletePost(@RequestParam int post_no) {
		postService.updateStatus(post_no, -1);
		System.out.println("delete");
		return "redirect:/";
	}
	
	// 파일 드래그앤 드랍 했을때 
	@RequestMapping(value="/uploadFile", method=RequestMethod.POST)
	public String uploadFile(@RequestParam MultipartFile[] files,Model model) {
		int seqPostNo = postService.selectCurrentSeq() + 1;
		model.addAttribute("index", files.length);
		Map<Integer, String> mType = new HashMap<Integer, String>();
		for(int i = 0; i < files.length; i++) {
			System.out.println(files[i].getOriginalFilename());
			String type = files[i].getContentType();
			System.out.println(type);
			
			String filetype = type.substring(0, type.indexOf("/"));
			String file_ext = type.substring(type.indexOf("/"), type.length());
			
			if(file_ext.equals("/gif")) {
				filetype = "video";
			}
			System.out.println(file_ext);
			mType.put(i, filetype);
			String path = AttachmentProcessing.MediaFileNameProcessing(seqPostNo);
			if(filetype.equals("video")) {
				AttachmentProcessing.TranscodingMP4(files[i],path);			
				model.addAttribute("path_" + i,path);
			}
			if(filetype.equals("image")) {
				AttachmentProcessing.TranscodingJpg(files[i],path);
				model.addAttribute("path_" + i,path);
			}
		}
		model.addAttribute("mediaType",mType);
		return "include/media";
	}
	
	@RequestMapping(value="/editPost", method=RequestMethod.POST)
	public String editPost(@RequestParam int post_no, @RequestParam String community_id,
									@RequestParam int category_no, @RequestParam String post_title,
									@RequestParam MultipartFile file,
									Model model) {
		String fileName = ContentReadAndWrite.WriteContent(file, post_no);
		PostsVo postVo = new PostsVo();
		postVo.setPost_no(post_no);
		postVo.setCategory_no(category_no);
		postVo.setCommunity_id(community_id);
		postVo.setPost_title(post_title);
		postVo.setPost_content_file(fileName);
		postService.updatePost(postVo);
		model.addAttribute("post_no", post_no);
		return "redirect:/include/modal";
	}


	@RequestMapping(value="/loadBanner")
	public String loadBanner(Model model) {
		
		return "";
	}

}
