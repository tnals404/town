package Service;

import java.util.List;

import Dto.ChatroomDTO;
import Dto.MessageDTO;

public interface ChatService {
	public List<MessageDTO> checkNull(MessageDTO dto);
	public int createChatroom(ChatroomDTO dto);
	public int insertMessage(MessageDTO dto);
	
}
