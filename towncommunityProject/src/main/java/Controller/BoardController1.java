package Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import Dto.BoardDTO;
import Service.BoardService1;
import jakarta.servlet.http.HttpSession;

@Controller
public class BoardController1 { //김종인 작성
	
	@Autowired
	@Qualifier("boardServiceImpl1")
	BoardService1 service;
	
	// 글 게시판 페이지
	@GetMapping("/basicBoard")
	public ModelAndView basicBoard(
			HttpSession session, 
			@RequestParam(value="ti", required=false, defaultValue="")String ti, 
			@RequestParam(value="ctgy", required=false, defaultValue="공지사항") String ctgy, 
			@RequestParam(value="sort", required=false, defaultValue="")String sort,
			@RequestParam(value="keyword", required=false, defaultValue="") String keyword,
			@RequestParam(value="page", required=false, defaultValue="1") int page
	) {
		ModelAndView mv = new ModelAndView();
		
		// session으로 전달된 회원 아이디, 회원 동네 아이디
		String member_id = String.valueOf(session.getAttribute("member_id"));
		String session_town_id = String.valueOf(session.getAttribute("town_id"));
		
		// ti 파라미터가 입력되지 않으면 회원 동네 아이디로 초기화
		if (ti.equals("")) { 
			ti = session_town_id;
		}
		
		// session으로 전달된 회원 아이디가 null이면 로그인 화면으로 이동
		if (session.getAttribute("member_id") == null) {
			mv.setViewName("/Signin");
			return mv;
		}
		mv.setViewName("basicBoard");
		
		// ctgy 파라미터 검사
		if (ctgy.equals("공지사항") || ctgy.equals("HOT 게시판") ||
			ctgy.equals("나의 일상") || ctgy.equals("사건, 사고 소식") ||
			ctgy.equals("오늘의 사진") || ctgy.equals("역대 당선작") ||
			ctgy.equals("같이 줄서요") || ctgy.equals("같이해요 소모임") ||
			ctgy.equals("분실물센터") || ctgy.equals("심부름센터") ||
			ctgy.equals("행사 소식") || ctgy.equals("새로 오픈했어요")) {
			mv.addObject("boardName", ctgy);
		} else {
			mv.addObject("boardName", "공지사항");
		}
		
		// 페이징 처리
		int postCntPerPage = 20; // 한 페이지에 표시할 게시글 수
		int pageCntPerPage = 10; // 한번에 표시할 페이지 개수
		int totalPostCnt = 0; // 전체 게시글 개수
		int totalPageCnt = 0; // 전체 페이지 개수
		int endPageNum = 0; // 현재 페이지에서 가장 끝 페이지 수
		int startPageNum = 0; // 현재 페이지에서 가장 첫 페이지 수
		boolean prev = true; // ◁, ◁◁ 버튼 표시 여부
		boolean next = true; // ▷, ▷▷ 버튼 표시 여부
		boolean isSearch = false; // 검색을 했는지 여부
		HashMap<String, Object> map = null; // 검색 안 했을 때 sql에 사용할 변수
		HashMap<String, Object> searchmap = null; // 검색 sql에 사용할 파라미터 저장할 변수
		
		// 만약 게시판에서 검색을 한 경우
		if (!sort.equals("")) {
			isSearch = true;
			searchmap = new HashMap<>();
			List<String> sortList = new ArrayList<>();
			if (sort.equals("board_title") || sort.equals("board_contents") || sort.equals("writer")) {
				sortList.add(sort);
			} else {
				sortList.add("board_title");
				sortList.add("board_preview");
				sortList.add("writer");
			}
			searchmap.put("board_name_inner", ctgy); // 게시판 카테고리
			searchmap.put("town_id", ti); // 회원 동네 아이디
			searchmap.put("keyword", "%" + keyword + "%"); // 검색할 키워드
			searchmap.put("sortList", sortList); // 검색할 분류(전체, 제목, 내용, 작성자)
			
			totalPostCnt = service.getBoardSearchCount(searchmap);
		} else { // 게시판 검색을 하지 않은 경우 총 게시글 수
			map = new HashMap<>();
			map.put("board_name_inner", ctgy);
			map.put("town_id", ti);
			totalPostCnt = service.getTotalArticleCount(map); 
		}
		
		totalPageCnt = (int) Math.ceil((double) totalPostCnt / (double) postCntPerPage); // 총 게시판 페이지 개수
		if (page < 1 || page > totalPageCnt) {
			page = 1;
		}
		endPageNum = (int) (Math.ceil((double) page / (double) pageCntPerPage) * pageCntPerPage); // 표시되는 페이지 번호 중 마지막 번호
		startPageNum = endPageNum - (pageCntPerPage - 1);
		// 마지막 페이지 다시 계산
		int endPageNumTemp = (int) (Math.ceil((double) totalPostCnt / (double) postCntPerPage));
		if (endPageNum > endPageNumTemp) {
			endPageNum = endPageNumTemp;
		}
		prev = startPageNum == 1 ? false : true;
		next = endPageNum * postCntPerPage >= totalPostCnt ? false : true;
		
		// sql limit절에 들어갈 숫자 List
		int limitindex = (page - 1) * postCntPerPage;
		int limitcount = postCntPerPage;
		
		
		// 페이징 처리된 게시글 리스트
		List<BoardDTO> boardlist = null;
		if (isSearch) {
			searchmap.put("limitindex", limitindex);
			searchmap.put("limitcount", limitcount);
			boardlist = service.getBoardSearchList(searchmap);
		} else {
			map.put("limitindex", limitindex);
			map.put("limitcount", limitcount);
			boardlist = service.getPagingBoardlist(map);
		}
		
		mv.addObject("selectedPageNum", page);
		mv.addObject("postCntPerPage", postCntPerPage);
		mv.addObject("totalPostCnt", totalPostCnt);
		mv.addObject("totalPageCnt", totalPageCnt);
		mv.addObject("pageCntPerPage", pageCntPerPage);
		mv.addObject("endPageNum", endPageNum);
		mv.addObject("startPageNum", startPageNum);
		mv.addObject("prev", prev);
		mv.addObject("next", next);
		mv.addObject("boardlist", boardlist);
		mv.addObject("member_id", member_id);
		mv.addObject("town_id", ti);
		return mv;
	}
	
