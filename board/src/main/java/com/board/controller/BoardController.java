package com.board.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.board.domain.BoardVO;
import com.board.service.BoardService;

@Controller
@RequestMapping("/board/")
public class BoardController {

	@Inject
	BoardService service;

	// 게시물 목록
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public void getList(Model model) throws Exception {
		
		List<BoardVO> list = null;
		list = service.list();
		
		model.addAttribute("list",list);
	}
	
	// 게시물 작성
	@RequestMapping(value ="/write", method=RequestMethod.GET)
	public void getWrite() throws Exception{
		
	}
	
	// 게시물 작성
	@RequestMapping(value = "/write", method = RequestMethod.POST)
	public String posttWrite(BoardVO vo) throws Exception {
		service.write(vo);
		
		return "redirect:/board/list";	//모든 작업을 마치고 /board/list 페이지로 이동
	}
	
	
	// 게시물 조회
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public void getView(@RequestParam("bno") int bno, Model model) throws Exception{	// @RequestParam을 이용하면 주소에 있는 특정한 값 가져올 수 있음
																						// 주소에서 bno를 찾아 그값을 int bno로 넣어줌
		
		BoardVO vo = service.view(bno);	// BoardVO를 이용하여 service에서 데이터를 받고
		
		model.addAttribute("view",vo);	// model을 이용해서 뷰에 데이터를 넘겨줌, 넘겨주는 모델의 이름은 view

	}

	// 게시물 수정
	@RequestMapping(value ="/modify", method = RequestMethod.GET)
	public void getModify(@RequestParam("bno") int bno, Model model) throws Exception{
		
		//수정할 때 내용이 있어야 하므로, 조회랑 똑같이 코드 작성
		
		BoardVO vo = service.view(bno);	
		model.addAttribute("view",vo);	
	}
	
	// 게시물 수정
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public String postmodify(BoardVO vo) throws Exception{
		service.modify(vo);		//뷰에서 컨트롤러로 넘어온 데이터(BoardVO)를 이용해 수정
		return "redirect:/board/view?bno="+vo.getBno();		// 현재 bno에 해당되는 조회 페이지로 이동
	}
	
	// 게시물 삭제
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String getDelete(@RequestParam("bno") int bno) throws Exception{
		service.delete(bno);
		return "redirect:/board/list";
	}
	
	// 게시물 목록 + 페이징 추가
		@RequestMapping(value = "/listPage", method = RequestMethod.GET)
		public void getListPage(Model model, @RequestParam("num") int num) throws Exception {
			
			// 게시물 총 갯수
			int count = service.count();
			
			// 한 페이지에 출력할 게시물 갯수
			int postNum = 10;
			
			// 하단 페이징 번호([게시물 총 갯수 + 한 페이지에 출력할 갯수]의 올림)
			int displayPost = (num-1) * postNum;
			
			List<BoardVO> list = null;
			list = service.listPage(displayPost, postNum);
			model.addAttribute("list",list);
			model.addAttribute("postNum", postNum);
		}
	
	
	
}