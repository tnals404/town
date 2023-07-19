package ServiceImpl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Dao.SignDAO;
import Dto.BoardDTO;
import Dto.MemberDTO;
import Service.SignService;

@Service("SignService")
public class SignServiceimple implements SignService{
	
	@Autowired
	SignDAO dao;
	
	public int insertMember(MemberDTO memberDTO) {
		return dao.insertMember(memberDTO);
	}
	public int dupliIDcheck(String member_id) {
		return dao.dupliIDcheck(member_id);
	}
	public int dupliEmailcheck(String email) {
		return dao.dupliEmailcheck(email);
	}
	public int dupliFindEmailcheck(String member_id,String email) {
		return dao.dupliFindEmailcheck(member_id,email);
	}
	public int LoginMember(HashMap<String, ?> map) {
		return dao.LoginMember(map);
	}
	public MemberDTO MyInfo(String member_id) {
		return dao.MyInfo(member_id);
	}
	public int updatemember(MemberDTO MemberDTO) {
		return dao.updatemember(MemberDTO);
	}
	public int Findpwupdate(MemberDTO MemberDTO) {
		return dao.Findpwupdate(MemberDTO);
	}
	public int deletemember(String member_id) {
		return dao.deletemember(member_id);
	}
	@Override
	public int getMyTotalArticleCount(HashMap<String, ?> map) {
		return dao.getMyArticleCount(map);
	}
	@Override
	public List<BoardDTO> getMyPagingBoardlist(HashMap<String, ?> map) {
		return dao.getMyPagingBoardlist(map);
	}
	@Override
	public int getMycommentTotalArticleCount(HashMap<String, ?> map) {
		return dao.getMycommentArticleCount(map);
	}
	@Override
	public List<BoardDTO> getMycommentPagingBoardlist(HashMap<String, ?> map) {
		return dao.getMycommentPagingBoardlist(map);
	}
	@Override
	public int getMygoodTotalArticleCount(HashMap<String, ?> map) {
		return dao.getMygoodArticleCount(map);
	}
	@Override
	public List<BoardDTO> getMygoodPagingBoardlist(HashMap<String, ?> map) {
		return dao.getMygoodPagingBoardlist(map);
	}

}
