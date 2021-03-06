package com.kmerz.app.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kmerz.app.dto.MemberInfoCardDto;
import com.kmerz.app.dto.MemberPagingDto;
import com.kmerz.app.dto.PostPagingDto;
import com.kmerz.app.service.AdminService;
import com.kmerz.app.service.BannerService;
import com.kmerz.app.service.CommunityService;
import com.kmerz.app.service.DeclaredService;
import com.kmerz.app.service.MemberLogService;
import com.kmerz.app.service.MemberService;
import com.kmerz.app.service.PointLogService;
import com.kmerz.app.service.PostService;
import com.kmerz.app.service.SteamAppService;
import com.kmerz.app.util.ContentReadAndWrite;
import com.kmerz.app.vo.AdminVo;
import com.kmerz.app.vo.BannerVo;
import com.kmerz.app.vo.CommunityVo;
import com.kmerz.app.vo.DeclaredVo;
import com.kmerz.app.vo.MemberLogVo;
import com.kmerz.app.vo.MemberVo;
import com.kmerz.app.vo.PointLogVo;
import com.kmerz.app.vo.PostsVo;
import com.kmerz.app.vo.SteamAppVo;

@Controller
@RequestMapping(value = "/admin")
public class ManagementController {

	@Inject
	AdminService adminService;
	@Inject
	BannerService bannerService;
	@Inject
	SteamAppService steamAppService;
	@Inject
	PostService postService;
	@Inject
	MemberService memberService;
	@Inject
	PointLogService pointLogService;
	@Inject
	DeclaredService declaredService;
	@Inject
	CommunityService communityService;
	@Inject
	MemberLogService memberLogService;
	
	@Autowired
	private JavaMailSender mailSender;
	
	// uri ????????? admin ???????????? ????????? ???????????? ???????????? ???????????? ?????????
	@RequestMapping
	public String admin(HttpSession session) throws Exception {
		AdminVo loginAdminVo = (AdminVo) session.getAttribute("loginAdminVo");
		if (loginAdminVo == null) {
			return "management/AdminLoginPage";
		} else {
			return "management/DashBoardPage";
		}
	}
	
	// ????????? ?????????
	@RequestMapping(value = "/loginForm", method = RequestMethod.GET)
	public String adminLoginForm(HttpSession session) throws Exception {
		if (session.getAttribute("loginAdminVo") != null) {
			return "redirect:/admin/dashBoard";
		}
		return "management/AdminLoginPage";
	}

	// ????????? ??????
	@ResponseBody
	@RequestMapping(value = "/loginRun", method = RequestMethod.POST)
	public void adminLoginRun(String admin_id, String admin_pw, HttpSession session) throws Exception {
		// System.out.println("????????? ???????????? ????????????"+admin_id+ admin_pw);
		AdminVo loginAdminVo = adminService.loginAdmin(admin_id, admin_pw);
		String result = "fail";
		if (loginAdminVo != null) {
			session.setAttribute("loginAdminVo", loginAdminVo);
			System.out.println("????????? ????????????:" + (AdminVo) session.getAttribute("loginAdminVo"));
			result = "success";
		}
		
		session.setAttribute("resultLogin", result);
	}

	// ???????????? ??????
	@RequestMapping(value = "/logoutRun", method = RequestMethod.GET)
	public String adminLogoutRun(HttpSession session) throws Exception {
		session.removeAttribute("loginAdminVo");
		return "redirect:/admin/loginForm";
	}

	// ???????????? ?????????
	@RequestMapping(value = "/dashBoard", method = RequestMethod.GET)
	public String dashBoard(HttpSession session) throws Exception {
		// ????????? ?????? ?????? ??????
		session.removeAttribute("resultLogin");

		return "management/DashBoardPage";
	}

	// ???????????? ????????? ??????
	@Transactional
	@ResponseBody
	@RequestMapping(value = "/dashBoardData", method = RequestMethod.GET)
	public Map<String, Object> dashBoardCard() throws Exception {
		// ?????? ?????????
		List<MemberLogVo> loginList = memberLogService.dailyLoginList();
		// ?????? ???????????? ?????????
		List<PostsVo> postList = postService.selectDailyPost();
		// ?????? ???????????? ????????????
		List<CommunityVo> commList = communityService.selectDailyComm();
		// ?????? ????????????
		List<MemberLogVo> signupList = memberLogService.dailySignUpList();
		Map<String, Object> map = new HashMap<>();
		map.put("loginList", loginList);
		map.put("postList", postList);
		map.put("commList", commList);
		map.put("signupList", signupList);
		return map;
	}
	
	
	
	
	// ?????? ?????? ?????????
	@RequestMapping(value = "/customers")
	public String customers(MemberPagingDto memberPagingDto, Model model) throws Exception {
		// ?????????
		memberPagingDto.setCount(memberService.getAllCount(memberPagingDto));
		// ?????? ?????????
		List<MemberVo> memberList = memberService.getAllMembers(memberPagingDto);
		model.addAttribute("memberList", memberList);
		System.out.println(memberList);
		return "management/CustomersPage";
	}
	
