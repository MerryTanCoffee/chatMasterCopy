package kr.or.ddit.mapper;

import java.util.List;


import kr.or.ddit.vo.ChatVO;
import kr.or.ddit.vo.MemberVO;

public interface ChatMapper {

	public MemberVO selectLastInform(String memId);

	public void insert(ChatVO vo);

	public String countMessageView(String userName);

	public List<ChatVO> chatList();


}
