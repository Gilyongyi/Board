package com.board.domain;

import java.io.Serializable;
import java.sql.Date;

public class Board implements Serializable {
	// 직렬화(바이트 배열로 변환하여 출력)할 때 데이터의 버전 정보를 기록하기 위함.
	// => 역직렬화(바이트 배열을 읽어 객체로 만들기)하는 쪽에서 
	private static final long serialVersionUID = 1L;

	private int no;				//게시물 번호
	private String title;		//게시물 제목
	private String content;		//게시물 내용
	private Date createdDate;	//게시물 등록일
	private String user;		//게시물 작성자
	private int totalCount;		//게시물 총 갯수
	private String pwd;			//게시물 비밀번호
	
	private String sValue;		//검색 조건
	private String sWord;		//검색어
	
	private int rno;			//댓글 번호
	private String rcontent;	//댓글 내용
	private Date rcreateDate;	//댓글 등록일
	private String ruser;		//댓글 작성자
	private int rp;				//댓글 갯수
	
	
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getRp() {
		return rp;
	}
	public void setRp(int rp) {
		this.rp = rp;
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getsValue() {
		return sValue;
	}
	public void setsValue(String sValue) {
		this.sValue = sValue;
	}
	public String getsWord() {
		return sWord;
	}
	public void setsWord(String sWord) {
		this.sWord = sWord;
	}
	public int getRno() {
		return rno;
	}
	public void setRno(int rno) {
		this.rno = rno;
	}
	public String getRcontent() {
		return rcontent;
	}
	public void setRcontent(String rcontent) {
		this.rcontent = rcontent;
	}
	public Date getRcreateDate() {
		return rcreateDate;
	}
	public void setRcreateDate(Date rcreateDate) {
		this.rcreateDate = rcreateDate;
	}
	public String getRuser() {
		return ruser;
	}
	public void setRuser(String ruser) {
		this.ruser = ruser;
	}
	@Override
	public String toString() {
		return "Board [no=" + no + ", title=" + title + ", content=" + content + ", createdDate=" + createdDate
				+ ", user=" + user + ", totalCount=" + totalCount + ", pwd=" + pwd + ", sValue=" + sValue + ", sWord="
				+ sWord + ", rno=" + rno + ", rcontent=" + rcontent + ", rcreateDate=" + rcreateDate + ", ruser="
				+ ruser + ", rp=" + rp + "]";
	}
	
	
}