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
		if (ti.equals("")) {
			ti = String.valueOf(session.getAttribute("town_id"));
		}
		
		ModelAndView mv = new ModelAndView();
		// session으로 전달된 회원 아이디가 null이면 로그인 화면으로 이동
		if (session.getAttribute("member_id") == null) {
			mv.setViewName("/Signin");
			return mv;
		}
		mv.setViewName("basicBoard");
		
		// session으로 전달된 회원 아이디
		String member_id = String.valueOf(session.getAttribute("member_id"));
		String session_town_id = String.valueOf(session.getAttribute("town_id"));
		if (ti.equals("")) { // ti 파라미터가 입력되지 않으면 회원 동네 아이디로 초기화
			ti = session_town_id;
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
				sortList.add("board_contents");
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
		
		mv.addObject("boardName", ctgy);
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
	
	// 글 작성 페이지
	@GetMapping("/writingForm")
	public ModelAndView writingForm(HttpSession session,
			@RequestParam(value="ti", required=false, defaultValue="")String ti, 
			@RequestParam(value="ctgy", required=false, defaultValue="나의 일상")String ctgy) {
		ModelAndView mv = new ModelAndView();
		if (session.getAttribute("member_id") == null) {
			mv.setViewName("Signin");
			return mv;
		}
		mv.setViewName("writingForm");
		
		// 동네 아이디 파라미터가 입력되지 않았으면 회원의 동네 아이디로 입력
		if (ti.equals("")) {
			ti = String.valueOf(session.getAttribute("town_id"));
		}
		mv.addObject("boardName", ctgy);
		return mv;
	}
	
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
	
	/* 이 아래부터는 삭제할 예정인 부분 */
	// 파일 업로드 예제
	@GetMapping("/uploadTest")
	public String uploadTestGet() {
		return "uploadTest";
	}
	
	@PostMapping("/uploadTest")
	@ResponseBody
	public String uploadTestPOST(MultipartFile[] uploadFile) {
		String result = null;
        // 내가 업로드 파일을 저장할 경로
        String uploadFolder = "C:\\upload";
        
        // 날짜 폴더 생성
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String formatDate = sdf.format(date);
        String datePath = formatDate.replace("-", File.separator);
        File uploadPath = new File(uploadFolder, datePath);
        if (uploadPath.exists() == false) {
            uploadPath.mkdirs();
        }
        
        // 파일 업로드
        for (MultipartFile multipartFile : uploadFile) {
            String uploadFileName = multipartFile.getOriginalFilename();
            
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
            result = "{\"uploadFolder\":\"" + uploadFolder + "\", \"uploadFileName\":\"" + uploadFileName + "\"}";
        }
        return result;
	}
	
	@RequestMapping("/kakaoMap")
	public String kakaoMap() {
		return "kakaoMap";
	}
	
}
