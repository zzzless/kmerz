package com.kmerz.app.Controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kmerz.app.service.MemberLogService;
import com.kmerz.app.service.MemberLogServiceImpl;
import com.kmerz.app.service.MemberService;
import com.kmerz.app.service.MemberServiceImpl;
import com.kmerz.app.vo.MemberLogVo;
import com.kmerz.app.vo.MemberVo;

@Controller
@RequestMapping(value = "/m")
public class MemberController {
	@Inject
	private MemberService memberService;
	
	@Inject
	private MemberLogService memberLogService;
	
	// 로그인 화면
	@RequestMapping(value = "/loginForm", method = RequestMethod.GET)
	public String loginForm() {
		return "member/loginForm";
	}
	
	// 리퀘스트가 있는 로그인 화면
	@RequestMapping(value="/requestLoginForm", method=RequestMethod.GET)
	public String reqLoginForm() {
		return "member/loginForm";
	}
	
	// 로그인
	@RequestMapping(value="/loginRun", method = RequestMethod.POST)
	public String loginRun(String user_id, String user_pw, RedirectAttributes rttr,
			HttpSession session) {
		MemberVo memberVo = memberService.login(user_id, user_pw);
		String resultLogin = null;
		String page = "member/loginForm";
		// 로그인 성공
		if(memberVo != null) {
			
			switch(memberVo.getUser_status()) {
			case MemberServiceImpl.STATUS_DENY:
				// 로그인 불가능
				resultLogin = "deny";
				page = "redirect:/";
				break;
			case MemberServiceImpl.STATUS_CLOSE:
				// 탈퇴한 회원
				resultLogin = "close";
				page = "redirect:/";
				break;
			case MemberServiceImpl.STATUS_ALLOW:
				// 로그인 가능
				resultLogin = "success";
				page = "redirect:/";
				session.setAttribute("loginVo", memberVo);
				break;
			case MemberServiceImpl.STATUS_WRITE_LOCK:
				// 로그인 가능하지만 글쓰기 안됨
				resultLogin = "write_lock";
				page = "redirect:/";
				session.setAttribute("loginVo", memberVo);
				break;
			}
			
			// 이전 페이지로 돌아가기
			String requestPath = (String)session.getAttribute("requestPath");
	 		session.removeAttribute("requestPath");
	 		if(requestPath != null) {
	 			//System.out.println(requestPath);
		 		if (requestPath.equals("/c/createForm")) {
		 			page = "community/createCommunityForm";
		 		}
	 		}
			
	 	// 로그인 실패
		} else {
			resultLogin = "fail";
			page = "redirect:/m/loginForm";
		}
		
		
		// 로그
		// ip 가져오기
		HttpServletRequest req = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		String ip = req.getHeader("X-FORWARDED-FOR");
		if (ip == null)
			ip = req.getRemoteAddr();
		MemberLogVo mlogVo = new MemberLogVo();
		if(session.getAttribute("loginVo") == null) {
			mlogVo.setMember_logtype(MemberLogServiceImpl.TYPE_LOGIN_FAIL);
		} else {
			mlogVo.setMember_logtype(MemberLogServiceImpl.TYPE_LOGIN_SUCCESS);
		}
		if(memberVo != null) {
			mlogVo.setUser_no(memberVo.getUser_no());
			mlogVo.setUser_id(memberVo.getUser_id());
			mlogVo.setUser_name(memberVo.getUser_name());
			mlogVo.setRequest_ip(ip);
			memberLogService.insertMemberLog(mlogVo);
			// 로그 끝
		}
		rttr.addFlashAttribute("resultLogin", resultLogin);
		return page;
	}
	
	// 로그아웃
	@RequestMapping(value = "/logoutRun", method = RequestMethod.GET)
	public String adminLogoutRun(HttpSession session) throws Exception {
		MemberVo memberVo = (MemberVo)session.getAttribute("loginVo");
		if(memberVo != null) {
			// 로그
			// ip 가져오기
			HttpServletRequest req = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
			String ip = req.getHeader("X-FORWARDED-FOR");
			if (ip == null)
				ip = req.getRemoteAddr();
			MemberLogVo mlogVo = new MemberLogVo();
			mlogVo.setMember_logtype(MemberLogServiceImpl.TYPE_LOGOUT);
			mlogVo.setUser_no(memberVo.getUser_no());
			mlogVo.setUser_id(memberVo.getUser_id());
			mlogVo.setUser_name(memberVo.getUser_name());
			mlogVo.setRequest_ip(ip);
			memberLogService.insertMemberLog(mlogVo);
			// 로그 끝
		}
		session.removeAttribute("loginVo");
		return "redirect:/";
	}

	// 회원가입 화면
	@RequestMapping(value = "/joinForm", method = RequestMethod.GET)
	public String joinForm() {
		return "member/joinForm";
	}
	
	// 회원가입
	@RequestMapping(value = "/joinRun", method = RequestMethod.POST)
	public String joinMember(MemberVo memberVo, RedirectAttributes rttr) {
		memberService.joinMember(memberVo);
		
		// 로그
		// ip 가져오기
		HttpServletRequest req = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		String ip = req.getHeader("X-FORWARDED-FOR");
		if (ip == null)
			ip = req.getRemoteAddr();
		MemberLogVo mlogVo = new MemberLogVo();
		mlogVo.setMember_logtype(MemberLogServiceImpl.TYPE_SIGNUP);
		mlogVo.setUser_no(memberVo.getUser_no());
		mlogVo.setUser_id(memberVo.getUser_id());
		mlogVo.setUser_name(memberVo.getUser_name());
		mlogVo.setRequest_ip(ip);
		memberLogService.insertMemberLog(mlogVo);
		// 로그 끝
		
		//System.out.println("회원가입 성공");
		rttr.addFlashAttribute("resultJoin", "success");
		return "redirect:/";
	}

