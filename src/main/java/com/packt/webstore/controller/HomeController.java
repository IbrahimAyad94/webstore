package com.packt.webstore.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.InternalResourceView;

@Controller
public class HomeController {

	@RequestMapping("/")
	public String welcome(Model model) {
		model.addAttribute("greeting", "Welcome to Web Store!");
		model.addAttribute("tagline", "The one and only amazing web store");
		return "welcome";
	}

	// not good but if you want to return view itself but good way return name of
	// view and
	// by configuration specify view type so you can support multiple view without
	// edit code
	@RequestMapping("/home")
	public ModelAndView notGoodWay(Map<String, Object> model) {
		model.put("greeting", "NOt Good Way To Return View");
		model.put("tagline", "NOt Good Way To Return View");
		View view = new InternalResourceView("/WEB-INF/jsp/welcome.jsp");
		return new ModelAndView(view, model);
	}
	
	// test redirect and forward
	@RequestMapping("/AfterRedirectAndForward")
	public String welcomeEmpty(Model model) {
		return "welcome";
	}
	
	// when redirect if you send model .. it lost it's data .. to solve this problem .. add in flash attribute
	@RequestMapping("/redirect")
	public String redirect(RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("greeting", "redirect View");
		redirectAttributes.addFlashAttribute("tagline", "redirect View");
		return "redirect:/AfterRedirectAndForward";
		
	}
	
	@RequestMapping("/forward")
	public String forward(Model model) {
		model.addAttribute("greeting", "Forward View");
		model.addAttribute("tagline", "Forward View");
		return "forward:/AfterRedirectAndForward";
		
	}
}
