package com.board.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.board.dao.BoardDao;
import com.board.domain.Board;
import com.board.service.BoardService;

@Service
public class BoardServiceImpl implements BoardService {
	@Autowired BoardDao boardDao;

	@Override
	public void addBoard(Board board) {
		boardDao.insert(board);
	}
	@Override
	public Board getBoard(int no) {
		return boardDao.selectOne(no);
	}

	@Override
	public int updateBoard(Board board) {
		return boardDao.update(board);
	}

	@Override
	public int deleteBoard(int no) {
		return boardDao.delete(no);
	}
	//mysql Method
	/*@Override
	public List<Board> getBoardList(int pageNo, int pageSize) {
		HashMap<String,Object> params = new HashMap<>();
		params.put("startIndex", (pageNo - 1) * pageSize);
		params.put("len", pageSize);
		return boardDao.selectList(params);
	}
	
	@Override
	public List<Board> getSearchList(int pageNo, int pageSize, String sValue, String sWord) {
		HashMap<String,Object> params = new HashMap<>();
		params.put("startIndex", (pageNo - 1) * pageSize);
		params.put("len", pageSize);
		params.put("sValue", sValue);
		params.put("sWord", sWord);
		return boardDao.searchList(params);
	}*/
	
	//oracle Method
	@Override
	public List<Board> getBoardList(int pageNo, int pageSize) {
		HashMap<String,Object> params = new HashMap<>();
		params.put("startIndex", ((pageNo - 1) * pageSize)+1);
		params.put("len", pageNo*pageSize);
		return boardDao.selectList(params);
	}
	@Override
	public List<Board> getSearchList(int pageNo, int pageSize, String sValue, String sWord) {
		HashMap<String,Object> params = new HashMap<>();
		if(pageNo==1){
			params.put("startIndex", (pageNo - 1) * pageSize);
		}else{
		params.put("startIndex", ((pageNo - 1) * pageSize)+1);
		}
		params.put("len", pageNo*pageSize);
		params.put("sValue", sValue);
		params.put("sWord", sWord);
		return boardDao.searchList(params);
	}
	
	//댓글
	@Override
	public List<Board> getReplyList(int no) {
		HashMap<String,Object> params = new HashMap<>();
		params.put("no", no);
		return boardDao.replyList(params);
	}

	@Override
	public void addReply(Board board) {
		boardDao.insertReply(board);
	}
	@Override
	public int deleteReply(int rno) {
		return boardDao.deleteReply(rno);
	}
	

}






