package com.board.service;

import java.util.List;

import com.board.domain.Board;

public interface BoardService {
  //게시물
  void addBoard(Board board);	//게시물 추가
  List<Board> getBoardList(int pageNo, int pageSize);	//게시물 리스트
  Board getBoard(int no);	//게시물 상세보기
  int updateBoard(Board board);	//게시물 업데이트
  int deleteBoard(int no);	//게시물 삭제
  List<Board> getSearchList(int pageNo, int PageSize, String sValue, String sWord);	//게시물 검색
  
  //리플
  List<Board> getReplyList(int no);	//리플 리스트
  void addReply(Board board);		//리플 추가
  int deleteReply(int rno);			//리플 삭제
}














