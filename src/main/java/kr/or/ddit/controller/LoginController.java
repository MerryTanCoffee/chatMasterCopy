package kr.or.ddit.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.or.ddit.ServiceResult;
import kr.or.ddit.service.LoginService;
import kr.or.ddit.vo.MemberVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/module")
public class LoginController {

	@Inject
	LoginService loginService;
	
	@RequestMapping(value = "/list.do", method = RequestMethod.GET)
	public String memberList(Model model) throws Exception{
		log.info("memberList : ");
		List<MemberVO> memberList = loginService.list();
		model.addAttribute("list",memberList);
		return "module/list";
	}

	
	@RequestMapping(value ="/signup.do", method = RequestMethod.GET)
	public String memberRegister(Model model) {
		model.addAttribute("bodyText","signup-page");
		return "module/signup";
	}
	
	@RequestMapping(value ="/idCheck.do", method = RequestMethod.POST)
	public ResponseEntity<ServiceResult> idCheck(String memId){ // 아작스의 키값
		ServiceResult result = loginService.idCheck(memId);
		return new ResponseEntity<ServiceResult>(result, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/signup.do", method = RequestMethod.POST)
	public String signup(MemberVO memberVO, Model model) {
		
		String goPage= "";
		
		// input의 name 값이 memberVO.getMemId() 값으로 넘어옴
		
		Map<String, String> errors = new HashMap<String, String>();
		if(StringUtils.isBlank(memberVO.getMemId())) {
			errors.put("memId", "아이디 입력 필");
		}
		if(StringUtils.isBlank(memberVO.getMemPw())) {
			errors.put("memPw", "비밀번호 입력 필");
		}
		if(StringUtils.isBlank(memberVO.getMemName())) {
			errors.put("memName", "이름 입력 필");
		}
		
		if(errors.size() > 0 ) {
			model.addAttribute("bodyText","signup-page");
			model.addAttribute("errors",errors);
			model.addAttribute("member",memberVO);
			goPage = "module/signup";
		} else {
			ServiceResult result = loginService.signup(memberVO);
			if(result.equals(ServiceResult.OK)) {
				goPage = "redirect:/module/chat.do";
			
			} else {
		
				model.addAttribute("bodyText","signup-page");
				model.addAttribute("message","서버에러 다시 시도해주세요");
				model.addAttribute("member",memberVO);
				goPage = "module/signup";
			}
		}
		return goPage;
	}	
	
	

	@RequestMapping(value ="/loginCheck.do", method = RequestMethod.POST)
	public String loginCheck(
			HttpServletRequest req,
			MemberVO member, Model model) {
		String goPage = "";
		
		Map<String, String> errors = new HashMap<String, String>();
		if(StringUtils.isBlank(member.getMemId())) {
			errors.put("memId", "아이디 입력 필");
		}
		if(StringUtils.isBlank(member.getMemPw())) {
			errors.put("memPw", "비밀번호 입력 필");
		}
		if(errors.size() > 0) {
			model.addAttribute("errors", errors);
			model.addAttribute("member", member);
			model.addAttribute("bodyText", "login-page");
			goPage = "module/access"; 
		} else {
		
			
		}
		return goPage;
	}
	
	@RequestMapping(value="/login.do", method = RequestMethod.GET)
	public String loginGet(Model model) {
		return "module/login";
	}

	
	@RequestMapping(value="/login.do", method = RequestMethod.POST)
	@ResponseBody
	public int loginPost(String memId, String memPw, HttpSession session) {
		int result = 0;
		MemberVO memberVO = loginService.login(memId);
		if(memberVO != null) {
			if(memberVO.getMemPw().equals(memPw)) {
				result = 1;
				String memName = memberVO.getMemName();
				session.setAttribute("memId", memId);
				session.setAttribute("memName", memName);
			} else {
				result = 2;
			}
		}
		return result;
	}
	

	@RequestMapping("/logout.do")
	public String logout(HttpSession session) {
		session.invalidate();
		return "module/home";
	}
	
	

}
