package kr.or.ddit.service;


import java.util.List;

import kr.or.ddit.ServiceResult;
import kr.or.ddit.vo.MemberVO;

public interface LoginService {

	public MemberVO loginCheck(MemberVO memberVO);

	public ServiceResult idCheck(String memId);

	public ServiceResult signup(MemberVO memberVO);

	public List<MemberVO> list();

	public MemberVO login(String memId);

}
    