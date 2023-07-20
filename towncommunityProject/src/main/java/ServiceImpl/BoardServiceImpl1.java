package ServiceImpl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import Dao.BoardDAO1;
import Dto.BoardDTO;
import Service.BoardService1;

@Service
public class BoardServiceImpl1 implements BoardService1 { //김종인 작성
	
	@Autowired
	@Qualifier("boardDAO1")
	BoardDAO1 dao;

	@Override
	public int insertBoard(BoardDTO dto) {
		return dao.insertBoard(dto);
	}
	
	@Override
	public int getMemberTownId(String member_id) {
		return dao.getMemberTownId(member_id);
	}

	@Override
	public int getTotalArticleCount(HashMap<String, ?> map) {
		return dao.getArticleCount(map);
	}

	@Override
	public List<BoardDTO> getPagingBoardlist(HashMap<String, ?> map) {
		return dao.getPagingBoardlist(map);
	}

	@Override
	public int getBoardSearchCount(HashMap<String, ?> searchmap) {
		return dao.getBoardSearchCount(searchmap);
	}

	@Override
	public List<BoardDTO> getBoardSearchList(HashMap<String, ?> searchmap) {
		return dao.getBoardSearchList(searchmap);
	}

	
	
	
}
