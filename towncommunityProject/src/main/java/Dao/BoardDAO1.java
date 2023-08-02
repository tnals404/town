package Dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import Dto.BoardDTO;

@Mapper
@Repository
public interface BoardDAO1 { //김종인 작성
	// 게시글 작성
	int insertBoard(BoardDTO dto);
	// 공지사항 작성
	List<Integer> getCurrentNoticeBoardId(HashMap<String, Object> noticemap);
	int insertNoticeBoard(HashMap<String, Object> noticemap);
	// 회원 동네 아이디 가져오기
	int getMemberTownId(String member_id);
	// 게시판 카테고리 선택시
	int getArticleCount(HashMap<String, Object> map);
	List<BoardDTO> getPagingBoardlist(HashMap<String, Object> map);
	// 게시판에서 검색시
	int getBoardSearchCount(HashMap<String, Object> searchmap);
	List<BoardDTO> getBoardSearchList(HashMap<String, Object> searchmap);
	// 관리자 페이지 공지사항 목록
	int getNoticeCnt();
	List<BoardDTO> getNoticeList(HashMap<String, Object> map);
	String getNoticeTownIds(int board_id);
	// 관리자 페이지 공지사항 검색시
	int getNoticeSearchCnt1(HashMap<String, Object> searchmap); // 제목, 내용, 작성자
	int getNoticeSearchCnt2(HashMap<String, Object> searchmap); // 동네 아이디
	int getNoticeSearchCnt3(HashMap<String, Object> searchmap); // 동네 이름
	int getNoticeSearchCnt4(HashMap<String, Object> searchmap); // 동네 이름
	List<BoardDTO> getNoticeSearchList1(HashMap<String, Object> searchmap);
	List<BoardDTO> getNoticeSearchList2(HashMap<String, Object> searchmap);
	List<BoardDTO> getNoticeSearchList3(HashMap<String, Object> searchmap);
	List<BoardDTO> getNoticeSearchList4(HashMap<String, Object> searchmap);
	// 회원 동네 아이디에 해당하는 동 이름 가져오기
	String getMemberDongAddress(int member_town_id);
	// 회원 프로필 사진 변경
	int updateProfileImage(String member_id, String profile_image);
	// 회원이 관리자인지 아닌지 판단
	int isAdmin(String member_id);
	// 동네 이름 모두 가져오기(동네 아이디 오름차순으로)
	List<String> getAllTownName();
	// 글 작성시 포인트 관련 sql
	int getWriteBoardCnt(HashMap<String, Object> map);
	int insertWriteBoardPoint(HashMap<String, Object> map);
	int updateMemberPoint(HashMap<String, Object> map);
}
