package Service;

import java.util.HashMap;
import java.util.List;

import Dto.BoardDTO;

public interface BoardService1 { //김종인 작성
	// 게시글 작성
	int insertBoard(BoardDTO dto);
	// 공지사항 작성
	int insertNoticeBoard(List<BoardDTO> query);
	// 회원 동네 아이디 가져오기
	int getMemberTownId(String member_id);
	// 게시판 카테고리 선택시
	int getTotalArticleCount(HashMap<String, ?> map);
	List<BoardDTO> getPagingBoardlist(HashMap<String, ?> map);
	// 게시판에서 검색시
	int getBoardSearchCount(HashMap<String, ?> searchmap);
	List<BoardDTO> getBoardSearchList(HashMap<String, ?> searchmap);
	// 회원 동네 아이디에 해당하는 동 이름 가져오기
	String getMemberDongAddress(int member_town_id);
	// 회원 프로필 사진 변경
	int updateProfileImage(String member_id, String profile_image);
	// 회원이 관리자인지 아닌지 판단
	boolean isAdmin(String member_id);
	// 동네 이름 모두 가져오기(동네 아이디 오름차순으로)
	List<String> getAllTownName();
}
