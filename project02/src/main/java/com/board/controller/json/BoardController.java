package com.board.controller.json;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.board.domain.Board;
import com.board.service.BoardService;
import com.google.gson.Gson;

@Controller
@RequestMapping("/board/")
public class BoardController {
  @Autowired BoardService boardService;
  
  //게시물
  @RequestMapping(path="list", produces="application/json;charset=UTF-8")
  @ResponseBody
  public String list(
      @RequestParam(defaultValue="1") int pageNo, 
      @RequestParam(defaultValue="5") int pageSize) {
    HashMap<String,Object> result = new HashMap<>();
    try {
      List<Board> list = boardService.getBoardList(pageNo, pageSize);
      
      result.put("status", "success");
      result.put("data", list);
    } catch (Exception e) {
      result.put("status", "failure");
    }
    return new Gson().toJson(result);
  }
  
  @RequestMapping(
      path="detail", 
      produces="application/json;charset=UTF-8")
  @ResponseBody
  public String detail(int no) {
    HashMap<String,Object> result = new HashMap<>();
    try {
      result.put("status", "success");
      result.put("data", boardService.getBoard(no));
    } catch (Exception e) {
      result.put("status", "failure");
    }
    return new Gson().toJson(result);
  }
  
  @RequestMapping(
      path="add", 
      method=RequestMethod.POST,
      produces="application/json;charset=UTF-8")
  @ResponseBody
  public String add(Board board) {
    HashMap<String,Object> result = new HashMap<>();
    try {
    	System.out.println("Board : "+board);
    	boardService.addBoard(board);
    	result.put("status", "success");
    	}catch (Exception e) {
    	result.put("status", "failure");
    	}
    return new Gson().toJson(result);
  }
  
  @RequestMapping(
      path="update", 
      method=RequestMethod.POST,
      produces="application/json;charset=UTF-8")
  @ResponseBody
  public String update(Board board) {
    HashMap<String,Object> result = new HashMap<>();
    try {
      boardService.updateBoard(board);
      result.put("status", "success");
    } catch (Exception e) {
      result.put("status", "failure");
    }
    return new Gson().toJson(result);
  }
  
  @RequestMapping(
      path="delete", 
      produces="application/json;charset=UTF-8")
  @ResponseBody
  public String delete(int no) {
    HashMap<String,Object> result = new HashMap<>();
    try {
      boardService.deleteBoard(no);
      result.put("status", "success");
    } catch (Exception e) {
      result.put("status", "failure");
    }
    return new Gson().toJson(result);
  }
  
  @RequestMapping(path="searchList", produces="application/json;charset=UTF-8")
  @ResponseBody
  public String searchList(
      @RequestParam(defaultValue="1") int pageNo, 
      @RequestParam(defaultValue="5") int pageSize,
      String sValue, String sWord) {
    HashMap<String,Object> result = new HashMap<>();
    try {
    	System.out.println("sValue : "+sValue+"/ sWord : "+sWord);
    	if(sWord==null || sWord==""){
    		System.out.println("BoardList start...");
    		List<Board> list = boardService.getBoardList(pageNo, pageSize);
    		result.put("status", "success");
    	    result.put("data", list);
    	}else{
    		System.out.println("SearchList start...");
    		List<Board> list = boardService.getSearchList(pageNo, pageSize, sValue, sWord);
    		result.put("status", "success");
    	    result.put("data", list);
    	}
      
    } catch (Exception e) {
      result.put("status", "failure");
    }
    return new Gson().toJson(result);
  }
  
  //리플
  @RequestMapping(path="replyList", produces="application/json;charset=UTF-8")
  @ResponseBody
  public String replyList(
      @RequestParam(defaultValue="1") int pageNo, 
      @RequestParam(defaultValue="30") int pageSize,
      @RequestParam int no) {
    HashMap<String,Object> result = new HashMap<>();
    try {
      List<Board> list = boardService.getReplyList(no);
      result.put("status", "success");
      result.put("data", list);
    } catch (Exception e) {
      result.put("status", "failure");
    }
    return new Gson().toJson(result);
  }
  
  @RequestMapping(
	      path="addReply", 
	      method=RequestMethod.POST,
	      produces="application/json;charset=UTF-8")
	  @ResponseBody
	  public String addReply(Board board) {
	    HashMap<String,Object> result = new HashMap<>();
	    try {
	      boardService.addReply(board);
	      result.put("status", "success");
	    } catch (Exception e) {
	      result.put("status", "failure");
	    }
	    return new Gson().toJson(result);
	  }
  
  @RequestMapping(path = "checkPwd", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String checkPwd(String pwd,int bno) {
		HashMap<String, Object> result = new HashMap<>();
		System.out.println("checkPwd Controller Start...");
		try {
			System.out.println("입력받은 PWD : "+pwd);
			System.out.println("입력받은 게시물 번호 : "+ bno +" / 입력받은 게시물 비밀번호 : "+boardService.getBoard(bno).getPwd());
			
			result.put("data", boardService.getBoard(bno).getPwd());
			result.put("status", "success");
		} catch (Exception e) {
			result.put("status", "failure");
		}
		return new Gson().toJson(result);
	}
}