	// ?????? ?????????, ??????
	@ResponseBody
	@RequestMapping(value="/customers/list", method=RequestMethod.GET)
	public Map<String, Object> customerPaging(MemberPagingDto memberPagingDto, Model model) throws Exception{
		int count = memberService.getAllCount(memberPagingDto);
		memberPagingDto.setCount(count);
		List<MemberVo> memberList = memberService.getAllMembers(memberPagingDto);
		
		model.addAttribute("memberPagingDto", memberPagingDto);
		Map<String, Object> map = new HashMap<>();
		map.put("memberList", memberList);
		map.put("memberPagingDto", memberPagingDto);
		return map;
	}
	
	// ?????? ??????
	@Transactional
	@ResponseBody
	@RequestMapping(value="/customers/userInfo", method=RequestMethod.GET)
	public Map<String, Object> getCustomerInfo(int user_no) throws Exception{
		Map<String, Object> map = new HashMap<>();
		// ?????? ??????
		MemberVo memberVo = memberService.selectNO(user_no);
		MemberInfoCardDto cardDto = new MemberInfoCardDto();
		if(memberVo != null) {
			String profileImgPath = memberVo.getUser_profileImage(); 
			if(profileImgPath != null) {
				int index = profileImgPath.indexOf("/sm_");
				if(index > -1) {
					String a = profileImgPath.substring(0, index+1);
					String b = profileImgPath.substring(index+4);
					cardDto.setUser_profileimage(a+b);
				} else {
					cardDto.setUser_profileimage(profileImgPath);					
				}
			}
			cardDto.setUser_no(memberVo.getUser_no());
			cardDto.setUser_name(memberVo.getUser_name());
			cardDto.setUser_id(memberVo.getUser_id());
			cardDto.setUser_point(memberVo.getUser_point());
			cardDto.setUser_status(memberVo.getStr_user_status());
			cardDto.setUser_post_count(postService.getUserPostCount(memberVo.getUser_no()));
		}
		map.put("cardDto", cardDto);
		
		// ?????? ????????? ?????? ??????
		List<PointLogVo> pointList = pointLogService.getPointLogList(user_no);
		map.put("pointList", pointList);
		// ?????? ????????? ?????? ??????
		List<PostsVo> postList = postService.getUserPostList(user_no);
		map.put("postList", postList);
		// ???????????? ???????????? ?????? ??????
		List<CommunityVo> communityList = communityService.getUserCommunityList(user_no);
		map.put("communityList", communityList);
		// ?????? ????????? ?????? ??????
		List<DeclaredVo> declaredList = declaredService.getTargetUserNOList(user_no);
		map.put("declaredList", declaredList);
		
		return map;
	}
	
	
	// ????????? ?????? ??????
	@ResponseBody
	@RequestMapping(value="/customers/userStatus", method=RequestMethod.POST)
	public String userStatus(String str_user_no, String str_user_status) throws Exception {
		System.out.println(str_user_no + " " + str_user_status);
		int user_no = Integer.parseInt(str_user_no);
		int user_status = Integer.parseInt(str_user_status);
		System.out.println(user_no + " " + user_status);
		switch(user_status) {
		case -2:
			memberService.setStatusDeny(user_no);
			break;
		case 0:
			memberService.setStatusAllow(user_no);
			break;
		case 1:
			memberService.setStatusWriteLock(user_no);
			break;
		}
		
		return "success";
	}
	
	// ?????? ?????????
	
	
	// ????????? ????????? ??????
	@ResponseBody
	@RequestMapping(value="/customers/userPoint", method=RequestMethod.POST)
	public String userPoint(@RequestBody PointLogVo pointLogVo) throws Exception {
		pointLogService.addPointLog(pointLogVo);
		return "success";
	}
	
