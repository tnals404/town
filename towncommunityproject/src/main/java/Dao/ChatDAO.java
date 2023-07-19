package Dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import Dto.ChatroomDTO;
import Dto.MessageDTO;

@Mapper
@Repository
public interface ChatDAO {
	public List<MessageDTO> checkNull(MessageDTO dto);
	public int createChatroom(ChatroomDTO dto);
	public int insertMessage(MessageDTO dto);
}
	
