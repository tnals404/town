package ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Dao.ChatDAO;
import Dto.ChatroomDTO;
import Dto.MessageDTO;
import Service.ChatService;

@Service("ChatService")
public class ChatServiceImpl implements ChatService {
	
	@Autowired
	ChatDAO dao;

	@Override
	public List<MessageDTO> checkNull(MessageDTO dto) {
		System.out.println(dto);
		return dao.checkNull(dto);
	}

	@Override
	public int createChatroom(ChatroomDTO dto) {
		dao.createChatroom(dto);
        int result = dto.getChat_id();
        return result;
	}

	@Override
	public int insertMessage(MessageDTO dto) {
		return dao.insertMessage(dto);
	}



	

}
