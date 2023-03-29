package kr.or.ddit.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kr.or.ddit.mapper.ChatMapper;
import kr.or.ddit.vo.ChatVO;
import kr.or.ddit.vo.MemberVO;

@Service
public class ChatServiceImpl implements ChatService{

	@Inject
	private ChatMapper chatMapper;
	
	@Override
	public MemberVO selectLastInform(String memId) {
		return chatMapper.selectLastInform(memId);
	}

	@Override
	public void insert(ChatVO vo) {
		chatMapper.insert(vo);
		
	}

	@Override
	public String countMessageView(String userName) {
		return chatMapper.countMessageView(userName);
	}

	@Override
	public List<ChatVO> chatList() {
		return chatMapper.chatList();
	}
}
