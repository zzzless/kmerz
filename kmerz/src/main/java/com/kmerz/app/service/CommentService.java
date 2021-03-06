package com.kmerz.app.service;

import java.util.List;

import com.kmerz.app.vo.CommentVo;

public interface CommentService {
	public List<CommentVo> selectCommentOnPost(int post_no);
	public void insertComment(CommentVo commentVo);
	public int getUserCommentCount(int user_no);
}
