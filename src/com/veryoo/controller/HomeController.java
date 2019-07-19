package com.veryoo.controller;

import com.veryoo.easymvc.BaseController;


public class HomeController extends BaseController{

	public String execute(){
		request.setAttribute("name", "obj");
		
		return "/WEB-INF/jsp/home.jsp";
	}
	
}
