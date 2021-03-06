package com.kmerz.app.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.kmerz.app.dto.MemberPagingDto;
import com.kmerz.app.vo.MemberVo;

@Repository
public class MemberDaoImpl implements MemberDao{

	private static final String NAMESPACE = "com.kmerz.app.member.";
	
	@Inject
	SqlSession sqlsession;
	
	@Override
	public int selectSeqUserNO() {
		// 회원가입 user_no 시퀀스
		return sqlsession.selectOne(NAMESPACE+"selectSeqUserNO");
	}
	
	// 회원 추가
	@Override
	public void insertMember(MemberVo memberVo) {
		sqlsession.insert(NAMESPACE+"insertMember", memberVo);
	}

	// 모든 회원 검색
	@Override
	public List<MemberVo> selectAll(MemberPagingDto memberPagingDto) {
		List<MemberVo> list = sqlsession.selectList(NAMESPACE+"selectAll", memberPagingDto);
		return list;
	}
	
	@Override
	public int selectAllCount(MemberPagingDto memberPagingDto) {
		// 모든 회원 카운트
		return sqlsession.selectOne(NAMESPACE+"selectAllCount", memberPagingDto);
	}


	// 로그인
	@Override
	public MemberVo selectUser(String user_id, String user_pw) {
		Map<String, String> map = new HashMap<>();
		map.put("user_id", user_id);
		map.put("user_pw", user_pw);
		MemberVo memberVo = sqlsession.selectOne(NAMESPACE + "selectUser", map);
		return memberVo;
	}

	@Override
	public void updateCurrentLogin(int user_no) {
		// 최근 로그인 업데이트
		sqlsession.update(NAMESPACE+"updateCurrentLogin", user_no);
	}
	
	
	// id로 유저 정보 찾기
	@Override
	public MemberVo selectID(String user_id) {
		return sqlsession.selectOne(NAMESPACE + "selectID", user_id);
	}
	
	// 유저고유번호로 유저 정보 가져오기
	@Override
	public MemberVo selectNO(int user_no) {
		return sqlsession.selectOne(NAMESPACE+"selectNO", user_no);
	}

	@Override
	public int selectUserIdCount(String user_id) {
		int user_status = 0;
		Map<String, Object> map = new HashMap<>();
		map.put("user_id", user_id);
		map.put("user_status", user_status);
		int count = sqlsession.selectOne(NAMESPACE + "selectUserIdCount", map);
		return count;
	}
	
	@Override
	public int selectUserNameCount(String user_name) {
		int user_status = 0;
		Map<String, Object> map = new HashMap<>();
		map.put("user_name", user_name);
		map.put("user_status", user_status);
		int count = sqlsession.selectOne(NAMESPACE + "selectUserNameCount", map);
		return count;
	}

	@Override
	public void updateUserName(int user_no, String user_name) {
		Map<String, Object> map = new HashMap<>();
		map.put("user_no", user_no);
		map.put("user_name", user_name);
		sqlsession.update(NAMESPACE + "updateUserName", map);
	}

	@Override
	public void updateUserPw(int user_no, String newPw) {
		Map<String, Object> map = new HashMap<>();
		map.put("user_no", user_no);
		map.put("user_pw", newPw);
		sqlsession.update(NAMESPACE + "updateUserPw", map);
	}

	@Override
	public void updateUserProfileImage(int user_no, String filePath) {
		Map<String, Object> map = new HashMap<>();
		map.put("user_no", user_no);
		map.put("filePath", filePath);
		sqlsession.update(NAMESPACE + "updateUserProfileImage", map);
	}

	@Override
	public void updateUserStatus(int user_no, int user_status) {
		// 유저 상태 변경 
		Map<String, Integer> map = new HashMap<>();
		map.put("user_no", user_no);
		map.put("user_status", user_status);
		sqlsession.update(NAMESPACE + "updateUserStatus", map);
	}

	@Override
	public void updateUserPoint(int user_no, int user_point, int user_totalpoint) {
		// 유저 포인트 변경
		Map<String, Integer> map = new HashMap<>();
		map.put("user_no", user_no);
		map.put("user_point", user_point);
		map.put("user_totalpoint", user_totalpoint);
		sqlsession.update(NAMESPACE+"updateUserPoint", map);
	}

}
