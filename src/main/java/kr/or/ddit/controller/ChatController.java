package kr.or.ddit.controller;


import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import kr.or.ddit.service.ChatService;
import kr.or.ddit.vo.ChatVO;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping("/module")
public class ChatController {
	
	@Inject
	private ChatService chatService;
	
	
	@RequestMapping(value="/chat.do",method=RequestMethod.POST)
	public void insert(ChatVO vo){
		chatService.insert(vo);
	}
	
	@RequestMapping(value = "/chat.do", method = RequestMethod.GET)
	public String chatList(Model model) throws Exception {
		log.info("chatList() : ");
		
		List<ChatVO> chatList = chatService.chatList();
		model.addAttribute("chatList", chatList);
		return "module/chat";
	}
	
	
}
