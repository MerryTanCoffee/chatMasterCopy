package kr.or.ddit.service;

import java.util.List;


import kr.or.ddit.vo.ChatVO;
import kr.or.ddit.vo.MemberVO;

public interface ChatService {

	public MemberVO selectLastInform(String memId);

	public void insert(ChatVO vo);

	public String countMessageView(String userName);

	public List<ChatVO> chatList();


}
