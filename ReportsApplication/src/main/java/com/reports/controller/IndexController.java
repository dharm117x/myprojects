package com.reports.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class IndexController {

	//get index page
	@RequestMapping
	public String index() {
		
		return "index";
	}
	
}
