package Controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import Dto.BoardDTO;
import Dto.CommentDTO;
import Dto.GoodHateDTO;
import Dto.MemberDTO;
import Pagination.PagingResponse;
import Pagination.SearchDTO;
import Service.BoardService;
import jakarta.servlet.http.HttpSession;

@Controller
public class BoardController { //안휘주 작성

	@Autowired
	@Qualifier("boardServiceImpl")
	BoardService service;
	
	//선택된 게시물 조회&조회수증가&댓글,대댓글 조회
	@RequestMapping("/boarddetail")
	public ModelAndView boarddetail(int bi, @RequestParam(value="page", required=false, defaultValue="0" ) int page, HttpSession session) {
		ModelAndView signin = new ModelAndView();
		if (session.getAttribute("member_id") == null) {
			signin.setViewName("/Signin");
			return signin;
		}

		int board_id = bi;
		BoardDTO dto = service.updateViewcntAndGetDetail(board_id);
		//글쓴사람 정보
		MemberDTO writerDto = service.boardWriterProfile(dto.getWriter());
		
		int commentCnt = service.getTotalCommentcnt(board_id);
		List<CommentDTO> commentDto = service.oneBoardComments(board_id);
		HashMap<String, String> commentWriterProfileMap = new HashMap<String, String>();
		for(CommentDTO cmt : commentDto) {
			MemberDTO commentWriterDto = service.boardWriterProfile(cmt.getComment_writer());
			commentWriterProfileMap.put(cmt.getComment_writer(),commentWriterDto.getProfile_image() );
		}
		
		//댓글 목록 불러오기
		String str_bi = String.valueOf(board_id);
		SearchDTO searchdto = new SearchDTO();
		searchdto.setSearchType1(str_bi);
	    searchdto.setRecordSize(20);
	    
		//처음 로드될때 최신댓글이 있는 마지막 페이지로 & page값 있을땐 그 page가 로드되도록
		int returnPage = 0;
		if(commentCnt > 0) {
			if(page==0) {
				if(commentCnt % 20 == 0){
					returnPage = commentCnt / 20;
				}
				else {
					returnPage = commentCnt / 20 + 1;
				}
			}else {
				returnPage = page;
			}
		}
		else {
			returnPage = 1;
		}

	   searchdto.setPage(returnPage);		    	
	    
		PagingResponse<CommentDTO> list = service.getCommentList(searchdto);

		//로그인한 사용자 해당 글에 좋아요,싫어요 체크 여부
		String memid = (String)session.getAttribute("member_id");
		//System.out.println(memid);
		GoodHateDTO ghdto = new GoodHateDTO();
		ghdto.setBoard_id(board_id);
		ghdto.setMember_id(memid);
		
		GoodHateDTO gohresultdto = service.isGoodOrHate(ghdto);
		String gohresult="";
		
		if(gohresultdto != null) {
			if(gohresultdto.isGood()) {
				gohresult = "good";
			}
			else if(gohresultdto.isHate()){
				gohresult = "hate";
			}				
		}
		else {
			gohresult = "none";
		}
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("detaildto", dto);
		mv.addObject("boardName", dto.getBoard_name_inner());
		mv.addObject("town_id", dto.getTown_id());
		mv.addObject("writerDto", writerDto);
		mv.addObject("commentWriterProfileMap", commentWriterProfileMap);				
		mv.addObject("commentCnt", commentCnt );
		mv.addObject("searchdto", searchdto);
		mv.addObject("response", list);
		mv.addObject("gohresult", gohresult );
		mv.setViewName("boardDetail2");
		return mv;	
		
	}
	
	//댓글쓰기
	@RequestMapping(value="/commentwrite", produces = {"application/json;charset=utf-8"})
	public @ResponseBody int commentwrite (CommentDTO dto) {
		return service.insertComment(dto); 
	}
	
	//댓글삭제
	@RequestMapping(value="/deletecomment", produces = {"application/json;charset=utf-8"})
	public @ResponseBody int deletecomment (int comment_id) {
		return service.deleteComment(comment_id);
	}
	