	// 글 쓰기 버튼 클릭했을 때 writingForm.jsp 보여주기
	@GetMapping("/writingForm")
	public ModelAndView writingForm(HttpSession session,
			@RequestParam(value="ti", required=false, defaultValue="")String ti, 
			@RequestParam(value="ctgy", required=false, defaultValue="나의 일상")String ctgy) {
		ModelAndView mv = new ModelAndView();
		// 동네 아이디 파라미터가 입력되지 않았으면 회원의 동네 아이디로 입력
		if (ti.equals("")) {
			ti = String.valueOf(session.getAttribute("town_id"));
		}
		// 로그인 하지 않은 상태면 로그인 화면으로 이동
		if (session.getAttribute("member_id") == null) {
			mv.setViewName("Signin");
			return mv;
		}
		// 회원 동네 아이디와 게시판 동네 아이디가 다를 경우 basicBoard로 이동
		String member_town_id = String.valueOf(session.getAttribute("town_id"));
		if (!member_town_id.equals(ti)) {
			mv.addObject("boardName", ctgy);
			mv.setViewName("basicBoard");
			return mv;
		}
		mv.addObject("boardName", ctgy);
		mv.setViewName("writingForm");
		return mv;
	}
	
	// 장소 추가 버튼 누르면 카카오맵 api 새창으로 띄우기
	@GetMapping("/kakaoMap")
	public ModelAndView kakaoMap(HttpSession session, int ti) {
		ModelAndView mv = new ModelAndView();
		if (session.getAttribute("member_id") == null) {
			mv.setViewName("Signin");
			return mv;
		}
		// 회원 동네 아이디에 해당하는 동 이름 가져오기
		int member_town_id = (int) session.getAttribute("town_id");
		String dongAddress = service.getMemberDongAddress(member_town_id);
		mv.addObject("dongAddress", dongAddress);
		mv.setViewName("kakaoMap");
		return mv;
	}
	
	// 작성완료 버튼 눌렀을 때 게시글 데이터 db에 insert 하고 게시판으로 돌아가기
	@PostMapping("/writingForm")
	@ResponseBody
	public HashMap<String, Object> writingFormResult(HttpSession session, BoardDTO dto) {
		HashMap<String, Object> result = new HashMap<>();
		int insertResult = 0;
		if (dto.getBoard_title().equals("")) {
			insertResult = -1;
			result.put("insertResult", insertResult);
			return result;
		} 
		if (dto.getTown_id() == 0) {
			dto.setTown_id((int)session.getAttribute("town_id"));
		}
		insertResult = service.insertBoard(dto);
		result.put("insertResult", insertResult);
		return result;
	}
	
