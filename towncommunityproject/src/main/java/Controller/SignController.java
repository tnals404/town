package Controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import Dto.BoardDTO;
import Dto.MemberDTO;
import Service.SignService;
import jakarta.servlet.http.HttpSession;

@Controller
public class SignController {
	
	@Autowired
	//@Qualifier("SignService")
	SignService Ss;
	
	
	@RequestMapping("/")
	public ModelAndView in() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("Signin");	
		return mv;
	}
	
	@RequestMapping("/Signin")
	public ModelAndView signin() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("Signin");	
		return mv;
	}
	//로그인구현
	@PostMapping("/Login")
	public String loginprocess(String member_id, String password,HttpSession session) {
		HashMap<String, Object> map = null;
		MemberDTO my_info = Ss.MyInfo(member_id);
		if(my_info != null) {
			if(my_info.getPassword().equals(password)) {
				map = new HashMap<>();
				session.setAttribute("member_id",my_info.getMember_id());
				session.setAttribute("town_id", my_info.getTown_id());
				int plus_invite = my_info.getInvite_sum();
				plus_invite += 1;
				map.put("member_id", my_info.getMember_id());
				map.put("invite_sum", plus_invite);
				Ss.LoginMember(map);
				return "redirect:/main" ;
			}
	}
		return "signin";

	}
	//회원가입
	@RequestMapping("/Signup")
	public ModelAndView signup() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("Signup");	
		return mv;
	}
	
	@PostMapping("/signup")
	public String signup(MemberDTO memberDTO) {
		if(Ss.insertMember(memberDTO)>0) {
			return "redirect:/Signin";
		}
		else return "Signup";
	}
	//회원가입시 중복체크
	@PostMapping("/dupliIDCheck")
	@ResponseBody
	public int duplieIDcheck(@RequestParam("member_id") String member_id) {
			int cnt=Ss.dupliIDcheck(member_id);
			return cnt;
	}
	
	@PostMapping("/dupliEmailCheck")
	@ResponseBody
	public int duplieEmailcheck(@RequestParam("email") String email) {
			int cnt=Ss.dupliEmailcheck(email);
			return cnt;
	}
	//비밀번호찾기시 id와 이메일 대조 
	@PostMapping("/dupliFindEmailCheck")
	@ResponseBody
	public int duplieFindEmailcheck(@RequestParam("member_id") String member_id,@RequestParam("email") String email) {
			int cnt=Ss.dupliFindEmailcheck(member_id,email);
			return cnt;
	}
	//정보수정 
	@GetMapping("/myinform")
	public ModelAndView myinform(HttpSession session) {
		ModelAndView mv = new ModelAndView();
		if (session.getAttribute("member_id") == null) {
			mv.setViewName("/Signin");
			return mv;
		}

		if (session.getAttribute("member_id") != null) {
			String my_id = session.getAttribute("member_id").toString();
			MemberDTO my_info = Ss.MyInfo(my_id);
			mv.addObject("my_info", my_info);
			
			mv.setViewName("myinform");
		}

		return mv;
	}
	
	@PostMapping("/update")
	public String updatemember(MemberDTO MemberDTO) {
		
			Ss.updatemember(MemberDTO);
			
		return"redirect:/myinform";
	}
	
	//비밀번호찾기시 임시비밀번호 변경
	@PostMapping("/Findpwupdate")
	public String Findpwupdate(MemberDTO MemberDTO) {
		
		Ss.Findpwupdate(MemberDTO);
		
		return"/Findpasswordend";
	}
	//회원삭제
	
	@GetMapping("/deletemember")
	public String deletemember(HttpSession session) {
		String member_id = String.valueOf(session.getAttribute("member_id"));
		Ss.deletemember(member_id);
		session.invalidate();
		return "redirect:/Signin";
	}
	
	
	
	//비밀번호 찾기 
	@RequestMapping("/Findpassword")
	public ModelAndView Findpassword() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("Findpassword");	
		return mv;
	}
	
	
	//마이페이지 구현단 -종인/영우같이 구현
	@RequestMapping("/myPage")
	public ModelAndView mypage(HttpSession session) {
	
	ModelAndView mv = new ModelAndView();
	// session으로 전달된 회원 아이디가 null이면 로그인 화면으로 이동
	if (session.getAttribute("member_id") == null) {
		mv.setViewName("/Signin");
		return mv;
	}
	
	HashMap<String, Object> map = new HashMap<>();
	String my_id = session.getAttribute("member_id").toString();
	MemberDTO my_info = Ss.MyInfo(my_id);
	SimpleDateFormat formater = new SimpleDateFormat("yyyy년 MM월 dd일");
	Date my_signup_date =my_info.getSignup_date();
	String Signup_date= formater.format(my_signup_date);
	
	map.put("member_id", session.getAttribute("member_id"));
	int MyTotalArticleCount = Ss.getMyTotalArticleCount(map); //내가 쓴 글 갯수
	int MycommentTotalArticleCount = Ss.getMycommentTotalArticleCount(map);//내가 쓴 댓글 갯수
	int getMygoodTotalArticleCount = Ss.getMygoodTotalArticleCount(map);//내가 좋아요 한 글 갯수
	
	mv.addObject("MyTotalArticleCount",MyTotalArticleCount);
	mv.addObject("MycommentTotalArticleCount",MycommentTotalArticleCount);
	mv.addObject("getMygoodTotalArticleCount",getMygoodTotalArticleCount);
	mv.addObject("Signup_date",Signup_date);
	mv.addObject("my_info", my_info);
	
	mv.setViewName("myPage");
	
	return mv;
	}
	
	//마이페이지 구현단 -종인/영우같이 구현
		@RequestMapping("/myPage2")
		public ModelAndView mypage2(
			HttpSession session,
			@RequestParam(value="ctgy", required=false, defaultValue="My page") String ctgy,
			@RequestParam(value="page", required=false, defaultValue="1") int page
		) {
		
		ModelAndView mv = new ModelAndView();
		// session으로 전달된 회원 아이디가 null이면 로그인 화면으로 이동
		if (session.getAttribute("member_id") == null) {
			mv.setViewName("/Signin");
			return mv;
		}
		mv.setViewName("myPage2");
		
		
		// 페이징 처리
		int postCntPerPage = 20; // 한 페이지에 표시할 게시글 수
		int pageCntPerPage = 10; // 한번에 표시할 페이지 개수
		int totalPostCnt = 0; // 전체 게시글 개수
		int totalPageCnt = 0; // 전체 페이지 개수
		int endPageNum = 0; // 현재 페이지에서 가장 끝 페이지 수
		int startPageNum = 0; // 현재 페이지에서 가장 첫 페이지 수
		boolean prev = true; // ◁, ◁◁ 버튼 표시 여부
		boolean next = true; // ▷, ▷▷ 버튼 표시 여부
		HashMap<String, Object> map = null; // 검색 안 했을 때 sql에 사용할 변수
		
		 // 게시판 검색을 하지 않은 경우 총 게시글 수
		map = new HashMap<>();
		map.put("member_id", session.getAttribute("member_id"));
		
		if (ctgy.equals("내가 쓴 글")) {
			totalPostCnt = Ss.getMyTotalArticleCount(map);
		} else if (ctgy.equals("내가 쓴 댓글")) {
			totalPostCnt = Ss.getMycommentTotalArticleCount(map);
		} else if (ctgy.equals("좋아요 한 글")) {
			totalPostCnt = Ss.getMygoodTotalArticleCount(map);
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
		map.put("limitindex", limitindex);
		map.put("limitcount", limitcount);
		if (ctgy.equals("내가 쓴 글")) {
			boardlist = Ss.getMyPagingBoardlist(map);
		} else if (ctgy.equals("내가 쓴 댓글")) {
			boardlist = Ss.getMycommentPagingBoardlist(map);
		} else if (ctgy.equals("좋아요 한 글")) {
			boardlist = Ss.getMygoodPagingBoardlist(map);
		}
		
		mv.addObject("ctgy", ctgy);
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
		return mv;
		}
	
	//이메일 구현단
	@Autowired
	JavaMailSender javaMailSender;
	
	@PostMapping("/CheckMail") 
	@ResponseBody  
	//회원가입시 이메일 인증번호 발송
	public String SendMail(String email) {
		
		
		Random random=new Random(); 
		String key="";  

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email); 
		for(int i =0; i<3;i++) {
			int index=random.nextInt(25)+65; 
			key+=(char)index;
		}
		int numIndex=random.nextInt(99999)+10000; 
		key+=numIndex;
		message.setSubject("인증번호 입력을 위한 메일 전송");
		message.setText("인증 번호 : "+key);
		message.setFrom("kakaoclone@naver.com");
		javaMailSender.send(message);
		
		
        return key;
	}
	
	@PostMapping("/FindpwSendEmail")
	@ResponseBody
	//비밀번호 찾기시 이메일로 임시 비밀번호 전송
	public String FindSendMail(String email) {
		Random random=new Random(); 
		String key="";  

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email); 
		for(int i =0; i<3;i++) {
			int index=random.nextInt(25)+65; 
			key+=(char)index;
		}
		int numIndex=random.nextInt(99999)+10000; 
		key+=numIndex;
		message.setSubject("임시비밀번호을 위한 메일 전송");
		message.setText("임시비밀번호 : "+key);
		message.setFrom("kakaoclone@naver.com");
		javaMailSender.send(message);
		
		
        return key;	
        }
}