	// 모든 회원 정보
//	@ResponseBody
//	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
//	public List<MemberVo> getAllMember() {
//		List<MemberVo> list = memberService.getAllMembers();
//		return list;
//	}
	
	// 회원정보 보기 페이지
	@RequestMapping(value = "/userInfo", method = RequestMethod.GET)
	public String getUserInfo(HttpSession session) {
		return "member/userInfo";
	}
	
	// 회원비밀번호 변경 페이지
	@RequestMapping(value = "/userPasswordChangeForm", method = RequestMethod.GET)
	public String userPasswordChangeForm() {
		return "member/userPasswordChangeForm";
	}
	
	// 회원 프로필 사진 변경 페이지
	@RequestMapping(value = "/userProfileImagesChangeForm", method = RequestMethod.GET)
	public String userProfileImagesChangeForm() {
		return "member/userProfileImagesChangeForm";
	}
	
	// 비밀번호 찾기 페이지
	@RequestMapping(value = "/findPasswordForm", method = RequestMethod.GET)
	public String findPasswordForm() {
		return "member/findPasswordForm";
	}
	
	// 회원 탈퇴 페이지
	@RequestMapping(value = "/userSecessionForm", method = RequestMethod.GET)
	public String userSecessionForm() {
		return "member/userSecessionForm";
	}
	
	// 이메이 중복 체크
	@RequestMapping(value = "/userIdCheck", method = RequestMethod.GET)
	@ResponseBody
	public String userIdCheck(String user_id) {
		int count = memberService.getUserIdCheckResult(user_id);
		// System.out.println("user_id: " + user_id);
		String userIdCheckResult = "";
		if(count == 0) {
			userIdCheckResult = "Available";
		} else if(count == 1) {
			userIdCheckResult = "unAvailable";
		}
		return userIdCheckResult;
	}
	
	// 유저 닉네임 변경 가능 여부 체크(사용가능 여부)
	@RequestMapping(value = "/userNameCheck", method = RequestMethod.GET)
	@ResponseBody
	public String userNameCheck(String user_name) {
		// System.out.println("user_name: " + user_name);
		int count = memberService.getUserNameCheckResult(user_name);
		String userNameCheckResult = "";
		if(count == 0) {
			userNameCheckResult = "Available";
		} else if(count == 1) {
			userNameCheckResult = "unAvailable";
		}
		return userNameCheckResult;
	}
	
	// 유저 닉네임 변경
	@RequestMapping(value = "/changeUserName", method = RequestMethod.GET)
	public String changeUserName(String user_name, HttpSession session) {
		// System.out.println("user_name :" + user_name);
		MemberVo getMemberVo = (MemberVo)session.getAttribute("loginVo");
		int user_no = getMemberVo.getUser_no();
		String user_id = getMemberVo.getUser_id();
		String user_pw = getMemberVo.getUser_pw();
		memberService.changeUserName(user_no, user_name);
		session.removeAttribute("loginVo");
		MemberVo membervo = memberService.login(user_id, user_pw);
		session.setAttribute("loginVo", membervo);
		return "redirect:/m/userInfo";
	}
	
	// 유저비밀번호 체크
	@ResponseBody
	@RequestMapping(value = "/userNewPwCheck", method = RequestMethod.GET)
	public String userNewPwCheck(String newPw, String newPwCheck) {
		String strComparison = "";
		if(newPw == null || newPw.isEmpty()) {
			strComparison = "nullNewPw";
		} else if(newPwCheck == null || newPwCheck.isEmpty()) {
			strComparison = "nullNewPwCheck";
		} else if(newPw.equals(newPwCheck)) {
			strComparison = "same";
		} else {
			strComparison = "different";
		}
		return strComparison;
	}
	
	// 비밀번호 확인
	@ResponseBody
	@RequestMapping(value = "/userNowPwCheck", method = RequestMethod.GET)
	public String userNowPwCheck(String nowPw, HttpSession session) {
		MemberVo memberVo = (MemberVo)session.getAttribute("loginVo");
		String user_pw = memberVo.getUser_pw();
		String nowPwResult = "";
		if(user_pw.equals(nowPw)) {
			nowPwResult = "same";
		} else {
			nowPwResult = "different";
		}
		return nowPwResult;
	}
	
	// 유저 비밀번호 변경
	@RequestMapping(value = "/changeUserPw", method = RequestMethod.POST)
	public String changeUserPw(String newPw, HttpSession session) {
		MemberVo getMemberVo = (MemberVo)session.getAttribute("loginVo");
		int user_no = getMemberVo.getUser_no();
		String user_id = getMemberVo.getUser_id();
		memberService.changeUserPw(user_no, newPw);
		session.removeAttribute("loginVo");
		MemberVo memberVo = memberService.login(user_id, newPw);
		session.setAttribute("loginVo", memberVo);
		return "redirect:/m/userInfo";
	}
	
	// 회원탈퇴
	@RequestMapping(value = "/secession", method = RequestMethod.POST)
	public String secession(HttpSession session) {
		MemberVo getMemberVo = (MemberVo)session.getAttribute("loginVo");
		int user_no = getMemberVo.getUser_no();
		memberService.setStatusClose(user_no);
		session.removeAttribute("loginVo");
		return "redirect:/";
	}
}
