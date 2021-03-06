package com.kmerz.app.service;

import java.util.List;

import com.kmerz.app.dto.MemberPagingDto;
import com.kmerz.app.vo.MemberVo;

public interface MemberService {
	public void joinMember(MemberVo memberVo);
	public List<MemberVo> getAllMembers(MemberPagingDto memberPagingDto);
	public int getAllCount(MemberPagingDto memberPagingDto);
	public MemberVo login(String user_id, String user_pw);
	public MemberVo selectID(String user_id);
	public MemberVo selectNO(int user_no);
	public int getUserNameCheckResult(String user_name);
	public int getUserIdCheckResult(String user_id);
	public void changeUserName(int user_no, String user_name);
	public void changeUserPw(int user_no, String newPw);
	public void changeProfileImage(int user_no, String filePath);
	public void setStatusDeny(int user_no);
	public void setStatusClose(int user_no);
	public void setStatusAllow(int user_no);
	public void setStatusWriteLock(int user_no);
}