	// 글 작성 페이지에서 이미지 업로드
	@PostMapping("/board/imageUpload")
	@ResponseBody
	public HashMap<String, Object> boardImageUpload(MultipartFile[] uploadFile) {
		HashMap<String, Object> result = new HashMap<>(); // ajax로 반환할 데이터(json)
        String uploadFolder = "C:\\upload";
        
        // 날짜 폴더 생성
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String formatDate = sdf.format(date); // 2023-07-17
        String datePath = formatDate.replace("-", File.separator); // 2023/07/17
        File uploadPath = new File(uploadFolder, datePath); // c:/upload/2023/07/17
        if (uploadPath.exists() == false) {
            uploadPath.mkdirs();
        }
        
        // 파일 업로드
        for (MultipartFile multipartFile : uploadFile) {
        	String fileName = multipartFile.getOriginalFilename();
            String uploadFileName = fileName;
            
            // UUID로 업로드 하는 파일명 생성
            String uuid = UUID.randomUUID().toString();
            uploadFileName = uuid + "_" + uploadFileName;
            
            // 저장할 파일, 생성자로 경로와 이름을 지정해줌.
            File saveFile = new File(uploadPath, uploadFileName);

            try {
                // void transferTo(File dest) throws IOException 업로드한 파일 데이터를 지정한 파일에 저장
                multipartFile.transferTo(saveFile);    
            } catch (Exception e) {
                e.printStackTrace();
            }
            result.put("uploadPath", uploadPath);
            result.put("uuid", uuid);
            result.put("fileName", fileName);
        }
        return result;
	}
	
	// 에디터에 업로드한 이미지 보여주기
	@ResponseBody
	@GetMapping(value = "/display")
	public ResponseEntity<byte[]> showImageGET(
	        @RequestParam("fileName") String fileName
	) {
        File file = new File(fileName);
        
	    ResponseEntity<byte[]> result = null;

	    try {

	        HttpHeaders header = new HttpHeaders();

	        /*
	        Files.probeContentType() 해당 파일의 Content 타입을 인식(image, text/plain ...)
	        없으면 null 반환

	        file.toPath() -> file 객체를 Path객체로 변환

	        */
	        header.add("Content-type", Files.probeContentType(file.toPath()));

	        result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);

	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    return result;
	}
	
	// 프로필 사진 변경 폼 열기
	@RequestMapping("/changeProfileImg")
	public String changeProfileImg() {
		return "changeProfileImg";
	}
	
	// 프로필 변경 적용
	@PostMapping("/updateProfileImg")
	@ResponseBody
	public HashMap<String, Object> updateProfileImg(HttpSession session, String imgSrc) {
		HashMap<String, Object> resultMap = new HashMap<>();
		String member_id = String.valueOf(session.getAttribute("member_id"));
		if (member_id == null) {
			resultMap.put("result", 0); // 로그인 되어 있지 않은 경우 로그인 화면으로 이동
			return resultMap;
		}
		// 로그인 되어 있는 경우 프로필 사진 업데이트
		resultMap.put("result", service.updateProfileImage(member_id, imgSrc));
		
		return resultMap;
	}
	
	@RequestMapping("/noticeWritingForm")
	public ModelAndView noticeWritingForm(HttpSession session) {
		ModelAndView mv = new ModelAndView();
		String member_id = String.valueOf(session.getAttribute("member_id"));
		if (member_id == null) {
			mv.setViewName("Signin");
			return mv;
		} else if (!service.isAdmin(member_id)) { // 관리자가 아니면 Main으로 이동
			mv.setViewName("Main");
			return mv;
		}
		List<String> townNameList = service.getAllTownName();
		mv.addObject("townNameList", townNameList);
		mv.setViewName("noticeWritingForm");
		return mv;
	}
	
	@PostMapping("/noticeWritingForm")
	@ResponseBody
	public HashMap<String, Object> noticeWriteEnd(
			BoardDTO dto, 
			@RequestParam(value="town_ids[]", required=false, defaultValue="") ArrayList<String> town_ids
	) {
		HashMap<String, Object> result = new HashMap<>();
		int insertResult = 0;
		if (dto.getBoard_title().equals("")) { // 제목을 입력하지 않았을 경우
			insertResult = -1;
			result.put("insertResult", insertResult);
			return result;
		} 
		if (town_ids.isEmpty()) { // 공지사항을 올릴 동네를 선택하지 않은 경우
			insertResult = -2;
			result.put("insertResult", insertResult);
			return result;
		}
		
		// 동네 아이디에 따라 공지사항 BoardDTO 생성
		List<BoardDTO> query = new ArrayList<>();
		for (String town_id : town_ids) {
			BoardDTO tempdto = new BoardDTO();
			tempdto.setBoard_name_inner("공지사항");
			tempdto.setBoard_title(dto.getBoard_title());
			tempdto.setBoard_contents(dto.getBoard_contents());
			tempdto.setBoard_imgurl(dto.getBoard_imgurl());
			tempdto.setBoard_fileurl(dto.getBoard_fileurl());
			tempdto.setBoard_preview(dto.getBoard_preview());
			tempdto.setWriter(dto.getWriter());
			tempdto.setTown_id(Integer.parseInt(town_id));
			query.add(tempdto);
		}
		insertResult = service.insertNoticeBoard(query);
		result.put("insertResult", insertResult);
		return result;
	}
	
}