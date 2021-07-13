package com.kmerz.app.Controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kmerz.app.service.MemberService;
import com.kmerz.app.vo.MemberVo;

@Controller
@RequestMapping(value = "/m")
public class MemberController {
	@Inject
	private MemberService memberService;

	// 로그인 화면
	@RequestMapping(value = "/loginForm", method = RequestMethod.GET)
	public String loginForm() {
		return "loginForm";
	}

	// 회원가입 화면
	@RequestMapping(value = "/joinForm", method = RequestMethod.GET)
	public String joinForm() {
		return "joinForm";
	}
	
	// 회원가입
	@RequestMapping(value = "/joinRun", method = RequestMethod.POST)
	public String joinMember(MemberVo memberVo, RedirectAttributes rttr) {
		memberService.joinMember(memberVo);
		System.out.println("회원가입 성공");
		rttr.addFlashAttribute("resultJoin", "success");
		return "redirect:/";
	}

	// 모든 회원 정보
	@ResponseBody
	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public List<MemberVo> getAllMember() {
		List<MemberVo> list = memberService.getAllMembers();
		return list;
	}
}
