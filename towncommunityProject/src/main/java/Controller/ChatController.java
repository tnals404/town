package Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import Dto.ChatroomDTO;
import Dto.MessageDTO;
import Service.ChatService;
import jakarta.servlet.http.HttpSession;

@Controller

public class ChatController {
	
	@Autowired
	@Qualifier("ChatService")
	ChatService service;
	
	@RequestMapping("/testchat")
	public String testchat() {
		return "testchatbtn";
	}
	
	@RequestMapping("/chatstart")
	public ModelAndView chatstart(HttpSession session,@RequestParam(value="touser_id",required=false ,defaultValue="0")String touser_id) {
		MessageDTO dto3 = new MessageDTO();
	    ModelAndView mv = new ModelAndView();
	    String member_id = String.valueOf(session.getAttribute("member_id"));;
	    System.out.println(member_id);
	    dto3.setMember_id(member_id);
	    dto3.setTouser_id(touser_id);
	    String a = dto3.getTouser_id();
	    System.out.println(a);
	    List<MessageDTO> list = service.checkNull(dto3);
	    
	    mv.addObject("list",list);
	    //mv.addObject("aValue", a);
	    mv.setViewName("/testchatbtn");
	    System.out.println(list.size()); 
	    
	    if (list.size() == 0) {
	    	ChatroomDTO dto4 = new ChatroomDTO();
	        int result = service.createChatroom(dto4);
		    System.out.println(result);
	    }
	    else {System.out.println(list.get(0).getChat_id());
	    //mv.addObject("thischatid",list.get(0).getChat_id());
	    }
		return mv;
	}
	
	@PostMapping("/sendChat")
	@ResponseBody
	public int sendChat(@RequestParam("message") String message, HttpSession session) {
		MessageDTO dto5 = new MessageDTO();
		String member_id = String.valueOf(session.getAttribute("member_id"));
		dto5.setMember_id(member_id);
		//int thischatid = (int) mv.getModel().get("thischatid");
		//dto5.setChat_id(thischatid);
		//String aValue = (String) mv.getModel().get("aValue");
		//dto5.setTouser_id(aValue);
		dto5.setChat_id(12);
		dto5.setTouser_id("abc");
		dto5.setMessage_content(message);
		int result = service.insertMessage(dto5);
		
		return result;
	}

	@RequestMapping("/chatlist")
	public String chatlist() {
		return "chatList";
	}

}
