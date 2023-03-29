package kr.or.ddit.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import kr.or.ddit.ServiceResult;
import kr.or.ddit.mapper.LoginMapper;
import kr.or.ddit.vo.MemberVO;

@Service
public class LoginServiceImpl implements LoginService {

	@Inject
	LoginMapper loginmapper;
	
	@Override
	public MemberVO loginCheck(MemberVO memberVO) {
		return loginmapper.loginCheck(memberVO);
	}

	@Override
	public ServiceResult idCheck(String memId) {
		ServiceResult result = null; 
		MemberVO member = loginmapper.idCheck(memId);
		
		if(member != null) {
			result = ServiceResult.EXIST;
			
		} else {
			result = ServiceResult.NOTEXIST;
		}
		return result;
	}

	@Override
	public ServiceResult signup(MemberVO memberVO) {

		ServiceResult result = null;
		int status = loginmapper.signup(memberVO);
		
		if(status > 0) {
			result = ServiceResult.OK;
		} else {
			result = ServiceResult.FAILED;
		}
		return result;
	}

	@Override
	public List<MemberVO> list() {
		return loginmapper.list();
	}

	@Override
	public MemberVO login(String memId) {
		return loginmapper.login(memId);
	} 

}
