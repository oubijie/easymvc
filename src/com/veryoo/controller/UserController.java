package com.veryoo.controller;

public class UserController extends BaseController{

	public String execute(){
		return "/WEB-INF/jsp/user.jsp";
	}
	
	public String list(){
		return "/WEB-INF/jsp/user/list.jsp";
	}
	
	public String add(){
		return "/WEB-INF/jsp/user/add.jsp";
	}
	
	public String edit(){
		return "/WEB-INF/jsp/user/edit.jsp";
	}
	
	public String save(){
		return "/WEB-INF/jsp/user/list.jsp";
	}
}
