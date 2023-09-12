package com.board.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.board.domain.BoardVO;
import com.board.domain.Page;
import com.board.domain.ReplyVO;
import com.board.service.BoardService;
import com.board.service.ReplyService;

@Controller
@RequestMapping("/board/*")
public class BoardController {

	@Inject
	private BoardService service;
	
	@Inject
	private ReplyService replyService;

	// 게시물 목록
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public void getList(Model model) throws Exception {

		List<BoardVO> list = null;
		list = service.list();

		model.addAttribute("list", list);
	}

	// 게시물 작성
	@RequestMapping(value = "/write", method = RequestMethod.GET)
	public void getWrite() throws Exception {

	}

	// 게시물 작성
	@RequestMapping(value = "/write", method = RequestMethod.POST)
	public String posttWrite(BoardVO vo) throws Exception {
		service.write(vo);

		return "redirect:/board/list"; // 모든 작업을 마치고 /board/list 페이지로 이동
	}

	// 게시물 조회
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public void getView(@RequestParam("bno") int bno, Model model) throws Exception { // @RequestParam을 이용하면 주소에 있는 특정한
																						// 값 가져올 수 있음
																						// 주소에서 bno를 찾아 그값을 int bno로 넣어줌
		BoardVO vo = service.view(bno); // BoardVO를 이용하여 service에서 데이터를 받고

		model.addAttribute("view", vo); // model을 이용해서 뷰에 데이터를 넘겨줌, 넘겨주는 모델의 이름은 view
		
		// 댓글 조회
		List<ReplyVO> reply = null;
		reply = replyService.list(bno);
		model.addAttribute("reply",reply);

	}

	// 게시물 수정
	@RequestMapping(value = "/modify", method = RequestMethod.GET)
	public void getModify(@RequestParam("bno") int bno, Model model) throws Exception {

		// 수정할 때 내용이 있어야 하므로, 조회랑 똑같이 코드 작성

		BoardVO vo = service.view(bno);
		model.addAttribute("view", vo);
	}

	// 게시물 수정
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public String postmodify(BoardVO vo) throws Exception {
		service.modify(vo); // 뷰에서 컨트롤러로 넘어온 데이터(BoardVO)를 이용해 수정
		return "redirect:/board/view?bno=" + vo.getBno(); // 현재 bno에 해당되는 조회 페이지로 이동
	}

	// 게시물 삭제
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String getDelete(@RequestParam("bno") int bno) throws Exception {
		service.delete(bno);
		return "redirect:/board/list";
	}

	// 게시물 목록 + 페이징 추가
	@RequestMapping(value = "/listPage", method = RequestMethod.GET)
	public void getListPage(Model model, @RequestParam("num") int num) throws Exception {

		Page page = new Page();

		page.setNum(num);
		page.setCount(service.count());

		List<BoardVO> list = null;
		list = service.listPage(page.getDisplayPost(), page.getPostNum());

		model.addAttribute("list", list);

		/*
		 * page데이터 model.addAttribute("pageNum",page.getPageNum());
		 * 
		 * model.addAttribute("startPageNum",page.getStartPageNum());
		 * model.addAttribute("endPageNum", page.getEndPageNum());
		 * 
		 * model.addAttribute("prev",page.getPrev()); model.addAttribute("next",
		 * page.getNext());
		 */

		model.addAttribute("page", page); // page데이터

		model.addAttribute("select", num);

		/*
		 * // 게시물 총 갯수 int count = service.count();
		 * 
		 * // 한 페이지에 출력할 게시물 갯수 int postNum = 10;
		 * 
		 * // 하단 페이징 번호([게시물 총 갯수 + 한 페이지에 출력할 갯수]의 올림) int pageNum =
		 * (int)Math.ceil((double)count/postNum);
		 * 
		 * // 출력할 게시물 int displayPost = (num-1) * postNum;
		 * 
		 * // 한번에 표시할 페이징 번호의 갯수 int pageNum_cnt = 10;
		 * 
		 * // 표시되는 페이지 번호 중 마지막 번호 int endPageNum =(int)(Math.ceil((double)num /
		 * (double)pageNum_cnt)*pageNum_cnt); // ☞ 마지막 페이지번호 = ((올림)(현재페이지번호/ 한번에 표시할
		 * 페이지 번호의 갯수))*한번에 표시할 페이지 번호의 갯수
		 * 
		 * // 표시되는 페이지 번호 중 첫번째 번호 int startPageNum = endPageNum - (pageNum_cnt -1); //
		 * ☞ 시작페이지 = 마지막페이지 번호 - 한번에 표시할 페이지 번호의 갯수 +1
		 * 
		 * // 마지막 번호 재계산 int endPageNum_tmp = (int)(Math.ceil((double)count /
		 * (double)pageNum_cnt));
		 * 
		 * if(endPageNum > endPageNum_tmp) { endPageNum = endPageNum_tmp; }
		 * 
		 * boolean prev = startPageNum == 1 ? false: true; boolean next = endPageNum *
		 * pageNum_cnt >= count ? false : true;
		 * 
		 * List<BoardVO> list = null; list = service.listPage(displayPost, postNum);
		 * model.addAttribute("list",list); model.addAttribute("pageNum", pageNum);
		 * 
		 * // 시작 및 끝 번호 model.addAttribute("startPageNum", startPageNum);
		 * model.addAttribute("endPageNum", endPageNum);
		 * 
		 * // 이전 및 다음 model.addAttribute("prev",prev); model.addAttribute("next",next);
		 * 
		 * // 현재 페이지 model.addAttribute("select",num);
		 */

	}

	// 게시물 목록 + 페이징 추가 + 검색
	@RequestMapping(value = "/listPageSearch", method = RequestMethod.GET)
	public void getListPageSearch(Model model, @RequestParam("num") int num,
			@RequestParam(value ="searchType",required=false, defaultValue= "title") String searchType, 
			@RequestParam(value ="keyword",required=false, defaultValue= "") String keyword) throws Exception {

		Page page = new Page();

		page.setNum(num);
		//page.setCount(service.count());	// 페이징을 만들 때 게시물의 갯수를 구하는 메서드
		page.setCount(service.searchCount(searchType, keyword));
		
		// 검색 타입과 검색어
		//page.setSearchTypeKeyword(searchType, keyword);
		page.setSearchType(searchType);
		page.setKeyword(keyword);

		List<BoardVO> list = null;
		//list = service.listPage(page.getDisplayPost(), page.getPostNum());
		list= service.listPageSearch(page.getDisplayPost(), page.getPostNum(), searchType, keyword);

		model.addAttribute("list", list);
		model.addAttribute("page", page);
		model.addAttribute("select", num);
		
		// 검색상태 유지 (모델로 저장해서 뷰로 전달)
//		model.addAttribute("searchType", searchType);
//		model.addAttribute("keyword",keyword);

	}

}