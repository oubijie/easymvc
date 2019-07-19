package com.veryoo.controller;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletResponse;

public class HomeController extends BaseController{

	public String execute(){
		request.setAttribute("name", "obj");
		
		return "/WEB-INF/jsp/home.jsp";
	}
	
}
