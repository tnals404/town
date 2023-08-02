package Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import Dto.BoardDTO;
import Dto.CommentDTO;
import Dto.ReportDTO;
import Pagination.PagingResponse;
import Pagination.SearchDTO;
import Service.BoardService;
import Service.ReportService;
import jakarta.servlet.http.HttpSession;

@Controller
public class ReportController { //안휘주 작성

	@Autowired
	@Qualifier("reportServiceImpl")
	ReportService service;
	
	@Autowired
	@Qualifier("boardServiceImpl")
	BoardService service2;
	
  	//글 신고 폼 열기
	@RequestMapping("/boardReportForm")
	public ModelAndView boardReportForm (int bi) {
		BoardDTO dto = service2.getDetail(bi);
		ModelAndView mv = new ModelAndView();
		mv.addObject("dto", dto);
		mv.setViewName("reportForm_board");
		return mv;			
	}
	
	//글 신고
	@RequestMapping(value="/reportBoard", produces = {"application/json;charset=utf-8"})
	public @ResponseBody int reportBoard (ReportDTO dto) {
		return service.reportBoard(dto);
	}
	
	//댓글 신고 폼 열기
	@RequestMapping("/commentReportForm")
	public ModelAndView commentReportForm (int ci) {
		CommentDTO dto = service.getCommentDetail(ci);
		ModelAndView mv = new ModelAndView();
		mv.addObject("dto", dto);
		mv.setViewName("reportForm_comment");
		return mv;			
	}
	
	//댓글 신고
	@RequestMapping(value="/reportComment", produces = {"application/json;charset=utf-8"})
	public @ResponseBody int reportComment (ReportDTO dto) {
		return service.reportComment(dto);
	}
	
	//관리자페이지 들어가기
	@RequestMapping("/manager3")
	public ModelAndView manager3() {	
		ModelAndView mv = new ModelAndView();
		mv.setViewName("managerPage3");
		return mv;
	}
	
	//신고 글 목록
	@PostMapping("/reportedBoardList")
	public ModelAndView reportedBoardList(String search, @RequestParam(value="page", required=false, defaultValue="1" ) int page, HttpSession session) {
//			ModelAndView signin = new ModelAndView();
//			if (session.getAttribute("member_id") == null) {
//				signin.setViewName("/Signin");
//				return signin;
//			}
		
		String adminMenu = "신고된 글";

		SearchDTO searchdto = new SearchDTO();
		searchdto.setSearchType1(search);
	    searchdto.setRecordSize(20);
	    searchdto.setPage(page);
	    
		PagingResponse<ReportDTO> list = service.reportedBoardList(searchdto);
		  
		ModelAndView mv = new ModelAndView();
		mv.addObject("searchdto", searchdto);
		mv.addObject("adminMenu", adminMenu);
		mv.addObject("response", list);
		mv.setViewName("reportedBoardList");
		return mv;
	}	
	
	//신고 댓글 목록
	@PostMapping("/reportedCommentList")
	public ModelAndView reportedCommentList(String search, @RequestParam(value="page", required=false, defaultValue="1" ) int page, HttpSession session) {
//			ModelAndView signin = new ModelAndView();
//			if (session.getAttribute("member_id") == null) {
//				signin.setViewName("/Signin");
//				return signin;
//			}
		
		String adminMenu = "신고된 댓글";
		
		SearchDTO searchdto = new SearchDTO();
		searchdto.setSearchType1(search);
		searchdto.setRecordSize(20);
		searchdto.setPage(page);
		
		PagingResponse<ReportDTO> list = service.reportedCommentList(searchdto);
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("searchdto", searchdto);
		mv.addObject("adminMenu", adminMenu);
		mv.addObject("response", list);
		mv.setViewName("reportedCommentList");
		return mv;
	}

		
}//class
