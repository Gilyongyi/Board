package com.board.controller.basic;

import java.io.File;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.board.domain.Board;
import com.board.service.BoardService;

@Controller
@RequestMapping("/board/")
public class BoardController {
  @Autowired BoardService boardService;
  
  @RequestMapping(
	      path="add.do" 
	      ,method=RequestMethod.POST
	      )
	  @ResponseBody
	  public String add(@ModelAttribute("board") Board board
			  ,@RequestParam("upFile") MultipartFile upFile
			  ) {
	    HashMap<String,Object> result = new HashMap<>();
	    System.out.println("Basic Controller add method start.....");
	    try {
	    	if(upFile!=null){
	    		System.out.println("File is exists...");
	    		String fileName = upFile.getOriginalFilename();
	    		board.setFileName(fileName);
	    		File uploadFile = new File("C:\\Users\\yongyi\\git\\Board\\project02\\src\\main\\webapp\\file\\"+fileName);
	    		upFile.transferTo(uploadFile);
	    	}
	    	boardService.addBoard(board);
	    	result.put("status", "success");
	    	System.out.println("success");
	    	}catch (Exception e) {
	    	result.put("status", "failure");
	    	System.out.println("fail");
	    	}
	    return "http://localhost:8080/project02/board/main.html";
	  }
}