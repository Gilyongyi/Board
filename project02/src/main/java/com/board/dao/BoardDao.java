package com.board.dao;

import java.util.List;
import java.util.Map;

import com.board.domain.Board;

public interface BoardDao {
  
	//게시물
	void insert(Board board); // Create
	List<Board> selectList(Map<String,Object> params); // Read List
	Board selectOne(int no); // Read
	int update(Board board); // Update
	int delete(int no); // Delete
	List<Board> searchList(Map<String,Object> params);	//List Search
	
	//댓글
	List<Board> replyList(Map<String,Object> params);	//Read List
	void insertReply(Board board);
}