	// ???????????? ?????? ?????????
	@ResponseBody
	@RequestMapping(value = "/mail/mailSending")
	public String mailSending(HttpServletRequest request) {
		String setfrom = "mailtest0516@gmail.com";
		String tomail = request.getParameter("tomail");
		String title = "???????????? ??????????????????.";
		String content = request.getParameter("content");
		System.out.println("tomail: "+tomail + "/ content: "+content);
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
			messageHelper.setFrom(setfrom);		// ????????? ??????
			messageHelper.setTo(tomail);		// ?????? ??????
			messageHelper.setSubject(title);	// ??????????????? ????????? ????????????.
			messageHelper.setText(content);		// ?????? ??????
			mailSender.send(message);
		} catch(Exception e) {
			System.out.println(e);
			return "fail";
		}
		return "success";
	}
	
	// ??????/???????????? ?????? ?????????
	@RequestMapping(value = "/contents/bsSettingPage", method = RequestMethod.GET)
	public String bsSetting(Model model) throws Exception {
		List<SteamAppVo> bannerList = bannerService.getBannerList();
		
		model.addAttribute("bannerAppList", bannerList);
		
		return "management/ContentsBSPage";
	}
	// ?????? ????????????
	@ResponseBody
	@RequestMapping(value = "/contents/setBanner",  method = RequestMethod.POST)
	public String setBanner(@RequestBody List<BannerVo> bannerList) throws Exception {
		bannerService.initBanner(bannerList);
		return "success";
	}
	
	// ????????? ?????? ?????????
	@RequestMapping(value = "/contents/postSettingPage", method = RequestMethod.GET)
	public String postSetting(PostPagingDto postPagingDto, Model model) throws Exception {
		// ?????????
		int count = postService.getCountAllPosts(postPagingDto);
		postPagingDto.setCount(count);
		List<PostsVo> postsVo = postService.selectAllPosts(postPagingDto);
		model.addAttribute("postList", postsVo);
		return "management/ContentsPostPage";
	}
	
	// ????????? ?????????, ??????
	@ResponseBody
	@RequestMapping(value = "/contents/postPaging", method = RequestMethod.GET)
	public Map<String, Object> postPaging(PostPagingDto postPagingDto) throws Exception {
		int count = postService.getCountAllPosts(postPagingDto);
		postPagingDto.setCount(count);
		//System.out.println("IN: " + postPagingDto);
		List<PostsVo> postList = postService.selectAllPosts(postPagingDto);
		
		//model.addAttribute("postPagingDto", postPagingDto);
		//System.out.println("OUT: " + postPagingDto);
		Map<String, Object> map = new HashMap<>();
		map.put("postList", postList);
		map.put("postPagingDto", postPagingDto);
		return map;
	}
	
	// ????????? ?????? ????????????
	@ResponseBody
	@RequestMapping(value = "/contents/getPostInfo", method = RequestMethod.GET)
	public PostsVo getPostInfo(int post_no) throws Exception {
		return postService.selectPost(post_no);
	}
	
	// ????????? ?????? ????????????
	@ResponseBody
	@RequestMapping(value = "/contents/getPostContent", method = RequestMethod.GET, produces = "text/html; charset=utf-8")
	public String getPostContent(String post_content_file) throws Exception {
		//System.out.println("????????? ?????? ????????????: "+post_content_file);
		String result = ContentReadAndWrite.ReadContent(post_content_file);
		//System.out.println(result);
		return result;
	}
	
	// ????????? ??????
	@ResponseBody
	@RequestMapping(value = "/contents/setPostLock", method=RequestMethod.GET)
	public String setPostLock(int post_no) throws Exception {
		postService.lockPost(post_no);
		return "success";
	}
	
	// ????????? ?????? ??????
	@ResponseBody
	@RequestMapping(value = "/contents/setPostUnlock", method=RequestMethod.GET)
	public String setPostUnlock(int post_no) throws Exception {
		postService.unlockPost(post_no);
		return "success";
	}
	
	// ????????? ????????? ??????
	@ResponseBody
	@RequestMapping(value = "/contents/setPostsLock", method=RequestMethod.POST)
	public List<PostsVo> setPostsLock(@RequestBody List<String> post_no) throws Exception {
		List<Integer> postnoList = new ArrayList<>();
		for(int i=0; i<post_no.size(); i++) {
			postnoList.add(Integer.parseInt(post_no.get(i)));
		}
		System.out.println(postnoList);
		List<PostsVo> list = postService.lockPostList(postnoList);
		return list;
	}
	
	// ????????? ????????? ?????? ??????
	@ResponseBody
	@RequestMapping(value = "/contents/setPostsUnlock")
	public List<PostsVo> setPostsUnlock(@RequestBody List<String> post_no) throws Exception {
		List<Integer> postnoList = new ArrayList<>();
		for(int i=0; i<post_no.size(); i++) {
			postnoList.add(Integer.parseInt(post_no.get(i)));
		}
		System.out.println(postnoList);
		List<PostsVo> list = postService.unlockPostList(postnoList);
		return list;
	}
	
	// ?????? ?????? ?????? ?????????
	@RequestMapping(value = "/orders", method = RequestMethod.GET)
	public String oders() throws Exception {
		return "management/OrdersPage";
	}

	// ?????? ?????? ?????????
	@RequestMapping(value = "/products", method = RequestMethod.GET)
	public String products() throws Exception {
		return "management/ProductsPage";
	}
	
	// ????????? ?????????
	@RequestMapping(value = "/reports", method = RequestMethod.GET)
	public String reports() throws Exception {
		return "management/ReportsPage";
	}
}
