package kr.or.ddit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/module")
public class HomeController {

	@RequestMapping(value="/home",method=RequestMethod.GET)
	public String home(Model model) {
		model.addAttribute("pageName","about.jsp");
		return "module/home";
	}
	

	@RequestMapping(value = "/chat", method = RequestMethod.GET)
	public String chat() {
		return "module/chat";
	}
}