	//사진 게시판 목록
	@RequestMapping("/photoBoard")
	public ModelAndView photoBoardList(String ctgy, String ti, @RequestParam(value="page", required=false, defaultValue="1" ) int page, HttpSession session) {
		ModelAndView signin = new ModelAndView();
		if (session.getAttribute("member_id") == null) {
			signin.setViewName("/Signin");
			return signin;
		}
		
		SearchDTO searchdto = new SearchDTO();
		searchdto.setSearchType1(ctgy);
		searchdto.setSearchType2(ti);
	    searchdto.setRecordSize(25);
	    searchdto.setPage(page);
	    
		PagingResponse<BoardDTO> list = service.getBoardList(searchdto);
		
		//각 글마다 댓글 수
		HashMap<Integer, Integer> boardCommentCnt = new HashMap<Integer, Integer>();
		for(BoardDTO b : list.getList()) {
			int cmtCnt = service.getTotalCommentcnt(b.getBoard_id());
			boardCommentCnt.put(b.getBoard_id(), cmtCnt);
		}
		  
		ModelAndView mv = new ModelAndView();
		mv.addObject("boardName", ctgy);
		mv.addObject("town_id", ti);		
		mv.addObject("searchdto", searchdto);
		mv.addObject("boardCommentCnt", boardCommentCnt);
		mv.addObject("response", list);
		mv.setViewName("photoBoard3");
		return mv;
	}	
	
	//photoBoard 선택된 게시물 조회&조회수증가&댓글,대댓글 조회
	@RequestMapping("/photoboarddetail")
	public ModelAndView photoboarddetail(int bi, @RequestParam(value="page", required=false, defaultValue="0" ) int page, HttpSession session) {
		
		ModelAndView signin = new ModelAndView();
		if (session.getAttribute("member_id") == null) {
			signin.setViewName("/Signin");
			return signin;
		}
		
		int board_id = bi;
		BoardDTO dto = service.updateViewcntAndGetDetail(board_id);
		//글쓴사람 정보
		MemberDTO writerDto = service.boardWriterProfile(dto.getWriter());
		
		int commentCnt = service.getTotalCommentcnt(board_id);
		List<CommentDTO> commentList = service.oneBoardComments(board_id);
		HashMap<String, String> commentWriterProfileMap = new HashMap<String, String>();
		for(CommentDTO cmt : commentList) {
			//후에 댓글 삭제하면 작성자 정보 없음 -> 작성자 정보 없으면 건너뛰기
			if(cmt.getComment_writer() == null || cmt.getComment_writer() == "") {
				continue;
			}
			MemberDTO commentWriterDto = service.boardWriterProfile(cmt.getComment_writer());
			commentWriterProfileMap.put(cmt.getComment_writer(),commentWriterDto.getProfile_image() );				
		}
		//댓글 목록 불러오기
		String str_bi = String.valueOf(board_id);
		SearchDTO searchdto = new SearchDTO();
		searchdto.setSearchType1(str_bi);
	    searchdto.setRecordSize(20);
	    
		//처음 로드될때 최신댓글이 있는 마지막 페이지로 & page값 있을땐 그 page가 로드되도록
		int returnPage = 0;
		if(commentCnt > 0) {
			if(page==0) {
				if(commentCnt % 20 == 0){
					returnPage = commentCnt / 20;
				}
				else {
					returnPage = commentCnt / 20 + 1;
				}
			}else {
				returnPage = page;
			}
		}
		else {
			returnPage = 1;
		}

	   searchdto.setPage(returnPage);		    	
	    
		PagingResponse<CommentDTO> list = service.getCommentList(searchdto);

		//로그인한 사용자 해당 글에 좋아요,싫어요 체크 여부
		String memid = (String)session.getAttribute("member_id");
		//System.out.println(memid);
		GoodHateDTO ghdto = new GoodHateDTO();
		ghdto.setBoard_id(board_id);
		ghdto.setMember_id(memid);
		
		GoodHateDTO gohresultdto = service.isGoodOrHate(ghdto);
		String gohresult="";
		
		if(gohresultdto != null) {
			if(gohresultdto.isGood()) {
				gohresult = "good";
			}
			else if(gohresultdto.isHate()){
				gohresult = "hate";
			}				
		}
		else {
			gohresult = "none";
		}
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("detaildto", dto);
		mv.addObject("boardName", dto.getBoard_name_inner());
		mv.addObject("town_id", dto.getTown_id());
		mv.addObject("writerDto", writerDto);
		mv.addObject("commentWriterProfileMap", commentWriterProfileMap);				
		mv.addObject("commentCnt", commentCnt );
		mv.addObject("searchdto", searchdto);
		mv.addObject("response", list);
		mv.addObject("gohresult", gohresult );
		mv.setViewName("photoBoardDetail");
		return mv;	
		
	}
		
