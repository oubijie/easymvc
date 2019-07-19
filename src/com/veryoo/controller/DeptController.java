package com.veryoo.controller;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class DeptController extends BaseController {

	public String execute(){
		return "/WEB-INF/jsp/dept.jsp";
	}
	
	public String check(){
		Map map = new HashMap();
		map.put("success", true);
		map.put("status", 200);
		map.put("message", "is ok~");
		
		//把json的内容放到request中
		putJson(JSON.toJSONString(map));
		
		return "json";  //返回json标识
	}
}
