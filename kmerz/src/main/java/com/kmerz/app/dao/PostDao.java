package com.kmerz.app.dao;

import java.util.List;

import com.kmerz.app.vo.PostsVo;

public interface PostDao {
	public List<PostsVo> selectAllPosts();
	public PostsVo selectPost(int post_no);
	public List<PostsVo> selectCommunityPostList(String community_id);
	public void posting(PostsVo vo);
}