	//좋아요 추가 삭제
		@RequestMapping(value="/likethisboard", produces = {"application/json;charset=utf-8"})
		public @ResponseBody String likethisboard (int board_id, String member_id) {	
			GoodHateDTO dto = new GoodHateDTO();
			dto.setBoard_id(board_id);
			dto.setMember_id(member_id);
			dto.setGood(true);
			
			GoodHateDTO resultdto = service.isGoodOrHate(dto);
			String result="";
			
			if(resultdto != null) {
				if(resultdto.isGood()) {
					service.deleteGoodMinusCnt(dto);
					result = "cancle";
				}
				if(resultdto.isHate()){
					service.deleteHateMinusCnt(dto);
					service.addGoodPlusCnt(dto);
					result = "onandoff";
				}				
			}
			else {
				service.addGoodPlusCnt(dto);				
				result = "add";
			}
			return "{\"result\" : \"" + result+ " \" }";
		}
		
		//싫어요 추가 삭제
		@RequestMapping(value="/hatethisboard", produces = {"application/json;charset=utf-8"})
		public @ResponseBody String hatethisboard (int board_id, String member_id) {	
			GoodHateDTO dto = new GoodHateDTO();
			dto.setBoard_id(board_id);
			dto.setMember_id(member_id);
			dto.setHate(true);
			
			GoodHateDTO resultdto = service.isGoodOrHate(dto);
			String result="";
			
			if(resultdto != null) {
				if(resultdto.isHate()) {
					service.deleteHateMinusCnt(dto);
					result = "cancle";
				}
				if(resultdto.isGood()){
					service.deleteGoodMinusCnt(dto);
					service.addHatePlusCnt(dto);
					result = "onandoff";
				}				
			}
			else {
				service.addHatePlusCnt(dto);				
				result = "add";
			}
			return "{\"result\" : \"" + result+ " \" }";
		}
		
		//보드 삭제
		@RequestMapping(value="/deleteboard", produces = {"application/json;charset=utf-8"})
		public @ResponseBody int deleteboard (int board_id) {
			return service.deleteBoard(board_id);
		}
		
		//대댓글쓰기
		@RequestMapping(value="/recommentwrite", produces = {"application/json;charset=utf-8"})
		public @ResponseBody int recommentwrite (CommentDTO dto) {
			return service.insertRecomment(dto);
		}
		
		//댓글수정
		@RequestMapping(value="/updatecomment", produces = {"application/json;charset=utf-8"})
		public @ResponseBody int updatecomment (CommentDTO dto) {
			return service.updateComment(dto);
		}
		
		//댓글 삭제 전 대댓글 존재여부 확인
		@RequestMapping(value="/recommentCnt", produces = {"application/json;charset=utf-8"})
		public @ResponseBody int recommentCnt (int comment_id) {
			return service.getRecommentCnt(comment_id);
		}
		
		//댓글 삭제(인듯 업데이트로 내용지우기)
		@RequestMapping(value="/deleteUpdateComment", produces = {"application/json;charset=utf-8"})
		public @ResponseBody int deleteUpdateComment (CommentDTO dto) {
			return service.deleteUpdateComment(dto);
		}
		
		
}//class
