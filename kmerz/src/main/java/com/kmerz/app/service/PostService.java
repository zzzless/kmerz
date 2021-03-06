package com.kmerz.app.service;

import java.util.List;

import com.kmerz.app.dto.PostPagingDto;
import com.kmerz.app.vo.PostsVo;

public interface PostService {
	public int getCountAllPosts(PostPagingDto postPagingDto);
	public List<PostsVo> selectAllPosts(PostPagingDto postPagingDto);
	public List<PostsVo> selectAllowPosts();
	public PostsVo selectPost(int post_no);
	public PostsVo viewPost(int post_no);
	public List<PostsVo> getCommunityPostList(String community_id);
	public List<PostsVo> getCategoryPostList(String community_id, int category_no);
	public void posting(PostsVo vo);
	public List<PostsVo> getUserPostList(int user_no);
	public int getUserPostCount(int user_no);
	public int getNewPostSeq();
	public PostsVo selectLoadPost(int init_post);
	public PostsVo selectLoadCommunityPost(int init_post, String community_id);
	public void updateStatus(int target, int status);
	public void updatePost(PostsVo postsVo);
	public void lockPost(int post_no);
	public List<PostsVo>  lockPostList(List<Integer> postnoList);
	public void unlockPost(int post_no);
	public List<PostsVo>  unlockPostList(List<Integer> postnoList);
	public int countPosts();
	public int selectCurrentSeq();
	public List<PostsVo> selectDailyPost();
	public int countCommPosts(String community_id);
}
