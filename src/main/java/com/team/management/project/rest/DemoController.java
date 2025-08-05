package com.team.management.project.rest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/hello")
public class DemoController {

	// create a mapping for "/hello"
	
	@GetMapping("/hello")
	//@ResponseBody
	public String sayHello(Model theModel) {
		
		theModel.addAttribute("theDate", new java.util.Date());		
		return "helloworld";
	}
	@PostMapping("/hello")
	//@ResponseBody
	public String hiii(Model theModel) {
		
		theModel.addAttribute("theDate", new java.util.Date());		
		return "helloworld";
	}
}








