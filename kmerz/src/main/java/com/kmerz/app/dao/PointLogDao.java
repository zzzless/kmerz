package com.kmerz.app.dao;

import java.util.List;

import com.kmerz.app.vo.PointLogVo;

public interface PointLogDao {
	public void insertPointLog(int user_no, String point_content, int point_score);
	public PointLogVo selectPreUserNo(int user_no);
	public List<PointLogVo> selectUserNo(int user_no);
}
