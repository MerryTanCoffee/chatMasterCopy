package kr.or.ddit.mapper;

import java.util.List;

import kr.or.ddit.vo.MemberVO;

public interface LoginMapper {

	public MemberVO loginCheck(MemberVO memberVO);

	public MemberVO idCheck(String memId);

	public int signup(MemberVO memberVO);

	public List<MemberVO> list();

	public MemberVO login(String memId);
}
